package com.pxy.pangjiao.mvp.viewmodel.model;

/**
 * Created by pxy on 2018/2/1.
 */

public enum SourceType {

    Text(0),
    Native(1);

    SourceType(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}
