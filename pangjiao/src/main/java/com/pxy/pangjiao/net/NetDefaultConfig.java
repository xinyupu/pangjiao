package com.pxy.pangjiao.net;

/**
 * Created by pxy on 2018/2/1.
 */

public class NetDefaultConfig {

    protected int connectTimeOut = 5000;
    protected int readTimeOut = 5000;
    protected String host = "";

    public NetDefaultConfig setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }

    public NetDefaultConfig setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public NetDefaultConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public String getHost() {
        return host;
    }
}
