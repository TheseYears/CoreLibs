package com.corelibs.views.cityselect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.corelibs.utils.DisplayUtil;

/**
 * 字母索引控件, 继承自View
 *
 */
public class LetterView extends View {

	public static final String DEFAULT = "#";
	
	/**
	 * 存放字母的字符串集合
	 */
	String b[] = { DEFAULT, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
			"Y", "Z" };
	Paint paint = new Paint();
	/**
	 * 控制是否显示背景颜色
	 */
	boolean showBg = false;
	
	/**
	 * 触摸字母监听
	 */
	OnTouchingLetterChangedListener listener;
	
	Context context;
	
	int color = 0xFF666666, textSize;
	
	public LetterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LetterView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		this.context = context;
		textSize = DisplayUtil.dip2px(context, 12);
	}
	
	public void setColor(int color) {
		this.color = color;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(0x00000000);
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / b.length; //通过控件的总高度除以字母个数算出单个字母的高度
		
		for(int i = 0; i < b.length; i++){
			paint.setColor(color);
			paint.setTypeface(Typeface.DEFAULT);
			paint.setTextSize(textSize);
			paint.setAntiAlias(true);
			
			float xPos = width / 2 - paint.measureText(b[i]) / 2; //算出字母的x坐标
			float yPos = (i + 1) * singleHeight; //算出字母的Y坐标
			
			canvas.drawText(b[i], xPos, yPos, paint);
			
			paint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float yPos = event.getY();
		final int currentLetterIndex = (int) yPos * b.length / getHeight(); //算出当前选中的字母的位置
		final OnTouchingLetterChangedListener l = listener;
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			showBg = true;
			if (currentLetterIndex >= 0 && currentLetterIndex < b.length) {
				l.onTouchingLetterChanged(b[currentLetterIndex]);
			}
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			
			if (currentLetterIndex >= 0 && currentLetterIndex < b.length) {
				l.onTouchingLetterChanged(b[currentLetterIndex]);
			}
			break;
		case MotionEvent.ACTION_UP:
			showBg = false;
			invalidate();
			break;
		default:
			break;
		}
		
		return true;
	}
	
	/**
	 * 触摸字母监听
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}
	
	/**
	 * 设置触摸字母监听器
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener l) {
		this.listener = l;
	}
	

}
