package com.pxy.pangjiao.compiler.mpv.config;

import com.pxy.pangjiao.compiler.mpv.annotation.Service;

import java.util.jar.Attributes;

import javax.lang.model.element.Name;

/**
 * Created by Administrator on 2018/3/16.
 */

public class AutoWireCompilerConfig {

    private String autoFieldClassName;

    private boolean isInterface;

    private String autoFieldClassImpName;

    private String fieldName;
    private Name simpleName;

    public Name getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(Name simpleName) {
        this.simpleName = simpleName;
    }

    public String getAutoFieldClassName() {
        return autoFieldClassName;
    }

    public void setAutoFieldClassName(String autoFieldClassName) {
        this.autoFieldClassName = autoFieldClassName;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    public String getAutoFieldClassImpName() {
        return autoFieldClassImpName;
    }

    public void setAutoFieldClassImpName(String autoFieldClassImpName) {
        this.autoFieldClassImpName = autoFieldClassImpName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
