package com.pxy.pangjiao.net;

/**
 * Created by pxy on 2018/2/1.
 */

public class NetModel<T> implements INetModel {

    @Override
    public T execute() {
        return NetCore.getHttpManger().parse(this);
    }
}
