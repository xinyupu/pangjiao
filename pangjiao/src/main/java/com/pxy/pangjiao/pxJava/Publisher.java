package com.pxy.pangjiao.pxJava;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2018/7/23.
 */

public class Publisher {


    public static <T> IFlow create() {
        IFlow<T> flow = new Flow<T>();
        InvocationHandler handler = new FlowProxy(flow);
        IFlow flowProxy = (IFlow) Proxy.newProxyInstance(handler.getClass().getClassLoader(), flow
                .getClass().getInterfaces(), handler);
        flow.setProxy(flowProxy);
        return flowProxy;
    }


}
