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
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.SystemBarTintManager;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.activity.EditIPActivity;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.adapter.PopRoleSelectListviewAdapter;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.model.DeviceInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * 登录页面
 * (两种方式：第一种，前台把用户名和密码传给后台，后台就收到收据后再去登录环信，返回给前台登录状态；第二种:前台先把用户名和密码传给后台，若登录成功
 * ，再去登录IM，两次登录都成功后才进入主界面)；注：登录IM对用户不可见，此代码是第二种方式
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
     * 被选中的用户角色
     */
    private UserLoginObjEntity userLoginObjEntity = null;
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

    private final int DOWN_ERROR = 4;

    private UpdataInfo info;
    private String localVersion;

    private int curWhich;

    private PopRoleSelectListviewAdapter adapter;
    private PopupWindow popupWindow;
    private int from = 0;

    private UserSeviceImpl.UserSeviceImplBackBooleanListenser loginRoleListener = new UserSeviceImpl.UserSeviceImplBackBooleanListenser() {

        @Override
        public void setUserSeviceImplListenser(
                Boolean backflag,
                String stRerror,
                String Exceptionerror) {
            String str = null;
            if (backflag) {
                str = stRerror;
                // 被选中的角色id
                selectedRolem = userLoginObjEntity.getRoleId();
                selectedRolemName = userLoginObjEntity.getRoleName();
                roleCode = userLoginObjEntity.getRoleCode();
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
                versionUpdate();
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
    };

    private UserSeviceImpl.UserSeviceImplBackBooleanListenser loginRoleListener2 = new UserSeviceImpl.UserSeviceImplBackBooleanListenser() {

        @Override
        public void setUserSeviceImplListenser(
                Boolean backflag, String stRerror,
                String Exceptionerror) {
            String str = null;
            if (backflag) {
                versionUpdate();
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
                str = Const.NETWORKERROR;
                ToastUtil.showLongToast(LoginActivity.this,
                        str);
            }
        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 11:
                    initPopupWindow();
                    break;
                case 12:
                    Utils.getInstance().showProgressDialog(LoginActivity.this, "",
                            Const.LOAD_MESSAGE);
                    sevice.loginRole(rolesId[0], loginRoleListener2);
                    break;
                case UPDATA_NONEED:
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
    * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    */
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    protected void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.pop_select_role, null);
        //内容，高度，宽度           
        popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        //菜单背景色    
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_login, null),
        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置背景半透明     
        backgroundAlpha(0.3f);
        //关闭事件   
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        ListView listView = popupWindowView.findViewById(R.id.pop_select_role_lv);
        TextView textView = popupWindowView.findViewById(R.id.pop_select_role_tv_cancel);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        adapter = new PopRoleSelectListviewAdapter(this, userEntity.getObj());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userLoginObjEntity = userEntity.getObj().get(i);
                popupWindow.dismiss();
                if(LoginActivity.this != null && !LoginActivity.this.isFinishing())
                    Utils.getInstance().showProgressDialog(LoginActivity.this, "",
                        "你选择了: " + userLoginObjEntity.getRoleName());
                // 选择角色
                sevice.loginRole(
                        userLoginObjEntity.getRoleId(), loginRoleListener);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度    
     * @param bgAlpha
    */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = LoginActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        LoginActivity.this.getWindow().setAttributes(lp);
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
            }
        });
        AlertDialog dialog = builer.create();
        dialog.setCanceledOnTouchOutside(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
        dialog.show();
    }

	/**
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

    /**
     * 进入程序的主界面
     */
    private void LoginMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
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

    private UserSeviceImpl.UserSeviceImplListListenser listener = new UserSeviceImpl.UserSeviceImplListListenser() {
        @Override
        public void setUserSeviceImplListListenser(
                Object object, String stRerror,
                String Exceptionerror) {
            // TODO Auto-generated method stub
            String str = null;
            // 若登录成功，再访问IM服务器
            if (object != null) {
                str = "登录成功";
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
                        sevice.loginRole(map.get("selectedRolem"), loginRoleListener2);
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
                str = Const.NETWORKERROR;
                ToastUtil
                        .showLongToast(LoginActivity.this, str);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View findViewById = findViewById(R.id.login);
        findViewById.setFitsSystemWindows(true);
        sevice = Control.getinstance().getUserSevice();
        service = MySharePreferencesService.getInstance(getApplicationContext());
        // 回显
        map = service.getPreferences();
        utils = Utils.getInstance();
        title = (TextView) findViewById(R.id.tv_actionbar_title);
        select_address_img = (ImageView) findViewById(R.id.tv_actionbar_rightimag);
        select_address_img.setVisibility(View.VISIBLE);
        title.setText("登录");
        usernameEditText = (EditText) findViewById(R.id.login_name_et);
        passwordEditText = (EditText) findViewById(R.id.login_psw_et);
        Log.i("onFailure", "loginActivity");
        /**获取个人信息不是null，说明已经登陆，
         * 无需再次登陆
         * */
        UserInfo myInfo = JMessageClient.getMyInfo();
        if (myInfo != null) {
            currentUsername = usernameEditText.getText().toString().trim();
            if (currentUsername == null || currentUsername.equals(""))
                currentUsername = MySharePreferencesService.getInstance(getApplicationContext()).getcontectName("loginName");
            currentPassword = passwordEditText.getText().toString().trim();
            if (currentPassword == null || currentPassword.equals(""))
                currentPassword = MySharePreferencesService.getInstance(getApplicationContext()).getcontectName("password");
            Log.i("onFailure", MySharePreferencesService.getInstance(getApplicationContext()).getcontectName("loginName"));
            Log.i("onFailure", MySharePreferencesService.getInstance(getApplicationContext()).getcontectName("password"));
            sevice.login(currentUsername, currentPassword, listener);
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

        // 当退出登录的时候，可以显示用户名
        if (map.get("loginName") != null) {
            usernameEditText.setText(map.get("loginName"));
            usernameEditText.setSelection(map.get("loginName").length());//将光标移至文字末尾
        }
        //防止输入框将布局顶上去
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            usernameEditText.requestFocus();
        }
    }

    public void editip(View view) {
        Intent intent = new Intent(LoginActivity.this, EditIPActivity.class);
        startActivityForResult(intent, 1);
    }

    private UserSeviceImpl.UserSeviceImplListListenser listListener = new UserSeviceImpl.UserSeviceImplListListenser() {

        @Override
        public void setUserSeviceImplListListenser(Object object,
                String stRerror, String Exceptionerror) {
            // TODO Auto-generated method stub
            String str = null;
            // 若登录成功，再访问IM服务器
            if (object != null) {
                str = "登录成功";
                userEntity = (UserEntity) object;
                if (userEntity.getSuccess().equals("true")) {
                    userId = userEntity.getAttributes().getId();
                    // 先去极光IM服务器注册（极光IM的用户名唯一，所以要用唯一标识去注册，这里用userID当极光IM的用户名和密码，即使本地的用户名和密码改变了也不用修改）
                    hxuserid = userId.replace("-", "_");
                    register(hxuserid, hxuserid, userEntity.getAttributes().getName());

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
                ToastUtil.showLongToast(LoginActivity.this, "网络连接超时，请检查网络设置或IP地址是否正确");
            }

        }
    };

    /**
     * 登录ESC服务器
     * @param view
     */
    public void login(View view) {
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
        sevice.login(currentUsername, currentPassword, listListener);
    }

    /**
     * 注册(IM)
     */
    private void register(final String hxuserid, final String pwd, final String name) {
        if (!TextUtils.isEmpty(hxuserid) && !TextUtils.isEmpty(pwd)) {
            final ProgressDialog pd = new ProgressDialog(this);
            // 正在注册的弹出框
            // pd.setMessage(getResources().getString(R.string.Is_the_registered));
            // pd.show();
            final RegisterOptionalUserInfo registerOptionalUserInfo = new RegisterOptionalUserInfo();
            registerOptionalUserInfo.setNickname(name);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        // 调用sdk注册方法
                        JMessageClient.register(hxuserid, pwd, registerOptionalUserInfo, new BasicCallback() {
                            @Override
                            public void gotResult(int responseCode, final String responseMessage) {
                                //responseCode - 0 表示正常。大于 0 表示异常，responseMessage 会有进一步的异常信息。
                                //responseMessage - 一般异常时会有进一步的信息提示。
                                if(responseCode == 0)
                                {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (!LoginActivity.this.isFinishing())
                                                pd.dismiss();
                                            // 注册成功后，调用sdk登录方法登录聊天服务器
                                            login(hxuserid, pwd);
                                        }
                                    });
                                }
                                //已注册
                                else if(responseCode == 898001)
                                {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (!LoginActivity.this.isFinishing())
                                                pd.dismiss();
                                            // 注册成功后，调用sdk登录方法登录聊天服务器
                                            login(hxuserid, pwd);
                                        }
                                    });
                                }
                                else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (!LoginActivity.this.isFinishing())
                                                pd.dismiss();
                                            Toast.makeText(getApplicationContext(), responseMessage,
                                                        Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

                    } catch (final Exception e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!LoginActivity.this.isFinishing())
                                    pd.dismiss();
                                Toast.makeText(getApplicationContext(), e.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }
    }

    /**
     * 登录(IM)
     */
    private void login(final String hxuserid, final String pwd) {
        RequestCallback<List<DeviceInfo>> callback = new RequestCallback<List<DeviceInfo>>() {
            @Override
            public void gotResult(int responseCode, final String responseMessage, List<DeviceInfo> deviceInfos) {
                //登录成功
                if(responseCode == 0)
                {
                    if (!progressShow) {
                        return;
                    }
                    try {
                        userSelectRole();
                        //如果当前用户昵称为空则更新
                        if("".equals(JMessageClient.getMyInfo().getNickname())) {
                            String nickName = MySharePreferencesService.getInstance(getApplicationContext()).getcontectName("name");
                            UserInfo userInfo = JMessageClient.getMyInfo();
                            userInfo.setNickname(nickName);
                            JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {

                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 取好友或者群聊失败，不让进入主页面
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                                JMessageClient.logout();
                                Toast.makeText(getApplicationContext(),
                                        R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                        pd.dismiss();
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            JMessageClient.logout();
                            Toast.makeText(getApplicationContext(),
                                    responseMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        JMessageClient.login(hxuserid, pwd, callback);
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
