package com.pxy.pangjiao.mvp.ioccontainer;

/**
 * Created by pxy on 2018/3/14.
 */

public class AutoWireConfig {

    private String fieldClassName;
    private String fieldName;

    public AutoWireConfig(String fieldClassName, String fieldName) {
        this.fieldClassName = fieldClassName;
        this.fieldName = fieldName;
    }

    public String getFieldClassName() {
        return fieldClassName;
    }

    public void setFieldClassName(String fieldClassName) {
        this.fieldClassName = fieldClassName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
