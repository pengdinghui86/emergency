/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
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
package com.easemob.chatuidemo.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.entity.user.UpdataInfo;
import com.dssm.esc.model.entity.user.UserEntity;
import com.dssm.esc.model.entity.user.UserLoginObjEntity;
import com.dssm.esc.model.jsonparser.user.UpdataInfoParser;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.DownLoadManager;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.SystemBarTintManager;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.activity.EditIPActivity;
import com.dssm.esc.view.activity.MainActivity;
import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.exceptions.EaseMobException;

import net.tsz.afinal.FinalHttp;

import org.apache.http.cookie.Cookie;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登陆页面
 * (两种方式：第一种，前台把用户名和密码传给后台，后台就收到收据后再去登陆环信，返回给前台登陆状态；第二种:前台先把用户名和密码传给后台，若登陆成功
 * ，再去登陆环信，两次登陆都成功后才进入主界面)；注：登陆环信界面对用户不可见，此代码是第二种方式
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";


    public static final int REQUEST_CODE_SETNICK = 1;
    private EditText usernameEditText;
    private EditText passwordEditText;
    /**
     * 标题
     */
    private TextView title;
    private boolean progressShow;
    private boolean autoLogin = false;
    private ImageView select_address_img;//去选择地址界面的image

    private String currentUsername = "";// 用户名
    private String currentPassword = "";// 密码
    private String userId = "";// 用户id
    private String name = "";//用户姓名
    private String hxuserid = "";// 环信的用户名和密码
    private ProgressDialog pd;
    /**
     * 用户服务类
     */
    private UserSevice sevice;
    /**
     * 自定义工具类
     */
    private Utils utils;
    /**
     * 用户实体类
     */
    private UserEntity userEntity;
    /**
     * 用户角色
     */
    private String[] identity;
    /**
     * 用户角色id
     */
    private String[] rolesId;
    /**
     * 用户角色编号
     */
    private String[] roleCodes;
    /**
     * 保存用户信息
     */
    private MySharePreferencesService service = null;
    /**
     * 保存用户名和密码的map集合
     */
    private Map<String, String> map;
    /**
     * 4.4版本以上的沉浸式
     */
    protected SystemBarTintManager mTintManager;
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

    private final int UPDATA_NONEED = 0;

    private final int UPDATA_CLIENT = 1;

    private final int GET_UNDATAINFO_ERROR = 2;

    private final int SDCARD_NOMOUNTED = 3;

    private final int DOWN_ERROR = 4;

    private Button getVersion;

    private UpdataInfo info;
    private FinalHttp finalHttp;
    private String localVersion;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 11:
                    new Builder(LoginActivity.this)
                            .setTitle("请选择角色")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setSingleChoiceItems(identity, 0,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            final int which) {
                                            dialog.dismiss();
                                            Toast.makeText(LoginActivity.this,
                                                    "你选择了: " + identity[which], Toast.LENGTH_SHORT)
                                                    .show();

                                            // 选择角色
                                            sevice.loginRole(
                                                    rolesId[which],
                                                    new UserSeviceImpl.UserSeviceImplBackBooleanListenser() {

                                                        @Override
                                                        public void setUserSeviceImplListenser(
                                                                Boolean backflag,
                                                                String stRerror,
                                                                String Exceptionerror) {
                                                            String str = null;
                                                            if (backflag) {
                                                                str = stRerror;
                                                                // 被选中的角色id
                                                                selectedRolem = rolesId[which];
                                                                selectedRolemName = identity[which];
                                                                roleCode = roleCodes[which];
                                                                // service.save(selectedRolem);
                                                                userId = userEntity.getAttributes().getId();
                                                                name = userEntity.getAttributes().getName();
                                                                service.save(
                                                                        currentUsername,
                                                                        currentPassword,
                                                                        converToString(rolesId),
                                                                        converToString(identity),
                                                                        converToString(roleCodes),
                                                                        selectedRolem,
                                                                        userEntity
                                                                                .getAttributes()
                                                                                .getPostFlag(), userId, selectedRolemName, roleCode, name);
                                                                Log.i("LoginActivity被选中的角色id",
                                                                        selectedRolem);
                                                                Log.i("LoginActivity被选中的角色名称",
                                                                        selectedRolemName);
                                                                Log.i("LoginActivity被选中的角色编号",
                                                                        roleCode);
                                                                // 进入主页面
//															Intent intent = new Intent(
//																	LoginActivity.this,
//																	MainActivity.class);
//															startActivity(intent);
//
//															finish();
                                                                keepCookie();
                                                            } else if (stRerror != null) {
                                                                if (pd != null) {
                                                                    pd.dismiss();
                                                                }
                                                                str = stRerror;
                                                                ToastUtil
                                                                        .showLongToast(
                                                                                LoginActivity.this,
                                                                                str);
                                                            } else if (Exceptionerror != null) {
                                                                if (pd != null) {
                                                                    pd.dismiss();
                                                                }
                                                                str = Const.NETWORKERROR
                                                                        + Exceptionerror;
                                                                ToastUtil
                                                                        .showLongToast(
                                                                                LoginActivity.this,
                                                                                str);
                                                            }

                                                        }
                                                    });

                                        }

                                    }).setNegativeButton("取消", null).show();

                    break;
                case 12:
                    sevice.loginRole(rolesId[0],
                            new UserSeviceImpl.UserSeviceImplBackBooleanListenser() {

                                @Override
                                public void setUserSeviceImplListenser(
                                        Boolean backflag, String stRerror,
                                        String Exceptionerror) {
                                    String str = null;
                                    if (backflag) {
                                        str = "登陆成功";
//									// 进入主页面
//									Intent intent = new Intent(
//											LoginActivity.this,
//											MainActivity.class);
//									startActivity(intent);
//
//									finish();
                                        keepCookie();
                                    } else if (stRerror != null) {
                                        if (pd != null) {
                                            pd.dismiss();
                                        }
                                        str = stRerror;
                                        ToastUtil.showLongToast(LoginActivity.this,
                                                str);
                                    } else if (Exceptionerror != null) {
                                        if (pd != null) {
                                            pd.dismiss();
                                        }
                                        str = Const.NETWORKERROR + Exceptionerror;
                                        ToastUtil.showLongToast(LoginActivity.this,
                                                str);
                                    }

                                }
                            });

                    break;
                case UPDATA_NONEED:
