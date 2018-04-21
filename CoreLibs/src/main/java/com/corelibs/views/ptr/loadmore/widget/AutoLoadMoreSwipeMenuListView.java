package com.corelibs.views.ptr.loadmore.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.corelibs.views.ptr.loadmore.AutoLoadMoreHandler;
import com.corelibs.views.ptr.loadmore.AutoLoadMoreHook;
import com.corelibs.views.ptr.loadmore.adapter.ListViewAdapter;
import com.corelibs.views.swipemenulistview.SwipeMenuListView;

public class AutoLoadMoreSwipeMenuListView extends SwipeMenuListView implements AutoLoadMoreHook {

    public AutoLoadMoreSwipeMenuListView(Context context) {
        super(context);
    }

    public AutoLoadMoreSwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadMoreSwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public AutoLoadMoreHandler getLoadMoreHandler() {
        return new AutoLoadMoreHandler<>(getContext(), new ListViewAdapter<SwipeMenuListView>(this));
    }
}
