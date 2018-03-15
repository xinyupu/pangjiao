package com.pxy.pangjiao.compiler.injectview.annotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by pxy on 2018/3/12.
 */

public class InitViewField {

    private VariableElement mVariableElement;
    private int id;

    public InitViewField(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only fields can be annotated with @%s",
                    InitView.class.getSimpleName()));
        }
        mVariableElement = (VariableElement) element;

        InitView bindView = mVariableElement.getAnnotation(InitView.class);
        id = bindView.id();
        if (id < 0) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for field %s is not valid !", InitView.class.getSimpleName(),
                            mVariableElement.getSimpleName()));
        }
    }

    public int getId() {
        return id;
    }

    public TypeMirror getFieldType() {
        return mVariableElement.asType();
    }

    public Name getFieldName() {
        return mVariableElement.getSimpleName();
    }
}
