package com.corelibs.pagination;

import com.corelibs.pagination.core.PaginationStrategy;

/**
 * 分页策略工厂，通过{@link #getStrategy(StrategyFactory)}来获取具体的策略。
 */
public enum StrategyFactory {

    PageStrategy("com.corelibs.pagination.strategy.PageStrategy"),
    ListResultStrategy("com.corelibs.pagination.strategy.ListResultStrategy");

    String value;

    public String getValue() {
        return value;
    }

    StrategyFactory(String value) {
        this.value = value;
    }

    /**
     * 获取具体的策略
     */
    public static PaginationStrategy getStrategy(StrategyFactory strategy) {
        PaginationStrategy result = null;
        try {
            result = (PaginationStrategy) Class.forName(strategy.getValue()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
