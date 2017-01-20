package com.corelibs.subscriber;

import com.corelibs.base.BaseView;
import com.corelibs.pagination.PaginationBridge;

import java.util.List;

/**
 * 使用此类来订阅分页的请求结果。参考{@link ResponseSubscriber}。
 */
public abstract class PaginationSubscriber<T> extends ResponseSubscriber<T> {

    private BaseView view;
    private PaginationBridge bridge;
    private boolean reload;

    /**
     * @param view mvp view接口
     * @param bridge {@link com.corelibs.pagination.core.BasePaginationPresenter}默认实现了此接口
     * @param reload 是否是刷新操作
     */
    public PaginationSubscriber(BaseView view, PaginationBridge bridge, boolean reload) {
        super(view);

        this.view = view;
        this.bridge = bridge;
        this.reload = reload;
    }

    @Override
    public void success(T t) {
        if (t instanceof ResponseHandler.IBaseData) {
            ResponseHandler.IBaseData data = (ResponseHandler.IBaseData) t;
            if (checkDataNotNull(data) && checkListNotNull(getListResult(t, checkDataNotNull(data)))) {
                Object condition = getCondition(t, checkDataNotNull(data));
                if (bridge != null) bridge.setCondition(condition);

                view.hideEmptyHint();
                onDataNotNull(t);
            } else {
                onDataIsNull();
            }
        } else {
            onDataNotNull(t);
            if (bridge != null) bridge.setCondition(null);
        }

        view = null;
    }

    /**
     * 当请求成功，并且数据不为空的情况下会回调此函数
     */
    protected abstract void onDataNotNull(T t);

    /**
     * 提供分页条件，如服务器返回的总页数，或者是服务器返回的分页集合数据。需要根据不同的分页策略返回不同的条件。
     */
    protected abstract Object getCondition(T t, boolean dataNotNull);

    /**
     * 提供分页结果的List数据
     */
    protected abstract List getListResult(T t, boolean dataNotNull);

    /**
     * 当请求成功，并且数据为空的情况下会回调此函数，默认会调用BaseView的showEmptyHint方法。
     */
    protected void onDataIsNull() {
        if (reload) view.showEmptyHint();
    }
}
