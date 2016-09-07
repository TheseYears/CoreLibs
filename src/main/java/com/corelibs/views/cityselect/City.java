package com.corelibs.views.cityselect;

import com.corelibs.utils.adapter.IdObject;

/**
 * 城市选择控件使用的数据类型. 要使用该控件必须让自定义的实体类实现此接口.
 * <p/>
 * Created by Ryan on 2016/3/1.
 */
public interface City extends IdObject {
    String getCityName();
    String getEnName();
}
