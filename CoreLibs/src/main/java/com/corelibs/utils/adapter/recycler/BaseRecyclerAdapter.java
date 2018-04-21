/**
 * Copyright 2013 Joan Zapata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.corelibs.utils.adapter.recycler;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.IdObject;
import com.corelibs.utils.adapter.WrapperUtils;
import com.corelibs.utils.adapter.multi.BaseItemViewDelegate;
import com.corelibs.utils.adapter.multi.ItemViewDelegateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction class of a RecyclerView.Adapter in which you only need
 * to provide the convert() implementation.<br/>
 * Using the provided BaseAdapterHelper, your code is minimalist.
 * @param <T> The type of the items in the list.
 */
public abstract class BaseRecyclerAdapter<T, H extends BaseAdapterHelper> extends RecyclerView.Adapter<H> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    protected final Context context;

    protected final int layoutResId;

    protected final List<T> data;

    protected final ItemViewDelegateManager<T, H> delegateManager;

    /**
     * Create a RecyclerAdapter.
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    public BaseRecyclerAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    /**
     * Same as BaseRecyclerAdapter#BaseRecyclerAdapter(Context,int) but with
     * some initialization data.
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public BaseRecyclerAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.context = context;
        this.layoutResId = layoutResId;
        delegateManager = new ItemViewDelegateManager<>();
    }

    public T getItem(int position) {
        if (position >= data.size()) return null;
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    private int getRealItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public long getItemId(int position) {
        T data = this.data.get(position);
        return data instanceof IdObject ? ((IdObject) data).getId() : position;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFooterViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }

        if (!hasMultiItemType())
            return super.getItemViewType(position);

        int realPosition = position - getHeadersCount();
        return delegateManager.getItemViewType(data.get(realPosition), realPosition);
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return getHeaderViewHolder(parent, mHeaderViews.get(viewType));
        } else if (mFooterViews.get(viewType) != null) {
            return getFooterViewHolder(parent, mFooterViews.get(viewType));
        }

        return getAdapterHelper(viewType, parent);
    }

    @Override
    public void onBindViewHolder(H helper, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }

        int realPosition = position - getHeadersCount();
        T item = data.get(realPosition);

        if (hasMultiItemType()) {
            BaseItemViewDelegate<T, H> delegate = delegateManager.getItemViewDelegate(item, realPosition);
            delegate.convert(helper, item, position);
        } else {
            convert(helper, item, position);
        }

        helper.setAssociatedObject(item);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (mFooterViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(H holder) {
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    protected boolean hasMultiItemType() {
        return delegateManager.getItemViewDelegateCount() > 0;
    }

    public List<T> getData() {
        return data;
    }

    public void add(int location, T elem) {
        data.add(location, elem);
        notifyDataSetChanged();
    }

    public void add(T elem) {
        data.add(elem);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        clear();
        addAll(elem);
    }

    public boolean contains(T elem) {
        return data.contains(elem);
    }

    /** Clear data list */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
        notifyDataSetChanged();
    }

    public void addFooterView(View view) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
        notifyDataSetChanged();
    }

    public void removeHeaderView(View view) {
        int key = mHeaderViews.indexOfValue(view);
        mHeaderViews.remove(key);
        notifyDataSetChanged();
    }

    public void removeFooterView(View view) {
        int key = mFooterViews.indexOfValue(view);
        mFooterViews.remove(key);
        notifyDataSetChanged();
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    protected abstract H getHeaderViewHolder(ViewGroup parent, View content);

    protected abstract H getFooterViewHolder(ViewGroup parent, View content);

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     * @param helper     A fully initialized helper.
     * @param item       The item that needs to be displayed.
     * @param position   The position that needs to be displayed.
     */
    protected abstract void convert(BaseAdapterHelper helper, T item, int position);

    /**
     * You can override this method to use a custom BaseAdapterHelper in order to fit your needs.
     */
    protected abstract H getAdapterHelper(int viewType, ViewGroup parent);

}
