package com.corelibs.views.ptr.loadmore.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.corelibs.views.ptr.loadmore.AutoLoadMoreHandler;
import com.corelibs.views.ptr.loadmore.AutoLoadMoreHook;
import com.corelibs.views.ptr.loadmore.adapter.RecyclerViewAdapter;

/**
 * 配合{@link com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout}
 * 一起使用可以实现带自动加载更多以及带下拉刷新的RecyclerView
 */
public class AutoLoadMoreRecyclerView extends RecyclerView implements AutoLoadMoreHook {

    public AutoLoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public AutoLoadMoreHandler getLoadMoreHandler() {
        return new AutoLoadMoreHandler<>(getContext(), new RecyclerViewAdapter<RecyclerView>(this));
    }
}
