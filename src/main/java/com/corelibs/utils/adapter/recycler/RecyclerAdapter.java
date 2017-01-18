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
 * Abstraction class of a RecyclerView.Adapter in which you only need
 * to provide the convert() implementation.<br/>
 * Using the provided BaseAdapterHelper, your code is minimalist.
 * @param <T> The type of the items in the list.
 */
public abstract class RecyclerAdapter<T> extends BaseRecyclerAdapter<T, BaseAdapterHelper> {

    public RecyclerAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public RecyclerAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected BaseAdapterHelper getAdapterHelper(int viewType, ViewGroup parent) {
        int res;
        if (hasMultiItemType()) {
            BaseItemViewDelegate<T, BaseAdapterHelper> delegate = delegateManager.getItemViewDelegate(viewType);
            res = delegate.getItemViewLayoutId();
        } else {
            res = layoutResId;
        }

        return BaseAdapterHelper.get(context, null, parent, res);
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
