package com.corelibs.views.recycler;

import android.view.View;
import android.view.ViewGroup;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.recycler.BaseRecyclerAdapter;

/**
 * RecyclerView Header和Footer的适配器, 传入真正的适配器, 并将此适配器设置到RecyclerView中即可.<BR/>
 * ViewHolder为BaseAdapterHelper
 * @param <T> 数据源类型
 */
public class HeaderAndFooterWrapper<T> extends AbstractHeaderAndFooterWrapper<T, BaseAdapterHelper> {

    public HeaderAndFooterWrapper(BaseRecyclerAdapter<T, BaseAdapterHelper> adapter) {
        super(adapter);
    }

    @Override
    protected BaseAdapterHelper getHeaderViewHolder(ViewGroup parent, View content) {
        return BaseAdapterHelper.get(parent.getContext(), null, parent, content);
    }

    @Override
    protected BaseAdapterHelper getFooterViewHolder(ViewGroup parent, View content) {
        return BaseAdapterHelper.get(parent.getContext(), null, parent, content);
    }
}
