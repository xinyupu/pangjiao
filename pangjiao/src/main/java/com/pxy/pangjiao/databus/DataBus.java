package com.pxy.pangjiao.databus;


import com.pxy.pangjiao.PangJiao;
import com.pxy.pangjiao.logger.Logger;
import com.pxy.pangjiao.mvp.MVPCore;
import com.pxy.pangjiao.mvp.ioccontainer.DataEventConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pxy on 2018/2/6.
 */

public class DataBus {

    private volatile static DataBus defaultInstance;
    private final List<Object> messagePool;
    private final List<Object> stickMessagePool;
    private final Map<Object, Method> observableMap;
    private final DataObservable dataObservable;

    public static DataBus getDefault() {
        if (defaultInstance == null) {
            synchronized (DataBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new DataBus();
                }
            }
        }
        return defaultInstance;
    }

    private DataBus() {
        messagePool = new ArrayList<>();
        stickMessagePool = new ArrayList<>();
        observableMap = new HashMap<>();
        dataObservable = new DataObservable(messagePool, stickMessagePool);
    }

    public <T> void postStick(T o) {
        stickMessagePool.add(o);
        Logger.info("DataBus", "stickMessagePool 消息池:" + stickMessagePool.size());
    }

    public <T> void post(T o) {
        messagePool.add(o);
        Logger.info("DataBus", "messagePool 消息池:" + messagePool.size());
        dataObservable.notifyChanged();
    }

    public void register(Object o) {
        List<DataEventConfig> list = MVPCore.getInstance().getDataEventContainer().get(o.getClass().getName());
        if (list != null) {
            for (DataEventConfig config : list) {
                try {
                    Method method = o.getClass().getMethod(config.getMethodName(), Object.class);
                    observableMap.put(o, method);
                    dataObservable.notifyChanged();
                    Logger.info("DataBus", "DataBus register()数量:" + observableMap.size());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void destroy(Object o) {
        observableMap.remove(o);
        Logger.info("DataBus", "DataBus destroy()后数量:" + observableMap.size());
    }

}
