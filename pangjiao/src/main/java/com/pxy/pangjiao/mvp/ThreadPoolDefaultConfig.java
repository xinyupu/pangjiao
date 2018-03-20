package com.pxy.pangjiao.mvp;

/**
 * Created by Administrator on 2018/3/20.
 */

public class ThreadPoolDefaultConfig {

    protected int coreThreadSize=5;

    protected int maxThreadSize=15;

    protected long keepAliveTime=2000;

    public int getCoreThreadSize() {
        return coreThreadSize;
    }

    public void setCoreThreadSize(int coreThreadSize) {
        this.coreThreadSize = coreThreadSize;
    }

    public int getMaxThreadSize() {
        return maxThreadSize;
    }

    public void setMaxThreadSize(int maxThreadSize) {
        this.maxThreadSize = maxThreadSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
