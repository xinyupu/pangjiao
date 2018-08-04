package pxy.com.service.imp;


import com.pxy.pangjiao.compiler.mpv.annotation.Service;

import pxy.com.adapter.protocol.RequestActive;
import pxy.com.adapter.protocol.ResponseActive;
import pxy.com.service.IAppService;

/**
 * Created by Administrator on 2018/3/15.
 */

@Service
public class AppService implements IAppService {

    @Override
    public String login(String name, String pwd) {
        return name;
    }

    @Override
    public ResponseActive active(String activeCode) {
        RequestActive requestActive = new RequestActive();
        requestActive.setActiveCode(activeCode);
        return requestActive.execute();
    }
}
