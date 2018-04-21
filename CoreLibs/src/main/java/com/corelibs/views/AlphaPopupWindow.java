package com.corelibs.views;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 可以设置窗口透明度的PopupWindow
 * Created by Ryan on 2016/7/14.
 */
public class AlphaPopupWindow extends PopupWindow {

    private Activity context;
    private float alpha = 0.7f;

    public AlphaPopupWindow(Activity context) {
        this.context = context;
    }

    protected void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
        setWindowAlpha(alpha);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setWindowAlpha(alpha);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setWindowAlpha(1f);
    }
}
