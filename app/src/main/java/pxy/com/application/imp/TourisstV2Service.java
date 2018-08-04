package pxy.com.application.imp;

import com.pxy.pangjiao.compiler.mpv.annotation.Autowire;
import com.pxy.pangjiao.compiler.mpv.annotation.Presenter;
import com.pxy.pangjiao.compiler.mpv.annotation.TargetView;

import pxy.com.adapter.protocol.RequestActive;
import pxy.com.adapter.protocol.ResponseActive;
import pxy.com.application.IMainActivityView;
import pxy.com.application.ITouristService;
import pxy.com.service.IAppService;
import pxy.com.service.imp.AppService;
import pxy.com.service.imp.DefaultAppService;
import pxy.com.test.User;

/**
 * Created by Administrator on 2018/3/29.
 */

@Presenter
public class TourisstV2Service implements ITouristService {


    @Autowire(imp = AppService.class)
    public IAppService appService;

    @TargetView
    public IMainActivityView view;

    @Override
    public void payCash(double money) {
        String fwetwe = appService.login("DefaultAppService", "fwetwe");
        view.showToast(fwetwe);
    }

    @Override
    public void active(String code) {

    }


    @Override
    public void onDestroy() {

    }
}
