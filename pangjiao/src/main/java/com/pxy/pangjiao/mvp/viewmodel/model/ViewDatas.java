package com.pxy.pangjiao.mvp.viewmodel.model;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ViewDatas {

    public void show( Object o){
        ViewDataInject.inject(this,o);
    }
}
