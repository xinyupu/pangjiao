package com.pxy.pangjiao.mvp;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import com.pxy.pangjiao.common.Utils;
import com.pxy.pangjiao.mvp.view.helper.ModelData;
import com.pxy.pangjiao.mvp.viewmodel.model.SourceType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by pxy on 2018/2/1.
 */

public class MVPHelper {


    public static Object mapper(Object viewModel, Object view) throws NoSuchFieldException, IllegalAccessException {
        Field[] viewFields = view.getClass().getDeclaredFields();

        Field[] viewModelFields = viewModel.getClass().getFields();

        for (Field viewField : viewFields) {
            if (viewField.isAnnotationPresent(ModelData.class)) {
                ModelData annotation = viewField.getAnnotation(ModelData.class);
                String fieldName = annotation.field();
                SourceType type = annotation.type();
                int id = annotation.id();
                if (Utils.isEmpty(fieldName)) {
                    for (Field viewModelField : viewModelFields) {
                        if (viewField.getName().equals(viewModelField.getName()) && viewField.getType() == viewModelField.getType()) {
                            viewModelField.set(viewModel, viewField.get(view));
                        }
                    }
                } else if (type == SourceType.Native && !Utils.isEmpty(fieldName) && id != 0 && viewField.get(view) == null) {
                    if (viewField.getType().getName().endsWith("EditText")) {
                        if (Activity.class.isAssignableFrom(view.getClass())) {
                            Activity activity = (Activity) view;
                            android.view.View contentView = activity.findViewById(android.R.id.content);
                            EditText editText = contentView.findViewById(id);
                            String s = editText.getText().toString();
                            Field field = viewModel.getClass().getField(fieldName);
                            field.set(viewModel, s);
                        }
                    } else if (viewField.getType().getName().endsWith("TextView")) {
                        if (Activity.class.isAssignableFrom(view.getClass())) {
                            Activity activity = (Activity) view;
                            android.view.View contentView = activity.findViewById(android.R.id.content);
                            TextView textView = contentView.findViewById(id);
                            String s = (String) textView.getText();
                            Field field = viewModel.getClass().getField(fieldName);
                            field.set(viewModel, s);
                        }
                    }
                } else if (type == SourceType.Native && !Utils.isEmpty(fieldName)) {
                    if (viewField.getType().getName().endsWith("EditText")) {
                        if (viewField.get(view) != null) {
                            EditText editText = (EditText) viewField.get(view);
                            String s = editText.getText().toString();
                            Field field = viewModel.getClass().getField(fieldName);
                            if (!Utils.isEmpty(s)) {
                                Object convert = null;
                                if (int.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())) {
                                    convert = Integer.parseInt(s);
                                } else if (long.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType())) {
                                    convert = Long.parseLong(s);
                                } else if (double.class.isAssignableFrom(field.getType()) || Double.class.isAssignableFrom(field.getType())) {
                                    convert = Double.parseDouble(s);
                                } else if (float.class.isAssignableFrom(field.getType()) || Float.class.isAssignableFrom(field.getType())) {
                                    convert = Double.parseDouble(s);
                                } else {
                                    convert = s;
                                }
                                field.set(viewModel, convert);
                            }
                        }
                    } else if (viewField.getType().getName().endsWith("TextView")) {
                        if (viewField.get(view) != null) {
                            TextView textView = (TextView) viewField.get(view);
                            String s = textView.getText().toString();
                            Field field = viewModel.getClass().getField(fieldName);
                            if (!Utils.isEmpty(s))
                                field.set(viewModel, s);
                        }
                    }

                }
            }
        }
        return viewModel;
    }

    public static void show(Object viewModel, Object view) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Field[] fields = view.getClass().getDeclaredFields();
        Field[] viewModelFields = viewModel.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ModelData.class)) {
                ModelData annotation = field.getAnnotation(ModelData.class);
                String fieldName = annotation.field();
                for (Field vFiled : viewModelFields) {
                    if (!vFiled.isAccessible()) vFiled.setAccessible(true);
                    if (vFiled.getName().equals(fieldName)) {
                        if (field.getType() == TextView.class || field.getType() == EditText.class) {
                            Object o = field.get(view);
                            if (field.get(view) != null) {
                                Method method = field.getType().getMethod("setText", CharSequence.class);
                                Method vMethod = null;
                                Method[] methods = viewModel.getClass().getDeclaredMethods();
                                if (methods.length > 0) {
                                    for (Method m : methods) {
                                        String mUp = m.getName().toUpperCase();
                                        String fUp = vFiled.getName().toUpperCase();
                                        if (mUp.startsWith("GET") && mUp.endsWith(fUp)) {
                                            vMethod = m;
                                            break;
                                        }
                                    }
                                }
                                if (Number.class.isAssignableFrom(vFiled.getType())
                                        || int.class.isAssignableFrom(vFiled.getType())
                                        || long.class.isAssignableFrom(vFiled.getType())
                                        || double.class.isAssignableFrom(vFiled.getType())
                                        || float.class.isAssignableFrom(vFiled.getType())) {
                                    if (vMethod == null) {
                                        Object o1 = vFiled.get(viewModel);
                                        method.invoke(o, o1 + "");
                                    } else {
                                        Object invoke = vMethod.invoke(viewModel);
                                        method.invoke(o, invoke + "");

                                    }
                                    break;
                                } else if (String.class.isAssignableFrom(vFiled.getType())) {
                                    if (vMethod == null) {
                                        Object o1 = vFiled.get(viewModel);
                                        if (!Utils.isEmpty((String) o1)) {
                                            method.invoke(o, o1);
                                        } else {
                                            method.invoke(o, "");
                                        }
                                    } else {
                                        Object o1 = vMethod.invoke(viewModel);
                                        if (!Utils.isEmpty((String) o1)) {
                                            method.invoke(o, o1);
                                        } else {
                                            method.invoke(o, "");
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
}
