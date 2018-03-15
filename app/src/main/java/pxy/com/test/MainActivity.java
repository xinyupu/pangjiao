package pxy.com.test;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pxy.pangjiao.compiler.injectview.annotation.InitView;
import com.pxy.pangjiao.compiler.injectview.annotation.OnClick;
import com.pxy.pangjiao.compiler.mpv.annotation.AutowireProxy;
import com.pxy.pangjiao.mvp.viewmodel.views.PJAppCompatActivity;
import com.pxy.pangjiao.mvp.viewmodel.views.PJSupportFragmentActivity;

import pxy.com.application.IMemberPresent;
import pxy.com.application.IMemberView;

public class MainActivity extends PJAppCompatActivity implements IMemberView {



    @AutowireProxy
    public IMemberPresent memberPresent;


    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        memberPresent.login("Tom", "123456");
    }

    @Override
    public void refresh(Object o) {

    }

}
