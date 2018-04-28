package com.dssm.esc.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.ControlSevice;
import com.dssm.esc.model.analytical.EmergencyService;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl.UserSeviceImplListListenser;
import com.dssm.esc.model.entity.user.UserEntity;
import com.dssm.esc.util.ActivityCollector;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.MyCookieStore;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.SystemBarTintManager;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;

import net.tsz.afinal.FinalActivity;

import java.util.Map;

import de.greenrobot.event.EventBus;
import com.easemob.chatuidemo.activity.LoginActivity;

/**
 * 基类Activity
 * 
 * @Description 
 *              TODO所有的Activity都继承自此Activity，并实现基类模板方法，本类的目的是为了规范团队开发项目时候的开发流程的命名
 *              ， 基类也用来处理需要集中分发处理的事件、广播、动画等，如开发过程中有发现任何改进方案都可以一起沟通改进。
 * @author Zsj
 * @date 2015-9-17
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class BaseActivity extends FinalActivity {
	/**
	 * 用户模块接口
	 */
	protected UserSevice usevice;
	/**
	 * 应急管理模块接口
	 */
	protected EmergencyService esevice;
	/**
	 * 控制中心模块接口
	 */
	protected ControlSevice csevice;
	/** 4.4版本以上的沉浸式 */
	protected SystemBarTintManager mTintManager;
	public Context context;
	// // 账号在别处登录
	// public static boolean isConflict = false;
	// // 账号被移除
	// private static boolean isCurrentAccountRemoved = false;
	public onInitNetListener netListener;

	protected Map<String, String> map;
	protected String reloginString = "";
	MySharePreferencesService service;

	/**
	 * 重新登录
	 */
	public void relogin() {
		map = service.getPreferences();
		usevice.relogin(map.get("loginName"), map.get("password"),
				map.get("selectedRolem"), new UserSeviceImplListListenser() {

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
								str = "BaseActivity重新登陆成功";
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

	public void setNetListener(onInitNetListener netListener) {
		this.netListener = netListener;
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStatusBarState();
		//Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程
		if (MyCookieStore.cookieStore != null) {
			usevice = Control.getinstance().getUserSevice();
			esevice = Control.getinstance().getEmergencyService();
			csevice = Control.getinstance().getControlSevice();
			service = MySharePreferencesService.getInstance(context);

			if (useEventBus()) {
				EventBus.getDefault().register(this);
			}
			// if (savedInstanceState != null
			// && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
			// false)) {
			// // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// // 三个fragment里加的判断同理
			// DemoHXSDKHelper.getInstance().logout(true, null);
			// finish();
			// startActivity(new Intent(this, LoginActivity.class));
			// return;
			// } else if (savedInstanceState != null
			// && savedInstanceState.getBoolean("isConflict", false)) {
			// // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// // 三个fragment里加的判断同理
			// finish();
			// startActivity(new Intent(this, LoginActivity.class));
			// return;
			// }

		} else {
			relogin();
		}

		context = getApplicationContext();
	}

	/**
	 * 
	 * 沉浸式（api19）
	 * 
	 * @version 1.0
	 * @createTime 2015-9-17,上午11:05:15
	 * @updateTime 2015-9-17,上午11:05:15
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
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

	protected boolean useEventBus() {
		return false;
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
	 * 
	 * 隐藏键盘
	 * 
	 * @version 1.0
	 * @createTime 2015-9-16,下午7:51:54
	 * @updateTime 2015-9-16,下午7:51:54
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	public void closeKeyboard() {
		try {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getApplicationWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus()
						.getApplicationWindowToken(), 0);
			}
		} catch (Exception e) {
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (useEventBus()) {
			EventBus.getDefault().unregister(this);
		}
		super.onDestroy();
		ActivityCollector.removeActivity(this);
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

	interface onInitNetListener {
		/** 访问网络的抽象方法 */
		void initNetData();
	}

	/*// 创建服务用于捕获崩溃异常
	private UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {
		public void uncaughtException(Thread thread, Throwable ex) {
			restartApp();// 发生崩溃异常时,重启应用
		}
	};

	private void restartApp() {
		// relogin();
		Intent intent = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());

		PendingIntent restartIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		// 退出程序
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
				restartIntent); // 1秒钟后重启应用
		Log.i("我是BaseActivity程序捕获异常", "");
		System.exit(0);

	}*/
}
