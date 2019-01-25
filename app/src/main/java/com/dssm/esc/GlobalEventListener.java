package com.dssm.esc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.dssm.esc.R;
import com.dssm.esc.util.ActivityCollector;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.activity.SplashActivity;
import com.dssm.esc.DemoApplication;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Message;

/**
 * 在demo中对于通知栏点击事件和在线消息接收事件，我们都直接在全局监听
 */
public class GlobalEventListener {
    private Context appContext;
    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    public GlobalEventListener(Context context) {
        appContext = context;
        JMessageClient.registerEventReceiver(this);
    }
    //点击通知栏消息时触发
    public void onEvent(NotificationClickEvent event) {
        jumpToActivity(event.getMessage());
    }
    //收到在线消息时触发
    public void onEvent(MessageEvent event) {
        Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
        intent.putExtra("msgType", "3");
        appContext.sendBroadcast(intent);
    }
    //登录状态异常时触发
    public void onEvent(LoginStateChangeEvent event) {
        final LoginStateChangeEvent.Reason reason = event.getReason();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //用户在其他设备登录
                if(reason == LoginStateChangeEvent.Reason.user_logout) {
                    showConflictDialog();
                }
                //用户账号被移除、被禁用或者密码被修改
                else if(reason == LoginStateChangeEvent.Reason.user_deleted
                        || reason == LoginStateChangeEvent.Reason.user_password_change
                        || reason == LoginStateChangeEvent.Reason.user_disabled) {
                    showAccountRemovedDialog();
                }
            }
        });
    }

    private void jumpToActivity(Message msg) {
        Intent intent2 = new Intent(appContext, SplashActivity.class);
        ActivityCollector.finishSplashActivity();
        if (Utils.isExistActivity(MainActivity.class)) {
            // 说明系统中存在这个activity
            intent2.putExtra("mainActivity", "live");
        }
        else {
            intent2.putExtra("mainActivity", "unLive");
        }
        String text = "IM被打开";
        Log.i("jgPush", text);
        String msgType = "3";
        intent2.putExtra("msgType", msgType);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        appContext.startActivity(intent2);
    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        String st = appContext.getResources().getString(R.string.Logoff_notification);
        // clear up global variables
        try {
            if (conflictBuilder == null) {
                Activity activity = ActivityCollector.getTopActivity();
                if(activity == null)
                    return;
                conflictBuilder = new android.app.AlertDialog.Builder(
                        activity);
            }
            conflictBuilder.setTitle(st);
            conflictBuilder.setMessage(R.string.connect_conflict);
            conflictBuilder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                            conflictBuilder = null;
                            DemoApplication.getInstance().return2Login();
                        }
                    });
            conflictBuilder.setCancelable(false);
            conflictBuilder.create().show();
        } catch (Exception e) {

        }
    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        String st5 = appContext.getResources().getString(R.string.Remove_the_notification);
        try {
            if (accountRemovedBuilder == null)
            {
                Activity activity = ActivityCollector.getTopActivity();
                if(activity == null)
                    return;
                accountRemovedBuilder = new android.app.AlertDialog.Builder(
                        activity);
            }
            accountRemovedBuilder.setTitle(st5);
            accountRemovedBuilder.setMessage(R.string.em_user_remove);
            accountRemovedBuilder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                            accountRemovedBuilder = null;
                            DemoApplication.getInstance().return2Login();
                        }
                    });
            accountRemovedBuilder.setCancelable(false);
            accountRemovedBuilder.create().show();
        } catch (Exception e) {

        }
    }
}
