package com.pxy.pangjiao.common;

import com.pxy.pangjiao.mvp.viewmodel.model.ModelField;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * Created by pxy on 2018/1/17.
 */

public class Mapper {

    public static void mapMVVP(Object dataSource,Object observableType){
        Field[] observableFields = observableType.getClass().getFields();
        Field[] dataSourceFields = dataSource.getClass().getDeclaredFields();

        for (Field field:observableFields){
            if (field.getType().getName().equals("android.databinding.ObservableField")){
                try {
                    dataSource.getClass().getField(field.getName());
                } catch (NoSuchFieldException e) {
                    for (Field dataSourceField:dataSourceFields){
                       if (dataSourceField.isAnnotationPresent(ModelField.class)){
                           ModelField annotation = dataSourceField.getAnnotation(ModelField.class);
                           String fieldName = annotation.fieldName();
                           try {
                               if (field.getName().equals(fieldName)){
                                   dataSourceField.setAccessible(true);
                                   Object o = field.get(observableType);
                                   Method set = field.getType().getMethod("set", Object.class);
                                   Object o1 = dataSourceField.get(dataSource);
                                   set.invoke(o,o1);
                                   break;
                               }
                           } catch (NoSuchMethodException e1) {
                               e1.printStackTrace();
                           } catch (IllegalAccessException e1) {
                               e1.printStackTrace();
                           } catch (InvocationTargetException e1) {
                               e1.printStackTrace();
                           }
                       }
                    }
                }
            }
        }
    }

    public static <T, V> T map(V source, T target) {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = target.getClass().getDeclaredFields();
        Field[] pubSourceFields = source.getClass().getFields();
        Field[] pubTargetFields = target.getClass().getFields();

        Field[] totalSourceFields = concat(sourceFields, pubSourceFields);
        Field[] totalTargetFields = concat(targetFields, pubTargetFields);

        List<Field> totalSourceFieldList = remove(totalSourceFields);
        List<Field> totalTargetFieldsList = remove(totalTargetFields);

        for (Field sourceField : totalSourceFieldList) {
            for (Field targetField : totalTargetFieldsList) {
                if (sourceField.getName().equals(targetField.getName())
                        && sourceField.getType().getClass().getName().equals(targetField.getType().getClass().getName())) {
                    if (!targetField.isAccessible()) targetField.setAccessible(true);
                    if (!sourceField.isAccessible()) sourceField.setAccessible(true);
                    try {
                        targetField.set(target, sourceField.get(source));
                        break;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return target;
    }

    public static <T> void map(Class target, T source) {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = target.getDeclaredFields();
        Field[] pubSourceFields = source.getClass().getFields();
        Field[] pubTargetFields = target.getFields();

        Field[] totalSourceFields = concat(sourceFields, pubSourceFields);
        Field[] totalTargetFields = concat(targetFields, pubTargetFields);

        List<Field> totalSourceFieldList = remove(totalSourceFields);
        List<Field> totalTargetFieldsList = remove(totalTargetFields);

        for (Field sourceField : totalSourceFieldList) {
            for (Field targetField : totalTargetFieldsList) {
                if (sourceField.getName().equals(targetField.getName())
                        && sourceField.getType().getClass().getName().equals(targetField.getType().getClass().getName())) {
                    if (!targetField.isAccessible()) targetField.setAccessible(true);
                    if (!sourceField.isAccessible()) sourceField.setAccessible(true);
                    try {
                        targetField.set(source, sourceField.get(source));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private static List<Field> remove(Field[] fields) {
        List<Field> list = new ArrayList<Field>();
        for (int i = 0; i < fields.length; i++) {
            if (!list.contains(fields[i])) {
                list.add(fields[i]);
            }
        }
        return list;
    }
}
