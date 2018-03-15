package com.pxy.pangjiao;

import android.app.Activity;
import android.app.Application;
import android.view.View;

import com.pxy.pangjiao.db.sql.PJDB;
import com.pxy.pangjiao.log.IPLog;
import com.pxy.pangjiao.mvp.MVPCore;
import com.pxy.pangjiao.mvp.ioccontainer.IContainerConfig;
import com.pxy.pangjiao.mvp.view.ViewInject;
import com.pxy.pangjiao.net.NetDefaultConfig;
import com.pxy.pangjiao.net.NetCore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by Administrator on 2018/3/12.
 */

public class PangJiao {

    public static void init(Application application) {
        String className = "com.pxy.pangjiao.mvp.MVPDefaultContainer";
        try {
            Class<?> aClass = Class.forName(className);
            try {
                Method method = aClass.getMethod("createInstance", null);
                try {
                    Object invoke = method.invoke(null);
                    init(application, (IContainerConfig) invoke);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void init(Application application, IContainerConfig config) {
        try {
            MVPCore.createInstance(application).initContainer(config);
            NetCore.init(new NetDefaultConfig());
            PJDB.init(application);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void inject(Activity activity) {
        ViewInject.inject(activity);
    }

    public static void inject(View view, Object target) {
        ViewInject.inject(view, target);
    }

    public static <T> T resolve(Class<T> impClass) {
        return MVPCore.getInstance().resolve(impClass);
    }

    public static void info(String msg) {
        MVPCore.getInstance().log.info("", msg);
    }

    public static void error(String msg) {
        MVPCore.getInstance().log.error("", msg);
    }

    public static void setLog(IPLog log) {
        MVPCore.getInstance().setLog(log);
    }

    public static void viewDestory(Object o) {
        // MVPCore.getInstance().removeContainer(o);
    }

    public static void register(Object o) {
        try {
            try {
                try {
                    try {
                        MVPCore.getInstance().autoWireProxy(o);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
