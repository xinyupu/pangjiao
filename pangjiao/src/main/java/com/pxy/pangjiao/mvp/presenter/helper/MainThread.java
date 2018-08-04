package com.pxy.pangjiao.mvp.presenter.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pxy on 2018/2/7.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MainThread {
    boolean showLoading() default true;
}
