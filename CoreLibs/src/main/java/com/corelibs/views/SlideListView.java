package com.corelibs.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * 
 * 侧滑删除Item的ListView，此处是对网上开源的一个Listview的完善，
 * 
 * 实现在手指滑动时item的透明度随之改变，并增加回到原位置的动画过程
 * 
 * @author zhangshuo
 */
@SuppressLint("NewApi")
public class SlideListView extends ListView {
	/**
	 * 
	 * 当前滑动的ListView　position
	 */
	private int slidePosition;
	/**
	 * 
	 * 手指按下X的坐标
	 */
	private int downY;
	/**
	 * 
	 * 手指按下Y的坐标
	 */
	private int downX;
	/**
	 * 
	 * 屏幕宽度
	 */
	private int screenWidth;
	/**
	 * 
	 * ListView的item
	 */
	private View itemView;
	/**
	 * 
	 * 滑动类
	 */
	private Scroller scroller;
	private static final int SNAP_VELOCITY = 600;
	/**
	 * 
	 * 速度追踪对象
	 */
	private VelocityTracker velocityTracker;
	/**
	 * 
	 * 是否响应滑动，默认为不响应
	 */
	private boolean isSlide = false;
	/**
	 * 
	 * 认为是用户滑动的最小距离
	 */
	private int mTouchSlop;
	/**
	 * 
	 * 移除item后的回调接口
	 */
	private RemoveListener mRemoveListener;
	/**
	 * 
	 * 标示是否移除
	 */
	private boolean isRemove = false;
	/**
	 * 
	 * 用来指示item滑出屏幕的方向,向左或者向右,用一个枚举值来标记
	 */
	private RemoveDirection removeDirection;

	// 滑动删除方向的枚举值
	public enum RemoveDirection {
		RIGHT, LEFT, NONE;
	}

	public SlideListView(Context context) {
		super(context);
		init(context);
	}

	public SlideListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SlideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		screenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		scroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	/**
	 * 
	 * 设置滑动删除的回调接口
	 * 
	 * @param removeListener
	 */
	public void setRemoveListener(RemoveListener removeListener) {
		this.mRemoveListener = removeListener;
	}

	/**
	 * 
	 * 分发事件，主要做的是判断点击的是那个item, 以及通过postDelayed来设置响应左右滑动事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// System.out.println("dispatch-->" + "down");
			addVelocityTracker(event);

			// 假如scroller滚动还没有结束，我们直接返回
			if (!scroller.isFinished()) {
				return false;
			}
			downX = (int) event.getX();
			downY = (int) event.getY();

			slidePosition = pointToPosition(downX, downY);

			// 无效的position, 不做任何处理
			if (slidePosition == AdapterView.INVALID_POSITION) {
				return super.dispatchTouchEvent(event);
			}
			
			// 获取我们点击的item view
			itemView = getChildAt(slidePosition - getFirstVisiblePosition());
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			// System.out.println("dispatch-->" + "move");
			if (slidePosition < getHeaderViewsCount()) {
				isSlide = false;
				return super.dispatchTouchEvent(event);
			}
			if (slidePosition > getCount() - getFooterViewsCount() - 1) {
				isSlide = false;
				return super.dispatchTouchEvent(event);
			}
			if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY || (Math.abs(event.getX() - downX) > mTouchSlop && Math.abs(event.getY() - downY) < mTouchSlop)) {
				isSlide = true;

			}
			break;
		}
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	/**
	 * 
	 * 往右滑动，getScrollX()返回的是左边缘的距离，就是以View左边缘为原点到开始滑动的距离，所以向右边滑动为负值
	 */
	private void scrollRight() {
		removeDirection = RemoveDirection.RIGHT;
		final int delta = (screenWidth + itemView.getScrollX());
		// 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
		scroller.startScroll(itemView.getScrollX(), 0, -delta, 0, Math.abs(delta));
		postInvalidate(); // 刷新itemView
	}

	/**
	 * 
	 * 向左滑动，根据上面我们知道向左滑动为正值
	 */
	private void scrollLeft() {
		removeDirection = RemoveDirection.LEFT;
		final int delta = (screenWidth - itemView.getScrollX());
		// 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
		scroller.startScroll(itemView.getScrollX(), 0, delta, 0, Math.abs(delta));
		postInvalidate(); // 刷新itemView
	}

