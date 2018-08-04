package pxy.com.application.imp;

import com.pxy.pangjiao.compiler.mpv.annotation.Autowire;
import com.pxy.pangjiao.compiler.mpv.annotation.Presenter;
import com.pxy.pangjiao.compiler.mpv.annotation.TargetView;

import pxy.com.adapter.protocol.ResponseActive;
import pxy.com.application.IMainActivityView;
import pxy.com.application.ITouristService;
import pxy.com.service.IAppService;

/**
 * Created by Administrator on 2018/3/17.
 */

@Presenter
public class TouristService implements ITouristService {


    @Autowire
    public IAppService appService;

    @TargetView
    public IMainActivityView memberView;


    @Override
    public void payCash(double money) {
        String fwetwe = appService.login("123", "fwetwe");
        memberView.showToast(fwetwe);
    }

    @Override
    public void active(String activeCode) {
        ResponseActive active = appService.active(activeCode);
        if (active.isConnectSuccess()) {

        } else {
            memberView.showToast(active.getException());
        }
    }


    @Override
    public void onDestroy() {
        this.memberView = null;
    }


}
