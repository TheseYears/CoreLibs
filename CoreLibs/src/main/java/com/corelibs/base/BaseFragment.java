package com.corelibs.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.ObservableTransformer;

/**
 * Fragment基类, 继承自此类的Fragment需要实现{@link #getLayoutId}, {@link #init}
 * 以及{@link #createPresenter()}, 不需要覆写onCreate方法.
 * <br/>
 * 鉴于FragmentManager的attach与detach会销毁Fragment的视图, 此基类会在onCreate中生成一个
 * parentView, 缓存起来, 并在onCreateView中直接返回该View, 来达到保存Fragment视图状态的目的,
 * 同时避免不停的销毁与创建.
 * <br/>
 * 实现此类需遵循MVP设计, 第一个泛型V需传入一个继承自{@link BaseView}的MVPView,
 * 第二个泛型需传入继承自{@link BasePresenter}的MVPPresenter.
 * <br/>
 * Presenter的生命周期已交由此类管理, 子类无需管理. 如果子类要使用多个Presenter, 则需要自行管理生命周期.
 * 此类已经实现了BaseView中的抽象方法, 子类无需再实现, 如需自定可覆写对应的方法.
 * <br/>
 * Created by Ryan on 2016/1/5.
 */
public abstract class BaseFragment<V extends BaseView, T extends BasePresenter<V>>
        extends RxFragment implements BaseView {

    private View parentView;
    private Unbinder unbinder;
    protected T presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(getActivity()).inflate(getLayoutId(), null, false);
        presenter = createPresenter();
        if (presenter != null) presenter.attachView((V) this);
        unbinder = ButterKnife.bind(this, parentView);

        init(savedInstanceState);
        if (presenter != null) presenter.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (parentView == null)
            parentView = inflater.inflate(getLayoutId(), null, false);
        return parentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.detachView();
        presenter = null;
        if (unbinder != null) unbinder.unbind();
    }

    public T getPresenter() {
        return presenter;
    }

    public View getParentView() {
        return parentView;
    }

    /**
     * 指定Fragment需加载的布局ID
     *
     * @return 需加载的布局ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化方法, 类似OnCreate, 仅在此方法中做初始化操作, findView与事件绑定请使用ButterKnife
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 创建Presenter, 然后通过调用{@link #getPresenter()}来使用生成的Presenter
     * @return Presenter
     */
    protected abstract T createPresenter();

    public void call(String tel) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        startActivity(callIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) presenter.onViewStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.onViewResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) presenter.onViewPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) presenter.onViewStop();
    }

    @Override
    public void showLoading() {
        if (getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).showLoading();
    }

    @Override
    public void hideLoading() {
        if (getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
    }

    @Override
    public void showToastMessage(String message) {
        if (getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).showToastMessage(message);
    }

    @Override
    public void showToastMessage(int message) {
        if (getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).showToastMessage(message);
    }

    @Override
    public void hideEmptyHint() {}

    @Override
    public void showEmptyHint() {}

    @Override
    public Context getViewContext() {
        return getActivity();
    }

    @Override
    public <V> ObservableTransformer<V, V> bind() {
        return bindToLifecycle();
    }

    @Override
    public <V> ObservableTransformer<V, V> bindUntil(FragmentEvent event) {
        return bindUntilEvent(event);
    }

    @Override
    public <V> ObservableTransformer<V, V> bindUntil(ActivityEvent event) {
        return null;
    }

    @Override
    public void finishView() {
        getActivity().finish();
    }

    /**
     * 设置全屏模式，并将状态栏设置为透明，支持4.4及以上系统
     */
    protected void setTranslucentStatusBar() {
        if (getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).setTranslucentStatusBar();
    }
}
