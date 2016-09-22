package com.corelibs.views.cityselect;

import android.content.Context;

import com.corelibs.R;
import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.normal.QuickAdapter;

import java.util.HashMap;
import java.util.List;

public class CityAdapter<T extends City> extends QuickAdapter<T> {

    private String current, previous;
    private HashMap<String, Integer> alphaIndex;

    public CityAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    private void setupAlphaIndex() {
        alphaIndex = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                alphaIndex.put(data.get(i).getEnName(), i);
            } else {
                current = data.get(i).getEnName();
                previous = data.get(i - 1).getEnName();

                if (!current.equals(previous)) {
                    alphaIndex.put(current, i);
                }
            }
        }
    }

    @Override
    public void replaceAll(List<T> elem) {
        super.replaceAll(elem);
        setupAlphaIndex();
    }

    public HashMap<String, Integer> getAlphaIndex() {
        return alphaIndex;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, T item, int position) {
        helper.setText(R.id.name, item.getCityName());

        if (position == 0) {
            helper.setText(R.id.alpha, item.getEnName());
            helper.setVisible(R.id.alpha, true);
        } else {
            current = item.getEnName();
            previous = data.get(position - 1).getEnName();
            if (!current.equals(previous)) {
                helper.setText(R.id.alpha, current);
                helper.setVisible(R.id.alpha, true);
            } else {
                helper.setVisible(R.id.alpha, false);
            }
        }
    }
}
