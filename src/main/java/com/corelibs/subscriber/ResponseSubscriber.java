package com.corelibs.subscriber;

import com.corelibs.base.BaseView;
import com.corelibs.subscriber.ResponseHandler.IBaseData;

import java.util.List;

import rx.Subscriber;

/**
 * 请使用此类来subscribe Retrofit返回Observable.
 * <p/>
 * <p/>
 * 此类会判断网络错误与业务错误, 并分发给{@link #error(Throwable)}
 * 与{@link #operationError(Object, int, String)}函数. 当请求成功同时业务成功的情况下会调用
 * {@link #success(Object)}函数. 如果在创建ResponseSubscriber对象的同时传入MVPView对象,
 * 此类会托管隐藏加载框与错误处理, 如果希望自行处理错误, 请覆写{@link #error(Throwable)}函数,
 * 并且返回true.
 */
public abstract class ResponseSubscriber<T> extends Subscriber<T>
        implements ResponseHandler.CustomHandler<T> {

    private ResponseHandler<T> handler;

    public ResponseSubscriber() {
        handler = new ResponseHandler<>(this);
    }

    public ResponseSubscriber(BaseView view) {
        handler = new ResponseHandler<>(this, view);
    }

    /**
     * 检查服务器返回的数据是否为空
     */
    public boolean checkDataNotNull(IBaseData data) {
        return handler.checkDataNotNull(data);
    }

    /**
     * 检查List中是否有数据
     */
    public boolean checkListNotNull(List data) {
        return handler.checkListNotNull(data);
    }

    @Override
    public void onCompleted() {
        handler.onCompleted();
        handler = null;
    }

    @Override
    public void onError(Throwable e) {
        handler.onError(e);
        handler = null;
    }

    @Override
    public void onNext(T t) {
        handler.onNext(t);
    }

    @Override
    public abstract void success(T t);

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
