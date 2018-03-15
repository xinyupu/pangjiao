package pxy.com.test;

import android.widget.TextView;

import com.pxy.pangjiao.compiler.injectview.annotation.InitView;
import com.pxy.pangjiao.compiler.mpv.annotation.AutowireProxy;
import com.pxy.pangjiao.mvp.viewmodel.views.PJAppCompatActivity;
import com.pxy.pangjiao.mvp.viewmodel.views.PJSupportFragmentActivity;

import pxy.com.application.IMemberPresent;
import pxy.com.application.IMemberView;

public class MainActivity extends PJAppCompatActivity implements IMemberView {

    @InitView(id = R.id.tv_title)
    public TextView tvTitle;

    @AutowireProxy
    public IMemberPresent memberPresent;


    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        tvTitle.setText("hello 庞娇");
        memberPresent.open("打开腿");
    }

    @Override
    public void refresh(Object o) {

    }
}