	/**
	 * 
	 * 滑动会原来的位置
	 */
	private void scrollBack() {
		removeDirection = RemoveDirection.NONE;
		scroller.startScroll(itemView.getScrollX(), 0, -itemView.getScrollX(), 0, Math.abs(itemView.getScrollX()));
		postInvalidate(); // 刷新itemView
	}

	/**
	 * 
	 * 根据手指滚动itemView的距离来判断是滚动到开始位置还是向左或者向右滚动
	 */
	private void scrollByDistanceX() {
		// 如果向左滚动的距离大于屏幕的二分之一，就让其删除
		if (itemView.getScrollX() >= screenWidth / 2) {
			scrollLeft();
		} else if (itemView.getScrollX() <= -screenWidth / 2) {
			scrollRight();
		} else {
			// 滚回到原始位置
			scrollBack();
		}

	}

	/**
	 * 
	 * 处理我们拖动ListView item的逻辑
	 */
	@SuppressLint("Recycle")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
			//System.out.println("touch-->" + "开始");
			requestDisallowInterceptTouchEvent(true);
			addVelocityTracker(ev);
			final int action = ev.getAction();
			int x = (int) ev.getX();
			switch (action) {
			case MotionEvent.ACTION_DOWN:

				// System.out.println("touch-->" + "down");

				break;

			case MotionEvent.ACTION_MOVE:

				// System.out.println("touch-->" + "move");

				MotionEvent cancelEvent = MotionEvent.obtain(ev);

				cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));

				onTouchEvent(cancelEvent);

				int deltaX = downX - x;

				// 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚

				itemView.scrollTo(deltaX, 0);

				// 根据手指滑动的距离，调整透明度

				itemView.setAlpha(1f - Math.abs((float) deltaX / screenWidth));

				return true; // 拖动的时候ListView不滚动

			case MotionEvent.ACTION_UP:

				// System.out.println("touch-->" + "up");

				// 手指离开的时候就不响应左右滚动

				isSlide = false;

				int velocityX = getScrollVelocity();

				if (velocityX > SNAP_VELOCITY) {

					scrollRight();

				} else if (velocityX < -SNAP_VELOCITY) {

					scrollLeft();

				} else {

					scrollByDistanceX();

				}

				recycleVelocityTracker();

				break;

			}

		}

		// 否则直接交给ListView来处理onTouchEvent事件

		return super.onTouchEvent(ev);

	}

	@Override
	public void computeScroll() {

		// 调用startScroll的时候scroller.computeScrollOffset()返回true，

		if (scroller.computeScrollOffset()) {

			// 让ListView item根据当前的滚动偏移量进行滚动

			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());

			itemView.setAlpha(1f - Math.abs((float) scroller.getCurrX() / screenWidth));

			postInvalidate();

			// 滚动动画结束的时候调用回调接口

			if (scroller.isFinished() && removeDirection != RemoveDirection.NONE) {

				// if (mRemoveListener == null) {
				//
				// throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");
				//
				// }

				itemView.scrollTo(0, 0);

				itemView.setAlpha(1f);
				// System.out.println(getHeaderViewsCount()+"-------------");
				if (mRemoveListener != null)
					mRemoveListener.removeItem(removeDirection, slidePosition);

			}

		}

	}

	/**
	 * 
	 * 添加用户的速度跟踪器
	 * 
	 * 
	 * 
	 * @param event
	 */

	private void addVelocityTracker(MotionEvent event) {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();

		}

		velocityTracker.addMovement(event);

	}

	/**
	 * 
	 * 移除用户速度跟踪器
	 */

	private void recycleVelocityTracker() {

		if (velocityTracker != null) {

			velocityTracker.recycle();
			velocityTracker = null;
		}
	}

	/**
	 * 
	 * 获取X方向的滑动速度,大于0向右滑动，反之向左
	 * 
	 * 
	 * 
	 * @return
	 */
	private int getScrollVelocity() {
		velocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) velocityTracker.getXVelocity();
		return velocity;
	}

	/**
	 * 
	 * 
	 * 
	 * 当ListView item滑出屏幕，回调这个接口
	 * 
	 * 我们需要在回调方法removeItem()中移除该Item,然后刷新ListView
	 * 
	 * 
	 * 
	 * @author xiaanming
	 * 
	 * 
	 */
	public interface RemoveListener {
		public void removeItem(RemoveDirection direction, int position);
	}
}
