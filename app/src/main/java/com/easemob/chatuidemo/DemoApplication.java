/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.dssm.esc.util.CrashHandler;
import com.dssm.esc.util.MySharePreferencesService;
import com.easemob.EMCallBack;
import com.easemob.chatuidemo.utils.SpUtil;
import com.squareup.leakcanary.LeakCanary;
import org.xutils.x;
import java.util.Map;

public class DemoApplication extends Application implements
        Thread.UncaughtExceptionHandler {

    public static Context applicationContext;
    private static DemoApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";
    public onInitNetListener netListener;
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();
    public String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        if (TextUtils.isEmpty(url)) {
            return SpUtil.getSpUtil("address", MODE_PRIVATE).getSPValue("url", "http://www.1sc-china.com/bcm/app/");
        //    return "http://www.1sc-china.com/bcm/app/";
        } else {
            return url;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        service = new MySharePreferencesService(applicationContext);
        // 回显
        map = service.getPreferences();
        Thread.setDefaultUncaughtExceptionHandler(this); // 程序崩溃时触发线程
        /**
         * this function will initialize the HuanXin SDK
         *
         * @return boolean true if caller can continue to call HuanXin related
         *         APIs after calling onInit, otherwise false.
         *
         *         环信初始化SDK帮助函数
         *         返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         *
         *         for example: 例子：
         *
         *         public class DemoHXSDKHelper extends HXSDKHelper
         *
         *         HXHelper = new DemoHXSDKHelper();
         *         if(HXHelper.onInit(context)){ // do HuanXin related work }
         */
        hxSDKHelper.onInit(applicationContext);
        //注册异常捕获工具类
        CrashHandler.getInstance().init(this);

        //xUtils初始化
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能

        //初始化LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    public static DemoApplication getInstance() {
        return instance;
    }

    public void setNetListener(onInitNetListener netListener) {
        this.netListener = netListener;
    }

    interface onInitNetListener {
        /** 访问网络的抽象方法 */
        void initNetData();
    }

    /**
     * 获取当前登录用户名
     *
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     *
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final boolean isGCM, final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(isGCM, emCallBack);
    }

    /** 保存用户信息 */
    private MySharePreferencesService service;
    /** 保存用户名和密码的map集合 */
    private Map<String, String> map;

    /**
     * 此时，程序已经全部挂掉，UI和接口数据已全部不存在，要引导界面重新打开程序
     */
    // 创建服务用于捕获崩溃异常
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // TODO Auto-generated method stub

        service.save(map.get("loginName"), "", "", "", "", "", "", "", "", "",
                "");
        logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
//				service.save(map.get("loginName"), "", "", "", "", "", "", "", "", "",
//						"");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {

            }
        });


        /**
         * 新增
         * 2017/10/16
         * 解决无限重启问题？
         */
  /*      Intent intent = new Intent(instance, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        instance.startActivity(intent);*/
        android.os.Process.killProcess(android.os.Process.myPid()); // 结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }
}
