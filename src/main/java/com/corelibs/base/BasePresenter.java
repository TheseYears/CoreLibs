package com.corelibs.base;

import android.content.Context;

import com.corelibs.api.ApiFactory;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;

import rx.Observable;

/**
 * Presenter基类, Fragment需使用继承自此类的子类, 泛型需传入继承自{@link BaseView}的MVPView.
 * <br/>
 * 子类可直接调用通过attachView传递过来的view来操作Activity, 无需再声明绑定.
 * <br/>
 * 如子类需要自行管理生命周期, 请在Activity/Fragment的onCreate中调用{@link #attachView}方法,
 * 并一定要在onDestroy中调用{@link #detachView}, 以防内存溢出.
 * <br/>
 * Created by Ryan on 2015/12/29.
 */
public abstract class BasePresenter<T extends BaseView> {

    protected T view;

    /**
     * 界面初始化完毕时调用, 此时界面控件等已初始化完毕
     */
    public abstract void onStart();

    /**
     * 当View与Presenter绑定时回调, 此时界面控件等均未初始化
     */
    protected void onViewAttach() {}

    protected void onViewStart() {}

    protected void onViewResume() {}

    protected void onViewPause() {}

    protected void onViewStop() {}

    protected void onViewDetach() {}

    public T getView() {
        return view;
    }

    /**
     * 将Presenter与MVPView绑定起来.
     * @param view MVPView
     */
    public void attachView(T view) {
        this.view = view;
        onViewAttach();
    }

    /**
     * 将Presenter与MVPView解除.
     */
    public void detachView() {
        onViewDetach();
        view = null;
    }

    /**
     * 根据String的resourceId来获取String对象
     * @param resId resourceId
     * @return 对应的字符串
     */
    protected String getString(int resId) {
        return view.getViewContext().getString(resId);
    }

    /**
     * 判断字符串是否为空
     */
    protected boolean stringIsNull (String str) {
        return str == null || str.trim().length() <= 0 || str.trim().equals("null") || str.trim().equals("NULL");
    }

    protected Context getContext() {
        return view.getViewContext();
    }

    protected <V> Observable.Transformer<V, V> bindToLifeCycle() {
        return view.bind();
    }

    protected <V> Observable.Transformer<V, V> bindUntilEvent(ActivityEvent event) {
        return view.bindUntil(event);
    }

    protected <V> Observable.Transformer<V, V> bindUntilEvent(FragmentEvent event) {
        return view.bindUntil(event);
    }

    /**
     * 获取Retrofit Api接口
     */
    protected <T> T getApi(Class<T> clz) {
        return ApiFactory.getFactory().create(clz);
    }

    /**
     * 获取Retrofit Api接口
     */
    protected <T> T getApi(int key, Class<T> clz) {
        return ApiFactory.getFactory().create(key, clz);
    }

    /**
     * 获取Retrofit Api接口
     */
    protected <T> T getApi(String key, Class<T> clz) {
        return ApiFactory.getFactory().create(key, clz);
    }
}
