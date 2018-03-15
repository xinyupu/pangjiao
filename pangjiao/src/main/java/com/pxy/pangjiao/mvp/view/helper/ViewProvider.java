package com.pxy.pangjiao.mvp.view.helper;

import android.view.View;

/**
 * Created by Administrator on 2018/3/12.
 */

public class ViewProvider implements IProvider {

    @Override
    public Object findView(Object o, int id) {
        return ((View) o).findViewById(id);
    }
}
