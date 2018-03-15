package com.pxy.pangjiao.databus;


import java.util.List;
import java.util.Observable;

/**
 * Created by pxy on 2018/2/6.
 */

public class DataObservable extends Observable {

    private List<Object> messagePool;
    private List<Object> stickMessagePool;


    public DataObservable(List<Object> messagePool, List<Object> stickMessagePool) {
        this.messagePool = messagePool;
        this.stickMessagePool = stickMessagePool;
    }

    public List<Object> getMessagePool() {
        return messagePool;
    }

    public List<Object> getStickMessagePool() {
        return stickMessagePool;
    }

    public void notifyChanged() {
        setChanged();
        notifyObservers();
    }

}