//				ToastUtil.showToast(getApplicationContext(), "不需要更新");
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case UPDATA_CLIENT:
                    // 对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    // 服务器超时
                //    ToastUtil.showToast(getApplicationContext(), "获取服务器更新信息失败");
                    Intent intent2 = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
                case DOWN_ERROR:
                    // 下载apk失败
                    ToastUtil.showToast(getApplicationContext(), "下载新版本失败");
                    Intent intent3 = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent3);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 保持cookie一直有效
     */
    private void keepCookie() {
        Cookie appCookie = null;
        List<Cookie> cookies = MyCookieStore.cookieStore.getCookies();
        if (!cookies.isEmpty()) {
            for (int i = cookies.size(); i > 0; i--) {
                Cookie cookie = cookies.get(i - 1);
                if (cookie.getName().equalsIgnoreCase("jsessionid")) {
                    appCookie = cookie; // 使用一个常量来保存这个cookie，用于做session共享之用
                    MyCookieStore.setcookieStore(finalHttp);
                }
            }
        }
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        Cookie sessionCookie = appCookie;
        if (sessionCookie != null) {
            String cookieString = sessionCookie.getName() + "="
                    + sessionCookie.getValue() + "; domain="
                    + sessionCookie.getDomain();
            cookieManager.setCookie(DemoApplication.getInstance().getUrl() + HttpUrl.VERSIONUPDATE, cookieString);
            CookieSyncManager.getInstance().sync();
        }
        versionUpdate();
    }

    /***
     * 检测版本更新
     */
    private void versionUpdate() {
        // TODO Auto-generated method stub
        try {
            localVersion = getVersionName();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CheckVersionTask cv = new CheckVersionTask();

        new Thread(cv).start();
    }

    /**
     * 从服务器获取xml解析并进行比对版本号
     */
    public class CheckVersionTask implements Runnable {
        InputStream is;

        public void run() {
            try {
                URL url = new URL(DemoApplication.getInstance().getUrl() + HttpUrl.VERSIONUPDATE);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    is = conn.getInputStream();
                }
                info = UpdataInfoParser.getUpdataInfo(is);
                if (info.getVersion().equals(localVersion)) {
                    Log.i(TAG, "版本号相同");
                    Message msg = new Message();
                    msg.what = UPDATA_NONEED;
                    handler.sendMessage(msg);
                    // LoginMain();
                } else {
                    Log.i(TAG, "版本号不相同 ");
                    Message msg = new Message();
                    msg.what = UPDATA_CLIENT;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();

            }
        }
    }

    protected void showUpdataDialog() {
        Builder builer = new Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage(info.getDescription());
        // 当点确定按钮时从服务器上下载 新的apk 然后安装 װ
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "下载apk,更新");
                downLoadApk();
            }
        });
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                LoginMain();
//				loing();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.setCanceledOnTouchOutside(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
        //dialog.setCanceleable(false);调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
        dialog.show();

    }

	/*
     *
	 * 从服务器中下载APK
	 */

    protected void downLoadApk() {
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = DownLoadManager.getFileFromServer(
                            info.getUrl(), pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); // 结束掉进度条对话框

                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /*
     * 进入程序的主界面
     */
    private void LoginMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        // 结束掉当前的activity
        this.finish();
    }

    /*
     * 获取当前程序的版本号
     */
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
                0);
        return packInfo.versionName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBarState();
        View findViewById = findViewById(R.id.login);
        findViewById.setFitsSystemWindows(true);
        sevice = Control.getinstance().getUserSevice();
        finalHttp = Utils.getInstance().getFinalHttp();
        service = MySharePreferencesService.getInstance(this);
        // 回显
        map = service.getPreferences();
        utils = Utils.getInstance();
        title = (TextView) findViewById(R.id.tv_actionbar_title);
        select_address_img = (ImageView) findViewById(R.id.tv_actionbar_rightimag);
        select_address_img.setVisibility(View.VISIBLE);
        TextView tv_actionbar_editData = (TextView) findViewById(R.id.tv_actionbar_editData);
        tv_actionbar_editData.setVisibility(View.GONE);
        title.setText("登录");
        usernameEditText = (EditText) findViewById(R.id.login_name_et);
        passwordEditText = (EditText) findViewById(R.id.login_psw_et);
        Log.i("onFailure", "loginActivity");
        if (DemoHXSDKHelper.getInstance().isLogined()) {// 环信被登录过了就不用再登录了，只重新登陆下本地服务器
            currentUsername = usernameEditText.getText().toString().trim();
            if (currentUsername == null || currentUsername.equals(""))
                currentUsername = MySharePreferencesService.getInstance(getApplicationContext()).getcontectName("loginName");
            currentPassword = passwordEditText.getText().toString().trim();
            if (currentPassword == null || currentPassword.equals(""))
                currentPassword = MySharePreferencesService.getInstance(getApplicationContext()).getcontectName("password");
            Log.i("onFailure", MySharePreferencesService.getInstance(getApplicationContext()).getcontectName("loginName"));
            Log.i("onFailure", MySharePreferencesService.getInstance(getApplicationContext()).getcontectName("password"));
            sevice.login(currentUsername, currentPassword,
                    new UserSeviceImpl.UserSeviceImplListListenser() {
                        @Override
                        public void setUserSeviceImplListListenser(
                                Object object, String stRerror,
                                String Exceptionerror) {
                            // TODO Auto-generated method stub
                            String str = null;
                            // 若登陆成功，再访问环信服务器
                            if (object != null) {
                                str = "登陆成功";
                                if (object instanceof UserEntity) {
                                    userEntity = (UserEntity) object;
                                    str = userEntity.getSuccess() + ", " + userEntity.getObjString() + "," + userEntity.getMessage();
                                } else if (object instanceof Map)
                                    str = "Map";
                                Log.i("onFailure", str);
                                userEntity = (UserEntity) object;
                                if (userEntity.getSuccess().equals("true")) {
                                    userId = userEntity.getAttributes().getId();
                                    if(map.get("selectedRolem") != null && !map.get("selectedRolem").equals("")) {
                                        sevice.loginRole(map.get("selectedRolem"),
                                            new UserSeviceImpl.UserSeviceImplBackBooleanListenser() {
                                                @Override
                                                public void setUserSeviceImplListenser(Boolean backflag, String stRerror, String Exceptionerror) {
                                                    // TODO Auto-generated
                                                    // method stub
                                                    if (backflag) {
                                                        keepCookie();
                                                    } else if (backflag == false) {

                                                    } else if (stRerror != null) {
                                                        ToastUtil
                                                                .showLongToast(
                                                                        LoginActivity.this,
                                                                        stRerror);
                                                    } else if (Exceptionerror != null) {
                                                        ToastUtil.showLongToast(LoginActivity.this, Const.NETWORKERROR + Exceptionerror);
                                                    }
                                                }
                                        });
                                    }
                                    else
                                        userSelectRole();

                                } else if (userEntity.getSuccess().equals(
                                        "false")
                                        && userEntity.getObjString() != null
                                        && userEntity.getObjString()
                                        .equals("1")) {
                                    if (pd != null) {
                                        pd.dismiss();
                                    }
                                    ToastUtil.showLongToast(LoginActivity.this,
                                            userEntity.getMessage());
                                } else if (userEntity.getSuccess().equals(
                                        "false")
                                        && userEntity.getObjString() != null
                                        && userEntity.getObjString()
                                        .equals("2")) {
                                    if (pd != null) {
                                        pd.dismiss();
                                    }
                                    ToastUtil.showLongToast(LoginActivity.this,
                                            userEntity.getMessage());
                                } else {
                                    ToastUtil.showLongToast(LoginActivity.this, "登录失败");
                                }
                            } else if (stRerror != null) {
                                if (pd != null) {
                                    pd.dismiss();
                                }
                                str = stRerror;
                                ToastUtil
                                        .showLongToast(LoginActivity.this, str);
                            } else if (Exceptionerror != null) {
                                if (pd != null) {
                                    pd.dismiss();
                                }
                                str = Const.NETWORKERROR + Exceptionerror;
                                ToastUtil
                                        .showLongToast(LoginActivity.this, str);
                            }
                        }
                    });

            return;
        }

        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // if (DemoApplication.getInstance().getUserName() != null) {
        // usernameEditText.setText(DemoApplication.getInstance()
        // .getUserName());
        // }
        // 当退出登录的时候，可以显示用户名
        if (map.get("loginName") != null) {
            usernameEditText.setText(map.get("loginName"));
            usernameEditText.setSelection(map.get("loginName").length());//将光标移至文字末尾
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            usernameEditText.setText("");
//            passwordEditText.setText("");
            usernameEditText.requestFocus();
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

    public void editip(View view) {

        Intent intent = new Intent(LoginActivity.this, EditIPActivity.class);
        startActivityForResult(intent, 1);

    }

    /**
     * 登陆ESC服务器
     *
     * @param view
     */
    public void login(View view) {
//        Toast.makeText(this, DemoApplication.getInstance().getUrl()+HttpUrl.LOGIN,
//                Toast.LENGTH_LONG).show();
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        currentUsername = usernameEditText.getText().toString().trim();
        currentPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        progressShow = true;
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();

        final long start = System.currentTimeMillis();

        // 先访问ESC服务器

        sevice.login(currentUsername, currentPassword,
                new UserSeviceImpl.UserSeviceImplListListenser() {

                    @Override
                    public void setUserSeviceImplListListenser(Object object,
                                                               String stRerror, String Exceptionerror) {
                        // TODO Auto-generated method stub
                        String str = null;
                        // 若登陆成功，再访问环信服务器
                        if (object != null) {
                            str = "登陆成功";
                            userEntity = (UserEntity) object;
                            if (userEntity.getSuccess().equals("true")) {
                                // 把户名和密码角色保存到PreferencesService中

                                Log.i("LoginActivity角色", map.get("roleNames"));
                                Log.i("LoginActivity用户名", map.get("loginName"));
                                Log.i("LoginActivity密码", map.get("password"));
                                userId = userEntity.getAttributes().getId();
                                // 先去环信服务器注册（环信的用户名唯一，所以要用唯一标识去注册，这里用userID当环信的用户名和密码，即使本地的用户名和密码改变了也不用修改）
                                hxuserid = userId.replace("-", "_");
                                register(hxuserid, hxuserid);

                            } else if (userEntity.getSuccess().equals("false")
                                    && userEntity.getObjString() != null
                                    && userEntity.getObjString().equals("1")) {
                                if (pd != null) {
                                    pd.dismiss();
                                }
                                ToastUtil.showLongToast(LoginActivity.this,
                                        userEntity.getMessage());
                            } else if (userEntity.getSuccess().equals("false")
                                    && userEntity.getObjString() != null
                                    && userEntity.getObjString().equals("2")) {
                                if (pd != null) {
                                    pd.dismiss();
                                }
                                ToastUtil.showLongToast(LoginActivity.this,
                                        userEntity.getMessage());
                            } else {
                                if (pd != null) {
                                    pd.dismiss();
                                }
                                ToastUtil.showLongToast(LoginActivity.this, "登录失败");
                            }
                        } else if (stRerror != null) {
                            if (pd != null) {
                                pd.dismiss();
                            }
                            str = stRerror;
                            ToastUtil.showLongToast(LoginActivity.this, str);
                        } else if (Exceptionerror != null) {
                            if (pd != null) {
                                pd.dismiss();
                            }
                            str = Const.NETWORKERROR + Exceptionerror;
                            //ToastUtil.showLongToast(LoginActivity.this, str);
                            ToastUtil.showLongToast(LoginActivity.this, "网络连接超时，请检查网络设置或IP地址是否正确");
                        }

                    }
                });

    }

    private void initializeContacts() {
        Map<String, User> userlist = new HashMap<String, User>();
        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        String strGroup = getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 添加"Robot"
        User robotUser = new User();
        String strRobot = getResources().getString(R.string.robot_chat);
        robotUser.setUsername(Constant.CHAT_ROBOT);
        robotUser.setNick(strRobot);
        robotUser.setHeader("");
        userlist.put(Constant.CHAT_ROBOT, robotUser);

        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(LoginActivity.this);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
    }

    /**
     * 注册(环信)
     */
    private void register(final String hxuserid, final String pwd) {
        if (!TextUtils.isEmpty(hxuserid) && !TextUtils.isEmpty(pwd)) {
            final ProgressDialog pd = new ProgressDialog(this);
            // 正在注册的弹出框
            // pd.setMessage(getResources().getString(R.string.Is_the_registered));
            // pd.show();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        // 调用sdk注册方法
                        EMChatManager.getInstance().createAccountOnServer(
                                hxuserid, pwd);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!LoginActivity.this.isFinishing())
                                    pd.dismiss();
                                // 保存用户名
                                DemoApplication.getInstance().setUserName(
                                        hxuserid);// 环信保存的是用户id

                                // 注册成功后，调用sdk登陆方法登陆聊天服务器(登录也用用户id去登陆)
                                login(hxuserid, hxuserid);
                            }
                        });
                    } catch (final EaseMobException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!LoginActivity.this.isFinishing())
                                    pd.dismiss();
                                int errorCode = e.getErrorCode();
                                if (errorCode == EMError.NONETWORK_ERROR) {// 无网络
                                    Toast.makeText(
                                            getApplicationContext(),
                                            getResources().getString(
                                                    R.string.network_anomalies),
                                            Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ALREADY_EXISTS) {// 如果用户已存在，直接登陆
                                    login(hxuserid, hxuserid);
                                } else if (errorCode == EMError.UNAUTHORIZED) {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            getResources()
                                                    .getString(
                                                            R.string.registration_failed_without_permission),
                                            Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.ILLEGAL_USER_NAME) {// 用户名不合法
                                    Toast.makeText(
                                            getApplicationContext(),
                                            getResources().getString(
                                                    R.string.illegal_user_name),
                                            Toast.LENGTH_SHORT).show();
                                } else {// 注册失败
                                    Toast.makeText(
                                            getApplicationContext(),
                                            getResources()
                                                    .getString(
                                                            R.string.Registration_failed)
                                                    + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();

        }

    }

    /**
     * 登陆(环信)
     */
    private void login(final String hxuserid, String pwd) {
        EMChatManager.getInstance().login(hxuserid, hxuserid, new EMCallBack() {

            @Override
            public void onSuccess() {
                if (!progressShow) {
                    return;
                }
                // 登陆成功，保存用户名密码
                DemoApplication.getInstance().setUserName(hxuserid);
                DemoApplication.getInstance().setPassword(hxuserid);
                Log.i("LoginActivity用户名--环信", DemoApplication.getInstance()
                        .getUserName());
                Log.i("LoginActivity密码--环信", DemoApplication.getInstance()
                        .getPassword());
                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();

                    // 处理好友和群组
                    initializeContacts();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            DemoHXSDKHelper.getInstance().logout(true, null);
                            Toast.makeText(getApplicationContext(),
                                    R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                // 更新当前用户的nickname
                // 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance()
                        .updateCurrentUserNick(
                                DemoApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                userSelectRole();

            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }

    /**
     * 用户选择角色 不管是一个角色还是多个角色，都要把角色id传给服务器
     */
    private void userSelectRole() {
        List<UserLoginObjEntity> obj = userEntity.getObj();
        identity = new String[obj.size()];
        rolesId = new String[obj.size()];
        roleCodes = new String[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            identity[i] = obj.get(i).getRoleName();
            rolesId[i] = obj.get(i).getRoleId();
            roleCodes[i] = obj.get(i).getRoleCode();
        }
        if (obj.size() == 1) {// 有一个角色
            selectedRolem = rolesId[0];
            selectedRolemName = identity[0];
            roleCode = roleCodes[0];
            userId = userEntity.getAttributes().getId();
            name = userEntity.getAttributes().getName();
            service.save(currentUsername, currentPassword, selectedRolem, selectedRolemName, roleCode, selectedRolem, userEntity.getAttributes()
                    .getPostFlag(), userId, selectedRolemName, roleCode, name);
            Message message = new Message();
            message.what = 12;
            handler.sendMessage(message);

        } else if (obj.size() > 1) {// 有多个角色
            Message message = new Message();
            message.what = 11;
            handler.sendMessage(message);

        }
    }

    /**
     * 沉浸式（api19）
     *
     * @param on
     * @version 1.0
     * @createTime 2015-9-17,上午11:05:15
     * @updateTime 2015-9-17,上午11:05:15
     * @createAuthor Zsj
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
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

    /**
     * @Description:把数组转换为一个用逗号分隔的字符串 ，以便于用in+String 查询
     */
    private String converToString(String[] ig) {
        String str = "";
        if (ig != null && ig.length > 0) {
            for (int i = 0; i < ig.length; i++) {
                str += ig[i] + ",";
            }
        }
        str = str.substring(0, str.length() - 1);
        return str;
    }
}
