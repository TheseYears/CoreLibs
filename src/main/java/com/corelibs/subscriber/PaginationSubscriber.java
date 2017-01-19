package com.corelibs.subscriber;

import com.corelibs.base.BaseView;
import com.corelibs.pagination.PaginationBridge;

import java.util.List;

public abstract class PaginationSubscriber<T> extends ResponseSubscriber<T> {

    private BaseView view;
    private PaginationBridge bridge;
    private boolean reload;

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

    protected abstract void onDataNotNull(T t);

    protected abstract Object getCondition(T t, boolean dataNotNull);

    protected abstract List getListResult(T t, boolean dataNotNull);

    protected void onDataIsNull() {
        if (reload) view.showEmptyHint();
    }
}
