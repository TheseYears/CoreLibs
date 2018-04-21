package com.corelibs.views.ptr.loadmore.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.corelibs.views.ptr.loadmore.OnScrollListener;

/**
 * 针对ListView/GridView等View的适配接口, 用于带自动加载更多的视图
 * <BR/>
 * Created by Ryan on 2016/1/21.
 */
public interface LoadMoreAdapter<T extends ViewGroup> {
    /**
     * 添加FooterView的适配
     */
    void addFooterView(View v, Object data, boolean isSelectable);
    /**
     * 删除FooterView的适配
     */
    boolean removeFooterView(View v);
    /**
     * 设置OnScrollListener的适配
     */
    void setOnScrollListener(OnScrollListener<T> l);
    /**
     * 获取总行数的适配
     */
    int getRowCount();
    /**
     * 获取被包装的View
     */
    T getView();
    /**
     * 是否要在初始化的时候就添加footer view，以适应不同控件。如Grid/List在初始化的时候不添加footer
     * 会报已设置adapter的错，recycler则不能在初始化的时候添加。
     */
    boolean addFooterAtInit();
}
