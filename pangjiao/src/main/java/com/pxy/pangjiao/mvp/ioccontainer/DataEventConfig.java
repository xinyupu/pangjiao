package com.pxy.pangjiao.mvp.ioccontainer;

/**
 * Created by pxy on 2018/3/14.
 */

public class DataEventConfig {

    private String hostClassName;
    private String methodName;

    public DataEventConfig(String hostClassName, String methodName) {
        this.hostClassName = hostClassName;
        this.methodName = methodName;
    }

    public String getHostClassName() {
        return hostClassName;
    }

    public void setHostClassName(String hostClassName) {
        this.hostClassName = hostClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
