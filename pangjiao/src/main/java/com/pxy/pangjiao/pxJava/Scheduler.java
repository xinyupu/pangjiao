package com.pxy.pangjiao.pxJava;


import android.os.Handler;

import com.pxy.pangjiao.mvp.MVPCore;
import com.pxy.pangjiao.mvp.ThreadPool.ThreadPool;

/**
 * Created by Administrator on 2018/7/23.
 */

public class Scheduler {

    public static String io = "IO";

    public static final String androidMain = "androidMain";

    private String currentThread = "IO";


    public void execute(Runnable runnable) {
        if (currentThread.equals(Scheduler.io)) {
            ThreadPool threadPool = MVPCore.getInstance().getThreadPool();
            threadPool.execute(runnable);
        } else if (currentThread.equals(Scheduler.androidMain)) {
            Handler mainHandler = MVPCore.getInstance().getMainHandler();
            mainHandler.post(runnable);
        }
    }

    public void setCurrentThread(String threadMain) {
        this.currentThread = threadMain;
    }

}
