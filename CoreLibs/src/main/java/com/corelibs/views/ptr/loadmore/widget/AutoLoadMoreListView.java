package com.corelibs.views.ptr.loadmore.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.corelibs.views.ptr.loadmore.AutoLoadMoreHandler;
import com.corelibs.views.ptr.loadmore.AutoLoadMoreHook;
import com.corelibs.views.ptr.loadmore.adapter.ListViewAdapter;

/**
 * 配合{@link com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout}
 * 一起使用可以实现带自动加载更多以及带下拉刷新的ListView
 */
public class AutoLoadMoreListView extends ListView implements AutoLoadMoreHook {

    public AutoLoadMoreListView(Context context) {
        super(context);
    }

    public AutoLoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public AutoLoadMoreHandler getLoadMoreHandler() {
        return new AutoLoadMoreHandler<>(getContext(), new ListViewAdapter<ListView>(this));
    }
}
