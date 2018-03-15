package com.pxy.pangjiao.mvp.presenter;


import com.pxy.pangjiao.mvp.presenter.helper.MainThread;

/**
 * Created by pxy on 2018/2/1.
 */

public interface IPresenter {

    @MainThread
    void build(Object view);

    @MainThread
    void onDestroy();

}
