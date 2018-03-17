package com.pxy.pangjiao.compiler.mpv.factory;

import com.pxy.pangjiao.compiler.Type;
import com.pxy.pangjiao.compiler.mpv.annotation.Autowire;
import com.pxy.pangjiao.compiler.mpv.annotation.AutowireProxy;
import com.pxy.pangjiao.compiler.mpv.config.CompilerClassConfig;
import com.pxy.pangjiao.compiler.mpv.config.IConfig;
import com.pxy.pangjiao.databus.DataEvent;
import com.pxy.pangjiao.mvp.ioccontainer.AutoWireConfig;
import com.pxy.pangjiao.mvp.ioccontainer.BeanConfig;
import com.pxy.pangjiao.mvp.ioccontainer.DataEventConfig;
import com.pxy.pangjiao.mvp.ioccontainer.IContainerConfig;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * Created by pxy on 2018/3/13.
 */

public class MVPDefaultContainerProduct {

    private List<IConfig> configs;
    private Map<String, List<IConfig>> autoWireMap;
    private Map<String, List<IConfig>> autoWireProxyMap;
    private Map<String, List<IConfig>> dataEventMap;

    public MVPDefaultContainerProduct() {
        configs = new ArrayList<>();
        autoWireMap = new HashMap<>();
        autoWireProxyMap = new HashMap<>();
        dataEventMap = new HashMap<>();
    }

    public void addConfig(CompilerClassConfig config) {
        configs.add(config);
    }

    public void addAllConfig(List<IConfig> configLsit) {
        configs.addAll(configLsit);
    }

