package pxy.com.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pxy.pangjiao.compiler.injectview.annotation.InitView;

public class MainActivity extends AppCompatActivity {

    @InitView(id = R.id.tv_title)
    public TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle.setText("hello pangjiao");
    }
}
