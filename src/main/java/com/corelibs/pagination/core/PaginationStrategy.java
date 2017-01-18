package com.corelibs.pagination.core;

public interface PaginationStrategy {
    boolean canDoPagination(boolean reload);
    void doPagination(boolean reload);
    Object getCondition();
    void setCondition(Object c);
}
