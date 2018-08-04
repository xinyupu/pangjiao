package com.pxy.pangjiao.logger;

import android.os.Environment;

/**
 * Created by Administrator on 2018/6/21.
 */

public class Logger {

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    private Logger logger;

    private Logger() {

    }

    private static Builder builder;


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DiskLogStage diskLogStage = new DiskLogStage(20, Environment.getExternalStorageState() + "/pangjiao/");
        private LogCatStage logCatStage = new LogCatStage();

        public void build() {
            builder = this;
        }

        public DiskLogStage getDiskLogStage() {
            return diskLogStage;
        }

        public Builder setDiskLogStage(DiskLogStage diskLogStage) {
            this.diskLogStage = diskLogStage;
            return this;
        }

        public LogCatStage getLogCatStage() {
            return logCatStage;
        }

        public Builder setLogCatStage(LogCatStage logCatStage) {
            this.logCatStage = logCatStage;
            return this;
        }
    }


    public static void e(String tag, String msg) {
        e(false, tag, msg);
    }

    public static void e(boolean isLocal, String tag, String msg) {
        if (builder != null) {
            if (isLocal) {
                builder.getDiskLogStage().log(Logger.ERROR, tag, msg);
                builder.getLogCatStage().log(Logger.ERROR, tag, msg);
            } else {
                builder.getLogCatStage().log(Logger.ERROR, tag, msg);
            }
        }
    }

    public static void info(String tag, String msg) {
        info(false, tag, msg);
    }

    public static void info(boolean isLocal, String tag, String msg) {
        if (builder != null) {
            if (isLocal) {
                builder.getDiskLogStage().log(Logger.INFO, tag, msg);
                builder.getLogCatStage().log(Logger.INFO, tag, msg);
            } else {
                builder.getLogCatStage().log(Logger.INFO, tag, msg);
            }
        }
    }

    public static void d(String tag, String msg) {
        d(false, tag, msg);
    }

    public static void d(boolean isLocal, String tag, String msg) {
        if (builder != null) {
            if (isLocal) {
                builder.getDiskLogStage().log(Logger.DEBUG, tag, msg);
                builder.getLogCatStage().log(Logger.DEBUG, tag, msg);
            } else {
                builder.getLogCatStage().log(Logger.DEBUG, tag, msg);
            }
        }
    }

    public static void logJson(String tag, String json) {
        logJson(false, tag, json);
    }

    public static void logJson(boolean isLocal, String tag, String json) {
        if (builder != null) {
            if (isLocal) {
                builder.getDiskLogStage().logJson(json, tag);
                builder.getLogCatStage().logJson(json, tag);
            } else {
                builder.getLogCatStage().logJson(json, tag);
            }
        }
    }


    public void logXmlJson(String tag, String json) {
        logXmlJson(false, tag, json);
    }

    public static void logXmlJson(boolean isLocal, String tag, String json) {
        if (builder != null) {
            if (isLocal) {
                builder.getDiskLogStage().logXml(json, tag);
                builder.getLogCatStage().logXml(json, tag);
            } else {
                builder.getLogCatStage().logXml(json, tag);
            }
        }
    }

}
