package com.corelibs.subscriber;

import com.corelibs.R;
import com.corelibs.base.BasePaginationView;
import com.corelibs.base.BaseView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 网络结果处理类, 此类会判断网络错误与业务错误.
 *
 * <P>
 *     {@link ResponseSubscriber}与{@link ResponseAction}均是调用此类来实现网络结果判断, 错误处理,
 *     以及重置加载状态.
 */
public class ResponseHandler<T> {

    private BaseView view;
    private CustomHandler<T> handler;

    public ResponseHandler(CustomHandler<T> handler) {
        this.handler = handler;
    }

    public ResponseHandler(CustomHandler<T> handler, BaseView view) {
        this.handler = handler;
        this.view = view;
    }

    public boolean checkDataNotNull(IBaseData data) {
        return data != null && data.data() != null;
    }

    public boolean checkListNotNull(List data) {
        return data != null && data.size() > 0;
    }

    public void onCompleted() {
        release();
    }

    public void onError(Throwable e) {
        resetLoadingStatus();
        e.printStackTrace();
        if (!handler.error(e)) {
            handleException(e);
        }
        release();
    }

    public void onNext(T t) {
        resetLoadingStatus();
        IBaseData data;
        if (t instanceof IBaseData) {
            data = (IBaseData) t;
            if (data.isSuccess()) {
                handler.success(t);
            } else {
                if (!handler.operationError(t, data.status(), data.msg())) {
                    handleOperationError(data.msg());
                }
            }
        } else {
            handler.success(t);
        }
        release();
    }

    public void resetLoadingStatus() {
        if (view != null && handler.autoHideLoading()) {
            if (view instanceof BasePaginationView) {
                BasePaginationView paginationView = (BasePaginationView) view;
                paginationView.onLoadingCompleted();
            }
            view.hideLoading();
        }
    }

    public void release() {
        view = null;
        handler = null;
    }

    public void handleException(Throwable e) {
        if (view != null) {
            if (e instanceof ConnectException) {
                view.showToastMessage(view.getViewContext().getString(R.string.network_error));
            } else if (e instanceof HttpException) {
                view.showToastMessage(view.getViewContext().getString(R.string.network_server_error));
            } else if (e instanceof SocketTimeoutException) {
                view.showToastMessage(view.getViewContext().getString(R.string.network_timeout));
            } else {
                view.showToastMessage(view.getViewContext().getString(R.string.network_other));
            }
        }
    }

    public void handleOperationError(String message) {
        if (view != null)
            view.showToastMessage(message);
    }

    public BaseView getView() {
        return view;
    }

    public interface CustomHandler<T> {
        /**
         * 请求成功同时业务成功的情况下会调用此函数
         */
        void success(T t);

        /**
         * 请求成功但业务失败的情况下会调用此函数.
         * @return 是否需要自行处理业务错误.
         * <P>
         * true - 需要, 父类不会处理错误
         * </P>
         * false - 不需要, 交由父类处理
         */
        boolean operationError(T t, int status, String message);

        /**
         * 请求失败的情况下会调用此函数
         * @return 是否需要自行处理系统错误.
         * <P>
         * true - 需要, 父类不会处理错误
         * </P>
         * false - 不需要, 交由父类处理
         */
        boolean error(Throwable e);

        /**
         * 是否需要自动隐藏加载框
         */
        boolean autoHideLoading();
    }

    public interface IBaseData {
        boolean isSuccess();
        int status();
        Object data();
        String msg();
    }
}
