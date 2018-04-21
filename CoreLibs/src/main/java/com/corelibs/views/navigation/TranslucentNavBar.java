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

/**
 * 用于适配沉浸式状态栏的控件。<BR />
 * 1. 在manifest中的application节点里，设置theme属性为CoreLibs的AppBaseCompactTheme<BR />
 * 2. 在activity里调用setTranslucentStatusBar()方法，默认已调用，无需额外调用<BR />
 * 3. 自定义一个标题栏并继承此类，覆写构造方法以及{@link #getLayoutId()}和{@link #initView()}<BR />
 * 4. 使用自定义好的标题栏即可，注意布局根节点不能使用fitSystemWindows＝“true”
 * 5. 通过一系列setXxxColor来设置标题栏和状态栏的颜色或者背景
 * 6. 如布局用有EditText，请参考{@link AndroidBug5497Workaround}
 */
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

    /**
     * 将标题栏与状态栏都设置为透明
     */
    public void setTransparentColor() {
        setColor(0x00000000);
    }

    /**
     * 将标题栏与状态栏都设置为给定的颜色值
     */
    public void setColor(int color) {
        setNavColor(color);
        setStatusBarColor(color);
    }

    /**
     * 将标题栏与状态栏都设置为给定的颜色资源
     */
    public void setColorRes(int color) {
        setNavColorRes(color);
        setStatusBarColorRes(color);
    }

    /**
     * 将标题栏设置为给定的颜色值
     */
    public void setNavColor(int color) {
        contentView.setBackgroundColor(color);
    }

    /**
     * 将标题栏设置为给定的颜色资源
     */
    public void setNavColorRes(int res) {
        setNavColor(ContextCompat.getColor(getContext(), res));
    }

    /**
     * 将状态栏设置为给定的颜色值
     */
    public void setStatusBarColor(int color) {
        statusBar.setBackgroundColor(color);
    }

    /**
     * 将状态栏设置为给定的颜色资源
     */
    public void setStatusBarColorRes(int res) {
        setStatusBarColor(ContextCompat.getColor(getContext(), res));
    }

    /**
     * 将标题栏与状态栏都设置为给定的图片背景
     */
    public void setImageBackground(int res) {
        imageBg.setVisibility(VISIBLE);
        imageBg.setImageResource(res);
    }

    /**
     * 设置图片背景的缩放方式
     */
    public void setImageBackgroundScaleType(ImageView.ScaleType scaleType) {
        imageBg.setScaleType(scaleType);
    }

    /**
     * 设置标题栏布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
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
