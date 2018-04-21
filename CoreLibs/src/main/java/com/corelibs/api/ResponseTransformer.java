package com.corelibs.api;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 用于对网络请求的Observable做转换.
 * <br/>
 * 可以将原始Observable声明在IO线程运行, 在main线程接收.
 * <br/>
 * Created by Ryan on 2015/12/30.
 */
public class ResponseTransformer<T> implements ObservableTransformer<T, T> {

    @Override
    public ObservableSource<T> apply(Observable<T> source) {
            return source.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
    }
}
