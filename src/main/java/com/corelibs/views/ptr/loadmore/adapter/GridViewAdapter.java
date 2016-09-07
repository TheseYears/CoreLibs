package com.corelibs.views.ptr.loadmore.adapter;

import android.view.View;
import android.widget.AbsListView;

import com.corelibs.views.GridViewWithHeaderAndFooter;

/**
 * 针对GridView或继承自GridView的控件的适配类
 * <BR/>
 * Created by Ryan on 2016/4/1.
 */
public class GridViewAdapter<T extends GridViewWithHeaderAndFooter> implements LoadMoreAdapter<T> {

    private T gridView;

    public GridViewAdapter(T gridView) {
        this.gridView = gridView;
    }

    @Override
    public void addFooterView(View v, Object data, boolean isSelectable) {
        gridView.addFooterView(v, data, isSelectable);
    }

    @Override
    public boolean removeFooterView(View v) {
        return gridView.removeFooterView(v);
    }

    @Override
    public int getLastVisiblePosition() {
        return gridView.getLastVisiblePosition();
    }

    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        gridView.setOnScrollListener(l);
    }

    @Override
    public int getRowCount() {
        return gridView.getCount();
    }

    @Override
    public T getView() {
        return gridView;
    }
}
