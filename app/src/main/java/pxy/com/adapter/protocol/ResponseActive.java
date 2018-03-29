package pxy.com.adapter.protocol;

import com.pxy.pangjiao.compiler.mpv.annotation.DataSource;

/**
 * Created by Administrator on 2018/3/29.
 */

@DataSource
public class ResponseActive {



    private boolean issuccess;
    private String msg;
    private String data;

    public boolean isIssuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
