package com.ryan.corelibs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.corelibs.views.navigation.TranslucentNavBar;
import com.ryan.corelibs.R;

import butterknife.BindView;

/**
 * 标题栏
 */
public class NavBar extends TranslucentNavBar {

    @BindView(R.id.tv_title) TextView tvTitle;

    public NavBar(Context context) {
        super(context);
    }

    public NavBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.v_nav;
    }

    @Override
    protected void initView() {
        setBackgroundColor(0xff333333);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(int title) {
        tvTitle.setText(title);
    }
}
