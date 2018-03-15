package com.pxy.pangjiao.compiler.mpv.config;

/**
 * Created by pxy on 2018/3/14.
 */

public enum AnnotationType {
    Class(0),
    Field(1),
    PARAMETER(2),
    Method(3);

    AnnotationType(int value) {
        this.value = value;
    }

    public int value;
}
