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
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.IdObject;
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

    protected final Context context;

    protected final int layoutResId;

    protected final List<T> data;

    protected final ItemViewDelegateManager<T, H> delegateManager;

    protected RecyclerView.Adapter wrapper;

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

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public long getItemId(int position) {
        T data = this.data.get(position);
        return data instanceof IdObject ? ((IdObject) data).getId() : position;
    }

    @Override
    public int getItemViewType(int position) {
        if (!hasMultiItemType())
            return super.getItemViewType(position);
        return delegateManager.getItemViewType(data.get(position), position);
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        return getAdapterHelper(viewType, parent);
    }

    @Override
    public void onBindViewHolder(H helper, int position) {
        T item = data.get(position);

        if (hasMultiItemType()) {
            BaseItemViewDelegate<T, H> delegate = delegateManager.getItemViewDelegate(item, position);
            delegate.convert(helper, item, position);
        } else {
            convert(helper, item, position);
        }

        helper.setAssociatedObject(item);
    }

    protected boolean hasMultiItemType() {
        return delegateManager.getItemViewDelegateCount() > 0;
    }

    public List<T> getData() {
        return data;
    }

    /** 如果使用多层次Adapter, 请调用此方法设置根Adapter, 避免数据不一致而导致RecyclerView崩溃 **/
    public void setWrapper(RecyclerView.Adapter wrapper) {
        this.wrapper = wrapper;
    }

    public void add(int location, T elem) {
        data.add(location, elem);
        onNotify();
    }

    public void add(T elem) {
        data.add(elem);
        onNotify();
    }

    public void addAll(List<T> elem) {
        data.addAll(elem);
        onNotify();
    }

    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        data.set(index, elem);
        onNotify();
    }

    public void remove(int index) {
        data.remove(index);
        onNotify();
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
        onNotify();
    }

    private void onNotify() {
        if (wrapper != null) wrapper.notifyDataSetChanged();
        else notifyDataSetChanged();
    }

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
