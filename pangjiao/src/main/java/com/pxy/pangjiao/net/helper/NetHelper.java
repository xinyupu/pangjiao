package com.pxy.pangjiao.net.helper;

import com.google.gson.Gson;
import com.pxy.pangjiao.net.NetModel;

import java.lang.reflect.Field;

/**
 * Created by pxy on 2018/2/1.
 */

public class NetHelper {

    public static final String GET = "GET";
    public static final String POST = "POST";

    public static Configs parse(Object o) {
        if (!o.getClass().isAnnotationPresent(PNet.class))
            throw new RuntimeException(o.getClass().getName() + " must @PNet.class");
        if (!NetModel.class.isAssignableFrom(o.getClass()))
            throw new RuntimeException(o.getClass().getName() + " must extends NetModel.class");
        PNet annotation = o.getClass().getAnnotation(PNet.class);

        String method = annotation.method();
        String api = annotation.api();
        String host = annotation.host();
        int connectTimeOut = annotation.connectTimeOut();
        int readTimeOut = annotation.readTimeOut();
        return new Configs(method, api, host,connectTimeOut,readTimeOut);
    }

    public static String parseRequestContent(String method, NetModel model) {
        Field[] declaredFields = model.getClass().getDeclaredFields();
        String request = "";
        StringBuilder datas = null;
        if (method.equals(POST)) {
            return new Gson().toJson(model);
        } else if (method.equals(GET)) {
            datas = new StringBuilder("?");
            for (Field field : declaredFields) {
                if (!field.isAccessible()) field.setAccessible(true);
                try {
                    if (field.get(model) != null)
                        datas.append(field.getName()).append("=").append(field.get(model)).append("&");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            request = datas.toString().substring(0, datas.toString().length() - 1);
        } else {
            throw new RuntimeException("not find request method " + method);
        }
        return request;
    }


    public static class Configs {

        private String method;
        private String api;
        private String host;
        private int connectTimeOut;
        private int readTimeOut;

        public Configs(String method, String api, String host, int connectTimeOut, int readTimeOut) {
            this.method = method;
            this.api = api;
            this.host = host;
            this.connectTimeOut = connectTimeOut;
            this.readTimeOut = readTimeOut;
        }


        public int getConnectTimeOut() {
            return connectTimeOut;
        }

        public void setConnectTimeOut(int connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
        }

        public int getReadTimeOut() {
            return readTimeOut;
        }

        public void setReadTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }
}
