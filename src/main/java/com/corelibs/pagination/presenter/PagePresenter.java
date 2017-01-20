package com.corelibs.pagination.presenter;

import com.corelibs.base.BasePaginationView;
import com.corelibs.pagination.StrategyFactory;
import com.corelibs.pagination.core.BasePaginationPresenter;
import com.corelibs.pagination.strategy.PageStrategy;

/**
 * 此类使用{@link PageStrategy}分页。使用{@link #getPageNo()}与{@link #getPageSize()}获取接口需要的分页参数。
 * 调用{@link #doPagination(boolean)}并根据其返回值判断是否能继续获取下一页。需要传入int类型的总页数的分页条件。
 */
public abstract class PagePresenter<T extends BasePaginationView> extends BasePaginationPresenter<T> {

    private PageStrategy pageStrategy;

    public PagePresenter() {
        setPaginationStrategy(StrategyFactory.getStrategy(StrategyFactory.PageStrategy));
        pageStrategy = (PageStrategy) strategy;
    }

    /** 获取当前页数下标 **/
    public int getPageNo() {
        return pageStrategy.getCondition().getPageNo();
    }

    /** 获取当前每页个数 **/
    public int getPageSize() {
        return pageStrategy.getCondition().getPageSize();
    }

    /** 设置每页个数 **/
    public void setPageSize(int size) {
        pageStrategy.setPageSize(size);
    }
}
