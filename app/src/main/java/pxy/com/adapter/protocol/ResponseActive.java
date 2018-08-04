package pxy.com.adapter.protocol;

import com.pxy.pangjiao.compiler.mpv.annotation.DataSource;
import com.pxy.pangjiao.mvp.viewmodel.model.ModelField;
import com.pxy.pangjiao.net.ResponseBase;

/**
 * Created by Administrator on 2018/3/29.
 */

@DataSource
public class ResponseActive extends ResponseBase {


    private boolean issuccess;
    private String msg;

    @ModelField(fieldName = "name")
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
