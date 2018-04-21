package com.corelibs.views.ptr.loadmore;

public interface OnScrollListener<T> {
    void onScroll(T view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}
