package com.pxy.pangjiao;

import android.app.Activity;
import android.app.Application;
import android.view.View;

import com.pxy.pangjiao.db.sql.PJDB;
import com.pxy.pangjiao.logger.IPLog;
import com.pxy.pangjiao.logger.Logger;
import com.pxy.pangjiao.mvp.MVPCore;
import com.pxy.pangjiao.mvp.ThreadPoolDefaultConfig;
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

    public static MVPCore init(Application application) {
        String className = "com.pxy.pangjiao.mvp.MVPDefaultContainer";
        try {
            Class<?> aClass = Class.forName(className);
            try {
                Method method = aClass.getMethod("createInstance");
                try {
                    Object invoke = method.invoke(null);
                    init(application, (IContainerConfig) invoke, new ThreadPoolDefaultConfig(), new NetDefaultConfig());
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
        return MVPCore.getInstance();
    }


    public static MVPCore init(Application application, NetDefaultConfig config) {
        init(application, new ThreadPoolDefaultConfig(), config);
        return MVPCore.getInstance();
    }

    public static MVPCore init(Application application, ThreadPoolDefaultConfig config) {
        init(application, config, new NetDefaultConfig());
        return MVPCore.getInstance();
    }

    public static MVPCore init(Application application, ThreadPoolDefaultConfig poolDefaultConfig, NetDefaultConfig config) {
        try {
            String className = "com.pxy.pangjiao.mvp.MVPDefaultContainer";
            Class<?> aClass = Class.forName(className);
            Method method = aClass.getMethod("createInstance");
            Object invoke = method.invoke(null);
            init(application, (IContainerConfig) invoke, poolDefaultConfig, config);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return MVPCore.getInstance();
    }

    public static MVPCore init(Application application, IContainerConfig config, ThreadPoolDefaultConfig poolDefaultConfig, NetDefaultConfig netConfig) {
        try {
            MVPCore.createInstance(application, poolDefaultConfig).initContainer(config);
            NetCore.init(netConfig);
            PJDB.init(application);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return MVPCore.getInstance();
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
        Logger.info("", msg);
    }

    public static void error(String msg) {
        Logger.e("", msg);
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
