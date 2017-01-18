package com.corelibs.pagination.strategy;

import java.util.List;

public class ListResultStrategy extends PageStrategy {

    private List lastResult;

    @Override
    public boolean canDoPagination(boolean reload) {
        return reload || (lastResult != null && lastResult.size() >= page.getPageSize());
    }

    @Override
    public void setCondition(Object c) {
        lastResult = (List) c;
    }
}
