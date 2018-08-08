package com.dssm.esc.util.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dssm.esc.util.ActivityCollector;
import com.dssm.esc.view.activity.MainActivity;
import com.easemob.chatuidemo.activity.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.PushReceiver;

public class JgReceiver extends PushReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("JgPush", "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + bundle.toString());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d("JgPush", "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...


        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d("JgPush", "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            String msgType = "";
            try {
                JSONObject obj = new JSONObject(bundle.getString(JPushInterface.EXTRA_MESSAGE));
                if (!obj.isNull("msgType")) {
                    msgType = obj.getString("msgType");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent1 = new Intent("com.dssm.esc.RECEIVER");
            intent1.putExtra("msgType", msgType);
            context.sendBroadcast(intent1);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d("JgPush", "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d("JgPush", "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d("JgPush", "[MyReceiver] 用户点击打开了通知");
            Intent intent2 = new Intent(context, MainActivity.class);
            ActivityCollector.finishSplashActivity();
            if (context.getPackageManager().resolveActivity(intent2, 0) == null) {
                // 说明系统中不存在这个activity
                intent2.setClass(context, SplashActivity.class);
                intent2.putExtra("mainActivity", "unLive");
            }
            else {
                intent2.setClass(context, SplashActivity.class);
                intent2.putExtra("mainActivity", "live");
            }
            String text = "通知被打开";
            Log.i("jgPush", text);
            String msgType = "";
            try {
                JSONObject obj = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                if (!obj.isNull("msgType")) {
                    msgType = obj.getString("msgType");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            intent2.putExtra("msgType", msgType);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity(intent2);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d("JgPush", "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w("JgPush", "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d("JgPush", "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }
}
