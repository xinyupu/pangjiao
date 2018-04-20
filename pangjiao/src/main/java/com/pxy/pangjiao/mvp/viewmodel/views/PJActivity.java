package com.pxy.pangjiao.mvp.viewmodel.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.pxy.pangjiao.PangJiao;
import com.pxy.pangjiao.databus.DataBus;
import com.pxy.pangjiao.mvp.presenter.IPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pxy on 2018/2/1.
 */

public abstract class PJActivity extends Activity {


    private Handler handler;

    public List<IPresenter> presenters;

    @SuppressLint("HandlerLeak")
    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenters = new ArrayList<>();
        PangJiao.register(this);
        beforeSetContentView();
        setContentView(initView());
        PangJiao.inject(this);
        initData();
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case 1:
                        Toast.makeText(getApplication(), (String) message.obj, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }


    public void beforeSetContentView(){

    }

    public abstract int initView();

    public abstract void initData();


    public final void showToast(String msg) {
        if (handler != null) {
            Message message = new Message();
            message.what = 1;
            message.obj = msg;
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (IPresenter presenter : presenters) {
            presenter.onDestroy();
        }
        DataBus.getDefault().destroy(this);
    }


}
