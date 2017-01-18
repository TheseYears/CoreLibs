package com.corelibs.views.navigation;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.corelibs.R;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

public abstract class TranslucentNavBar extends RelativeLayout {

    protected View statusBar, contentView;
    protected ImageView imageBg;

    protected int statusBarHeight;

    public TranslucentNavBar(Context context) {
        this(context, null);
    }

    public TranslucentNavBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranslucentNavBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setTransparentColor() {
        setColor(0x00000000);
    }

    public void setColor(int color) {
        setNavColor(color);
        setStatusBarColor(color);
    }

    public void setColorRes(int color) {
        setNavColorRes(color);
        setStatusBarColorRes(color);
    }

    public void setNavColor(int color) {
        contentView.setBackgroundColor(color);
    }

    public void setNavColorRes(int res) {
        setNavColor(ContextCompat.getColor(getContext(), res));
    }

    public void setStatusBarColor(int color) {
        statusBar.setBackgroundColor(color);
    }

    public void setStatusBarColorRes(int res) {
        setStatusBarColor(ContextCompat.getColor(getContext(), res));
    }

    public void setImageBackground(int res) {
        imageBg.setVisibility(VISIBLE);
        imageBg.setImageResource(res);
    }

    public void setImageBackgroundScaleType(ImageView.ScaleType scaleType) {
        imageBg.setScaleType(scaleType);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    private void init() {
        boolean fullScreen = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        initStatusBar();
        initImageBgView();

        contentView = LayoutInflater.from(getContext()).inflate(getLayoutId(), this, false);
        addView(imageBg);
        addView(statusBar);
        addView(contentView);

        LayoutParams ruleParams = (LayoutParams) contentView.getLayoutParams();
        ruleParams.addRule(BELOW, statusBar.getId());

        ButterKnife.bind(this, contentView);
        initView();

        int height = contentView.getLayoutParams().height;
        LayoutParams imageParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        if (!fullScreen) {
            statusBar.setVisibility(GONE);
            imageParams.height = height;
        } else {
            if (height <= 0) imageParams.height = height;
            else imageParams.height = height + statusBarHeight;
        }
        imageBg.setLayoutParams(imageParams);
    }

    private void initImageBgView() {
        imageBg = new ImageView(getContext());
        imageBg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageBg.setVisibility(GONE);
    }

    private void initStatusBar() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        statusBar = new View(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusBar.setLayoutParams(params);
        statusBar.setId(R.id.nav_status_bar);
    }
}
