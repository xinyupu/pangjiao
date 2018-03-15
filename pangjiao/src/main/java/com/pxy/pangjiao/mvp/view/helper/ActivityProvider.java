package com.pxy.pangjiao.mvp.view.helper;

import android.app.Activity;

/**
 * Created by Administrator on 2018/3/12.
 */

public class ActivityProvider implements IProvider {

    @Override
    public Object findView(Object o, int id) {
        return ((Activity) o).findViewById(id);
    }
}
