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
package com.dssm.esc;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.dssm.esc.util.ActivityCollector;
import com.dssm.esc.util.CrashHandler;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.PathUtil;
import com.dssm.esc.view.activity.LoginActivity;
import com.dssm.esc.util.SpUtil;
import com.squareup.leakcanary.LeakCanary;

import org.xutils.x;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

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
    public String url;
    //连续登录超时计数
    public static int sessionTimeoutCount = 0;

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
        //注册异常捕获工具类
        CrashHandler.getInstance().init(this);
        //初始化聊天文件存储目录
        PathUtil.getInstance().initDirs("chat", "jMessage", this);
        //设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(true);
        //初始化JPush
        JPushInterface.init(this);
        //极光IM初始化
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this, true);
        //注册全局事件监听类
        JMessageClient.registerEventReceiver(new GlobalEventListener(getApplicationContext()));

        //xUtils初始化
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能

        //初始化LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        /**
         * 以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
         * 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
         */
        AutoSizeConfig.getInstance()

                //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
                //如果没有这个需求建议不开启
                .setCustomFragment(true)

        //是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
//                .setLog(false)

        //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
        //AutoSize 会将屏幕总高度减去状态栏高度来做适配, 如果设备上有导航栏还会减去导航栏的高度
        //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏以及导航栏高度
//                .setUseDeviceSize(true)

        //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
                .setBaseOnWidth(false)

        //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
//                .setAutoAdaptStrategy(new AutoAdaptStrategy())
        ;
        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        //在 Demo 中跳转的三方库中的 DefaultErrorActivity 就是在另外一个进程中, 所以要想适配这个 Activity 就需要调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this);
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
     * 退出登录,清空数据
     */
    public void logout() {
        // 先调用sdk logout，在清理app中自己的数据
        JMessageClient.logout();
    }

    public void return2Login() {
        logout();
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                /** 推送在退出登录时解除账号绑定 */
                JPushInterface.stopPush(getApplicationContext());
                JPushInterface.deleteAlias(getApplicationContext(), 2018);
                // 清除本地的sharepreference缓存
                MySharePreferencesService.getInstance(getApplicationContext()).clear();
                // 重新显示登录页面
                ActivityCollector.finishAll();
                startActivity(new Intent(getApplicationContext(),
                        LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
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
        logout();


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
