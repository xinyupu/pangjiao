package pxy.com.test;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.pxy.pangjiao.compiler.injectview.annotation.InitView;
import com.pxy.pangjiao.compiler.injectview.annotation.OnClick;
import com.pxy.pangjiao.compiler.mpv.annotation.AutowireProxy;
import com.pxy.pangjiao.compiler.mpv.annotation.Views;
import com.pxy.pangjiao.logger.Logger;
import com.pxy.pangjiao.mvp.viewmodel.views.PJAppCompatActivity;

import pxy.com.application.IMemberPresent;
import pxy.com.application.IMainActivityView;
import pxy.com.application.ITouristService;
import pxy.com.application.imp.TouristService;
import pxy.com.model.UserView;

@Views
public class MainActivity extends PJAppCompatActivity implements IMainActivityView {


    @AutowireProxy
    public IMemberPresent memberPresent;

    @AutowireProxy(imp = TouristService.class)
    public ITouristService touristService;

    @InitView(R.id.tv_text)
    public TextView tvText;


    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

        UserView view = new UserView();
        view.setName("张三");
        view.setPhone("132659856623");

        Logger.info(true, "text", "测试日志模块");

        Logger.e("text", "测试日志模块");

    }


    private void showRemoteView() {
        Intent intent = new Intent(this, NotifyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.layout_custom_notifycation);
        remoteViews.setImageViewResource(R.id.iv_icon, R.mipmap.ic_icon);
        remoteViews.setTextViewText(R.id.tv_title, "我是一个有内涵的程序猿");
        remoteViews.setTextViewText(R.id.tv_description,
                "你懂我的，作为新时代的程序员，要什么都会背砖搬砖都要会，");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_icon);
        builder.setContent(remoteViews);
        builder.mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        builder.setContentIntent(contentIntent);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            builder.setCustomBigContentView(remoteViews);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(111, builder.build());
    }


    @OnClick({R.id.tv_text, R.id.tv_title, R.id.request, R.id.notify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_text:
            case R.id.tv_title:
                showToast(((TextView) view).getText().toString());
                break;
            case R.id.request:
                touristService.active("富翁");
                break;
            case R.id.notify:
                memberPresent.active("6666");
                break;
        }
    }

}
