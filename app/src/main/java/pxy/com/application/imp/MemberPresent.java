package pxy.com.application.imp;

import com.pxy.pangjiao.compiler.mpv.annotation.Presenter;

import pxy.com.application.IMemberPresent;
import pxy.com.application.IMemberView;

/**
 * Created by Administrator on 2018/3/15.
 */

@Presenter
public class MemberPresent implements IMemberPresent {

    private IMemberView memberView;

    @Override
    public void build(Object o) {
        memberView = (IMemberView) o;
    }

    @Override
    public void onDestroy() {
        this.memberView = null;
    }

    @Override
    public void open(String smg) {
        memberView.showToast(smg);
    }
}
