package com.pxy.pangjiao.mvp.viewmodel.views;


import com.pxy.pangjiao.mvp.presenter.helper.MainThread;

/**
 * Created by pxy on 2018/2/1.
 */


public interface IView {

    @MainThread
    void showToast(String msg);
}
