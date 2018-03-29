package pxy.com.test;

import android.app.Application;

import com.pxy.pangjiao.PangJiao;
import com.pxy.pangjiao.net.NetDefaultConfig;

/**
 * Created by Administrator on 2018/3/15.
 */

public class TSApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PangJiao.init(this);
    }
}
