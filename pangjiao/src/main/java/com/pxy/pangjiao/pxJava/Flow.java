package com.pxy.pangjiao.pxJava;

import android.os.Handler;

import com.pxy.pangjiao.mvp.MVPCore;
import com.pxy.pangjiao.mvp.ThreadPool.ThreadPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/23.
 */

public class Flow<T> implements IFlow {


    private IFlow flowProxy;
    private Scheduler scheduler;
    private List<ISubscriber> subscribers;
    private List<String> switchs;

    public Flow() {
        scheduler = new Scheduler();
        subscribers = new ArrayList<>();
        switchs = new ArrayList<>();
    }

    @Override
    public IFlow onSwitch(String threadMain) {
        switchs.add(threadMain);
        return flowProxy;
    }

    @Override
    public IFlow doNext(ISubscriber subscriber) {
        return flowProxy;
    }

    @Override
    public IFlow onSubscribe(ISubscriber subscriber) {
        InvocationHandler handler = new SubscriberProxy(subscriber);
        ISubscriber iSubscriber = (ISubscriber) Proxy.newProxyInstance(handler.getClass().getClassLoader(), subscriber
                .getClass().getInterfaces(), handler);
        subscribers.add(iSubscriber);
        return flowProxy;
    }

    @Override
    public void setProxy(IFlow flow) {
        this.flowProxy = flow;
    }

    @Override
    public void subscribe() {
        for (ISubscriber subscriber : subscribers) {
            subscriber.onSubscribe();
        }
    }
}
