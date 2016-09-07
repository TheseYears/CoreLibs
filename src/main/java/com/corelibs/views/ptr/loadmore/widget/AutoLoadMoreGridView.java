package com.corelibs.views.ptr.loadmore.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.corelibs.views.GridViewWithHeaderAndFooter;
import com.corelibs.views.ptr.loadmore.AutoLoadMoreHandler;
import com.corelibs.views.ptr.loadmore.AutoLoadMoreHook;
import com.corelibs.views.ptr.loadmore.adapter.GridViewAdapter;

public class AutoLoadMoreGridView extends GridViewWithHeaderAndFooter implements AutoLoadMoreHook {

    public AutoLoadMoreGridView(Context context) {
        super(context);
    }

    public AutoLoadMoreGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadMoreGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public AutoLoadMoreHandler getLoadMoreHandler() {
        return new AutoLoadMoreHandler<>(getContext(),
                new GridViewAdapter<GridViewWithHeaderAndFooter>(this));
    }
}
