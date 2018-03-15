package com.pxy.pangjiao.compiler.injectview;

import com.pxy.pangjiao.compiler.injectview.annotation.InitView;
import com.pxy.pangjiao.compiler.injectview.annotation.InitViewField;
import com.pxy.pangjiao.compiler.injectview.annotation.OnClick;
import com.pxy.pangjiao.compiler.injectview.annotation.OnclickMethod;
import com.pxy.pangjiao.compiler.injectview.factory.InitViewProduct;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by pxy on 2018/3/13.
 */

public class InjectCompiler {

    private Elements mElementUtils;
    private RoundEnvironment env;
    private Map<String, InitViewProduct> mInitViewProductMap;

    public InjectCompiler(RoundEnvironment env, Elements mElementUtils) {
        this.env = env;
        mInitViewProductMap = new TreeMap<>();
        this.mElementUtils = mElementUtils;
        init();
    }

    private InjectCompiler init() {
        processInitView(this.env);
        processOnClick(this.env);
        return this;
    }

    private void processInitView(RoundEnvironment env) {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(InitView.class);
        for (Element element : elements) {
            InitViewProduct initViewProduct = getInitViewProductClass(element);
            InitViewField field = new InitViewField(element);
            initViewProduct.addInitView(field);
        }
    }

    private void processOnClick(RoundEnvironment env) {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(OnClick.class);
        for (Element element : elements) {
            InitViewProduct initViewProduct = getInitViewProductClass(element);
            OnclickMethod method = new OnclickMethod(element);
            initViewProduct.addOnclick(method);
        }
    }

    private InitViewProduct getInitViewProductClass(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String s = typeElement.getQualifiedName().toString();
        InitViewProduct viewProduct = mInitViewProductMap.get(s);
        if (viewProduct == null) {
            viewProduct = new InitViewProduct(typeElement, mElementUtils);
            mInitViewProductMap.put(s, viewProduct);
        }
        return viewProduct;
    }

    public Map<String, InitViewProduct> getmInitViewProductMap() {
        return mInitViewProductMap;
    }
}
