package com.corelibs.base;

import android.view.View;

import butterknife.ButterKnife;

/**
 * 利用ButterKnife与内部类做额外布局的控件查找
 * Created by Ryan on 2016/2/2.
 */
public class BaseInnerViewHolder {
    private View view;

    public View getView() {
        return view;
    }

    public BaseInnerViewHolder(View view) {
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public void unBind() {
        ButterKnife.unbind(this);
        view = null;
    }
}
