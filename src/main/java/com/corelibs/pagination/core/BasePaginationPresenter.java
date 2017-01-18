package com.corelibs.pagination.core;

import com.corelibs.base.BasePaginationView;
import com.corelibs.base.BasePresenter;
import com.corelibs.pagination.PaginationBridge;
import com.corelibs.pagination.StrategyFactory;

public abstract class BasePaginationPresenter<T extends BasePaginationView>
        extends BasePresenter<T> implements PaginationBridge {

    protected PaginationStrategy strategy;

    public BasePaginationPresenter() {
        setPaginationStrategy(StrategyFactory.getStrategy(StrategyFactory.PageStrategy));
    }

    public void setPaginationStrategy(PaginationStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean doPagination(boolean reload) {
        boolean can = strategy.canDoPagination(reload);
        if (can) strategy.doPagination(reload);
        else view.onAllPageLoaded();
        return can;
    }

    @Override
    public void setCondition(Object c) {
        strategy.setCondition(c);
    }
}
