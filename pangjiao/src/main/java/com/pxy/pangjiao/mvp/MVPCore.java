package com.pxy.pangjiao.mvp;

import android.app.Application;
import android.os.Handler;

import com.pxy.pangjiao.PangJiao;
import com.pxy.pangjiao.common.ExpUtil;
import com.pxy.pangjiao.log.IPLog;
import com.pxy.pangjiao.log.PLog;
import com.pxy.pangjiao.mvp.ioccontainer.AutoWireConfig;
import com.pxy.pangjiao.mvp.ioccontainer.BeanConfig;
import com.pxy.pangjiao.mvp.ioccontainer.IContainerConfig;
import com.pxy.pangjiao.mvp.presenter.ILoading;
import com.pxy.pangjiao.mvp.presenter.PresentProxy;
import com.pxy.pangjiao.mvp.viewmodel.ViewProxy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/3/13.
 */

public class MVPCore {

    private static MVPCore _instance;
    private Map<String, BeanConfig> beanContainer;
    private Map<String, List> autoWireContainer;
    private Map<String, List> autoWireProxyContainer;
    private Map<String, List> dataEventContainer;
    private IContainerConfig containerConfig;
    private List<String> superClass;
    public IPLog log;
    private Handler mainHandler;
    private long mainThreadId;
    private ExecutorService executorService;
    private ILoading loading;


    public static MVPCore createInstance(Application application) {
        if (_instance == null) {
            synchronized (MVPCore.class) {
                if (_instance == null) {
                    _instance = new MVPCore();
                }
            }
        }
        return _instance;
    }


    private MVPCore() {
        beanContainer = new HashMap<>();
        superClass = new ArrayList<>();
        autoWireProxyContainer = new HashMap<>();
        dataEventContainer = new HashMap<>();
        this.log = new PLog();
        mainThreadId = Thread.currentThread().getId();
        mainHandler = new Handler();
        executorService = Executors.newCachedThreadPool();
    }

    public void initContainer(IContainerConfig config) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        this.containerConfig = config;
        this.beanContainer = this.containerConfig.getClassTypeContainer();
        this.autoWireContainer = this.containerConfig.getAutoWireContainer();
        this.autoWireProxyContainer = this.containerConfig.getAutoWireProxyContainer();
        this.dataEventContainer = this.containerConfig.getDataEvnetContainer();
        for (String beanCls : this.beanContainer.keySet()) {
            Class<?> aClass = Class.forName(beanCls);
            Class<?>[] interfaces = aClass.getInterfaces();
            this.beanContainer.get(beanCls).setSuperClass(interfaces);
            for (Class cls : interfaces) {
                if (!superClass.contains(cls.getName())) {
                    superClass.add(cls.getName());
                }
            }
        }
        initAutoWire();
    }

    private void initAutoWire() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        for (String cls : this.beanContainer.keySet()) {
            BeanConfig beanConfig = this.beanContainer.get(cls);
            if (beanConfig.getSuperClass() != null) {
                List<AutoWireConfig> list = this.autoWireContainer.get(cls);
                if (list != null) {
                    for (AutoWireConfig configAuto : list) {
                        String autoFieldClassName = configAuto.getFieldClassName();
                        String autoFieldName = configAuto.getFieldName();
                        Class<?> autoFieldClass = Class.forName(autoFieldClassName);
                        for (String beanClassName : this.beanContainer.keySet()) {
                            Class<?> beanClass = Class.forName(beanClassName);
                            if (autoFieldClass.isAssignableFrom(beanClass)) {
                                Field declaredField = this.beanContainer.get(cls).getObject().getClass().getDeclaredField(autoFieldName);
                                declaredField.setAccessible(true);
                                declaredField.set(beanConfig.getObject(), this.beanContainer.get(beanClass.getName()).getObject());
                            }
                        }
                    }
                }
            }
        }
    }


    public void autoWireProxy(Object o) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        List<AutoWireConfig> list = this.autoWireProxyContainer.get(o.getClass().getName());
        if (list != null) {
            for (AutoWireConfig config : list) {
                String autoClassName = config.getFieldClassName();
                Class<?> autoClass = Class.forName(autoClassName);
                for (String className : this.beanContainer.keySet()) {
                    Class<?> beanClass = Class.forName(className);
                    if (autoClass.isAssignableFrom(beanClass)) {
                        BeanConfig beanConfig = this.beanContainer.get(className);
                        Object proxy;
                        if (beanConfig.isSingleton()) {
                            proxy = new PresentProxy().getProxy(beanConfig.getObject());
                        } else {
                            Object o1 = beanConfig.getObject().getClass().newInstance();
                            autoWire(o1);
                            proxy = new PresentProxy().getProxy(o1);
                        }
                        Field declaredField = o.getClass().getDeclaredField(config.getFieldName());
                        declaredField.setAccessible(true);
                        declaredField.set(o, proxy);
                        Method build = proxy.getClass().getMethod("build", Object.class);
                        Object viewProxy = new ViewProxy().getProxy(o);
                        build.invoke(proxy, viewProxy);
                        Field presenters = o.getClass().getField("presenters");
                        if (presenters != null) {
                            Method add = presenters.getType().getMethod("add", Object.class);
                            add.invoke(presenters.get(o), proxy);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void autoWire(Object o) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        List<AutoWireConfig> list = autoWireContainer.get(o.getClass().getName());
        if (list != null) {
            for (AutoWireConfig configAuto : list) {
                String autoFieldClassName = configAuto.getFieldClassName();
                String autoFieldName = configAuto.getFieldName();
                Class<?> autoFieldClass = Class.forName(autoFieldClassName);
                for (String beanClassName : this.beanContainer.keySet()) {
                    Class<?> beanClass = Class.forName(beanClassName);
                    if (autoFieldClass.isAssignableFrom(beanClass)) {
                        Field declaredField = this.beanContainer.get(o.getClass().getName()).getObject().getClass().getDeclaredField(autoFieldName);
                        declaredField.setAccessible(true);
                        declaredField.set(o, this.beanContainer.get(beanClass.getName()).getObject());
                    }
                }
            }
        }
    }


    public static MVPCore getInstance() {
        return _instance;
    }

    public <T> T resolve(Class<T> s) {
        BeanConfig beanConfig = beanContainer.get(s.getName());
        if (beanConfig != null) {
            return (T) beanConfig.getObject();
        }
        return null;
    }

    public void setLoadingView(ILoading loading) {
        this.loading = loading;
    }

    public ILoading getLoadingView() {
        return loading;
    }

    public void setLog(IPLog log) {
        this.log = log;
    }

    public Object parse(Object viewModel, Object view) {
        try {
            return MVPHelper.mapper(viewModel, view);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            PangJiao.error(ExpUtil.getStackTrace(e));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            PangJiao.error(ExpUtil.getStackTrace(e));
        }
        return null;
    }


    public <T> void show(T viewModel, Object view) {
        try {
            try {
                MVPHelper.show(viewModel, view);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                PangJiao.error(ExpUtil.getStackTrace(e));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                PangJiao.error(ExpUtil.getStackTrace(e));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            PangJiao.error(ExpUtil.getStackTrace(e));
        }
    }

    public long getMainThreadId() {
        return mainThreadId;
    }

    public void postMain(Runnable runnable) {
        mainHandler.post(runnable);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Map<String, List> getDataEventContainer() {
        return dataEventContainer;
    }
}
