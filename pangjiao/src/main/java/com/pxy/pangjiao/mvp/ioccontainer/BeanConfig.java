package com.pxy.pangjiao.mvp.ioccontainer;

/**
 * Created by pxy on 2018/3/13.
 */

public class BeanConfig {

    private Object object;
    private boolean isSingleton = false;
    private Class<?>[] superClass = null;

    public BeanConfig(Object object, boolean isSingleton) {
        this.object = object;
        this.isSingleton = isSingleton;
    }


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean singleton) {
        isSingleton = singleton;
    }

    public Class<?>[] getSuperClass() {
        return superClass;
    }

    public void setSuperClass(Class<?>[] superClass) {
        this.superClass = superClass;
    }
}
