package com.pxy.pangjiao.compiler.mpv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pxy on 2018/3/14.
 */

/*
 *自动注入对象，注入的对象需要添加@Service或者@Presenter或者@Views注解
 * */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Autowire {
    Class<?> imp() default Void.class;
}
