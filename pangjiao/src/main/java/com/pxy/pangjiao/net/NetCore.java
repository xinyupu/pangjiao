package com.pxy.pangjiao.net;


import com.google.gson.Gson;
import com.pxy.pangjiao.common.Mapper;
import com.pxy.pangjiao.common.Utils;
import com.pxy.pangjiao.net.helper.INetListener;
import com.pxy.pangjiao.net.helper.NetHelper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by pxy on 2018/2/1.
 */

public class NetCore {

    private static NetCore httpManager;

    private NetDefaultConfig config;
    private INetListener listener;

    public static NetCore init(NetDefaultConfig config) {
        if (httpManager == null) {
            synchronized (NetCore.class) {
                if (httpManager == null) {
                    httpManager = new NetCore(config);
                }
            }
        }
        return httpManager;
    }

    private NetCore() {
    }


    private NetCore(NetDefaultConfig config) {
        this.config = config;
    }

    public static NetCore getHttpManger() {
        return httpManager;
    }

    public <T extends NetModel, V> V parse(T t) {
        NetHelper.Configs parse = NetHelper.parse(t);
        int connectTimeOut = parse.getConnectTimeOut();
        int readTimeOut = parse.getReadTimeOut();
        String url;
        if (!Utils.isEmpty(this.config.getHost())) {
            url = this.config.getHost() + parse.getApi();
        } else {
            url = parse.getHost() + parse.getApi();
        }
        if (parse.getMethod().equals("POST")) {
            String content = NetHelper.parseRequestContent(NetHelper.POST, t);
            HttpEngine.Response response;
            if (this.config.getGlobeConnectTimeOut() != connectTimeOut && this.config.getGlobeReadTimeOut() != readTimeOut) {
                NetDefaultConfig mConfig = new NetDefaultConfig();
                mConfig.setGlobeConnectTimeOut(connectTimeOut);
                mConfig.setGlobeReadTimeOut(readTimeOut);
                response = HttpEngine.post(url, content, mConfig);
            } else if (this.config.getGlobeReadTimeOut() != readTimeOut) {
                NetDefaultConfig mConfig = new NetDefaultConfig();
                mConfig.setGlobeConnectTimeOut(config.getGlobeConnectTimeOut());
                mConfig.setGlobeReadTimeOut(readTimeOut);
                response = HttpEngine.post(url, content, mConfig);
            } else if (this.config.getGlobeConnectTimeOut() != connectTimeOut) {
                NetDefaultConfig mConfig = new NetDefaultConfig();
                mConfig.setGlobeConnectTimeOut(connectTimeOut);
                mConfig.setGlobeReadTimeOut(config.getGlobeReadTimeOut());
                response = HttpEngine.post(url, content, mConfig);
            } else {
                response = HttpEngine.post(url, content, config);
            }
            Type genericSuperclass = t.getClass().getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                Class<? extends ResponseBase> aClass = (Class<? extends ResponseBase>) parameterizedType.getActualTypeArguments()[0];
                if (response.isSuccess()) {
                    ResponseBase responseBase = new Gson().fromJson(response.getData(), aClass);
                    responseBase.isConnectSuccess = true;
                    return (V) responseBase;
                } else {
                    ResponseBase responseBase = null;
                    try {
                        responseBase = aClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    responseBase.isConnectSuccess = false;
                    responseBase.exception = response.getData();
                    return (V) responseBase;
                }
            }
        } else if (parse.getMethod().equals("GET")) {
            String content = NetHelper.parseRequestContent(NetHelper.GET, t);
            String response;
            if (this.config.getGlobeConnectTimeOut() != connectTimeOut && this.config.getGlobeReadTimeOut() != readTimeOut) {
                NetDefaultConfig mConfig = new NetDefaultConfig();
                mConfig.setGlobeConnectTimeOut(connectTimeOut);
                mConfig.setGlobeReadTimeOut(readTimeOut);
                response = HttpEngine.get(url + content, mConfig);
            } else if (this.config.getGlobeReadTimeOut() != readTimeOut) {
                NetDefaultConfig mConfig = new NetDefaultConfig();
                mConfig.setGlobeConnectTimeOut(config.getGlobeConnectTimeOut());
                mConfig.setGlobeReadTimeOut(readTimeOut);
                response = HttpEngine.get(url + content, mConfig);
            } else if (this.config.getGlobeConnectTimeOut() != connectTimeOut) {
                NetDefaultConfig mConfig = new NetDefaultConfig();
                mConfig.setGlobeConnectTimeOut(connectTimeOut);
                mConfig.setGlobeReadTimeOut(config.getGlobeReadTimeOut());
                response = HttpEngine.get(url + content, mConfig);
            } else {
                response = HttpEngine.get(url + content, config);
            }

            Type genericSuperclass = t.getClass().getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                Class<? extends Type> aClass = (Class<? extends Type>) parameterizedType.getActualTypeArguments()[0];
                return (V) new Gson().fromJson(response, aClass);
            }
        } else {
            throw new RuntimeException("not find request method " + parse.getMethod());
        }
        return null;
    }

    private void setNetListener(INetListener listener) {
        this.listener = listener;
    }

    public INetListener getListener() {
        return listener;
    }
}
