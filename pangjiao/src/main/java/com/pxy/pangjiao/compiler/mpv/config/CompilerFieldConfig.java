package com.pxy.pangjiao.compiler.mpv.config;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by pxy on 2018/3/13.
 */

public class CompilerFieldConfig implements IConfig {

    private VariableElement element;

    private boolean isSingleton = true;
    private Class annotationClass;
    private TypeElement typeElement;

    public CompilerFieldConfig(Element element, Class cls) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only FIELD can be annotated with @%s",
                    cls.getSimpleName()));
        }
        this.annotationClass = cls;
        this.element = (VariableElement) element;
        typeElement = (TypeElement) this.element.getEnclosingElement();
    }

    @Override
    public TypeName getType() {
        return null;
    }

    @Override
    public TypeMirror getFieldType() {
        return element.asType();
    }


    @Override
    public TypeElement getOwnerType() {
        return typeElement;
    }

    @Override
    public Name getFieldName() {
        return this.element.getSimpleName();
    }

    @Override
    public Name getMethodName() {
        return null;
    }

    @Override
    public AnnotationType getAnnotationType() {
        return AnnotationType.Field;
    }

    @Override
    public boolean isSingleton() {
        return this.isSingleton;
    }

    @Override
    public Class getAnnotation() {
        return this.annotationClass;
    }

}
