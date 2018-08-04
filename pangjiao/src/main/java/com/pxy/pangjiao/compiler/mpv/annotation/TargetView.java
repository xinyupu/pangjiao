package com.pxy.pangjiao.compiler.mpv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 绑定present的view
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TargetView {

}
