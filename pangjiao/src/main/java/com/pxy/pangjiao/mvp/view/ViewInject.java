package com.pxy.pangjiao.mvp.view;

import android.app.Activity;
import android.view.View;

import com.pxy.pangjiao.mvp.view.helper.ActivityProvider;
import com.pxy.pangjiao.mvp.view.helper.IProvider;
import com.pxy.pangjiao.mvp.view.helper.ViewProvider;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2018/3/12.
 */

public class ViewInject {

    private static final ActivityProvider activityProvider = new ActivityProvider();

    private static final ViewProvider viewProvider = new ViewProvider();

    private static final Map<String, IViewInject> injectMap = new HashMap<>();

    public static void inject(Activity activity) {
        inject(activity, activity, activityProvider);
    }

    public static void inject(View view, Object target) {
        inject(target, view, viewProvider);
    }

    private static void inject(Object host, Object o, IProvider provider) {
        String clsName = host.getClass().getName();
        IViewInject inject = injectMap.get(clsName);
        if (inject == null) {
            try {
                Class<?> cls = Class.forName(clsName + "$$ViewInject");
                Object o1 = cls.newInstance();
                injectMap.put(clsName, (IViewInject) o1);
                ((IViewInject) o1).inject(host, o, provider);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            inject.inject(host, o, provider);
        }

    }
}
