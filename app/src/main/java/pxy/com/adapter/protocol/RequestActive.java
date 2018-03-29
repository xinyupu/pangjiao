package pxy.com.adapter.protocol;

import com.pxy.pangjiao.net.NetModel;
import com.pxy.pangjiao.net.helper.PNet;

/**
 * Created by Administrator on 2018/3/29.
 */

@PNet(api = "http://jtc.damuzhikj.com/Api/Car/Active")
public class RequestActive extends NetModel<ResponseActive>{

    private String ActiveCode;

    public String getActiveCode() {
        return ActiveCode;
    }

    public void setActiveCode(String ActiveCode) {
        this.ActiveCode = ActiveCode;
    }
}
