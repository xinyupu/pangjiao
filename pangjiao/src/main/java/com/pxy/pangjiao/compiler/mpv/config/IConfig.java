package com.pxy.pangjiao.compiler.mpv.config;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by pxy on 2018/3/14.
 */

public interface IConfig {
    TypeName getType();

    TypeMirror getFieldType();

    TypeElement getOwnerType();

    Name getFieldName();

    Name getMethodName();

    AnnotationType getAnnotationType();

    boolean isSingleton();

    Class getAnnotation();
}
