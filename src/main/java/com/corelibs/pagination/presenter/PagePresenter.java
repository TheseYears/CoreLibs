package com.corelibs.pagination.presenter;

import com.corelibs.base.BasePaginationView;
import com.corelibs.pagination.StrategyFactory;
import com.corelibs.pagination.core.BasePaginationPresenter;
import com.corelibs.pagination.strategy.PageStrategy;

public abstract class PagePresenter<T extends BasePaginationView> extends BasePaginationPresenter<T> {

    private PageStrategy pageStrategy;

    public PagePresenter() {
        setPaginationStrategy(StrategyFactory.getStrategy(StrategyFactory.PageStrategy));
        pageStrategy = (PageStrategy) strategy;
    }

    public int getPageNo() {
        return pageStrategy.getCondition().getPageNo();
    }

    public int getPageSize() {
        return pageStrategy.getCondition().getPageSize();
    }

    public void setPageSize(int size) {
        pageStrategy.setPageSize(size);
    }
}
