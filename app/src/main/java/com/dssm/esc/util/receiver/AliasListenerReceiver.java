package com.dssm.esc.util.receiver;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class AliasListenerReceiver extends JPushMessageReceiver {

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d("JgPush", "[MyReceiver] onAliasOperatorResult - " + jPushMessage.getAlias());

    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d("JgPush", "[MyReceiver] onAliasOperatorResult - " + jPushMessage.getAlias());

    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d("JgPush", "[MyReceiver] onAliasOperatorResult - " + jPushMessage.getAlias());

    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d("JgPush", "[MyReceiver] onAliasOperatorResult - " + jPushMessage.getAlias());

    }
}