    public JavaFile generateFile() {
        FieldSpec.Builder mParameterizedField = FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, BeanConfig.class),
                "container",
                Modifier.PRIVATE);
        FieldSpec.Builder auotwireContainer = FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, List.class),
                "auotwireContainer",
                Modifier.PRIVATE);

        FieldSpec.Builder auotwireProxyContainer = FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, List.class),
                "auotwireProxyContainer",
                Modifier.PRIVATE);

        FieldSpec.Builder dataEventContainer = FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, List.class),
                "dataEventContainer",
                Modifier.PRIVATE);
        FieldSpec.Builder viewContainer = FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, List.class),
                "viewContainer",
                Modifier.PRIVATE);

        FieldSpec.Builder instance = FieldSpec.builder(ClassName.get("com.pxy.pangjiao.mvp", "MVPDefaultContainer"), "instance")
                .addModifiers(Modifier.PRIVATE)
                .addModifiers(Modifier.STATIC);

        FieldSpec.Builder autoWireInject = FieldSpec.builder(ClassName.get("com.pxy.pangjiao.mvp", "AutoWireInject"), "autoWireInject")
                .addModifiers(Modifier.PRIVATE);

        MethodSpec.Builder methodInit = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PRIVATE)
                .returns(TypeName.VOID);


        diffConfig(methodInit);
        // methodInit.addCode(CodeBlock.builder().add("\n").build());
        // initAutWire(methodInit);
        // initAutWireProxy(methodInit);
         initDateEvent(methodInit);


        MethodSpec.Builder getClassTypeContainer = MethodSpec.methodBuilder("getClassTypeContainer")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Map.class, String.class, BeanConfig.class));

        MethodSpec.Builder getAutoWireContainer = MethodSpec.methodBuilder("getAutoWireContainer")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Map.class, String.class, List.class));

        MethodSpec.Builder getAutoWireProxyContainer = MethodSpec.methodBuilder("getAutoWireProxyContainer")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Map.class, String.class, List.class));
        MethodSpec.Builder getDataEvnetContainer = MethodSpec.methodBuilder("getDataEvnetContainer")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Map.class, String.class, List.class));
        MethodSpec.Builder initAutoWire = MethodSpec.methodBuilder("initAutoWire")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Map.class,String.class,List.class))
                .addStatement("autoWireInject=new AutoWireInject(container)")
                .addStatement("this.viewContainer=autoWireInject.autoWire()");

        MethodSpec.Builder getViewContainer = MethodSpec.methodBuilder("getViewContainer")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Map.class,String.class,List.class));

        MethodSpec.Builder   autoWireFactory=MethodSpec.methodBuilder("autoWireFactory")
        .addModifiers(Modifier.PUBLIC)
        .addParameter(Object.class,"o")
        .returns(Object.class)
        .addStatement("return autoWireInject.autoWireFactory(o)");

        TypeSpec typeSpec = TypeSpec.classBuilder("MVPDefaultContainer").addModifiers(Modifier.PUBLIC)
                .addField(mParameterizedField.build())
                .addField(auotwireContainer.build())
                .addField(instance.build())
                .addField(auotwireProxyContainer.build())
                .addField(dataEventContainer.build())
                .addField(viewContainer.build())
                .addField(autoWireInject.build())
                .addMethod(initCreateMethod().addStatement("return instance").build())
                .addMethod(initConstruct().build())
                .addSuperinterface(IContainerConfig.class)
                .addMethod(methodInit.build())
                .addMethod(getClassTypeContainer.addStatement("return container").build())
                .addMethod(initAutoWire.addStatement("return this.viewContainer").build())
                .addMethod(getAutoWireContainer.addStatement("return auotwireContainer").build())
                .addMethod(getAutoWireProxyContainer.addStatement("return auotwireProxyContainer").build())
                .addMethod(getDataEvnetContainer.addStatement("return dataEventContainer").build())
                .addMethod(getViewContainer.addStatement("return this.viewContainer").build())
                .addMethod(autoWireFactory.build())
                .build();

        return JavaFile.builder("com.pxy.pangjiao.mvp", typeSpec).build();
    }


    private void diffConfig(MethodSpec.Builder methodInit) {
        methodInit.addCode(CodeBlock.builder().add("\n").build());
        for (IConfig config : configs) {
            if (config.getAnnotation() == Autowire.class) {
                String fieldOwnerTypeClassName = config.getOwnerType().getQualifiedName().toString();
                List<IConfig> iConfigs = autoWireMap.get(fieldOwnerTypeClassName);
                if (iConfigs == null) {
                    iConfigs = new ArrayList<>();
                    iConfigs.add(config);
                    autoWireMap.put(fieldOwnerTypeClassName, iConfigs);
                } else {
                    iConfigs.add(config);
                }
            } else if (config.getAnnotation() == AutowireProxy.class) {
                String fieldOwnerTypeClassName = config.getOwnerType().getQualifiedName().toString();
                List<IConfig> iConfigs = autoWireProxyMap.get(fieldOwnerTypeClassName);
                if (iConfigs == null) {
                    iConfigs = new ArrayList<>();
                    iConfigs.add(config);
                    autoWireProxyMap.put(fieldOwnerTypeClassName, iConfigs);
                } else {
                    iConfigs.add(config);
                }
            } else if (config.getAnnotation() == DataEvent.class) {
                String fieldOwnerTypeClassName = config.getOwnerType().getQualifiedName().toString();
                List<IConfig> iConfigs = dataEventMap.get(fieldOwnerTypeClassName);
                if (iConfigs == null) {
                    iConfigs = new ArrayList<>();
                    iConfigs.add(config);
                    dataEventMap.put(fieldOwnerTypeClassName, iConfigs);
                } else {
                    iConfigs.add(config);
                }
            } else {
                TypeName type = config.getType();
                boolean singleton = config.isSingleton();
                methodInit.addStatement("container.put($T.class.getName(),new BeanConfig(new $T(),$L))", type, type, singleton);
            }
        }
    }

    private void initAutWire(MethodSpec.Builder methodInit) {
        for (String keyOwner : autoWireMap.keySet()) {
            List<IConfig> iConfigs = autoWireMap.get(keyOwner);
            String variableName = null;
            for (IConfig config : iConfigs) {
                TypeMirror fieldType = config.getFieldType();
                String fieldTypeClassName = fieldType.toString();
                variableName = config.getOwnerType().toString().replace(".", "_");
                methodInit.addStatement("$T $N=new $T<>()", ParameterizedTypeName.get(List.class, AutoWireConfig.class), variableName, ArrayList.class);
                methodInit.addStatement("$N.add(new AutoWireConfig($S,$S))", variableName, fieldTypeClassName, config.getFieldName().toString());
            }
            methodInit.addStatement("auotwireContainer.put($S,$L)", keyOwner, variableName);
            methodInit.addCode(CodeBlock.builder().add("\n").build());
        }
    }

    private void initAutWireProxy(MethodSpec.Builder methodInit) {
        for (String keyOwner : autoWireProxyMap.keySet()) {
            List<IConfig> iConfigs = autoWireProxyMap.get(keyOwner);
            String variableName = "";
            for (IConfig config : iConfigs) {
                TypeMirror fieldType = config.getFieldType();
                String fieldTypeClassName = fieldType.toString();
                String oldName = config.getOwnerType().toString().replace(".", "_");
                if (!variableName.equals(oldName)) {
                    variableName = oldName;
                    methodInit.addStatement("$T $N=new $T<>()", ParameterizedTypeName.get(List.class, AutoWireConfig.class), variableName, ArrayList.class);
                }
                methodInit.addStatement("$N.add(new AutoWireConfig($S,$S))", variableName, fieldTypeClassName, config.getFieldName().toString());
            }
            methodInit.addStatement("auotwireProxyContainer.put($S,$L)", keyOwner, variableName);
            methodInit.addCode(CodeBlock.builder().add("\n").build());
        }
    }

    private void initDateEvent(MethodSpec.Builder methodInit) {
        for (String keyOwner : dataEventMap.keySet()) {
            List<IConfig> iConfigs = dataEventMap.get(keyOwner);
            String variableName = "";
            for (IConfig config : iConfigs) {
                String fieldTypeClassName = config.getOwnerType().toString();
                String oldName = config.getOwnerType().toString().replace(".", "_") + "_DataEvent";
                if (!variableName.equals(oldName)) {
                    variableName = oldName;
                    methodInit.addStatement("$T $N=new $T<>()", ParameterizedTypeName.get(List.class, DataEventConfig.class), variableName, ArrayList.class);
                }
                methodInit.addStatement("$N.add(new DataEventConfig($S,$S))", variableName, fieldTypeClassName, config.getMethodName().toString());
            }
            methodInit.addStatement("dataEventContainer.put($S,$L)", keyOwner, variableName);
            methodInit.addCode(CodeBlock.builder().add("\n").build());
        }
    }

    private MethodSpec.Builder initCreateMethod() {
        MethodSpec.Builder createMethod = MethodSpec.methodBuilder("createInstance")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addStatement("if(instance==null){\n" +
                        "    synchronized (MVPDefaultContainer.class){\n" +
                        "     if(instance==null){\n" +
                        "          instance=new MVPDefaultContainer();\n" +
                        "        }" +
                        "\n    }" +
                        "\n}")
                .returns(ClassName.get("com.pxy.pangjiao.mvp", "MVPDefaultContainer"));
        return createMethod;
    }

    private MethodSpec.Builder initConstruct() {
        MethodSpec.Builder constructMethod = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE);
        constructMethod.addStatement("container=new $T<>()", ClassName.get("java.util", "HashMap"));
        constructMethod.addStatement("auotwireContainer=new $T<>()", ClassName.get("java.util", "HashMap"));
        constructMethod.addStatement("auotwireProxyContainer=new $T<>()", ClassName.get("java.util", "HashMap"));
        constructMethod.addStatement("dataEventContainer=new $T<>()", ClassName.get("java.util", "HashMap"));
        constructMethod.addStatement("init()");
        constructMethod.addStatement("initAutoWire()");
        return constructMethod;
    }
}
