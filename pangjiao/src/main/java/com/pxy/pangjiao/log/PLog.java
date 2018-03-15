package com.pxy.pangjiao.log;

import android.util.Log;

/**
 * Created by pxy on 2018/2/2.
 */

public class PLog implements IPLog {

    public static final String TAG = "PangJiao";

    @Override
    public void info(String tag, String msg) {
        Log.i(TAG, msg + "");
    }

    @Override
    public void error(String tag, String msg) {
        Log.i(TAG, msg);
    }
}
