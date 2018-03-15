package pxy.com.application.imp;

import com.pxy.pangjiao.compiler.mpv.annotation.Autowire;
import com.pxy.pangjiao.compiler.mpv.annotation.AutowireProxy;
import com.pxy.pangjiao.compiler.mpv.annotation.Presenter;

import pxy.com.application.IMemberPresent;
import pxy.com.application.IMemberView;
import pxy.com.service.IAppService;

/**
 * Created by Administrator on 2018/3/15.
 */

@Presenter
public class MemberPresent implements IMemberPresent {


    @Autowire
    public IAppService appService;

    private IMemberView memberView;

    @Override
    public void build(Object o) {
        memberView = (IMemberView) o;
    }

    @Override
    public void onDestroy() {
        this.memberView = null;
    }


    @Override
    public void login(String name, String pwd) {
        appService.login(name, pwd);
    }
}
