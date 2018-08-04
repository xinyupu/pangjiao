package pxy.com.application;

import com.pxy.pangjiao.mvp.presenter.IPresenter;

import pxy.com.test.User;

/**
 * Created by Administrator on 2018/3/17.
 */

public interface ITouristService extends IPresenter {

    void payCash(double money);

    void active(String code);
}
