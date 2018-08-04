package com.pxy.pangjiao.pxJava;

/**
 * Created by Administrator on 2018/7/23.
 */

public interface ISubscriber<T> {
    void onSubscribe();

    void error(Throwable e);

    void onNext(T t);

    void onCompleted();
}
