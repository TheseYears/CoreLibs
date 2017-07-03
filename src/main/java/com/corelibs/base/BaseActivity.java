package com.corelibs.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.corelibs.common.AppManager;
import com.corelibs.utils.ToastMgr;
import com.corelibs.views.LoadingDialog;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import rx.Observable;

/**
 * Activity基类, 继承自此类的Activity需要实现{@link #getLayoutId},{@link #init}
 * 以及{@link #createPresenter()}, 不需要覆写onCreate方法.
 * <br/>
 * 实现此类需遵循MVP设计, 第一个泛型V需传入一个继承自{@link BaseView}的MVPView,
 * 第二个泛型需传入继承自{@link BasePresenter}的MVPPresenter.
 * <br/>
 * Presenter的生命周期已交由此类管理, 子类无需管理. 如果子类要使用多个Presenter, 则需要自行管理生命周期.
 * 此类已经实现了BaseView中的抽象方法, 子类无需再实现, 如需自定可覆写对应的方法.
 * <br/>
 * Created by Ryan on 2015/12/28.
 */
public abstract class BaseActivity<V extends BaseView, T extends BasePresenter<V>>
        extends RxAppCompatActivity implements BaseView {

    protected T presenter;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        AppManager.getAppManager().addActivity(this);
        presenter = createPresenter();
        if (presenter != null) presenter.attachView((V) this);

        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(this);

        setTranslucentStatusBar();
        init(savedInstanceState);
        if (presenter != null) presenter.onStart();
    }

    /**
     * 指定Activity需加载的布局ID, {@link BaseActivity}BaseActivity
     * 会通过{@link #setContentView}方法来加载布局
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

    /**
     * 获取通过{@link #createPresenter()}生成的presenter对象
     * @return Presenter
     */
    public T getPresenter() {
        return presenter;
    }

    /**
     * 获取LoadingDialog, 用来显示加载中
     * @return LoadingDialog
     */
    public LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.detachView();
        presenter = null;
        AppManager.getAppManager().finishActivity(this);
    }

    public void showToast(String s) {
        ToastMgr.show(s);
    }

    public void showToast(int r) {
        ToastMgr.show(r);
    }

    public String getText(TextView editText) {
        String text = editText.getText().toString().trim();
        return TextUtils.isEmpty(text) ? null : text;
    }

    public void call(String tel) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        startActivity(callIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) presenter.onViewStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) presenter.onViewResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null) presenter.onViewPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) presenter.onViewStop();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showLoading() {
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        showToast(message);
    }

    @Override
    public void showToastMessage(int message) {
        showToast(message);
    }

    @Override
    public void hideEmptyHint() {}

    @Override
    public void showEmptyHint() {}

    @Override
    public <T> Observable.Transformer<T, T> bind() {
        return bindToLifecycle();
    }

    @Override
    public <T> Observable.Transformer<T, T> bindUntil(ActivityEvent event) {
        return bindUntilEvent(event);
    }

    @Override
    public <T> Observable.Transformer<T, T> bindUntil(FragmentEvent event) {
        return null;
    }

    /**
     * 设置全屏模式，并将状态栏设置为透明，支持4.4及以上系统
     */
    protected void setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            setFullScreen();
        }
    }

    /**
     * 设置状态栏为浅色模式，状态栏上的图标都会变为深色。仅支持6.0及以上系统
     */
    protected void setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 设置全屏模式
     */
    protected void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * 设置系统状态颜色，仅支持6.0及以上系统
     */
    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(color);
        }
    }
}
