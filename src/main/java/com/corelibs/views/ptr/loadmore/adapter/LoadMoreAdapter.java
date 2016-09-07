package com.corelibs.views.ptr.loadmore.adapter;

import android.view.View;
import android.widget.AbsListView;

/**
 * 针对ListView/GridView等View的适配接口, 用于带自动加载更多的视图
 * <BR/>
 * Created by Ryan on 2016/1/21.
 */
public interface LoadMoreAdapter<T> {
    /**
     * 添加FooterView的适配
     */
    void addFooterView(View v, Object data, boolean isSelectable);
    /**
     * 删除FooterView的适配
     */
    boolean removeFooterView(View v);
    /**
     * 获取最后可见的position的适配
     */
    int getLastVisiblePosition();
    /**
     * 设置OnScrollListener的适配
     */
    void setOnScrollListener(AbsListView.OnScrollListener l);
    /**
     * 获取总行数的适配
     */
    int getRowCount();
    /**
     * 获取被包装的View
     */
    T getView();
}
