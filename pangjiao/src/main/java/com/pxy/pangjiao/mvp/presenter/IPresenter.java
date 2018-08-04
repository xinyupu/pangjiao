package com.pxy.pangjiao.mvp.presenter;


import com.pxy.pangjiao.mvp.presenter.helper.MainThread;

/**
 * Created by pxy on 2018/2/1.
 */
/*
 *  IPresenter 里的方法默认执行在子线程
 *  不进行耗时操作的方法，可以添加@MainThread 选择执行在主线程
 *
 * */
public interface IPresenter {

    @MainThread
    void onDestroy();

}
