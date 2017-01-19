package com.corelibs.views.ptr.loadmore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.corelibs.R;
import com.corelibs.views.CircularBar;
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.corelibs.views.ptr.loadmore.adapter.LoadMoreAdapter;

/**
 * 自动加载功能的处理类. 需要一个{@link LoadMoreAdapter}对象来适配ListView/GridView. <BR/>
 * 此类通过LoadMoreWrapper来处理何时出现正在加载的视图, 正在加载的视图的样式, 以及触发
 * {@link PtrAutoLoadMoreLayout.RefreshLoadCallback#onLoading(PtrFrameLayout)}
 * <BR/>
 * Created by Ryan on 2016/1/20.
 */
public class AutoLoadMoreHandler<H extends ViewGroup, T extends LoadMoreAdapter<H>> {

    public static final int DEFAULT_WHEN_TO_LOADING = 3;

    private Context context;
    private T adapter;
    private State state = State.ENABLED;

    private PtrFrameLayout ptrFrameLayout;

    private View loadingView;
    private View loadingContent;
    private TextView loadingLabel;
    private CircularBar progress;

    private PtrAutoLoadMoreLayout.RefreshLoadCallback callback;

    private int whenToLoading = DEFAULT_WHEN_TO_LOADING;
    private boolean isLoading = false;
    private boolean isCustomBackground = false;

    private OnScrollListener<H> scrollListener;

    public AutoLoadMoreHandler(Context context, T adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    public void setup(PtrFrameLayout ptrFrameLayout) {
        this.ptrFrameLayout = ptrFrameLayout;
        init();
    }

    public void setRefreshLoadCallback(PtrAutoLoadMoreLayout.RefreshLoadCallback callback) {
        this.callback = callback;
    }

    public void setOnScrollListener(OnScrollListener<H> listener) {
        scrollListener = listener;
    }

    private void init() {
        if (adapter.addFooterAtInit()) initLoadingView();
        adapter.setOnScrollListener(new OnScrollListener<H>() {
            @Override public void onScroll(H view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollListener != null) scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                if (state != State.DISABLED && state != State.REFRESHING
                        && state != State.FORCE_REFRESH && !ptrFrameLayout.isRefreshing()) {
                    if (visibleItemCount < totalItemCount) {
                        if (visibleItemCount > 0 && totalItemCount > 0)
                            if (aboutToLoad(firstVisibleItem + visibleItemCount - 1))
                                setLoadingStatus();
                    }
                }
            }
        });
    }

    @SuppressLint("InflateParams")
    private void initLoadingView() {
        if (loadingView == null) {
            loadingView = LayoutInflater.from(context).inflate(R.layout.loading_more, adapter.getView(), false);
            loadingContent = loadingView.findViewById(R.id.content);
            loadingLabel = (TextView) loadingView.findViewById(R.id.loadingText);
            progress = (CircularBar) loadingView.findViewById(R.id.progress);

            adapter.addFooterView(loadingView, null, false);
        }
        loadingContent.setVisibility(View.GONE);
    }

    private boolean aboutToLoad(int lastVisiblePosition) {
        for (int i = 0; i < whenToLoading; i++) {
            if (lastVisiblePosition == (adapter.getRowCount() - whenToLoading - i))
                return true;
        }
        return false;
    }

    private void load() {
        if (loadingView == null) initLoadingView();

        switch (state) {
            case ENABLED:
                break;

            case REFRESHING:
            case FORCE_REFRESH:
                showLoadingView();
                break;

            case DISABLED:
            case FINISHED:
                hideLoadingView();
                break;
        }
    }

    /**
     * 显示加载视图
     */
    private synchronized void showLoadingView() {
        if (!isLoading) {
            loadingContent.setVisibility(View.VISIBLE);
            progress.startAnimation();
            isLoading = true;
            if (callback != null)
                callback.onLoading(ptrFrameLayout);
        }
    }

    /**
     * 隐藏加载视图
     */
    private synchronized void hideLoadingView() {
        if (isLoading) {
            loadingContent.setVisibility(View.GONE);
            progress.stopAnimation();
            isLoading = false;
        }
    }

    /**
     * 获取控件当前状态
     */
    public State getLoadingState() {
        return state;
    }

    /**
     * 设置当前状态
     */
    private void setLoadingState(State state) {
        if (this.state == State.DISABLED && (state == State.FINISHED || state == State.REFRESHING))
            return;
        this.state = state;
        load();
    }

    /**
     * 强制刷新, 并显示加载视图
     */
    public void setLoading() {
        setLoadingState(State.FORCE_REFRESH);
    }

    /**
     * 将控件设置成刷新中状态
     */
    private void setLoadingStatus() {
        setLoadingState(State.REFRESHING);
    }

    /**
     * 加载完成时调用此方法
     */
    public void loadingComplete() {
        setLoadingState(State.FINISHED);
    }

    /**
     * 启动自动加载
     */
    public void enableLoading() {
        setLoadingState(State.ENABLED);
    }

    /**
     * 禁用自动加载
     */
    public void disableLoading() {
        setLoadingState(State.DISABLED);
    }

    /**
     * 设置加载视图中的文字
     */
    public void setLoadingLabel(String label) {
        if (!isCustomBackground) {
            loadingLabel.setText(label);
        }
    }

    /**
     * 设置加载视图中的文字
     */
    public void setLoadingLabel(int label) {
        if (!isCustomBackground) {
            loadingLabel.setText(label);
        }
    }

    /**
     * 设置加载视图中文字的颜色
     */
    public void setLoadingLabelColor(int color) {
        if (!isCustomBackground) {
            loadingLabel.setTextColor(color);
        }
    }

    /**
     * 设置加载视图的背景色
     */
    public void setLoadingBackgroundColor(int color) {
        if (!isCustomBackground) {
            loadingView.setBackgroundColor(color);
        }
    }

    /**
     * 获取何时会触发自动加载的值
     */
    public int getWhenToLoading() {
        return whenToLoading;
    }

    /**
     * 设置何时会触发自动加载的值
     */
    public void setWhenToLoading(int whenToLoading) {
        this.whenToLoading = whenToLoading;
    }

    public enum State {
        /** 刷新结束 **/
        FINISHED,
        /** 刷新中 **/
        REFRESHING,
        /** 失效 **/
        DISABLED,
        /** 生效, 默认状态 **/
        ENABLED,
        /** 强制刷新 **/
        FORCE_REFRESH
    }
}
