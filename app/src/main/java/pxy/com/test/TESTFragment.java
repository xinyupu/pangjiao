package pxy.com.test;

import android.widget.Button;

import com.pxy.pangjiao.compiler.injectview.annotation.InitView;
import com.pxy.pangjiao.mvp.viewmodel.views.PJFragment;

/**
 * Created by Administrator on 2018/3/15.
 */

public class TESTFragment extends PJFragment {

    @InitView(id = R.id.btn_test)
    public Button btnTest;


    @Override
    protected int initView() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initData() {
        btnTest.setText("hello pangjiao");
    }
}
