package com.pxy.pangjiao.logger;

/**
 * Created by pxy on 2018/2/8.
 */

public interface IPLog {

    void info(String tag, String msg);

    void error(String tag, String msg);
}
