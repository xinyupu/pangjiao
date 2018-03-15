package com.pxy.pangjiao.compiler.mpv;

import com.pxy.pangjiao.compiler.mpv.annotation.Autowire;
import com.pxy.pangjiao.compiler.mpv.annotation.AutowireProxy;
import com.pxy.pangjiao.compiler.mpv.annotation.Presenter;
import com.pxy.pangjiao.compiler.mpv.annotation.Service;
import com.pxy.pangjiao.compiler.mpv.config.CompilerClassConfig;
import com.pxy.pangjiao.compiler.mpv.config.CompilerFieldConfig;
import com.pxy.pangjiao.compiler.mpv.config.CompilerMethodConfig;
import com.pxy.pangjiao.compiler.mpv.config.IConfig;
import com.pxy.pangjiao.databus.DataEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

/**
 * Created by pxy on 2018/3/13.
 */

public class MvpCompiler {

    private Elements mElementUtils;

    private RoundEnvironment env;
    private List<IConfig> configs;

    public MvpCompiler(RoundEnvironment env, Elements mElementUtils) {
        this.env = env;
        this.mElementUtils = mElementUtils;
        configs = new ArrayList<>();
        initService();
        initPresent();
        initAutoWire();
        initAutWireProxy();
        initDataEvent();
    }

    private void initService() {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(Service.class);
        for (Element element : elements) {
            CompilerClassConfig beanConfig = new CompilerClassConfig(element, Service.class);
            configs.add(beanConfig);
        }
    }

    private void initPresent() {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(Presenter.class);
        for (Element element : elements) {
            CompilerClassConfig beanConfig = new CompilerClassConfig(element, Presenter.class);
            configs.add(beanConfig);
        }
    }

    private void initAutoWire() {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(Autowire.class);
        for (Element element : elements) {
            CompilerFieldConfig beanConfig = new CompilerFieldConfig(element, Autowire.class);
            configs.add(beanConfig);
        }
    }

    private void initAutWireProxy() {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(AutowireProxy.class);
        for (Element element : elements) {
            CompilerFieldConfig beanConfig = new CompilerFieldConfig(element, AutowireProxy.class);
            configs.add(beanConfig);
        }
    }

    private void initDataEvent() {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(DataEvent.class);
        for (Element element : elements) {
            CompilerMethodConfig beanConfig = new CompilerMethodConfig(element, DataEvent.class);
            configs.add(beanConfig);
        }
    }


    public List<IConfig> getConfigs() {
        return configs;
    }
}
