package com.pxy.pangjiao.net;

/**
 * Created by pxy on 2018/2/1.
 */

public class NetDefaultConfig {

    protected int globeConnectTimeOut = 5000;
    protected int globeReadTimeOut = 5000;
    protected String host = "";

    public NetDefaultConfig setGlobeConnectTimeOut(int globeConnectTimeOut) {
        this.globeConnectTimeOut = globeConnectTimeOut;
        return this;
    }

    public NetDefaultConfig setGlobeReadTimeOut(int globeReadTimeOut) {
        this.globeReadTimeOut = globeReadTimeOut;
        return this;
    }

    public NetDefaultConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public int getGlobeConnectTimeOut() {
        return globeConnectTimeOut;
    }

    public int getGlobeReadTimeOut() {
        return globeReadTimeOut;
    }

    public String getHost() {
        return host;
    }
}
