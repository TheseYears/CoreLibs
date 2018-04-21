package com.corelibs.views.cityselect;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.corelibs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 城市选择, 请一定调用{@link #setupAdapter}方法来初始化Adapter,
 * 并确保{@link #setupAdapter}方法在{@link #addHeader}方法后调用, 以兼容低版本的ListView.
 *
 * <p>
 * 在Activity/Fragment的onDestroy中调用{@link #release()}释放资源.
 * </p>
 *
 * <p>
 * Created by Ryan on 2016/2/1.
 * </p>
 */
public class SelectCityView extends FrameLayout implements
        LetterView.OnTouchingLetterChangedListener {

    private ListView lvCities;
    private LetterView letterView;

    private Context context;
    private CityAdapter<City> adapter;

    private TextView overlay;
    private OverlayThread overlayThread = new OverlayThread();
    private WindowManager windowManager;
    private HashMap<String, Integer> alphaIndex;
    private Handler handler = new Handler();

    @Override
    public void onTouchingLetterChanged(String s) {
        overlay.setText(s.substring(0, 1));
        overlay.setVisibility(VISIBLE);
        if (alphaIndex != null && alphaIndex.get(s) != null) {
            int position = alphaIndex.get(s);
            lvCities.setSelection(position + lvCities.getHeaderViewsCount());
        }
        handler.removeCallbacks(overlayThread);
        handler.postDelayed(overlayThread, 1000);
    }

    private class OverlayThread implements Runnable {
        @Override public void run() {
            overlay.setVisibility(GONE);
        }
    }

    public SelectCityView(Context context) {
        this(context, null);
    }

    public SelectCityView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectCityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.view_select_city, this);
        lvCities = (ListView) findViewById(R.id.lv_cities);
        letterView = (LetterView) findViewById(R.id.letter_view);

        initList();
        initOverlay();
    }

    private void initList() {
        adapter = new CityAdapter<>(context, R.layout.item_select_city);
        letterView.setOnTouchingLetterChangedListener(this);
    }

    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(context);
        overlay = (TextView) inflater.inflate(R.layout.view_overlay, this, false);
        overlay.setVisibility(INVISIBLE);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    public <T extends City> void setData(List<T> cities) {
        List<City> transData = new ArrayList<>();
        transData.addAll(cities);
        adapter.replaceAll(transData);
        alphaIndex = adapter.getAlphaIndex();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        lvCities.setOnItemClickListener(listener);
    }

    public void addHeader(final View header) {
        if (header == null) return;
        lvCities.addHeaderView(header);
        header.post(new Runnable() {
            @Override
            public void run() {
                int height = header.getHeight();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                        letterView.getLayoutParams();
                params.setMargins(0, height, 0, 0);
                letterView.setLayoutParams(params);
            }
        });
    }

    /**
     * 初始化Adapter, 确保此方法在{@link #addHeader}方法后调用, 以兼容低版本的ListView
     */
    public void setupAdapter() {
        lvCities.setAdapter(adapter);
    }

    /**
     * 释放资源
     */
    public void release() {
        if (windowManager != null && overlay != null)
            windowManager.removeViewImmediate(overlay);
    }

}
