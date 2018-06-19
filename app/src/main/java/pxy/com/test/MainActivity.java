package pxy.com.test;


import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.Button;

import com.pxy.pangjiao.compiler.injectview.annotation.OnClick;
import com.pxy.pangjiao.compiler.mpv.annotation.AutowireProxy;
import com.pxy.pangjiao.compiler.mpv.annotation.Views;
import com.pxy.pangjiao.mvp.viewmodel.views.PJAppMVVPCompatActivity;

import pxy.com.application.IMemberPresent;
import pxy.com.application.IMainActivityView;
import pxy.com.application.ITouristService;
import pxy.com.application.imp.TourisstV2Service;
import pxy.com.application.imp.TouristService;
import pxy.com.model.UserView;

@Views
public class MainActivity extends PJAppMVVPCompatActivity implements IMainActivityView {


    @AutowireProxy
    public IMemberPresent memberPresent;

    @AutowireProxy(imp = TouristService.class)
    public ITouristService touristService;


    private User user;

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }


    @OnClick(R.id.request)
    public View.OnClickListener btn_Click=v -> {
        touristService.active(user);
    };

    @Override
    public void refresh(Object o) {

    }

}
