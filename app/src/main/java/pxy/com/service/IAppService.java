package pxy.com.service;

import pxy.com.adapter.protocol.ResponseActive;

/**
 * Created by Administrator on 2018/3/15.
 */

public interface IAppService {
    String login(String name, String pwd);

    ResponseActive active(String activeCode);
}
