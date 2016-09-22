package com.corelibs.views.ptr.loadmore.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.corelibs.views.ptr.loadmore.AutoLoadMoreHandler;
import com.corelibs.views.ptr.loadmore.AutoLoadMoreHook;
import com.corelibs.views.ptr.loadmore.adapter.RecyclerViewAdapter;
import com.corelibs.views.recycler.EnhancesRecyclerView;

/**
 * 配合{@link com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout}
 * 一起使用可以实现带自动加载更多以及带下拉刷新的RecyclerView
 */
public class AutoLoadMoreRecyclerView extends EnhancesRecyclerView implements AutoLoadMoreHook {

    public AutoLoadMoreRecyclerView(Context context) {
        super(context);
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public AutoLoadMoreHandler getLoadMoreHandler() {
        return new AutoLoadMoreHandler<>(getContext(), new RecyclerViewAdapter<EnhancesRecyclerView>(this));
    }
}
