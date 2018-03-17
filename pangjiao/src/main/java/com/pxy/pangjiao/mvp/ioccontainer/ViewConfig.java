package com.pxy.pangjiao.mvp.ioccontainer;

/**
 * Created by Administrator on 2018/3/17.
 */

public class ViewConfig {

    public String fieldName;
    public String impClassName;

    public ViewConfig(String fieldName,String impClassName) {
        this.fieldName = fieldName;
        this.impClassName=impClassName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getImpClassName() {
        return impClassName;
    }

    public void setImpClassName(String impClassName) {
        this.impClassName = impClassName;
    }
}
