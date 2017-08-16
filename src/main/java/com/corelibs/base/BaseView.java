package com.corelibs.base;

import android.content.Context;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;


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

    <T> LifecycleTransformer<T> bind();

    <T> LifecycleTransformer<T> bindUntil(FragmentEvent event);

    <T> LifecycleTransformer<T> bindUntil(ActivityEvent event);
}
