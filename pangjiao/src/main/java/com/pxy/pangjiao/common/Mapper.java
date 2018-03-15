package com.pxy.pangjiao.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pxy on 2018/1/17.
 */

public class Mapper {

    public static <T, V> T map(V source, T target) {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = target.getClass().getDeclaredFields();
        Field[] pubSourceFields = source.getClass().getFields();
        Field[] pubTargetFields = target.getClass().getFields();

        Field[] totalSourceFields = concat(sourceFields, pubSourceFields);
        Field[] totalTargetFields = concat(targetFields, pubTargetFields);

        List<Field> totalSourceFieldList = romve(totalSourceFields);
        List<Field> totalTargetFieldsList = romve(totalTargetFields);

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

        List<Field> totalSourceFieldList = romve(totalSourceFields);
        List<Field> totalTargetFieldsList = romve(totalTargetFields);

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

    private static List<Field> romve(Field[] fields) {
        List<Field> list = new ArrayList<Field>();
        for (int i = 0; i < fields.length; i++) {
            if (!list.contains(fields[i])) {
                list.add((Field) fields[i]);
            }
        }
        return list;
    }
}
