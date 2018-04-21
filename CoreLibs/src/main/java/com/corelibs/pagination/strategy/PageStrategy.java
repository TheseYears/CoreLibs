package com.corelibs.pagination.strategy;

import com.corelibs.pagination.core.PaginationStrategy;

/**
 * 根据页数分页。需要传入int类型的pageCount作为分页条件。<BR />
 * 具体算法：如果当前页数小于总页数则可以获取下一页。
 */
public class PageStrategy implements PaginationStrategy {

    protected Page page = new Page(1, 15, -1);

    /**
     * 获取当前每页个数
     */
    public void setPageSize(int size) {
        page.pageSize = size;
    }

    @Override
    public boolean canDoPagination(boolean reload) {
        return reload || page.pageNo < page.pageCount;
    }

    @Override
    public void doPagination(boolean reload) {
        if (reload) page.pageNo = 1;
        else page.pageNo++;
    }

    @Override
    public Page getCondition() {
        return page;
    }

    @Override
    public void setCondition(Object c) {
        page.pageCount = (int) c;
    }

    public class Page {

        /** 当前页数 **/
        private int pageNo;
        /** 每页个数 **/
        private int pageSize;
        /** 总页数 **/
        private int pageCount;

        public Page(int pageNo, int pageSize, int pageCount) {
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.pageCount = pageCount;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getPageNo() {
            return pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }
    }
}
