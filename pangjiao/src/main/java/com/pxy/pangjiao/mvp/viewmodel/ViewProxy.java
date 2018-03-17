package com.pxy.pangjiao.mvp.viewmodel;

import com.pxy.pangjiao.PangJiao;
import com.pxy.pangjiao.common.ExpUtil;
import com.pxy.pangjiao.mvp.MVPCore;
import com.pxy.pangjiao.mvp.view.helper.CurrentThread;
import com.pxy.pangjiao.mvp.viewmodel.views.IView;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *   Created by pxy on 2018/2/1.
 */

public class ViewProxy implements InvocationHandler {

    private Object obj;

    public Object getProxy(Object obj) {
        this.obj = obj;
        Class<?> aClass = obj.getClass();
        Class<?>[] interfaces = aClass.getInterfaces();
        Class interfaceInject = null;
        for (Class c : interfaces) {
            if (IView.class.isAssignableFrom(c)) {
                interfaceInject = c;
                break;
            }
        }
        if (interfaceInject==null){
            throw new RuntimeException(aClass.getName() +"must extends ro implements IView.class");
        }
        return Proxy.newProxyInstance(this.obj.getClass().getClassLoader(), new Class[]{interfaceInject}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object object = null;
        if (method.isAnnotationPresent(CurrentThread.class)) {
            try {
                method.invoke(obj, args);
            } catch (Exception e) {
                e.printStackTrace();
                PangJiao.error(ExpUtil.getStackTrace(e));
            }
        } else if (Thread.currentThread().getId() == MVPCore.getInstance().getMainThreadId()) {
            try {
                object = method.invoke(obj, args);
            } catch (Exception e) {
                e.printStackTrace();
                PangJiao.error(ExpUtil.getStackTrace(e));
            }
        } else {
            MVPCore.getInstance().postMain(() -> {
                try {
                    method.invoke(obj, args);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    PangJiao.error(ExpUtil.getStackTrace(e));
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    PangJiao.error(ExpUtil.getStackTrace(e));
                }
            });
        }
        return object;
    }
}
