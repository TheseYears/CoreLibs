package com.corelibs.views.ptr.loadmore;

/**
 * {@link com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout}的child view需要实现此接口,
 * 供PtrAutoLoadMoreLayout获取{@link AutoLoadMoreHandler}.
 * <BR/>
 * Created by Ryan on 2016/4/13.
 */
public interface AutoLoadMoreHook {
    /**
     * {@link com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout}需通过此方法获取
     * {@link AutoLoadMoreHandler}对象.
     */
    AutoLoadMoreHandler getLoadMoreHandler();
}
