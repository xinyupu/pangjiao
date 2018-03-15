package com.pxy.pangjiao.databus;


import com.pxy.pangjiao.PangJiao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by pxy on 2018/2/6.
 */

public class DataObserver implements Observer {

    private Map<Object, Method> observableMap;

    public DataObserver(Map<Object, Method> observableMap, DataObservable dataObservable) {
        this.observableMap = observableMap;
        dataObservable.addObserver(this);
    }

    @Override
    public void update(Observable dataObservable, Object arg) {
        List<Object> messagePool = ((DataObservable) dataObservable).getMessagePool();
        List<Object> stickMessagePool = ((DataObservable) dataObservable).getStickMessagePool();
        sendStickEvent(stickMessagePool);
        sendEvent(messagePool);
    }

    private void sendEvent(List<Object> messagePool) {
        if (messagePool.size() == 0) return;
        for (Object o : observableMap.keySet()) {
            Method method = observableMap.get(o);
            try {
                for (Object os : messagePool) {
                    method.invoke(o, os);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        messagePool.clear();
        PangJiao.info("messagePool 消息池:" + messagePool.size());
    }

    private void sendStickEvent(List<Object> stickMessagePool) {
        if (stickMessagePool.size() == 0) return;
        for (Object o : observableMap.keySet()) {
            Method method = observableMap.get(o);
            try {
                for (Object os : stickMessagePool) {
                    method.invoke(o, os);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        stickMessagePool.clear();
        PangJiao.info("stickMessagePool 消息池:" + stickMessagePool.size());
    }
}
