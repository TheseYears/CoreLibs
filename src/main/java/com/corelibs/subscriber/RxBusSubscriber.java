package com.corelibs.subscriber;

import io.reactivex.Observer;

/**
 * 请使用此类来subscribe RxBus返回的Observable以简化onError与onCompleted函数.
 */
public abstract class RxBusSubscriber<T> implements Observer<T> {


    @Override
    public void onComplete() {
        completed();
    }

    @Override
    public void onError(Throwable e) {
        error(e);
    }

    @Override
    public void onNext(T t) {
        receive(t);
    }

    public abstract void receive(T data);
    public void error(Throwable e) {
        e.printStackTrace();
    }
    public void completed() {}

}
