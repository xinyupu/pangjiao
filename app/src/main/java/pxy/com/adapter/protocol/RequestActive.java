package pxy.com.adapter.protocol;

import com.pxy.pangjiao.net.NetModel;
import com.pxy.pangjiao.net.helper.Net;

/**
 * Created by Administrator on 2018/3/29.
 */

@Net(api = "http://jtc.damuzhikj.com", connectTimeOut = 10000)
public class RequestActive extends NetModel<ResponseActive> {

    private String ActiveCode;

    public String getActiveCode() {
        return ActiveCode;
    }

    public void setActiveCode(String ActiveCode) {
        this.ActiveCode = ActiveCode;
    }
}
