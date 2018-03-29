package pxy.com.service.imp;


import com.pxy.pangjiao.compiler.mpv.annotation.Service;

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
}
