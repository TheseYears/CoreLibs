package com.corelibs.pagination.strategy;

import java.util.List;

/**
 * 根据服务器返回的数据集合是否为空分页。需要传入List集合作为分页条件。<BR />
 * 具体算法：如果传入的List集合不为空并且元素个数大于等于分页的每页个数，则可以获取下一页。
 */
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
