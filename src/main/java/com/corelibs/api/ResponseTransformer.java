package com.corelibs.api;


import com.trello.rxlifecycle2.LifecycleTransformer;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.SafeSubscriber;

/**
 * 用于对网络请求的Observable做转换.
 * Created by Ryan on 2015/12/30.
 */
public class ResponseTransformer<T> implements FlowableTransformer<T, T> {

    @Override
    public Publisher<T> apply(@NonNull Flowable<T> source) {

            return source.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
    }
}
