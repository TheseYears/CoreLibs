package com.corelibs.views.ptr.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.corelibs.R;
import com.corelibs.utils.DisplayUtil;
import com.corelibs.views.cube.ptr.PtrDefaultHandler;
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.cube.ptr.PtrHandler;
import com.corelibs.views.cube.ptr.PtrUIHandler;
import com.corelibs.views.cube.ptr.header.MaterialHeader;

/**
 * 扩展于Ultra-Pull-To-Refresh库, 提取的一个Lollipop风格的BaseLayout.
 * 要使用Lollipop风格的下拉刷新, 只需用此控件包裹任意其他View即可. <BR/>
 * 注意: 1. 此控件只能包含一个子View. <BR/>
 * 2. 此控件仅支持下拉刷新, 如果需要自动加载, 请使用 {@link PtrAutoLoadMoreLayout} <BR/>
 * 3. 如果出现横向滑动冲突, 请设置 {@link #disableWhenHorizontalMove(boolean)} 为true. <BR/>
 * 4. 如果不想为child设置id并使用findViewById取出, 可以在声明PtrLollipopLayout的时候带上child类型的泛型,
 *    然后就可以使用{@link #getPtrView()}取出child. 如PtrLollipopLayout&lt;ScrollView&gt;. <BR/>
 * 5. 刷新完成或加载完成后请调用{@link #complete()}.
 * 
 * @author Ryan
 */
public class PtrLollipopLayout<T> extends PtrFrameLayout implements PtrHandler {

	protected Context context;

	/** 刷新回调 **/
	protected RefreshCallback callback;
	/** 刷新头部, 自定的头部需实现PtrUIHandler, 默认的头部是Android Lollipop风格 **/
	protected View header;
	protected boolean canRefresh = true;

    protected ScrollingConflictHandler scrollingConflictHandler = new ScrollingConflictHandler() {
        @Override public boolean handleScroll(PtrFrameLayout frame, View content, View header) {
            return canRefresh && PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
        }
    };

	public PtrLollipopLayout(Context context) {
		this(context, null);
	}

	public PtrLollipopLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PtrLollipopLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

    @SuppressWarnings("unchecked")
    public T getPtrView() {
        return (T) mContent;
    }
	
	private void init(Context context) {
		this.context = context;
		setPtrHandler(this);
		header = onPtrHeaderCreated();
        setHeaderView(header);
		setupPtrFrame();
	}

    protected View onPtrHeaderCreated() {
		MaterialHeader header = new MaterialHeader(context);
		int[] colors = getResources().getIntArray(R.array.google_colors);
		header.setColorSchemeColors(colors);
		header.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		header.setPadding(0, DisplayUtil.dip2px(context, 15), 0, DisplayUtil.dip2px(context, 10));
		return header;
	}

    /**
     * 设置滑动冲突处理
     */
    public void setScrollingConflictHandler(ScrollingConflictHandler conflictHandler) {
        if (conflictHandler == null) return;
        this.scrollingConflictHandler = conflictHandler;
    }

    private void setupPtrFrame() {
		setLoadingMinTime(1000);

		setDurationToCloseHeader(300);
		setPinContent(true);
		setDurationToClose(300);
        setKeepHeaderWhenRefresh(true);
        setRatioOfHeaderHeightToRefresh(1.2f);
        setResistance(1.7f);
    }

    /**
     * 设置自定义的头部, 必须实现PtrUIHandler
     */
    @Override
    public void setHeaderView(View header) {
        this.header = header;
        addHeader();
    }

    private void addHeader() {
        if(!(header instanceof PtrUIHandler))
            throw new IllegalStateException("PtrHeader must implement PtrUIHandler");

        if(header instanceof MaterialHeader)
            ((MaterialHeader) header).setPtrFrameLayout(this);

        super.setHeaderView(header);
        super.addPtrUIHandler((PtrUIHandler) header);
    }

    @Override
	public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
		return scrollingConflictHandler.handleScroll(frame, content, header);
	}

	@Override
	public void onRefreshBegin(final PtrFrameLayout frame) {
		if (callback != null)
			callback.onRefreshing(frame);
	}

    public void complete() {
		postDelayed(new Runnable() {
			@Override public void run() {
				refreshComplete();
			}
		}, 300);
    }

	/**
     * 设置是否能进行下拉刷新
     */
    public void setCanRefresh(boolean canRefresh) {
		this.canRefresh = canRefresh;
	}
	
	/**
	 * 将ptr设置为刷新状态
	 */
	public void setRefreshing() {
		post(new Runnable() {
			@Override public void run() {
				autoRefresh(true);
			}
		});
	}

	@Override
    protected void onAttachedToWindow() {
		super.onAttachedToWindow();
        refreshComplete();
	}

	/**
	 * 设置ptr刷新回调
	 */
	public void setRefreshCallback(RefreshCallback callback) {
		this.callback = callback;
	}

    /**
     * ptr刷新回调
     */
    public interface RefreshCallback {
        /**
         * 下拉刷新时会回调此方法
         */
        void onRefreshing(final PtrFrameLayout frame);
    }

	/**
	 * 下拉刷新冲突处理, 当下拉刷新与其他效果有滚动冲突时, 使用此接口来决定何时才能下拉刷新
	 */
	public interface ScrollingConflictHandler {
		/**
		 * 当结果返回true时意味着可以下拉刷新, 继续往下滑动就会出现刷新头
         */
        boolean handleScroll(PtrFrameLayout frame, View content, View header);
	}
}
