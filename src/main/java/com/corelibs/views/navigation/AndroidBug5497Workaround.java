package com.corelibs.views.navigation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 用于解决Activity在全屏下，同时windowSoftInputMode为adjustResize时，软键盘弹起却不会Resize布局的情况。<BR />
 * 使用方法：在设置了content view的Activity中调用AndroidBug5497Workaround.assistActivity(activity);
 */
public class AndroidBug5497Workaround {
    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    private Context context;
    private View mChildOfContent;
    private int usableHeightPrevious, statusBarHeight, bottomBarHeight;
    private boolean fullScreen;
    private FrameLayout.LayoutParams frameLayoutParams;

    private AndroidBug5497Workaround(Activity activity, boolean fullScreen) {
        this.fullScreen = fullScreen;
        context = activity;

        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();

        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = activity.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于解决Activity在全屏下，同时windowSoftInputMode为adjustResize时，软键盘弹起却不会Resize布局的情况。
     */
    public static void assistActivity(Activity activity) {
        new AndroidBug5497Workaround(activity, true);
    }

    public static void assistActivity(Activity activity, boolean fullScreen) {
        new AndroidBug5497Workaround(activity, fullScreen);
    }

    private void possiblyResizeChildOfContent() {
        setupBottomBarHeight();
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight() - bottomBarHeight;

            if (fullScreen && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                usableHeightSansKeyboard = usableHeightSansKeyboard - statusBarHeight;

            int heightDifference = usableHeightSansKeyboard - usableHeightNow;

            if (fullScreen && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                heightDifference = heightDifference - statusBarHeight;

            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private void setupBottomBarHeight() {
        bottomBarHeight = getDpi(context) - getScreenHeight(context);
        if (bottomBarHeight > 0) {
            if (AndroidRomUtil.isEMUI()) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                    bottomBarHeight = 0;
            }
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    private int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
