package com.corelibs.base;

import android.os.Bundle;

/**
 * 懒加载的Fragment，在界面加载完毕并且可见之后会调用{@link #onVisible()}方法。
 * @param <V>
 * @param <T>
 */
public abstract class LazyFragment<V extends BaseView, T extends BasePresenter<V>>
        extends BaseFragment<V, T> {

    private boolean isVisibleBeforeInit = false;

    /**
     * 界面加载完毕并且可见之后调用此方法。<BR />
     * 注意：覆写init方法的时候不能去掉super.init(savedInstanceState);
     */
    protected abstract void onVisible();

    @Override
    protected void init(Bundle savedInstanceState) {
        if (isVisibleBeforeInit) onVisible();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (presenter == null) isVisibleBeforeInit = true;
            else onVisible();
        }
    }

}
