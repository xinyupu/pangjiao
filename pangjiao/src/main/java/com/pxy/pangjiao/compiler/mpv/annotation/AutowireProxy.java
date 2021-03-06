package com.pxy.pangjiao.compiler.mpv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pxy on 2018/3/14.
 */
/*
 * 自动注入代理对象，需使用在UI层
 * */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface AutowireProxy {
    Class<?> imp() default Void.class;
}
