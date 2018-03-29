package com.pxy.pangjiao.compiler.mpv.config;

import com.pxy.pangjiao.compiler.mpv.annotation.DataField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ViewDataConfig {

   private RoundEnvironment env;
   private Element element;
   private Class[] classes;
   private Map<Class,List<DataFieldConfig>>  classListMap;
    private Map<Class,List<DataFieldConfig>>  bindMap;

    public ViewDataConfig(RoundEnvironment env, Element element, Class[] classes) {
        this.env = env;
        this.element = element;
        this.classes = classes;
        classListMap=new HashMap<>();
        bindMap=new HashMap<>();

        try {
            initDataField();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Map<Class,List<DataFieldConfig>> classListMap(){

        for (Class cls:classes){
            if (classListMap.get(cls)==null){
                List<DataFieldConfig> list=new ArrayList<>();

            }
        }

        return classListMap;
    }

    private void initDataField() throws ClassNotFoundException {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(DataField.class);
        for (Element element:elements){
            TypeMirror typeMirror = element.getEnclosingElement().asType();
            String ownerClassName=typeMirror.toString();
            if (element.getKind() == ElementKind.FIELD){
                Class<?> aClass = Class.forName(ownerClassName);
                List<DataFieldConfig> list;
                if (bindMap.get(aClass)==null){
                    list = bindMap.get(aClass);
                }else {
                    list=new ArrayList<>();
                }
                ExecutableElement typeElement = (ExecutableElement) element;
                DataField annotation = typeElement.getAnnotation(DataField.class);
                String fieldName = annotation.value();
                String fieldTyeName = typeElement.asType().toString();
                DataFieldConfig fieldConfig=new DataFieldConfig();
                fieldConfig.fieldName=fieldName;
                fieldConfig.fieldTypeName=fieldTyeName;
                list.add(fieldConfig);
            }else {
                throw new RuntimeException("@DataField must on FIELD");
            }
        }
    }

    public static class DataFieldConfig{
        public String fieldName;
        public String fieldTypeName;
    }
}
