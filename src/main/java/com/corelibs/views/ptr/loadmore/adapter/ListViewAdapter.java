package com.corelibs.views.ptr.loadmore.adapter;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.corelibs.views.ptr.loadmore.OnScrollListener;

/**
 * 针对ListView或继承自ListView的控件的适配类
 * <BR/>
 * Created by Ryan on 2016/1/21.
 */
public class ListViewAdapter<T extends ListView> implements LoadMoreAdapter<T> {

    private T listView;

    public ListViewAdapter(T listView) {
        this.listView = listView;
    }

    @Override
    public void addFooterView(View v, Object data, boolean isSelectable) {
        listView.addFooterView(v, data, isSelectable);
    }

    @Override
    public boolean removeFooterView(View v) {
        return listView.removeFooterView(v);
    }

    @Override
    public void setOnScrollListener(final OnScrollListener<T> l) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override public void onScroll(AbsListView view, int firstVisibleItem,
                                           int visibleItemCount, int totalItemCount) {
                if (l != null)
                    l.onScroll(listView, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
    }

    @Override
    public int getRowCount() {
        return listView.getCount();
    }

    @Override
    public T getView() {
        return listView;
    }

    @Override
    public boolean addFooterAtInit() {
        return true;
    }
}
