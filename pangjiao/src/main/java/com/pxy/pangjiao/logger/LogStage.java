package com.pxy.pangjiao.logger;

import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/6/21.
 */

public interface LogStage {

    void log(int level, String tag, String msg);

    void logJson(String json, String tag);

    void logXml(@Nullable String xml, String tag);
}
