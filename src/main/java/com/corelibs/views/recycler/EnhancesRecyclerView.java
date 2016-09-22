package com.corelibs.views.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.corelibs.utils.adapter.recycler.BaseRecyclerAdapter;

/**
 * 带Header与Footer的的RecyclerView
 */
public class EnhancesRecyclerView extends RecyclerView {

    private HeaderAndFooterWrapper wrapper;

    public EnhancesRecyclerView(Context context) {
        this(context, null);
    }

    public EnhancesRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnhancesRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        wrapper = new HeaderAndFooterWrapper(null);
        super.setAdapter(wrapper);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof BaseRecyclerAdapter)) return;
        BaseRecyclerAdapter recyclerAdapter = (BaseRecyclerAdapter) adapter;
        wrapper.setInnerAdapter(recyclerAdapter);
        recyclerAdapter.setWrapper(wrapper);
    }

    public void addHeaderView(View view) {
        wrapper.addHeaderView(view);
    }

    public void addFooterView(View view) {
        wrapper.addFootView(view);
    }
}
