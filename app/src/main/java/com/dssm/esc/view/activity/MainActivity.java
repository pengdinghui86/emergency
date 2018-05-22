package com.dssm.esc.view.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.database.DataBaseManage;
import com.dssm.esc.model.entity.user.UserEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.DataCleanManager;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.PermissionsChecker;
import com.dssm.esc.util.SystemBarTintManager;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.fragment.AdrressListFragment;
import com.dssm.esc.view.fragment.ControlCenterFragment;
import com.dssm.esc.view.fragment.EmergencyManageFragment;
import com.dssm.esc.view.fragment.MessageFragment;
import com.dssm.esc.view.widget.RedPointView;
import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.LoginActivity;
import com.easemob.chatuidemo.activity.SplashActivity;
import com.easemob.util.EMLog;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 主界面
 *
 * @author Zsj
 * @Description TODO
 * @date 2015-9-17
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 * Ltd. Inc. All rights reserved.
 */
public class MainActivity extends FragmentActivity implements EMEventListener {

    /**
     * 记录退出按下时间
     */
    private long exitTime = 0;
    /**
     * 标签内容
     */
    private RadioGroup tabGroup;
    /**
     * 消息碎片
     */
    private MessageFragment messageFragment;
    /**
     * 通讯录碎片
     */
    private AdrressListFragment adrressListFragment;
    /**
     * 应急管理碎片
     */
    private EmergencyManageFragment emergencyManageFragment;
    /**
     * 控制中心碎片
     */
    private ControlCenterFragment controlCenterFragment;
    /**
     * 4.4版本以上的沉浸式
     */
    protected SystemBarTintManager mTintManager;

    RedPointView redPointView;
    private TextView button;// 用来显示提示信息的
    private RadioButton rdb0, rdb1, rdb2, rdb3;
    /**
     * 保存用户信息
     */
    private MySharePreferencesService preferencesService;
    /**
     * 保存用户名和密码的map集合
     */
    private Map<String, String> map;
    /**
     * 每个用户的每个角色的三张表：任务，系统，紧急
     */
    private String table1 = "";
    private String table2 = "";
    private String table3 = "";
    private String table4 = "";
    private String usertable = "";
    private Context context;
    private int unReadCount = 0;
    private int currentTabIndex;
    protected UserSevice usevice;
    public onInitNetListener netListener;

    private DrawerLayout mDrawerLayout;
    private TextView tv_item_info1;
    private TextView tv_item_info2;
    private TextView tv_item_exit;
    private TextView tv_item_exchange;

