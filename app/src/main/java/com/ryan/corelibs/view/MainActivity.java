package com.ryan.corelibs.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.corelibs.base.BaseActivity;
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.ryan.corelibs.R;
import com.ryan.corelibs.model.entity.Repository;
import com.ryan.corelibs.presenter.MainPresenter;
import com.ryan.corelibs.view.interfaces.MainView;
import com.ryan.corelibs.widget.NavBar;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView {

    @BindView(R.id.nav) NavBar nav;
    @BindView(R.id.ptr) PtrAutoLoadMoreLayout<RecyclerView> ptr;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setTitle("search CoreLibs");

        ptr.setRefreshLoadCallback(new PtrAutoLoadMoreLayout.RefreshLoadCallback() {
            @Override public void onLoading(PtrFrameLayout frame) {
                presenter.search(false);
            }

            @Override public void onRefreshing(PtrFrameLayout frame) {
                ptr.enableLoading();
                if (!frame.isAutoRefresh()) {
                    presenter.search(true);
                }
            }
        });
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onLoadingCompleted() {

    }

    @Override
    public void onAllPageLoaded() {

    }

    @Override
    public void renderResult(List<Repository> repositories, boolean reload) {
    }
}
