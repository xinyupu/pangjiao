package com.pxy.pangjiao.compiler;

import com.squareup.javapoet.ClassName;

/**
 * Created by pxy on 2018/3/12.
 */

public class Type {
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName IViewInject = ClassName.get("com.pxy.pangjiao.mvp.view", "IViewInject");
    public static final ClassName IProvider = ClassName.get("com.pxy.pangjiao.mvp.view.helper", "IProvider");
    public static final ClassName Map = ClassName.get("java.util", "Map");

}