    private ProgressDialog pd;
    /**
     * 当前被选中的角色id
     */
    private String selectedRolem;
    /**
     * 当前被选中的角色名称
     */
    private String selectedRolemName;
    /**
     * 当前被选中的角色编号
     */
    private String roleCode;
    private String name;// 用户姓名
    // 用户角色
    private String[] identity;
    // 用户角色id
    private String[] rolesId;
    /**
     * 用户角色编号
     */
    private String[] roleCodes;
    private UserSevice userSevice;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码
    // 所需的全部权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 检查当前用户是否被删除
     */
    public static boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //如果从最近使用的应用里面点击我们的应用，系统会恢复之前被回收的Activity，这个时候FragmentActivity在oncreate里面也会做Fragment的恢复
            //但是此时恢复出的Fragment，在调用getActivity的时候会返回null
            savedInstanceState.putParcelable("android:fragments", null);
            //这里MainActivity的启动模式为singleTask，所以为避免展示空的MainActivity界面重新做跳转
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main);
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        context = this;

        if (savedInstanceState != null
                && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) { //
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash // 三个fragment里加的判断同理
            DemoHXSDKHelper.getInstance().logout(true, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null
                && savedInstanceState.getBoolean("isConflict", false)) { //
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        if (getIntent().getBooleanExtra("conflict", false)
                && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
                && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
        // 使布局在状态栏下面
        View findViewById = findViewById(R.id.main);
        findViewById.setFitsSystemWindows(true);
        // 标题栏和状态栏风格一致
        setStatusBarState();
        // Thread.setDefaultUncaughtExceptionHandler(restartHandler); //
        // 程序崩溃时触发线程
        preferencesService = new MySharePreferencesService(this);
        usevice = Control.getinstance().getUserSevice();
        // 回显
        map = preferencesService.getPreferences();
        usertable = map.get("userId").replace("-", "");
        table1 = "task_" + usertable;
        table2 = "system_" + usertable;
        table3 = "emergency_" + usertable;
        table4 = "my_" + usertable;

        userSevice = Control.getinstance().getUserSevice();
        selectedRolemName = map.get("selectedRolemName");
        name = map.get("name");
        identity = convertStrToArray(map.get("roleNames"));
        rolesId = convertStrToArray(map.get("roleIds"));
        roleCodes = convertStrToArray(map.get("roleCodes"));

        // 创建数据库
        DataBaseManage.createDataBase(table1, table2, table3, table4);
        if (MyCookieStore.cookieStore != null) {
            XGPushConfig.enableDebug(this, true);
            context = getApplicationContext();
            Log.i("postFlag岗位标识", map.get("postFlag"));
            /** 账号绑定，第二个参前台与后台预定好的，要保持一致（最好用用户名+“_”+用户id,保持唯一），在登录成功后调用 */
            XGPushManager.registerPush(context, map.get("postFlag"),
                    new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object data, int flag) {
                            Log.d("TPush", "注册成功，设备token为：" + data);
                        }

                        @Override
                        public void onFail(Object data, int errCode, String msg) {
                            Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息："
                                    + msg);
                        }
                    });

            initView();
            init();
        } else {
            // Intent intent = new Intent(getApplicationContext(),
            // LoginActivity.class);
            // ToastUtil.showToast(getApplicationContext(), "请求实效，请重新登陆");
            // startActivity(intent);
            relogin();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
        tv_item_info1 = (TextView) findViewById(R.id.tv_item_info1);
        tv_item_info1.setText(name);
        tv_item_info2 = (TextView) findViewById(R.id.tv_item_info2);
        tv_item_info2.setText(selectedRolemName);

        tv_item_exit = (TextView) findViewById(R.id.tv_item_exit);
        tv_item_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        tv_item_exchange = (TextView) findViewById(R.id.tv_item_exchange);
        tv_item_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRoles();
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        });

        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        List<String> permissionList = mPermissionsChecker.lacksPermissions(PERMISSIONS);
        if (permissionList.size() > 0) {
            startPermissionsActivity(permissionList.toArray(new String[permissionList.size()]));
        }
    }

    private MyConnectionListener connectionListener = null;

    private void init() {
        connectionListener = new MyConnectionListener();
        ToastUtil.showToast(context, "登录成功");
        EMChatManager.getInstance().addConnectionListener(connectionListener);
        /*
         * Log.i("MainActivity用户名--环信", DemoApplication.getInstance()
		 * .getUserName()); Log.i("MainActivity密码--环信",
		 * DemoApplication.getInstance().getPassword());
		 */
        tabGroup = (RadioGroup) findViewById(R.id.main_tab);
        rdb0 = (RadioButton) findViewById(R.id.rdb0);
        rdb1 = (RadioButton) findViewById(R.id.rdb1);
        rdb2 = (RadioButton) findViewById(R.id.rdb2);
        rdb3 = (RadioButton) findViewById(R.id.rdb3);
        button = (TextView) findViewById(R.id.bt);
        remind(button, unReadCount);
        switchView(0);
        // tabGroup.check(TabTypeEnum.message.getId());
        tabGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // button.setFocusable(false);
                // button.setFocusableInTouchMode(false);
                // TODO Auto-generated method stub
                if (checkedId == rdb0.getId()) {
                    switchView(0);

                } else if (checkedId == rdb1.getId()) {
                    switchView(1);
                } else if (checkedId == rdb2.getId()) {
                    switchView(2);
                } else if (checkedId == rdb3.getId()) {
                    switchView(3);
                }
            }
        });
    }

    public void setNetListener(onInitNetListener netListener) {
        this.netListener = netListener;
    }

    public interface onInitNetListener {
        /**
         * 访问网络的抽象方法
         */
        void initNetData();
    }

    /**
     * RedPointView具体的使用
     *
     * @param view
     */
    public void remind(View view, int content) {
        redPointView = new RedPointView(this, view);
        LayoutParams lP = (LayoutParams) new LayoutParams(
                25, 25);
        redPointView.setLayoutParams(lP);
        redPointView.setText(String.valueOf(content));// 需要显示的提醒类容
        redPointView.setPosition(0, Gravity.RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        redPointView.setTextSize(12); // 文本大小
        redPointView.hide();
    }

    private void initView() {
        tabGroup = (RadioGroup) findViewById(R.id.main_tab);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false)
                && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
                && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
        // setIntent(intent);// 必须要调用这句
    }

    // 账号在别处登录
    public static boolean isConflict = false;
    // 账号被移除
    private static boolean isCurrentAccountRemoved = false;
    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(
                            MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                conflictBuilder = null;
                                finish();
                                startActivity(new Intent(MainActivity.this,
                                        LoginActivity.class));
                            }
                        });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e("MainActivity", "---------color conflictBuilder error"
                        + e.getMessage());
            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(true, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(
                            MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                accountRemovedBuilder = null;
                                finish();
                                startActivity(new Intent(MainActivity.this,
                                        LoginActivity.class));
                            }
                        });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e("MainActivity",
                        "---------color userRemovedBuilder error"
                                + e.getMessage());
            }

        }

    }

    /**
     * 选择界面
     *
     * @param position
     * @version 1.0
     * @createTime 2015-9-17,上午11:47:17
     * @updateTime 2015-9-17,上午11:47:17
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public void switchView(int position) {
        // 获取Fragment的操作对象
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:// 添加 并展示 消息碎片
                currentTabIndex = 0;
                if (messageFragment == null) {
                    messageFragment = new MessageFragment(this, table1, table2,
                            table3, table4);
                    transaction.add(R.id.view_parent, messageFragment);
                    // messageFragment.initData(0);
                } else {
                    transaction.show(messageFragment);
                }
                redPointView.setText(String.valueOf(unReadCount));
                Log.i("unReadCount", String.valueOf(unReadCount));
                if (unReadCount > 0) {

                    redPointView.show();
                } else {
                    redPointView.hide();
                }
                break;
            case 1:// 添加 并展示 通讯录碎片
                currentTabIndex = 1;
                if (adrressListFragment == null) {
                    adrressListFragment = new AdrressListFragment(this);
                    transaction.add(R.id.view_parent, adrressListFragment);
                    // adrressListFragment.initGetData();
                } else {
                    transaction.show(adrressListFragment);
                }
                // EventBus.getDefault().post(new mainEvent("sasasasa"));//
                // 用EventBus发送数据
                break;
            case 2:// 添加 并展示应急管理碎片
                currentTabIndex = 2;
                if (emergencyManageFragment == null) {
                    emergencyManageFragment = new EmergencyManageFragment(this);
                    transaction.add(R.id.view_parent, emergencyManageFragment);
                    // emergencyManageFragment.initGetData();
                } else {
                    emergencyManageFragment.getUserPower();
                    transaction.show(emergencyManageFragment);
                }
                break;
            case 3:// 添加 并展示 控制中心碎片
                currentTabIndex = 3;
                if (controlCenterFragment == null) {
                    controlCenterFragment = new ControlCenterFragment(this);
                    transaction.add(R.id.view_parent, controlCenterFragment);
                    // controlCenterFragment.initGetData();
                } else {
                    transaction.show(controlCenterFragment);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 隐藏所有Fragment
     *
     * @param transaction
     * @version 1.0
     * @createTime 2015-9-17,上午11:47:33
     * @updateTime 2015-9-17,上午11:47:33
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
            // messageFragment.onPause();(可以在此重新刷新界面等一系列操作)
        }
        if (adrressListFragment != null) {
            transaction.hide(adrressListFragment);
        }
        if (emergencyManageFragment != null) {
            transaction.hide(emergencyManageFragment);
        }
        if (controlCenterFragment != null) {
            transaction.hide(controlCenterFragment);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {// 按两次退出
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                // 要执行的事件
                // DialogUtil.showExitsDg(MainActivity.this);对话框退出
                // 判断2次点击事件时间
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    ToastUtil.showToast(MainActivity.this,
                            getString(R.string.diver_exit_time));
                    exitTime = System.currentTimeMillis();
                } else {
                    // JPushSetting.setOffLine();
                    ToastUtil.cancle();

                    finish();
                    /*
                     * Intent intent = new Intent(Intent.ACTION_MAIN);
					 * intent.addCategory(Intent.CATEGORY_HOME);
					 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					 * startActivity(intent);
					 */
                }

            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 判断某一个类是否存在任务栈里面
     *
     * @return
     */
    private boolean isExsitMianActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }

    /**
     * 进行逻辑处理
     */
    public void dealWithIntent() {
        if (isExsitMianActivity(SplashActivity.class)) {//存在这个类
            //进行操作
        } else {//不存在这个类
            //进行操作
        }
    }

    /***
     *
     * 沉浸式
     *
     * @version 1.0
     * @createTime 2015-9-17,上午11:48:45
     * @updateTime 2015-9-17,上午11:48:45
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 沉浸式
     *
     * @version 1.0
     * @createTime 2015-9-17,上午11:48:58
     * @updateTime 2015-9-17,上午11:48:58
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private void setStatusBarState() {
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT >= 19) {
            setTranslucentStatus(true);
            mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            // 使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
            mTintManager.setStatusBarTintResource(R.color.title_bg);
            // 设置状态栏的文字颜色
            mTintManager.setStatusBarDarkMode(false, this);
        }
    }

    /***
     * 打开EventBus开关
     */
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 接收推送过来的消息类型，向MessageFragment发送消息，要显示哪个界面
     *
     * @param data
     */

    public void onEvent(mainEvent data) {
        if (data.getData().equals("t")) {

            switchView(0);// 展示MessageFragment
            rdb0.setChecked(true);
            rdb1.setChecked(false);
            rdb2.setChecked(false);
            rdb3.setChecked(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        ProgressDialog progressDialog = Utils.getInstance().progressDialog;
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;

            }
        }
        ToastUtil.cancle();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 连接监听listener
     */
    public class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        showAccountRemovedDialog();
                    } else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
                    }
                    /** 信鸽推送，账号在其他地方登录时解除账号绑定 */
                    XGPushManager.registerPush(context, "*");
                }

            });
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        if (connectionListener != null) {
            EMChatManager.getInstance().removeConnectionListener(
                    connectionListener);
        }
        // 清除本地的sharepreference缓存
        DataCleanManager.cleanSharedPreference(context);
        super.onDestroy();
    }

    @Override
    public void onEvent(EMNotifierEvent event) {
        // TODO Auto-generated method stub
        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) event.getData();

                // 提示新消息
                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

                refreshUI();
                break;
            }

            case EventOfflineMessage: {
                refreshUI();
                break;
            }

            case EventConversationListChanged: {
                refreshUI();
                break;
            }

            default:
                break;
        }
    }

    public int msgcount;

    private void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                msgcount = updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    if (messageFragment != null) {
                        messageFragment.refresh(msgcount);
                    }
                }
            }
        });
    }

    /**
     * 刷新未读消息数
     */
    public int updateUnreadLabel() {
        return getUnreadMsgCountTotal();

    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public static int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance()
                .getAllConversations().values()) {
            if (conversation.getType() == EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount
                        + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.i("MainActivity", "onResume");
        if (!isConflict && !isCurrentAccountRemoved) {
            // msgcount=updateUnreadLabel();
            EMChatManager.getInstance().activityResumed();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
                .getInstance();
        sdkHelper.pushActivity(this);

        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(
                this,
                new EMNotifierEvent.Event[]{
                        EMNotifierEvent.Event.EventNewMessage,
                        EMNotifierEvent.Event.EventOfflineMessage,
                        EMNotifierEvent.Event.EventConversationListChanged});
    }

    private void startPermissionsActivity(String[] permissions) {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, permissions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            final String[] PERMISSIONS = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            List<String> permissionList = mPermissionsChecker.lacksPermissions(PERMISSIONS);
            if (permissionList.size() > 0) {
                if(permissionList.get(0).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    finish();
            }
        }
    }

    /*
	 * // 创建服务用于捕获崩溃异常 private UncaughtExceptionHandler restartHandler = new
	 * UncaughtExceptionHandler() { public void uncaughtException(Thread thread,
	 * Throwable ex) { restartApp();// 发生崩溃异常时,重启应用 } };
	 * 
	 * private void restartApp() { //// relogin(); // Intent intent =
	 * getBaseContext().getPackageManager() // .getLaunchIntentForPackage( //
	 * getBaseContext().getPackageName()); // // PendingIntent restartIntent =
	 * PendingIntent.getActivity( // getApplicationContext(), 0, intent, //
	 * Intent.FLAG_ACTIVITY_NEW_TASK); // // 退出程序 // AlarmManager mgr =
	 * (AlarmManager) getSystemService(Context.ALARM_SERVICE); //
	 * mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, //
	 * restartIntent); // 1秒钟后重启应用 // Log.i("我是MainCtivity程序捕获异常", ""); //
	 * System.exit(0);
	 * 
	 * 
	 * }
	 */

    /**
     * 重新登录
     */
    public void relogin() {
        usevice.relogin(map.get("loginName"), map.get("password"),
                map.get("selectedRolem"), new UserSeviceImpl.UserSeviceImplListListenser() {

                    @Override
                    public void setUserSeviceImplListListenser(Object object,
                                                               String stRerror, String Exceptionerror) {
                        // TODO Auto-generated method stub
                        String str = null;
                        String string = "";
                        // 若登陆成功，直接进入主界面
                        if (object != null) {
                            UserEntity userEntity = (UserEntity) object;
                            if (userEntity.getSuccess().equals("true")) {
                                str = "MainActivity重新登陆";
                                ToastUtil.showLongToast(context, str);
                                netListener.initNetData();
                            } else {
                                str = "密码已失效,请重新登陆";
                                ToastUtil.showLongToast(context, str);
                                Intent intent = new Intent(context,
                                        LoginActivity.class);
                                context.startActivity(intent);

                            }

                        } else if (stRerror != null) {

                            str = stRerror;
                            ToastUtil.showLongToast(context, str);
                        } else if (Exceptionerror != null) {

                            str = Const.NETWORKERROR + Exceptionerror;
                            ToastUtil.showLongToast(context, str);
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
                .getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    /**
     * 退出登录
     */
    private void logout() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHXSDKHelper.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        /** 信鸽推送，在退出登录时解除账号绑定 */
                        XGPushManager.registerPush(context, "*");
                        // 重新显示登陆页面
                        finish();
                        startActivity(new Intent(MainActivity.this,
                                LoginActivity.class));

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,
                                "unbind devicetokens failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    /**
     * 切换角色
     */
    private void changeRoles() {
        if (identity.length > 1) {

            new AlertDialog.Builder(this)
                    .setTitle("当前角色：" + selectedRolemName)
                    .setSingleChoiceItems(identity, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    final int which) {
                                    dialog.dismiss();
                                    Toast.makeText(context,
                                            "你选择了: " + identity[which], Toast.LENGTH_SHORT)
                                            .show();
                                    // 选择角色
                                    userSevice.loginRole(rolesId[which],
                                            new UserSeviceImpl.UserSeviceImplBackBooleanListenser() {

                                                @Override
                                                public void setUserSeviceImplListenser(
                                                        Boolean backflag,
                                                        String stRerror,
                                                        String Exceptionerror) {
                                                    String str = null;
                                                    if (backflag) {
                                                        str = stRerror;
                                                        // 被选中的角色id,再次保存
                                                        selectedRolem = rolesId[which];
                                                        selectedRolemName = identity[which];
                                                        roleCode = roleCodes[which];

                                                        preferencesService.save(
                                                                map.get("loginName"),
                                                                map.get("password"),
                                                                map.get("roleIds"),
                                                                map.get("roleNames"),
                                                                map.get("roleCodes"),
                                                                selectedRolem,
                                                                map.get("postFlag"),
                                                                map.get("userId"),
                                                                selectedRolemName,
                                                                roleCode,
                                                                name);
                                                        tv_item_info1.setText(name);
                                                        tv_item_info2.setText(selectedRolemName);
                                                    } else if (stRerror != null) {
                                                        str = stRerror;
                                                        ToastUtil
                                                                .showLongToast(
                                                                        context,
                                                                        str);
                                                    } else if (Exceptionerror != null) {
                                                        str = Const.NETWORKERROR + Exceptionerror;
                                                        ToastUtil.showLongToast(context, str);
                                                    }
                                                }
                                            });
                                }

                            }).setNegativeButton("取消", null).show();
        } else if (identity.length == 1) {
            ToastUtil.showToast(context, "无角色进行切换");
        }
    }


    /**
     * 将字符串用","切割成数组
     *
     * @param str
     * @return
     */
    private String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); // 拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }
}
