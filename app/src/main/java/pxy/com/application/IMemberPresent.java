package pxy.com.application;

import com.pxy.pangjiao.mvp.presenter.IPresenter;
import com.pxy.pangjiao.mvp.view.helper.CurrentThread;

/**
 * Created by Administrator on 2018/3/15.
 */


public interface IMemberPresent extends IPresenter {
    void login(String name, String pwd);

    void active(String activeCode);
}
