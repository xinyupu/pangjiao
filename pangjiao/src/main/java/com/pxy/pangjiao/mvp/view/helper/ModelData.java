package com.pxy.pangjiao.mvp.view.helper;

import com.pxy.pangjiao.mvp.viewmodel.model.SourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pxy on 2018/2/1.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelData {

    int id() default 0;

    SourceType type() default SourceType.Native;

    String field() default "";

}
