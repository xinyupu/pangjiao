package com.pxy.pangjiao.logger;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/6/21.
 */

public class TimeFormat {

    public static String getTimeFormatAll() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }

    public static String getTimeFormatDay() {
        SimpleDateFormat format = new SimpleDateFormat("dd hh:mm:ss");
        return format.format(new Date());
    }

    public static String getTimeFormatHours() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        return format.format(new Date());
    }

    public static String getTimeEndDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
}
