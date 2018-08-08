package com.dssm.esc.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.jpush.android.api.JPushInterface;

/**
 * 程序卸载广播
 * @author zsj
 *
 */
public class BootReceiver extends BroadcastReceiver{
	  
	@Override  
    public void onReceive(Context context, Intent intent){
        //接收安装广播 
		
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {   
            String packageName = intent.getDataString();   
            System.out.println("安装了:" +packageName + "包名的程序");     
        }   
        //接收卸载广播  
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {   
            String packageName = intent.getDataString();   
            System.out.println("卸载了:"  + packageName + "包名的程序");
            /** 推送在程序卸载时解除账号绑定 */
            JPushInterface.stopPush(context.getApplicationContext());
            JPushInterface.deleteAlias(context.getApplicationContext(), 2018);
        }
    }
}  