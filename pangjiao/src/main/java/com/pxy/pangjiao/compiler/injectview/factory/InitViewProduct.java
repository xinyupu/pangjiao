package com.pxy.pangjiao.compiler.injectview.factory;


import android.view.View;

import com.pxy.pangjiao.compiler.Type;
import com.pxy.pangjiao.compiler.injectview.annotation.InitViewField;
import com.pxy.pangjiao.compiler.injectview.annotation.OnclickMethod;
import com.pxy.pangjiao.compiler.mpv.config.AnnotationType;
import com.pxy.pangjiao.mvp.view.helper.IProvider;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by pxy on 2018/3/12.
 */

public class InitViewProduct {

    private TypeElement mTypeElement;
    private ArrayList<InitViewField> mFields;
    private ArrayList<OnclickMethod> mMethods;
    private Elements mElements;


    public InitViewProduct(TypeElement typeElement, Elements elements) {
        mTypeElement = typeElement;
        mElements = elements;
        mFields = new ArrayList<>();
        mMethods = new ArrayList<>();
    }

    public void addOnclick(OnclickMethod method) {
        mMethods.add(method);
    }

    public void addInitView(InitViewField field) {
        mFields.add(field);
    }

    public JavaFile generateFile() {

        MethodSpec.Builder injectMethod = MethodSpec.methodBuilder("inject").addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(mTypeElement.asType()), "host", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "o")
                .addParameter(IProvider.class, "provider")
                .addAnnotation(Override.class);


        for (InitViewField field : mFields) {
            injectMethod.addStatement("host.$N=($T)provider.findView(o,$L)",
                    field.getFieldName(),
                    ClassName.get(field.getFieldType()), field.getId());
        }

        for (OnclickMethod method : mMethods) {
            // VariableElement
            if (method.getAnnotationType() == AnnotationType.Method) {
                TypeSpec listener = TypeSpec.anonymousClassBuilder("").addSuperinterface(Type.ANDROID_ON_CLICK_LISTENER)
                        .addMethod(MethodSpec.methodBuilder("onClick")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.VOID)
                                .addParameter(Type.ANDROID_VIEW, "view")
                                .addStatement("host.$N(view)", method.getMethodName()).build()).build();

                //     injectMethod.addStatement("listener = $L", listener);
                for (int id : method.getResIds()) {
                    injectMethod.addStatement("(($T)provider.findView(o,$L)).setOnClickListener($L)", Type.ANDROID_VIEW, id, listener);
                }
            } else {
                for (int id : method.getResIds()) {
                    injectMethod.addStatement("(($T)provider.findView(o,$L)).setOnClickListener(host.$N)", Type.ANDROID_VIEW, id, method.getFieldName());
                }
            }

        }


        TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$$ViewInject")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(Type.IViewInject, TypeName.get(mTypeElement.asType())))
                .addMethod(injectMethod.build())
                .build();

        String packageName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, injectClass).build();
    }

}
