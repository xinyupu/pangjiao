package com.pxy.pangjiao.mvp.viewmodel.model;

import com.pxy.pangjiao.PangJiao;
import com.pxy.pangjiao.common.ExpUtil;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ViewDataInject {

    public static void inject(Object souerce,Object bind){
        String clsName = bind.getClass().getName() + "$$ViewData";
        try {
            Class<?> aClass = Class.forName(clsName);
            IViewDataBind o = (IViewDataBind)aClass.newInstance();
            o.bindData(souerce,bind);
        } catch (Exception e) {
            e.printStackTrace();
            PangJiao.error(ExpUtil.getStackTrace(e));
        }
    }
}
