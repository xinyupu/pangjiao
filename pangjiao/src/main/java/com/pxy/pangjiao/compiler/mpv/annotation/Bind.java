package com.pxy.pangjiao.compiler.mpv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2018/3/26.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.PARAMETER)
public @interface Bind {
}
