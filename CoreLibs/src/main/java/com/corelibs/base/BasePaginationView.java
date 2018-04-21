package com.corelibs.base;

/**
 * 带有分页功能的BaseView
 * <BR/>
 * Created by Ryan on 2016/1/7.
 */
public interface BasePaginationView extends BaseView {
    /**
     * 一次页面加载完成操作
     */
    void onLoadingCompleted();

    /**
     * 所有页面均加载完成
     */
    void onAllPageLoaded();
}
