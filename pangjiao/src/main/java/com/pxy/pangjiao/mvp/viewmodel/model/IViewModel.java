package com.pxy.pangjiao.mvp.viewmodel.model;

/**
 * Created by pxy on 2018/2/1.
 */

public interface IViewModel {

    <T> T mapper(Object o);

    void show(Object o);
}
