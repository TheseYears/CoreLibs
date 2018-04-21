package com.corelibs.views.ptr.loadmore.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.corelibs.views.ptr.loadmore.AutoLoadMoreHandler;
import com.corelibs.views.ptr.loadmore.AutoLoadMoreHook;
import com.corelibs.views.ptr.loadmore.adapter.ListViewAdapter;

public class AutoLoadMoreExpandableListView extends ExpandableListView implements AutoLoadMoreHook {

    public AutoLoadMoreExpandableListView(Context context) {
        super(context);
    }

    public AutoLoadMoreExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadMoreExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public AutoLoadMoreHandler getLoadMoreHandler() {
        return new AutoLoadMoreHandler<>(getContext(), new ListViewAdapter<ExpandableListView>(this));
    }
}
