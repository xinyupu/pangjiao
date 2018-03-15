package com.pxy.pangjiao.net;


import com.google.gson.Gson;
import com.pxy.pangjiao.common.Utils;
import com.pxy.pangjiao.net.helper.NetHelper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by pxy on 2018/2/1.
 */

public class NetCore {

    private static NetCore httpManager;

    private NetDefaultConfig config;

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
        String url;
        if (!Utils.isEmpty(this.config.getHost())) {
            url = this.config.getHost() + parse.getApi();
        } else {
            url = parse.getHost() + parse.getApi();
        }
        if (parse.getMethod().equals("POST")) {
            String content = NetHelper.parseRequestContent(NetHelper.Post, t);
            String response = HttpEngine.post(url, content, config);
            Type genericSuperclass = t.getClass().getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                Class<? extends Type> aClass = (Class<? extends Type>) parameterizedType.getActualTypeArguments()[0];
                return (V) new Gson().fromJson(response, aClass);
            }
        } else if (parse.getMethod().equals("GET")) {
            String content = NetHelper.parseRequestContent(NetHelper.Get, t);
            String response = HttpEngine.get(url + content, config);
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

}
