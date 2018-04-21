package com.corelibs.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.corelibs.R;
import com.corelibs.utils.DisplayUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 特殊动画的圆形进度条
 */
public class CircularBar extends View {

	/** 默认宽度, dp */
	private static final int DEFAULT_WIDTH_DP = 5;

	private Paint paint;
	private final RectF oval = new RectF();

	private float startAngle;
	private float sweepAngle;

	private ObjectAnimator angleAnimator;
	private ObjectAnimator sweepAnimator;

	private boolean flag = true;
	/** 背景色 */
	private int backColor = 0xFFDDDDDD;
	/** 前景色 */
	private int frontColor = 0xFF009077;
	/** 圆弧宽度 */
	private float width;

	public CircularBar(Context context) {
		this(context, null);
	}

	public CircularBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircularBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
		init();
	}

	private void init(AttributeSet attrs) {
		if(attrs == null) {
			width = DisplayUtil.dip2px(getContext(), DEFAULT_WIDTH_DP);
		} else {
			TypedArray attribute = getContext().obtainStyledAttributes(attrs, R.styleable.CircularBar);
			frontColor = attribute.getColor(R.styleable.CircularBar_circularColor, 0xFF009077);
			backColor = attribute.getColor(R.styleable.CircularBar_circularBackgroundColor, 0xFFDDDDDD);
			width = attribute.getDimension(R.styleable.CircularBar_circularWidth, DisplayUtil.dip2px(getContext(), DEFAULT_WIDTH_DP));
			attribute.recycle();
		}
	}

	private void init(){
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(frontColor);
		paint.setStrokeWidth(width);

		startAngle = 0f;
		sweepAngle = 0f;

		angleAnimator = ObjectAnimator.ofFloat(this, "startAngle", 360f).setDuration(2000);
		angleAnimator.setRepeatCount(ValueAnimator.INFINITE);
		angleAnimator.setRepeatMode(ValueAnimator.RESTART);
		angleAnimator.setInterpolator(new LinearInterpolator());

		sweepAnimator = ObjectAnimator.ofFloat(this, "sweepAngle", 360f).setDuration(600);
		sweepAnimator.setRepeatCount(ValueAnimator.INFINITE);
		sweepAnimator.setRepeatMode(ValueAnimator.RESTART);
		sweepAnimator.setInterpolator(new DecelerateInterpolator());
		sweepAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				flag = !flag;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {

		float _sweepAngle = sweepAngle;

		if(flag){
			_sweepAngle = sweepAngle;
		} else {
			_sweepAngle = sweepAngle - 360;
		}

		oval.top = 10;
		oval.bottom = getHeight()-10;
		oval.left = 10;
		oval.right = getHeight()-10;

		paint.setColor(backColor);
		canvas.drawOval(oval, paint);
		paint.setStrokeWidth(width);

		paint.setColor(frontColor);
		canvas.drawArc(oval, startAngle, _sweepAngle, false, paint);
		//canvas.drawRect(10, 10, getHeight()-10, getHeight()-10, paint);

		super.onDraw(canvas);
	}

	public float getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(float startAngle) {
		if(this.startAngle == startAngle)
			return;
		this.startAngle = startAngle;
		invalidate();
	}

	public float getSweepAngle() {
		return sweepAngle;
	}

	public void setSweepAngle(float sweepAngle) {
		if(this.sweepAngle == sweepAngle)
			return;
		this.sweepAngle = sweepAngle;
		invalidate();
	}

	/**
	 * 启动动画, 动画默认启动
	 */
	public void startAnimation() {
		if (!angleAnimator.isRunning())
			angleAnimator.start();
		if (!sweepAnimator.isRunning())
			sweepAnimator.start();
	}

    /**
     * 停止动画
     */
    public void stopAnimation() {
        if (angleAnimator.isRunning())
            angleAnimator.cancel();
        if (sweepAnimator.isRunning())
            sweepAnimator.cancel();
    }

	/**
	 * 设置圆弧颜色
	 */
	public void setFrontColor(int color) {
		this.frontColor = color;
		invalidate();
	}

	/**
	 * 设置圆弧背景色
	 */
	public void setBackgroundColor(int color) {
		this.backColor = color;
		invalidate();
	}

	/**
	 * 设置圆弧宽度, 单位px
	 */
	public void setStrokeWidth(float width) {
		this.width = width;
		invalidate();
	}

	@Override
	protected void onDetachedFromWindow() {
		stopAnimation();
		super.onDetachedFromWindow();
	}
}
