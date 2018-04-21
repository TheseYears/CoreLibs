package com.ryan.corelibs.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.corelibs.base.BaseActivity;
import com.corelibs.utils.DisplayUtil;
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.ryan.corelibs.R;
import com.ryan.corelibs.adapter.GithubAdapter;
import com.ryan.corelibs.model.entity.Repository;
import com.ryan.corelibs.presenter.MainPresenter;
import com.ryan.corelibs.view.interfaces.MainView;
import com.ryan.corelibs.widget.NavBar;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView {

    @BindView(R.id.nav) NavBar nav;
    @BindView(R.id.ptr) PtrAutoLoadMoreLayout<RecyclerView> ptr;

    private GithubAdapter adapter;
    private Paint paint;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setTitle("search CoreLibs");

        adapter = new GithubAdapter(this);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFFDDDDDD);
        paint.setStrokeWidth(DisplayUtil.dip2px(this, 0.5f));

        ptr.getPtrView().setLayoutManager(new LinearLayoutManager(this));
        ptr.getPtrView().addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                int count = parent.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (position > 0) {
                        c.drawLine(child.getLeft(), child.getTop(), child.getRight(),
                                child.getTop(), paint);
                    }
                }
            }
        });

        ptr.getPtrView().setAdapter(adapter);
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
    public void showLoading() {
        ptr.setRefreshing();
    }

    @Override
    public void hideLoading() {
        ptr.complete();
    }

    @Override
    public void onLoadingCompleted() {
        hideLoading();
    }

    @Override
    public void onAllPageLoaded() {
        ptr.disableLoading();
    }

    @Override
    public void renderResult(List<Repository> repositories, boolean reload) {
        if (reload) adapter.replaceAll(repositories);
        else adapter.addAll(repositories);
    }
}
