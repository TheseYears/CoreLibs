package com.corelibs.pagination.presenter;

import com.corelibs.base.BasePaginationView;
import com.corelibs.pagination.StrategyFactory;

public abstract class ListPagePresenter<T extends BasePaginationView> extends PagePresenter<T> {

    public ListPagePresenter() {
        setPaginationStrategy(StrategyFactory.getStrategy(StrategyFactory.ListResultStrategy));
    }
}
