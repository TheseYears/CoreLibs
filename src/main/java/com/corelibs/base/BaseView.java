package com.corelibs.base;

import android.content.Context;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;

import rx.Observable;

/**
 * MVPView基础接口
 * <br/>
 * Created by Ryan on 2015/12/31.
 */
public interface BaseView {

    /**
     * 加载时显示加载框
     */
    void showLoading();
    /**
     * 加载完成时隐藏加载框
     */
    void hideLoading();
    /**
     * 显示提示消息
     */
    void showToastMessage(String message);
    /**
     * 显示提示消息
     */
    void showToastMessage(int message);
    /**
     * 隐藏空列表的提示
     */
    void hideEmptyHint();
    /**
     * 显示空列表的提示
     */
    void showEmptyHint();

    /**
     * 获取Context对象
     */
    Context getViewContext();

    <T> Observable.Transformer<T, T> bind();

    <T> Observable.Transformer<T, T> bindUntil(FragmentEvent event);

    <T> Observable.Transformer<T, T> bindUntil(ActivityEvent event);
}
