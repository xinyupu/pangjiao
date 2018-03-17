package com.pxy.pangjiao.mvp;

import android.app.Application;
import android.os.Handler;

import com.pxy.pangjiao.PangJiao;
import com.pxy.pangjiao.common.ExpUtil;
import com.pxy.pangjiao.log.IPLog;
import com.pxy.pangjiao.log.PLog;
import com.pxy.pangjiao.mvp.ioccontainer.BeanConfig;
import com.pxy.pangjiao.mvp.ioccontainer.IContainerConfig;
import com.pxy.pangjiao.mvp.ioccontainer.ViewConfig;
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
    private Map<String, List> viewContainer;
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
        this.viewContainer=this.containerConfig.getViewContainer();
        this.dataEventContainer=this.containerConfig.getDataEvnetContainer();
    }



    public void autoWireProxy(Object o) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        List<ViewConfig> list = this.viewContainer.get(o.getClass().getName());
        if (list!=null){
            for (ViewConfig config:list){
                Field field = o.getClass().getField(config.fieldName);
                String impClassName = config.getImpClassName();
                Class<?> imp = Class.forName(impClassName);
                if (field!=null){
                    if (field.getType().isAssignableFrom(imp)||field.getType()==imp){
                        BeanConfig beanConfig = this.beanContainer.get(impClassName);
                        setProxy(o,beanConfig,config.getFieldName());
                    }
                }
            }
        }else {
            throw new RuntimeException(o.getClass().getName()+"must add @View,In order to use @Autowire or @AutowireProxy ");
        }
    }

    private void setProxy(Object o,BeanConfig beanConfig ,String fieldName) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        Object proxy;
        if (beanConfig.isSingleton()) {
            proxy = new PresentProxy().getProxy(beanConfig.getObject());
        } else {
            Object o1 = beanConfig.getObject().getClass().newInstance();
            autoWireNew(o1);
            proxy = new PresentProxy().getProxy(o1);
        }
        Field declaredField = o.getClass().getDeclaredField(fieldName);
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
    }

    private void autoWireNew(Object o) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
         this.containerConfig.autoWireFactory(o);

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
