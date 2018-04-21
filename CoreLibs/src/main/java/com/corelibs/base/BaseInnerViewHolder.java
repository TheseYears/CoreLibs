package com.corelibs.base;

import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 利用ButterKnife与内部类做额外布局的控件查找
 * Created by Ryan on 2016/2/2.
 */
public class BaseInnerViewHolder {
    private View view;
    private Unbinder unbinder;

    public View getView() {
        return view;
    }

    public BaseInnerViewHolder(View view) {
        this.view = view;
        unbinder = ButterKnife.bind(this, view);
    }

    public void unBind() {
        unbinder.unbind();
        view = null;
    }
}
