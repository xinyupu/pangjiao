package pxy.com.test;


import android.databinding.DataBindingUtil;

import com.pxy.pangjiao.compiler.mpv.annotation.AutowireProxy;
import com.pxy.pangjiao.compiler.mpv.annotation.Views;
import com.pxy.pangjiao.mvp.viewmodel.views.PJAppMVVPCompatActivity;

import pxy.com.application.IMemberPresent;
import pxy.com.application.IMainActivityView;
import pxy.com.application.ITouristService;
import pxy.com.application.imp.TourisstV2Service;
import pxy.com.model.UserView;
import pxy.com.test.databinding.ActivityMainBinding;

@Views
public class MainActivity extends PJAppMVVPCompatActivity implements IMainActivityView {


    @AutowireProxy
    public IMemberPresent memberPresent;

    @AutowireProxy(imp = TourisstV2Service.class)
    public ITouristService touristService;

    private ActivityMainBinding binding;

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    public void initData() {
        User user=new User();
        user.name.set("哥哥");
        binding.setUser(user);
        memberPresent.login("Tom", "123456");
        touristService.payCash(2.0);

    }

    @Override
    public void refresh(Object o) {

    }

}
