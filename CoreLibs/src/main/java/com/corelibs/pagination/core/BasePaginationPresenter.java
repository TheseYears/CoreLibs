package com.corelibs.pagination.core;

import com.corelibs.base.BasePaginationView;
import com.corelibs.base.BasePresenter;
import com.corelibs.pagination.PaginationBridge;
import com.corelibs.pagination.StrategyFactory;

/**
 * 包含分页基础逻辑的类，继承自{@link BasePresenter}。请使用此类的直接子类分页。
 */
public abstract class BasePaginationPresenter<T extends BasePaginationView>
        extends BasePresenter<T> implements PaginationBridge {

    protected PaginationStrategy strategy;

    public BasePaginationPresenter() {
        setPaginationStrategy(StrategyFactory.getStrategy(StrategyFactory.PageStrategy));
    }

    /**
     * 设置分页策略
     */
    public void setPaginationStrategy(PaginationStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 分页操作, 具体页面效果需自行实现
     * @param reload 是否是刷新操作, true为刷新, false则为加载下一页
     * @return 分页是否成功
     */
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
