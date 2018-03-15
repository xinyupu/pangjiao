package com.pxy.pangjiao.mvp.viewmodel;

import com.pxy.pangjiao.mvp.MVPCore;
import com.pxy.pangjiao.mvp.viewmodel.model.IViewModel;

/**
 * Created by pxy on 2018/2/1.
 */

public class ViewModel implements IViewModel {

    @Override
    public Object mapper(Object o) {
        return MVPCore.getInstance().parse(this, o);
    }

    @Override
    public void show(Object o) {
        MVPCore.getInstance().show(this, o);
    }
}
