package com.pxy.pangjiao.mvp.view;

import com.pxy.pangjiao.mvp.view.helper.IProvider;

/**
 * Created by Administrator on 2018/3/12.
 */

public interface IViewInject<T> {

    void inject(T host, Object source, IProvider provider);
}
