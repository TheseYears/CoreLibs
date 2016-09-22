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
package com.corelibs.utils.adapter.normal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.IdObject;
import com.corelibs.utils.adapter.multi.BaseItemViewDelegate;
import com.corelibs.utils.adapter.multi.ItemViewDelegateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction class of a BaseAdapter in which you only need
 * to provide the convert() implementation.<br/>
 * Using the provided BaseAdapterHelper, your code is minimalist.
 * @param <T> The type of the items in the list.
 */
public abstract class BaseQuickAdapter<T, H extends BaseAdapterHelper> extends BaseAdapter {

    protected final Context context;

    protected final int layoutResId;

    protected final List<T> data;

    protected final ItemViewDelegateManager<T, H> delegateManager;

    /**
     * Create a QuickAdapter.
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    public BaseQuickAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public BaseQuickAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.context = context;
        this.layoutResId = layoutResId;
        delegateManager = new ItemViewDelegateManager<>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        if (position >= data.size()) return null;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        T data = this.data.get(position);
        return data instanceof IdObject ? ((IdObject) data).getId() : position;
    }

    @Override
    public int getViewTypeCount() {
        int count = delegateManager.getItemViewDelegateCount();
        return count < 1 ? 1 : count;
    }

    @Override
    public int getItemViewType(int position) {
        if (!hasMultiItemType())
            return super.getItemViewType(position);
        return delegateManager.getItemViewType(getItem(position), position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H helper;
        T item = getItem(position);

        if (hasMultiItemType()) {
            BaseItemViewDelegate<T, H> delegate = delegateManager.getItemViewDelegate(item, position);
            helper = getAdapterHelper(position, delegate, convertView, parent);
            delegate.convert(helper, item, position);
        } else {
            helper = getAdapterHelper(position, null, convertView, parent);
            convert(helper, item, position);
        }

        helper.setAssociatedObject(item);
        return helper.getView();
    }

    private boolean hasMultiItemType() {
        return delegateManager.getItemViewDelegateCount() > 0;
    }

    public List<T> getData() {
        return data;
    }

    @Override
    public boolean isEnabled(int position) {
        return position < data.size();
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

    public void remove(T elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return data.contains(elem);
    }

    /** Clear data list */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     * @param helper     A fully initialized helper.
     * @param item       The item that needs to be displayed.
     * @param position   The position that needs to be displayed.
     */
    protected abstract void convert(H helper, T item, int position);

    /**
     * You can override this method to use a custom BaseAdapterHelper in order to fit your needs
     * @param position    The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return An instance of BaseAdapterHelper
     */
    protected abstract H getAdapterHelper(int position, BaseItemViewDelegate<T, H> delegate, View convertView, ViewGroup parent);

}
