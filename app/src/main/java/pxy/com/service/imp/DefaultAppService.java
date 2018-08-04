package pxy.com.service.imp;

import com.pxy.pangjiao.compiler.mpv.annotation.Service;

import pxy.com.adapter.protocol.ResponseActive;
import pxy.com.service.IAppService;

/**
 * Created by Administrator on 2018/3/16.
 */
@Service
public class DefaultAppService implements IAppService {

    @Override
    public String login(String name, String pwd) {
        return "失败";
    }

    @Override
    public ResponseActive active(String activeCode) {
        return null;
    }
}
