package pxy.com.application.imp;

import com.pxy.pangjiao.compiler.mpv.annotation.Autowire;
import com.pxy.pangjiao.compiler.mpv.annotation.Presenter;

import pxy.com.application.IMemberPresent;
import pxy.com.application.IMainActivityView;
import pxy.com.service.IAppService;
import pxy.com.service.IMachineService;
import pxy.com.service.imp.DefaultAppService;


/**
 * Created by Administrator on 2018/3/15.
 */

@Presenter
public class MemberPresent implements IMemberPresent {

    @Autowire
    public IAppService appService;

    @Autowire
    public IMachineService machineService;

    private IMainActivityView memberView;

    @Override
    public void build(Object o) {
        memberView = (IMainActivityView) o;
    }

    @Override
    public void onDestroy() {
        this.memberView = null;
    }


    @Override
    public void login(String name, String pwd) {
        String login = appService.login(name, pwd);
        memberView.showToast(login);
    }
}
