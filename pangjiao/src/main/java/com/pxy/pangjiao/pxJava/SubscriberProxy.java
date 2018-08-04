package com.pxy.pangjiao.pxJava;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/7/23.
 */

public class SubscriberProxy implements InvocationHandler {

    //　这个就是我们要代理的真实对象
    private ISubscriber subscriber;

    //    构造方法，给我们要代理的真实对象赋初值
    public SubscriberProxy(ISubscriber subscriber) {
        this.subscriber = subscriber;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object o = method.invoke(subscriber, args);
        return o;
    }
}
