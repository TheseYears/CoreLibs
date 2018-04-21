package com.corelibs.views.ptr.loadmore.adapter;

import android.view.View;
import android.widget.AbsListView;

import com.corelibs.views.GridViewWithHeaderAndFooter;
import com.corelibs.views.ptr.loadmore.OnScrollListener;

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
    public void setOnScrollListener(final OnScrollListener<T> l) {
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override public void onScroll(AbsListView view, int firstVisibleItem,
                                           int visibleItemCount, int totalItemCount) {
                if (l != null)
                    l.onScroll(gridView, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
    }

    @Override
    public int getRowCount() {
        return gridView.getCount();
    }

    @Override
    public T getView() {
        return gridView;
    }

    @Override
    public boolean addFooterAtInit() {
        return true;
    }
}
