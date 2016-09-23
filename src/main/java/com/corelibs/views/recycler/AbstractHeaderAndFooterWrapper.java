package com.corelibs.views.recycler;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.recycler.BaseRecyclerAdapter;

/**
 * RecyclerView Header和Footer的适配器, 传入真正的适配器, 并将此适配器设置到RecyclerView中即可.
 * @param <T> 数据源类型
 * @param <H> ViewHolder类型
 */
public abstract class AbstractHeaderAndFooterWrapper<T, H extends BaseAdapterHelper> extends RecyclerView.Adapter<H> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private BaseRecyclerAdapter<T, H> mInnerAdapter;

    private RecyclerView recyclerView;

    public AbstractHeaderAndFooterWrapper(BaseRecyclerAdapter<T, H> adapter) {
        mInnerAdapter = adapter;
    }

    public void setInnerAdapter(BaseRecyclerAdapter<T, H> mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
        attachedToRecyclerView();
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return getHeaderViewHolder(parent, mHeaderViews.get(viewType));
        } else if (mFootViews.get(viewType) != null) {
            return getFooterViewHolder(parent, mFootViews.get(viewType));
        }

        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount() {
        return mInnerAdapter == null ? 0 : mInnerAdapter.getItemCount();
    }

    @Override
    public void onBindViewHolder(H helper, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }

        mInnerAdapter.onBindViewHolder(helper, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        if (mInnerAdapter != null) {
            attachedToRecyclerView();
        }
    }

    private void attachedToRecyclerView() {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null) {
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
        if (mInnerAdapter != null) mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public void removeFootView(View view) {
        int key = mFootViews.indexOfValue(view);
        mFootViews.remove(key);
        notifyDataSetChanged();
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    protected abstract H getHeaderViewHolder(ViewGroup parent, View content);

    protected abstract H getFooterViewHolder(ViewGroup parent, View content);
}
