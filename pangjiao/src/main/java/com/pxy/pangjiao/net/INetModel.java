package com.pxy.pangjiao.net;

/**
 * Created by pxy on 2018/2/1.
 */

public interface INetModel<T> {
    T execute() throws InstantiationException, IllegalAccessException;
}
