package com.pxy.pangjiao.mvp.ThreadPool;

import com.pxy.pangjiao.mvp.ThreadPoolDefaultConfig;

public class ThreadPool   {

    private static ThreadPoolProxy mNormalPool = null;
    private static ThreadPool _intance = null;

    private ThreadPool(ThreadPoolDefaultConfig config) {
        if (mNormalPool == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mNormalPool == null) {
                    mNormalPool = new ThreadPoolProxy(config.getCoreThreadSize(), config.getMaxThreadSize(), config.getKeepAliveTime());
                }
            }
        }
    }

    public static ThreadPool getInstance(ThreadPoolDefaultConfig config) {
        if (_intance == null) {
            synchronized (ThreadPool.class) {
                _intance = new ThreadPool(config);
            }
        }
        return _intance;
    }

    public void execute(Runnable runnable) {
        mNormalPool.execute(runnable);
    }
}
