package com.pxy.pangjiao.compiler.mpv.config;

/**
 * Created by Administrator on 2018/3/16.
 */

public class InterfaceConfig {
    private String interfaceClassName;
    private String interfaceImpClassName;

    public InterfaceConfig(String interfaceClassName, String interfaceImpClassName) {
        this.interfaceClassName = interfaceClassName;
        this.interfaceImpClassName = interfaceImpClassName;
    }

    public String getInterfaceClassName() {
        return interfaceClassName;
    }

    public void setInterfaceClassName(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }

    public String getInterfaceImpClassName() {
        return interfaceImpClassName;
    }

    public void setInterfaceImpClassName(String interfaceImpClassName) {
        this.interfaceImpClassName = interfaceImpClassName;
    }
}
