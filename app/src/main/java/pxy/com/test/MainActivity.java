package pxy.com.test;

import android.widget.TextView;

import com.pxy.pangjiao.compiler.injectview.annotation.InitView;
import com.pxy.pangjiao.mvp.viewmodel.views.PJSupportFragmentActivity;

public class MainActivity extends PJSupportFragmentActivity {

    @InitView(id = R.id.tv_title)
    public TextView tvTitle;


    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        tvTitle.setText("hello 庞娇");
    }
}
