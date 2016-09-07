package com.corelibs.views.zoom;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.corelibs.R;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.utils.DisplayUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultiZoomImageActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_INIT_POSITION = "EXTRA_INIT_POSITION";
    public static final String EXTRA_IMAGES = "EXTRA_IMAGES";

    private int backgroundColor, indicatorResId, indicatorWidth, indicatorHeight,
            indicatorLeftMargin, indicatorRightMargin;

    private View parent;
    private ViewPager vp_ads;
    private LinearLayout ll_dots;
    private List<String> images;
    private List<ZoomImageView> imageViews;
    private List<View> dots;

    private int position;
    private int count = 0;

    public static Intent getLaunchIntent(Context context, List<String> images, int position) {
        Intent intent = new Intent(context, MultiZoomImageActivity.class);
        intent.putExtra(EXTRA_IMAGES, (Serializable) images);
        intent.putExtra(EXTRA_INIT_POSITION, position);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_multi_zoom_image;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initStyledAttributes();
        initViews();
        initBackground();
        initData();

        if (count > 0) {
            initChildViews();
            initViewPager();
        }
    }

    private void initStyledAttributes() {
        int defaultSideLength = DisplayUtil.dip2px(this, 8);
        int defaultMargin = DisplayUtil.dip2px(this, 3);
        int defaultColor = 0xFF333333;

        TypedArray typedArray = obtainStyledAttributes(null, R.styleable.MultiZoomImages);
        backgroundColor = typedArray.getColor(
                R.styleable.MultiZoomImages_backgroundColor, defaultColor);
        indicatorResId = typedArray.getResourceId(
                R.styleable.MultiZoomImages_indicator, R.drawable.default_mzi_indicator);
        indicatorWidth = (int) typedArray.getDimension(
                R.styleable.MultiZoomImages_indicatorWidth, defaultSideLength);
        indicatorHeight = (int) typedArray.getDimension(
                R.styleable.MultiZoomImages_indicatorHeight, defaultSideLength);
        indicatorLeftMargin = (int) typedArray.getDimension(
                R.styleable.MultiZoomImages_indicatorLeftMargin, defaultMargin);
        indicatorRightMargin = (int) typedArray.getDimension(
                R.styleable.MultiZoomImages_indicatorRightMargin, defaultMargin);

        typedArray.recycle();
    }

    private void initViews() {
        parent = findViewById(R.id.parent);
        vp_ads = (ViewPager) findViewById(R.id.vp_ads);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
    }

    private void initBackground() {
        parent.setBackgroundColor(backgroundColor);
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        position = getIntent().getIntExtra(EXTRA_INIT_POSITION, 0);
        images = (List<String>) getIntent().getSerializableExtra(EXTRA_IMAGES);
        count = images == null ? 0 : images.size();
    }

    private void initChildViews() {
        imageViews = new ArrayList<>();
        dots = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            View view = buildChildView();
            ll_dots.addView(view);
            dots.add(view);
        }

        for (int i = 0; i < count; i++) {
            ZoomImageView imageView = new ZoomImageView(this);
            Glide.with(this).load(images.get(i)).into(imageView);
            imageView.setOnClickListener(this);
            imageViews.add(imageView);
        }
    }

    private void initViewPager() {
        vp_ads.setAdapter(new MultiImagesAdapter());
        vp_ads.addOnPageChangeListener(new MultiImagesPageChangedListener());
        vp_ads.setCurrentItem(position);
        dots.get(position).setSelected(true);
    }

    private View buildChildView() {
        View view = new View(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(indicatorWidth, indicatorHeight);
        view.setBackgroundResource(indicatorResId);
        lp.setMargins(indicatorLeftMargin, 0, indicatorRightMargin, 0);
        view.setLayoutParams(lp);

        return view;
    }

    class MultiImagesPageChangedListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {}

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < count; i ++)
                dots.get(i).setSelected(i == position);
        }

    }

    class MultiImagesAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
