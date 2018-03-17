package com.pxy.pangjiao.compiler.mpv;

import com.pxy.pangjiao.compiler.mpv.annotation.Autowire;
import com.pxy.pangjiao.compiler.mpv.annotation.AutowireProxy;
import com.pxy.pangjiao.compiler.mpv.annotation.Presenter;
import com.pxy.pangjiao.compiler.mpv.annotation.Service;
import com.pxy.pangjiao.compiler.mpv.annotation.Views;
import com.pxy.pangjiao.compiler.mpv.config.AutoWireCompilerConfig;
import com.pxy.pangjiao.compiler.mpv.config.CompilerClassConfig;
import com.pxy.pangjiao.compiler.mpv.config.CompilerFieldConfig;
import com.pxy.pangjiao.compiler.mpv.config.CompilerMethodConfig;
import com.pxy.pangjiao.compiler.mpv.config.IConfig;
import com.pxy.pangjiao.compiler.mpv.config.InterfaceConfig;
import com.pxy.pangjiao.compiler.mpv.config.ViewsConfig;
import com.pxy.pangjiao.compiler.mpv.factory.AutoWireInjectProduct;
import com.pxy.pangjiao.databus.DataEvent;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by pxy on 2018/3/13.
 */

public class MvpCompiler {

    private Elements mElementUtils;

    private RoundEnvironment env;
    private List<IConfig> configs;
    private AutoWireInjectProduct injectProduct;
    private Map<String, List<InterfaceConfig>> interfaceMaps;
    private Map<String, List<AutoWireCompilerConfig>> autoWireConfigMaps;
    private Map<String, List<AutoWireCompilerConfig>> autoWireProxyConfigMaps;
    private Map<String, ViewsConfig> viewConfigMaps;

    public MvpCompiler(RoundEnvironment env, Elements mElementUtils, AutoWireInjectProduct injectProduct) {
        this.env = env;
        this.mElementUtils = mElementUtils;
        this.injectProduct = injectProduct;
        configs = new ArrayList<>();
        interfaceMaps = new HashMap<>();
        autoWireConfigMaps = new HashMap<>();
        autoWireProxyConfigMaps = new HashMap<>();
        viewConfigMaps = new HashMap<>();
        initService();
        initPresent();
        initViews();
        parse(Autowire.class, autoWireConfigMaps);
        parse(AutowireProxy.class, autoWireConfigMaps);

        //  initAutoWire();
        // initAutWireProxy();
        initDataEvent();
    }

