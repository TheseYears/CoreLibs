package com.corelibs.pagination.core;

/**
 * 分页策略
 */
public interface PaginationStrategy {
    /**
     * 根据分页条件判断是否能分页
     */
    boolean canDoPagination(boolean reload);

    /**
     * 分页具体逻辑
     */
    void doPagination(boolean reload);

    /**
     * 获取分页条件
     */
    Object getCondition();

    /**
     * 设置分页条件
     */
    void setCondition(Object c);
}
