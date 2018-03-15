package com.pxy.pangjiao.mvp.ioccontainer;
import com.pxy.pangjiao.mvp.MVPCore;

/**
 * Created by pxy on 2018/3/13.
 */

public class IOCContainer implements IContainer {

    private static IOCContainer _instance;


    public IOCContainer createInstance() {
        if (_instance == null) {
            synchronized (MVPCore.class) {
                if (_instance == null) {
                    _instance = new IOCContainer();
                }
            }
        }
        return _instance;
    }


    @Override
    public void register(Object o) {

    }

    @Override
    public <T> T resolve(Class<T> t) {
        return null;
    }
}
