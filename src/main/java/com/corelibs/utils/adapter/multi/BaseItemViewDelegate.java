package com.corelibs.utils.adapter.multi;

import com.corelibs.utils.adapter.BaseAdapterHelper;

/**
 * Adapter中多布局代理
 * @param <T> 数据源类型
 * @param <H> ViewHolder类型
 */
public interface BaseItemViewDelegate<T, H extends BaseAdapterHelper> {

    /** 布局资源id **/
    int getItemViewLayoutId();

    /** 判断该position是否要加载此类型的布局 **/
    boolean isForViewType(T item, int position);

    /**
     * 当需要条目将被展示到界面上时, 通过此方法适配界面
     * @param helper ViewHolder
     * @param item 数据
     * @param position 位置
     */
    void convert(H helper, T item, int position);
}
