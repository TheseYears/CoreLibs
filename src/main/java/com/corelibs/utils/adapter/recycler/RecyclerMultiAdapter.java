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
public class RecyclerMultiAdapter<T> extends BaseRecyclerAdapter<T, BaseAdapterHelper> {

    public RecyclerMultiAdapter(Context context) {
        super(context, 0);
    }

    public RecyclerMultiAdapter(Context context, List<T> data) {
        super(context, 0, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, T item, int position) {}

    @Override
    protected BaseAdapterHelper getAdapterHelper(int viewType, ViewGroup parent) {
        BaseItemViewDelegate<T, BaseAdapterHelper> delegate = delegateManager.getItemViewDelegate(viewType);
        return BaseAdapterHelper.get(context, null, parent, delegate.getItemViewLayoutId());
    }

    @Override
    protected BaseAdapterHelper getHeaderViewHolder(ViewGroup parent, View content) {
        return BaseAdapterHelper.get(parent.getContext(), null, parent, content);
    }

    @Override
    protected BaseAdapterHelper getFooterViewHolder(ViewGroup parent, View content) {
        return BaseAdapterHelper.get(parent.getContext(), null, parent, content);
    }

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
