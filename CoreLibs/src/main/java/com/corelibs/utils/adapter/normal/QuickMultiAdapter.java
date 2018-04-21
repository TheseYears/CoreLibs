package com.corelibs.utils.adapter.normal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.multi.BaseItemViewDelegate;

import java.util.List;

/**
 * 简便的多布局适配器, 无需实现任何方法, 使用{@link #addItemViewDelegate(BaseItemViewDelegate)}
 * 来添加布局. <BR/>
 * 默认使用{@link BaseAdapterHelper}, 当然也可以使用自己的ViewHolder.
 */
public class QuickMultiAdapter<T> extends BaseQuickAdapter<T, BaseAdapterHelper> {

    /**
     * Create a QuickMultiItemTypeAdapter.
     * @param context     The context.
     */
    public QuickMultiAdapter(Context context) {
        super(context, 0);
    }

    /**
     * Same as QuickMultiItemTypeAdapter#QuickMultiItemTypeAdapter(Context,int) but with
     * some initialization data.
     * @param context     The context.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public QuickMultiAdapter(Context context, List<T> data) {
        super(context, 0, data);
    }

    @Override
    protected BaseAdapterHelper getAdapterHelper(int position, BaseItemViewDelegate<T, BaseAdapterHelper> delegate,
                                                 View convertView, ViewGroup parent) {
        int resId = delegate == null ? layoutResId : delegate.getItemViewLayoutId();
        return BaseAdapterHelper.get(context, convertView, parent, resId, position);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, T item, int position) {}

    public void addItemViewDelegate(BaseItemViewDelegate<T, BaseAdapterHelper> itemViewDelegate) {
        delegateManager.addDelegate(itemViewDelegate);
    }

    public void addItemViewDelegate(int viewType, BaseItemViewDelegate<T, BaseAdapterHelper> delegate) {
        delegateManager.addDelegate(viewType, delegate);
    }

    public void removeItemViewDelegate(BaseItemViewDelegate<T, BaseAdapterHelper> delegate) {
        delegateManager.removeDelegate(delegate);
    }

    public void removeItemViewDelegate(int itemType) {
        delegateManager.removeDelegate(itemType);
    }

}
