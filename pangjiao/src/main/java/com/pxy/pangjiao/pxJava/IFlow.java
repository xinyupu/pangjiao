package com.pxy.pangjiao.pxJava;

/**
 * Created by Administrator on 2018/7/23.
 */

public interface IFlow<T> {

    IFlow onSwitch(String threadMain);

    IFlow doNext(ISubscriber<T> subscriber);

    IFlow onSubscribe(ISubscriber<T> subscriber);

    void setProxy(IFlow flow);

    void subscribe();
}
