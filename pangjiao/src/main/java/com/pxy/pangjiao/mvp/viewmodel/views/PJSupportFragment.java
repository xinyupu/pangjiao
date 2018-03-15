package com.pxy.pangjiao.mvp.viewmodel.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pxy.pangjiao.PangJiao;
import com.pxy.pangjiao.databus.DataBus;
import com.pxy.pangjiao.mvp.presenter.IPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pxy on 2018/2/6.
 */

public abstract class PJSupportFragment extends Fragment {


    protected Activity activity;

    public List<IPresenter> presenters;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        presenters = new ArrayList<>();
        PangJiao.register(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(initView(), container, false);
        PangJiao.inject(inflate, this);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract int initView();

    protected abstract void initData();

    public void showToast(String msg) {
        activity.runOnUiThread(() -> Toast.makeText(activity.getApplication(), msg, Toast.LENGTH_SHORT).show());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        for (IPresenter presenter : presenters) {
            presenter.build(this);
        }
        DataBus.getDefault().destroy(this);
    }
}
