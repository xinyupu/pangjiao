package com.pxy.pangjiao.compiler.mpv.factory;

import com.pxy.pangjiao.compiler.mpv.config.AutoWireCompilerConfig;
import com.pxy.pangjiao.compiler.mpv.config.ViewsConfig;
import com.pxy.pangjiao.mvp.ioccontainer.BeanConfig;
import com.pxy.pangjiao.mvp.ioccontainer.ViewConfig;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

/**
 * Created by Administrator on 2018/3/16.
 */

public class AutoWireInjectProduct {

    private Map<String, List<AutoWireCompilerConfig>> autoWireConfigMaps;


    Map<String, ViewsConfig> viewConfigMaps;

    public void setAutoWireConfigMaps(Map<String, List<AutoWireCompilerConfig>> autoWireConfigMaps) {
        this.autoWireConfigMaps = autoWireConfigMaps;
    }

    public void setViewConfigMaps(Map<String, ViewsConfig> viewConfigMaps) {
        this.viewConfigMaps = viewConfigMaps;
    }

    /*
         *   Map<String, BeanConfig> container
         * */
    public JavaFile generateFile() throws ClassNotFoundException {

        FieldSpec.Builder container = FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, BeanConfig.class), "container")
                .addModifiers(Modifier.PRIVATE);

        FieldSpec.Builder viewContainer = FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, List.class), "viewContainer");

        ParameterSpec constructorMethodParameter = ParameterSpec.builder(ParameterizedTypeName.get(Map.class, String.class, BeanConfig.class),
                "container")
                .build();

        MethodSpec.Builder constructorMethod = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.container=container")
                .addStatement("this.viewContainer=new $T<>()", HashMap.class)
                .addParameter(constructorMethodParameter);

        MethodSpec.Builder autoWire = MethodSpec.methodBuilder("autoWire")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Map.class,String.class,List.class));
        MethodSpec.Builder getAutoWireInject=MethodSpec.methodBuilder("getAutoWireInject")
                .returns(ClassName.get("com.pxy.pangjiao.mvp","AutoWireInject"))
                .addStatement("return this")
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder autoWireFactory=MethodSpec.methodBuilder("autoWireFactory")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Object.class,"o")
                .addStatement("String className=o.getClass().getName()")
                .returns(Object.class);
        for (String key : autoWireConfigMaps.keySet()) {

            if (viewConfigMaps.get(key)==null){
                List<AutoWireCompilerConfig> configList = autoWireConfigMaps.get(key);
                if (configList != null && configList.size() > 0) {
                    String typeName = key.substring(key.lastIndexOf(".") + 1);
                    String typePackageName=key.replace("."+typeName,"");
                    autoWireFactory.beginControlFlow("if(o.getClass().getName().equals($S))",key);
                    for (AutoWireCompilerConfig config : configList) {
                        String fileClassName = config.getAutoFieldClassImpName();
                        if (fileClassName==null){
                            throw new ClassNotFoundException("\ncause by:\n"+config.getAutoFieldClassName()+" not implemented,or not and @Service @Presenter");
                        }
                        String autoFieldClassName = config.getAutoFieldClassName();
                        String autoFieldTypeName=autoFieldClassName.substring(autoFieldClassName.lastIndexOf(".") + 1);
                        String autoFieldTypePackageName=autoFieldClassName.replace("."+autoFieldTypeName,"");
                        autoWireFactory.addStatement("(($T)o).$N=($T)container.get($S).getObject()",
                                ClassName.get(typePackageName, typeName),
                                config.getFieldName(),
                                ClassName.get(autoFieldTypePackageName, autoFieldTypeName),
                                config.getAutoFieldClassImpName()
                        );
                    }
                    autoWireFactory.endControlFlow();
                }
            }
        }

        for (String key : viewConfigMaps.keySet()) {
            String variableName=key.replace(".","_");
            List<AutoWireCompilerConfig> configList = autoWireConfigMaps.get(key);
            autoWire.addStatement("$T<$T> $N=new $T<>()",List.class,ViewConfig.class,variableName,ArrayList.class);
            for (AutoWireCompilerConfig config:configList){
              String  fileClassName= config.getAutoFieldClassImpName();
                if (fileClassName==null){
                    throw new ClassNotFoundException("\ncause by:\n"+config.getAutoFieldClassName()+" not implemented,or not and @Service @Presenter");
                }
                autoWire.addStatement("$N.add(new $T($S,$S))",variableName,ViewConfig.class,config.getFieldName(),fileClassName);
            }
            autoWire.addStatement("this.viewContainer.put($S,$N)", key, variableName);
        }
        for (String key : autoWireConfigMaps.keySet()) {
            if (viewConfigMaps.get(key)==null){
                List<AutoWireCompilerConfig> configList = autoWireConfigMaps.get(key);
                if (configList != null && configList.size() > 0) {
                    String typeName = key.substring(key.lastIndexOf(".") + 1);
                    String variableName = toLowerCase(typeName);
                    String typePackageName = key.replace("." + typeName, "");
                    autoWire.addStatement("$T $N=($T)container.get($S).getObject()",
                            ClassName.get(typePackageName, typeName),
                            variableName,
                            ClassName.get(typePackageName, typeName),
                            key
                    );
                    for (AutoWireCompilerConfig config : configList) {
                        String fileClassName = config.getAutoFieldClassImpName();
                        if (fileClassName==null){
                            throw new ClassNotFoundException("\ncause by:\n"+config.getAutoFieldClassName()+" not implemented,or not and @Service @Presenter");
                        }
                        String autoFieldName = fileClassName.substring(fileClassName.lastIndexOf(".") + 1);
                        String autoFieldClassPackageName = fileClassName.replace("." + autoFieldName, "");
                        autoWire.addStatement("$N.$N=($T)container.get($S).getObject()",
                                variableName, config.getFieldName(), ClassName.get(autoFieldClassPackageName, autoFieldName),
                                config.getAutoFieldClassImpName()

                        );
                    }
                }
            }
        }

        TypeSpec typeSpec = TypeSpec.classBuilder("AutoWireInject")
                .addField(container.build())
                .addField(viewContainer.build())
                .addMethod(getAutoWireInject.build())
                .addMethod(autoWire.addStatement("return this.viewContainer").build())
                .addMethod(constructorMethod.build())
                .addMethod(autoWireFactory.addStatement("return o").build())
                .addModifiers(Modifier.PUBLIC).build();

        return JavaFile.builder("com.pxy.pangjiao.mvp", typeSpec).build();
    }

    public String toLowerCase(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
