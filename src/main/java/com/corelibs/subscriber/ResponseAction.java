package com.corelibs.subscriber;

import com.corelibs.base.BaseView;
import com.corelibs.subscriber.ResponseHandler.IBaseData;

import rx.functions.Func1;

/**
 * 适用于一个网络请求依赖于另一个网络请求结果的情况.
 *
 * <P>
 * 此类会判断网络错误与业务错误, 并分发给{@link #error(Throwable)}
 * 与{@link #operationError(Object, int, String)}函数. 当请求成功同时业务成功的情况下会调用
 * {@link #success(Object)}函数. 如果在创建ResponseAction对象的同时传入MVPView对象,
 * 此类会托管隐藏加载框与错误处理, 如果希望自行处理错误, 请覆写{@link #error(Throwable)}函数,
 * 并且返回true.
 *
 */
public abstract class ResponseAction<T, R> implements Func1<T, R>,
        ResponseHandler.CustomHandler<T> {

    private ResponseHandler<T> handler;

    public ResponseAction() {
        handler = new ResponseHandler<>(this);
    }

    public ResponseAction(BaseView view) {
        handler = new ResponseHandler<>(this, view);
    }

    @Override
    public R call(T t) {
        IBaseData data;
        if (t instanceof IBaseData) {
            data = (IBaseData) t;
            if (data.isSuccess()) {
                return successCall(t);
            } else {
                if (!operationError(t, data.status(), data.msg())) {
                    handler.handleOperationError(data.msg());
                }
                return errorCall();
            }
        } else {
            return successCall(t);
        }
    }

    public R successCall(T t) {
        handler.release();
        handler = null;
        return onCall(t);
    }

    public R errorCall() {
        handler.release();
        handler = null;
        return null;
    }

    public abstract R onCall(T t);

    @Override
    public void success(T t) {}

    @Override
    public boolean operationError(T t, int status, String message) {
        return false;
    }

    @Override
    public boolean error(Throwable e) {
        return false;
    }

    @Override
    public boolean autoHideLoading() {
        return true;
    }
}
