package com.pxy.pangjiao.pxJava;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/7/23.
 */

public class FlowProxy implements InvocationHandler {

    private IFlow flow;

    public FlowProxy(IFlow flow) {
        this.flow = flow;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(flow, args);
    }
}
