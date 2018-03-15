package com.pxy.pangjiao.compiler.mpv.config;

import com.pxy.pangjiao.compiler.mpv.annotation.Presenter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by pxy on 2018/3/13.
 */

public class CompilerClassConfig implements IConfig {

    private TypeElement element;

    private boolean isSingleton = true;
    private Class annotationClass;

    public CompilerClassConfig(Element element, Class cls) {
        if (element.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(String.format("Only CLASS can be annotated with @%s",
                    cls.getSimpleName()));
        }
        this.annotationClass = cls;
        this.element = (TypeElement) element;
        if (cls == Presenter.class) {
            isSingleton = this.element.getAnnotation(Presenter.class).singleton();
        }
    }

    public TypeMirror getAnnotatedClass() {
        TypeMirror superclass = this.element.getSuperclass();
        return superclass;
    }

    public TypeName getType() {
        return ClassName.get(element);
    }

    @Override
    public TypeMirror getFieldType() {
        return null;
    }

    @Override
    public TypeElement getOwnerType() {
        return null;
    }

    @Override
    public Name getFieldName() {
        return null;
    }

    @Override
    public Name getMethodName() {
        return null;
    }

    @Override
    public AnnotationType getAnnotationType() {
        return AnnotationType.Class;
    }

    public boolean isSingleton() {
        return this.isSingleton;
    }

    @Override
    public Class getAnnotation() {
        return this.annotationClass;
    }

}
