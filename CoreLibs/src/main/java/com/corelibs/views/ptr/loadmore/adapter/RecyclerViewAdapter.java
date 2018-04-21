package com.corelibs.views.ptr.loadmore.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.corelibs.utils.adapter.recycler.BaseRecyclerAdapter;
import com.corelibs.views.ptr.loadmore.OnScrollListener;

/**
 * 针对RecyclerView或继承自RecyclerView的控件的适配类
 * <BR/>
 * Created by Ryan on 2016/9/21.
 */
public class RecyclerViewAdapter<T extends RecyclerView>
        implements LoadMoreAdapter<T> {

    private T recyclerView;

    public RecyclerViewAdapter(T recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void addFooterView(View v, Object data, boolean isSelectable) {
        BaseRecyclerAdapter adapter =
                (BaseRecyclerAdapter) recyclerView.getAdapter();
        adapter.addFooterView(v);
    }

    @Override
    public boolean removeFooterView(View v) {
        BaseRecyclerAdapter adapter =
                (BaseRecyclerAdapter) recyclerView.getAdapter();
        adapter.removeFooterView(v);
        return true;
    }

    @Override @SuppressWarnings("unchecked")
    public void setOnScrollListener(final OnScrollListener<T> l) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItem = 0, lastVisibleItem, visibleItemCount = 0;
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    firstVisibleItem = manager.findFirstVisibleItemPosition();
                    lastVisibleItem = manager.findLastVisibleItemPosition();
                    visibleItemCount = lastVisibleItem - firstVisibleItem + 1;
                }

                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                    firstVisibleItem = manager.findFirstVisibleItemPositions(null)[0];
                    lastVisibleItem = manager.findLastVisibleItemPositions(null)[1];
                    visibleItemCount = lastVisibleItem - firstVisibleItem + 1;
                }

                if (l != null)
                    l.onScroll((T) recyclerView, firstVisibleItem, visibleItemCount, recyclerView.getAdapter().getItemCount());

            }
        });
    }

    @Override
    public int getRowCount() {
        return recyclerView.getAdapter().getItemCount();
    }

    @Override
    public T getView() {
        return recyclerView;
    }

    @Override
    public boolean addFooterAtInit() {
        return false;
    }
}
