package com.pxy.pangjiao.compiler.injectview.annotation;


import android.util.Log;
import android.view.View;

import com.pxy.pangjiao.compiler.mpv.config.AnnotationType;
import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created by pxy on 2018/3/12.
 */

public class OnclickMethod {

    private ExecutableElement executableElement;
    private VariableElement variableElement;

    private int[] resIds;
    private Name mMethodName;

    private AnnotationType annotationType;

    public OnclickMethod(Element element) {
        if (element.getKind() != ElementKind.METHOD && element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only methods or Filed can be annotated with @%s",
                    OnClick.class.getSimpleName()));
        }
        if (element.getKind() == ElementKind.METHOD) {
            executableElement = (ExecutableElement) element;
            annotationType = AnnotationType.Method;
            resIds = executableElement.getAnnotation(OnClick.class).value();
            mMethodName = executableElement.getSimpleName();
            List<? extends VariableElement> parameters = executableElement.getParameters();
            if (parameters.size() != 1)
                throw new IllegalArgumentException("There must be and only one parameter");
            if (!parameters.get(0).asType().toString().equals("android.view.View")) {
                throw new IllegalArgumentException("The argument to the method must be a view");
            }
        } else {
            variableElement = (VariableElement) element;
            annotationType = AnnotationType.Field;
            resIds = variableElement.getAnnotation(OnClick.class).value();
        }

        if (resIds == null) {
            throw new IllegalArgumentException(String.format("Must set valid ids for @%s",
                    OnClick.class.getSimpleName()));
        } else {
            for (int id : resIds) {
                if (id < 0) {
                    throw new IllegalArgumentException(String.format("Must set valid id for @%s",
                            OnClick.class.getSimpleName()));
                }
            }
        }

    }

    /**
     * 获取方法名称
     *
     * @return
     */
    public Name getMethodName() {
        return mMethodName;
    }

    /**
     * 获取id数组
     *
     * @return
     */
    public int[] getResIds() {
        return resIds;
    }

    public TypeMirror getFieldType() {
        return variableElement.asType();
    }

    public Name getFieldName() {
        return variableElement.getSimpleName();
    }

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public List<? extends VariableElement> getParameter() {
        return executableElement.getParameters();
    }

}
