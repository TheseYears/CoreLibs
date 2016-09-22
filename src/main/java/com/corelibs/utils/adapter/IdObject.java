package com.corelibs.utils.adapter;

import com.corelibs.utils.adapter.normal.QuickAdapter;

/**
 * 配合{@link QuickAdapter}使用, 传入{@link QuickAdapter}中的数据类型如果实现此接口,
 * 则可以直接调用{@link QuickAdapter#getItemId(int)}来获取数据的id.
 * <p/>
 * Created by Ryan on 2016/1/22.
 */
public interface IdObject {
    long getId();
}
