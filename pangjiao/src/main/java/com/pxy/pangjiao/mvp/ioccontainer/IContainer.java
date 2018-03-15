package com.pxy.pangjiao.mvp.ioccontainer;

/**
 * Created by pxy on 2018/3/13.
 */

public interface IContainer {

    void register(Object o);

    <T> T resolve(Class<T> t);
}
