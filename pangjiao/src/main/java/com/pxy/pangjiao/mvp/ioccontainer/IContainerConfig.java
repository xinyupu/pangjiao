package com.pxy.pangjiao.mvp.ioccontainer;

import java.util.List;
import java.util.Map;

/**
 * Created by pxy on 2018/3/14.
 */

public interface IContainerConfig {

    Map<String, BeanConfig> getClassTypeContainer();

    Map<String, List> getAutoWireContainer();

    Map<String, List> getAutoWireProxyContainer();

    Map<String, List> getDataEvnetContainer();
    Map<String, List> getViewContainer();

    Object autoWireFactory(Object o);
}
