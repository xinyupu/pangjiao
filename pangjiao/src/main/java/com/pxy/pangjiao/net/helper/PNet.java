package com.pxy.pangjiao.net.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pxy on 2018/2/1.
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PNet {
    String method() default "POST";

    String host() default "";

    String api() default "";

    int connectTimeOut() default 5000;

    int readTimeOut() default 5000;
}