    private void initService() {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(Service.class);
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
                for (TypeMirror type : interfaces) {
                    String interfaceName = type.toString();
                    if (interfaceMaps.get(interfaceName) == null) {
                        List<InterfaceConfig> interfaceList = new ArrayList<>();
                        interfaceList.add(new InterfaceConfig(interfaceName, typeElement.toString()));
                        interfaceMaps.put(interfaceName, interfaceList);
                    } else {
                        List<InterfaceConfig> interfaceConfigs = interfaceMaps.get(interfaceName);
                        interfaceConfigs.add(new InterfaceConfig(interfaceName, typeElement.toString()));
                    }
                }
                CompilerClassConfig beanConfig = new CompilerClassConfig(element, Service.class, mElementUtils);
                configs.add(beanConfig);
            }
        }
    }

    private void initPresent() {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(Presenter.class);
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
                for (TypeMirror type : interfaces) {
                    String interfaceName = type.toString();
                    if (interfaceMaps.get(interfaceName) == null) {
                        List<InterfaceConfig> interfaceList = new ArrayList<>();
                        interfaceList.add(new InterfaceConfig(interfaceName, typeElement.toString()));
                        interfaceMaps.put(interfaceName, interfaceList);
                    } else {
                        List<InterfaceConfig> interfaceConfigs = interfaceMaps.get(interfaceName);
                        interfaceConfigs.add(new InterfaceConfig(interfaceName, typeElement.toString()));
                    }
                }
                CompilerClassConfig beanConfig = new CompilerClassConfig(element, Presenter.class, mElementUtils);
                configs.add(beanConfig);
            }
        }
    }

    public <T extends Annotation> void parse(Class<T> clsT, Map<String, List<AutoWireCompilerConfig>> map) {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(clsT);
        for (Element element : elements) {
            if (element.getKind() != ElementKind.FIELD) {
                throw new IllegalArgumentException(String.format("Only FIELD can be annotated with @%s",
                        clsT.getSimpleName()));
            }
            T annotation = element.getAnnotation(clsT);
            TypeMirror autowireImp = null;
            if (clsT == Autowire.class) {
                autowireImp = getAutowireImp((Autowire) annotation);
            } else if (clsT == AutowireProxy.class) {
                autowireImp = getAutowireImp((AutowireProxy) annotation);
            }
            String autoWireClassName = "";
            if (null != autowireImp) {
                autoWireClassName = autowireImp.toString();
            }
            VariableElement fieldElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            List<AutoWireCompilerConfig> configLists = map.get(typeElement.asType().toString());
            String fieldTypeClassName = fieldElement.asType().toString();
            AutoWireCompilerConfig compilerConfig = new AutoWireCompilerConfig();
            compilerConfig.setFieldName(element.getSimpleName().toString());
            compilerConfig.setAutoFieldClassName(element.toString());
            compilerConfig.setSimpleName(element.getSimpleName());
            compilerConfig.setFieldName(element.getSimpleName().toString());
            compilerConfig.setAutoFieldClassName(fieldTypeClassName);
            if (configLists == null) {
                configLists = new ArrayList<>();
                pareAutoWire(fieldTypeClassName, compilerConfig, autoWireClassName);
                configLists.add(compilerConfig);
                map.put(typeElement.asType().toString(), configLists);
            } else {
                pareAutoWire(fieldTypeClassName, compilerConfig, autoWireClassName);
                configLists.add(compilerConfig);
            }

            CompilerFieldConfig beanConfig = new CompilerFieldConfig(element, clsT);
            configs.add(beanConfig);
        }
    }

    private void initAutoWire() {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(Autowire.class);
        for (Element element : elements) {
            if (element.getKind() != ElementKind.FIELD) {
                throw new IllegalArgumentException(String.format("Only FIELD can be annotated with @%s",
                        Autowire.class.getSimpleName()));
            }
            Autowire annotation = element.getAnnotation(Autowire.class);
            TypeMirror autowireImp = getAutowireImp(annotation);
            String autoWireClassName = "";
            if (autowireImp != null) {
                autoWireClassName = autowireImp.toString();
            }
            VariableElement fieldElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            List<AutoWireCompilerConfig> configLists = autoWireConfigMaps.get(typeElement.asType().toString());
            String fieldTypeClassName = fieldElement.asType().toString();
            AutoWireCompilerConfig compilerConfig = new AutoWireCompilerConfig();
            compilerConfig.setFieldName(element.getSimpleName().toString());
            compilerConfig.setAutoFieldClassName(element.toString());
            compilerConfig.setSimpleName(element.getSimpleName());
            compilerConfig.setFieldName(element.getSimpleName().toString());
            compilerConfig.setAutoFieldClassName(fieldTypeClassName);
            if (configLists == null) {
                configLists = new ArrayList<>();
                pareAutoWire(fieldTypeClassName, compilerConfig, autoWireClassName);
                configLists.add(compilerConfig);
                autoWireConfigMaps.put(typeElement.asType().toString(), configLists);
            } else {
                pareAutoWire(fieldTypeClassName, compilerConfig, autoWireClassName);
                configLists.add(compilerConfig);
            }

            CompilerFieldConfig beanConfig = new CompilerFieldConfig(element, Autowire.class);
            configs.add(beanConfig);
        }
    }


    private void pareAutoWire(String fieldTypeClassName, AutoWireCompilerConfig compilerConfig, String autoWireClassName) {
        List<InterfaceConfig> interfaceConfigs = interfaceMaps.get(fieldTypeClassName);
        if (interfaceConfigs != null && interfaceConfigs.size() > 0) {
            compilerConfig.setInterface(true);
            List<String> interfaceImp = new ArrayList<>();
            for (InterfaceConfig config : interfaceConfigs) {
                interfaceImp.add(config.getInterfaceImpClassName());
            }
            if (!autoWireClassName.equals("java.lang.Void")) {
                if (interfaceImp.contains(autoWireClassName)) {
                    compilerConfig.setAutoFieldClassImpName(autoWireClassName);
                } else if (!autoWireClassName.equals("java.lang.Void")) {
                    throw new NullPointerException(String.format("\ncause by:\n"+"not add @Service or @Presenter on %s",
                            autoWireClassName));
                }
            } else {
                compilerConfig.setAutoFieldClassImpName(interfaceConfigs.get(0).getInterfaceImpClassName());
            }
        } else {
            compilerConfig.setInterface(false);
        }

    }


    private void initAutWireProxy() {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(AutowireProxy.class);
        for (Element element : elements) {
            CompilerFieldConfig beanConfig = new CompilerFieldConfig(element, AutowireProxy.class);
            configs.add(beanConfig);
        }
    }

    private void initViews() {
        Set<? extends Element> elements = this.env.getElementsAnnotatedWith(Views.class);
        for (Element element : elements) {
            CompilerClassConfig compilerClassConfig = new CompilerClassConfig(element, Views.class, mElementUtils);
            String typeName = compilerClassConfig.getType().toString();
            if (viewConfigMaps.get(typeName) == null) {
                ViewsConfig viewsConfig = new ViewsConfig();
                viewsConfig.setClassName(typeName);
                viewConfigMaps.put(typeName, viewsConfig);
            }
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

    public Map<String, List<AutoWireCompilerConfig>> getAutoWireConfigMaps() {
        return autoWireConfigMaps;
    }

    public Map<String, List<AutoWireCompilerConfig>> getAutoWireProxyConfigMaps() {
        return autoWireProxyConfigMaps;
    }

    public Map<String, ViewsConfig> getViewConfigMaps() {
        return viewConfigMaps;
    }

    private TypeMirror getAutowireImp(Autowire annotation) {
        try {
            annotation.imp();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null;
    }

    private TypeMirror getAutowireImp(AutowireProxy annotation) {
        try {
            annotation.imp();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null;
    }
}
