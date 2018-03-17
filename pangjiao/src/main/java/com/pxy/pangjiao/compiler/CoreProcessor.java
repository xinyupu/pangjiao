package com.pxy.pangjiao.compiler;

import com.google.auto.service.AutoService;
import com.pxy.pangjiao.compiler.injectview.InjectCompiler;
import com.pxy.pangjiao.compiler.injectview.annotation.InitView;
import com.pxy.pangjiao.compiler.injectview.annotation.OnClick;
import com.pxy.pangjiao.compiler.injectview.factory.InitViewProduct;
import com.pxy.pangjiao.compiler.mpv.MvpCompiler;
import com.pxy.pangjiao.compiler.mpv.annotation.Autowire;
import com.pxy.pangjiao.compiler.mpv.annotation.Presenter;
import com.pxy.pangjiao.compiler.mpv.annotation.Service;
import com.pxy.pangjiao.compiler.mpv.annotation.Views;
import com.pxy.pangjiao.compiler.mpv.factory.AutoWireInjectProduct;
import com.pxy.pangjiao.compiler.mpv.factory.MVPDefaultContainerProduct;
import com.pxy.pangjiao.databus.DataEvent;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by pxy on 2018/3/12.
 */
@AutoService(Processor.class)
public class CoreProcessor extends AbstractProcessor {


    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;
    private MVPDefaultContainerProduct product;
    private AutoWireInjectProduct injectProduct;
    private InjectCompiler injectCompiler;
    private JavaFile javaMVConfigFile = null;
    private JavaFile javaAutoWireConfigFile = null;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        product = new MVPDefaultContainerProduct();
        injectProduct = new AutoWireInjectProduct();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        injectCompiler = new InjectCompiler(roundEnvironment, mElementUtils);
        MvpCompiler mvpCompiler = new MvpCompiler(roundEnvironment, mElementUtils, injectProduct);
        try {
            if (javaMVConfigFile == null) {
                product.addAllConfig(mvpCompiler.getConfigs());
                javaMVConfigFile = product.generateFile();
                javaMVConfigFile.writeTo(mFiler);
            }

            if (javaAutoWireConfigFile == null) {
                injectProduct.setAutoWireConfigMaps(mvpCompiler.getAutoWireConfigMaps());
                injectProduct.setViewConfigMaps(mvpCompiler.getViewConfigMaps());
                javaAutoWireConfigFile = injectProduct.generateFile();
                javaAutoWireConfigFile.writeTo(mFiler);
            }
            for (String key : injectCompiler.getmInitViewProductMap().keySet()) {
                InitViewProduct initViewProduct = injectCompiler.getmInitViewProductMap().get(key);
                initViewProduct.generateFile().writeTo(mFiler);
            }

        } catch (Exception e) {
            e.printStackTrace();
            error(e.getMessage());
        }
        return false;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(InitView.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        types.add(Service.class.getCanonicalName());
        types.add(Views.class.getCanonicalName());
        types.add(Presenter.class.getCanonicalName());
        types.add(Autowire.class.getCanonicalName());
        types.add(DataEvent.class.getCanonicalName());
        return types;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
