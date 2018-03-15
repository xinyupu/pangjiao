package com.pxy.pangjiao.common;

/**
 * Created by pxy on 2017/11/30.
 */

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by shandengx on 2017-09-22.
 */

public class ExpUtil
{
    public static String getStackTrace(Throwable throwable)
    {
        StringWriter stackTrace = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString();
    }
}
