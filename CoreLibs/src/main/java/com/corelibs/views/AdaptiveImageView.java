package com.corelibs.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * 根据网络图片自动适应高度, 适用于Picasso, 高度可使用wrap_content
 */
public class AdaptiveImageView extends ImageView {

	public AdaptiveImageView(Context context) {
		super(context);
	}

	public AdaptiveImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AdaptiveImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setImageDrawable(final Drawable drawable) {
		super.setImageDrawable(drawable);
		if (drawable != null) {
			post(new Runnable() {
				@Override
				public void run() {
					float mWidth = getWidth();
					float pWidth = drawable.getBounds().width();
					float pHeight = drawable.getBounds().height();
					int mHeight = (int) ((mWidth / pWidth) * pHeight);

					LayoutParams params = getLayoutParams();
					params.height = mHeight;
					params.width = (int) mWidth;
					setLayoutParams(params);
				}
			});
		}
	}
}
