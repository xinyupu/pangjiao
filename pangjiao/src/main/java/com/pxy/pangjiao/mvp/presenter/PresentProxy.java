package com.pxy.pangjiao.mvp.presenter;


import com.pxy.pangjiao.PangJiao;
import com.pxy.pangjiao.common.ExpUtil;
import com.pxy.pangjiao.mvp.MVPCore;
import com.pxy.pangjiao.mvp.presenter.helper.HideLoading;
import com.pxy.pangjiao.mvp.presenter.helper.MainThread;
import com.pxy.pangjiao.mvp.view.helper.VProxy;
import com.pxy.pangjiao.mvp.viewmodel.ViewProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by pxy on 2018/2/5.
 */

public class PresentProxy implements InvocationHandler {

    private Object obj;

    public Object getProxy(Object obj) {
        this.obj = obj;
        return Proxy.newProxyInstance(this.obj.getClass().getClassLoader(), this.obj.getClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object object = null;
        if (method.isAnnotationPresent(MainThread.class)) {
            if (method.getName().equals("build")) {
                Method[] buildMs = obj.getClass().getMethods();
                outerLoop:
                for (Method m : buildMs) {
                    String name = m.getName();
                    if (name.equals("build")) {
                        Annotation[][] builds = m.getParameterAnnotations();
                        for (Annotation[] build : builds) {
                            for (Annotation aBuild : build) {
                                if (aBuild.annotationType() == VProxy.class) {
                                    Object viewProxy = new ViewProxy().getProxy(args[0]);
                                    try {
                                        object = method.invoke(obj, viewProxy);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        PangJiao.error(ExpUtil.getStackTrace(e));
                                    }
                                    break outerLoop;
                                }
                            }
                        }
                        try {
                            object = method.invoke(obj, args);
                        } catch (Exception e) {
                            e.printStackTrace();
                            PangJiao.error(ExpUtil.getStackTrace(e));
                        }
                        break;
                    }
                }

            } else {
                try {
                    object = method.invoke(obj, args);
                } catch (Exception e) {
                    e.printStackTrace();
                    PangJiao.error(ExpUtil.getStackTrace(e));
                }
            }
        } else {
            MVPCore.getInstance().getExecutorService().execute(() -> {
                try {
                    if (!method.isAnnotationPresent(HideLoading.class) && MVPCore.getInstance().getLoadingView() != null) {
                        MVPCore.getInstance().getLoadingView().show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PangJiao.error(ExpUtil.getStackTrace(e));
                }
                try {
                    method.invoke(obj, args);
                } catch (Exception e) {
                    e.printStackTrace();
                    PangJiao.error(ExpUtil.getStackTrace(e));
                }

                try {
                    if (!method.isAnnotationPresent(HideLoading.class) && MVPCore.getInstance().getLoadingView() != null) {
                        MVPCore.getInstance().getLoadingView().close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PangJiao.error(ExpUtil.getStackTrace(e));
                }
            });
        }
        return object;
    }
}
