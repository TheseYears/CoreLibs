package com.corelibs.pagination;

/**
 * 此接口为{@link com.corelibs.pagination.core.BasePaginationPresenter}
 * 与{@link com.corelibs.subscriber.PaginationSubscriber}提供了设置分页条件的通道。
 */
public interface PaginationBridge {
    /** 设置分页条件 **/
    void setCondition(Object c);
}
