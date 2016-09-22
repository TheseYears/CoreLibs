package com.corelibs.views.ptr.layout;

import android.content.Context;
import android.util.AttributeSet;

import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.loadmore.AutoLoadMoreHandler;
import com.corelibs.views.ptr.loadmore.AutoLoadMoreHook;
import com.corelibs.views.ptr.loadmore.OnScrollListener;

/**
 * 扩展于Ultra-Pull-To-Refresh库, 此控件为Lollipop风格的下拉刷新并带自动加载的BaseLayout.
 * 只需用此控件包裹任意其他的实现了{@link AutoLoadMoreHook}的view即可. <BR/>
 * 注意: 1. 如果只需下拉刷新功能, 请使用{@link PtrLollipopLayout}. <BR/>
 * 2. 此控件中的child view必须实现{@link AutoLoadMoreHook} <BR/>
 * 3. 此控件是对{@link AutoLoadMoreHandler}功能的转发. {@link AutoLoadMoreHook#getLoadMoreHandler()}
 *    需要的就是AutoLoadMoreHandler. <BR/>
 * 4. 刷新完成或加载完成后请调用{@link #complete()}, 而不是{@link #refreshComplete()}或{@link #loadingFinished()}.
 *
 * @author Ryan
 */
public class PtrAutoLoadMoreLayout<T> extends PtrLollipopLayout<T> {

    private AutoLoadMoreHandler loadMoreHandler;

    public PtrAutoLoadMoreLayout(Context context) {
        this(context, null);
    }

    public PtrAutoLoadMoreLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrAutoLoadMoreLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {}

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        loadMoreHandler = setupHook().getLoadMoreHandler();

        if (loadMoreHandler == null)
            throw new IllegalStateException("AutoLoadMoreHandler should not be null");

        loadMoreHandler.setup(this);
    }

    public void setRefreshLoadCallback(RefreshLoadCallback callback) {
        this.callback = callback;
        loadMoreHandler.setRefreshLoadCallback(callback);
    }

    @Override
    public void setRefreshCallback(RefreshCallback callback) {
        super.setRefreshCallback(callback);
        if (callback == null || !(callback instanceof RefreshLoadCallback))
            throw new IllegalStateException("PtrAutoLoadMoreLayout callback should not be null " +
                    "and should be RefreshLoadCallback");

        loadMoreHandler.setRefreshLoadCallback((RefreshLoadCallback) callback);
    }

    private AutoLoadMoreHook setupHook() {
        if (mContent != null && mContent instanceof AutoLoadMoreHook) {
            return (AutoLoadMoreHook) mContent;
        } else {
            throw new IllegalStateException("PtrAutoLoadMoreLayout child should implement AutoLoadMoreHook");
        }
    }

    @Override
    public void complete() {
        super.complete();
        loadMoreHandler.loadingComplete();
    }

    @SuppressWarnings("unchecked")
    public void setOnScrollListener(OnScrollListener listener) {
        loadMoreHandler.setOnScrollListener(listener);
    }

    /**
     * 获取控件当前状态
     */
    public AutoLoadMoreHandler.State getLoadingState() {
        return loadMoreHandler.getLoadingState();
    }

    /**
     * 启动自动加载
     */
    public void enableLoading() {
        loadMoreHandler.enableLoading();
    }

    /**
     * 强制刷新, 并显示加载视图
     */
    public void setLoading() {
        loadMoreHandler.setLoading();
    }

    /**
     * 加载完成时调用此方法
     */
    public void loadingFinished() {
        loadMoreHandler.loadingComplete();
    }

    /**
     * 禁用自动加载
     */
    public void disableLoading() {
        loadMoreHandler.disableLoading();
    }

    /**
     * 设置加载视图中的文字
     */
    public void setLoadingLabel(String label) {
        loadMoreHandler.setLoadingLabel(label);
    }

    /**
     * 设置加载视图中的文字
     */
    public void setLoadingLabel(int label) {
        loadMoreHandler.setLoadingLabel(label);
    }

    /**
     * 设置加载视图中文字的颜色
     */
    public void setLoadingLabelColor(int color) {
        loadMoreHandler.setLoadingLabelColor(color);
    }

    /**
     * 设置加载视图的背景色
     */
    public void setLoadingBackgroundColor(int color) {
        loadMoreHandler.setLoadingBackgroundColor(color);
    }

    /**
     * 获取何时会触发自动加载的值
     */
    public int getWhenToLoading() {
        return loadMoreHandler.getWhenToLoading();
    }

    /**
     * 设置何时会触发自动加载的值
     */
    public void setWhenToLoading(int whenToLoading) {
        loadMoreHandler.setWhenToLoading(whenToLoading);
    }

    /**
     * ptr刷新与加载回调
     */
    public interface RefreshLoadCallback extends RefreshCallback {
        /**
         * 自动加载时会回调此方法, 此方法需在自定的自动加载ListView中调用.
         */
        void onLoading(final PtrFrameLayout frame);
    }
}
