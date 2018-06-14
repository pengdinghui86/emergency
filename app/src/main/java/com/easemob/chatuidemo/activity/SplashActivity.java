package com.easemob.chatuidemo.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.entity.user.UpdataInfo;
import com.dssm.esc.model.entity.user.UserEntity;
import com.dssm.esc.model.jsonparser.user.UpdataInfoParser;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.DownLoadManager;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.event.PushMessageEvent;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.MainActivity;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * 开屏页
 * 
 */
public class SplashActivity extends BaseActivity {

	private static final int sleepTime = 2000;
	// 用户服务类
	private UserSevice sevice;
	private MySharePreferencesService service;
	// /** 当前被选中的角色 */
	// private String selectedRolem;
	// /** 用户实体类 */
	// private UserEntity userEntity;
	public Map<String, String> map;
	private final String TAG = this.getClass().getName();

	private final int UPDATA_NONEED = 0;

	private final int UPDATA_CLIENT = 1;

	private final int GET_UNDATAINFO_ERROR = 2;

	private final int SDCARD_NOMOUNTED = 3;

	private final int DOWN_ERROR = 4;

	private Button getVersion;

	private UpdataInfo info;
	private String localVersion;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 11:// （如果已经登录过）每次进入都要访问登录接口，看本地保存的用户名和密码是否和服务器上的一致
				// 回显
				map = service.getPreferences();
				sevice.login(map.get("loginName"), map.get("password"),
						new UserSeviceImpl.UserSeviceImplListListenser() {

							@Override
							public void setUserSeviceImplListListenser(
									Object object, String stRerror,
									String Exceptionerror) {
								// TODO Auto-generated method stub
								String str;
								// 若登陆成功，直接进入主界面
								if (object != null) {
									UserEntity userEntity = (UserEntity) object;
									if (userEntity.getSuccess().equals("true")) {
										str = "登陆成功";
										sevice.loginRole(
												map.get("selectedRolem"),
												new UserSeviceImpl.UserSeviceImplBackBooleanListenser() {
													@Override
													public void setUserSeviceImplListenser(
															Boolean backflag,
															String stRerror,
															String Exceptionerror) {
														// TODO Auto-generated
														// method stub
														if (backflag) {
															versionUpdate();

														} else if (backflag == false) {

														} else if (stRerror != null) {

															ToastUtil
																	.showLongToast(
																			SplashActivity.this,
																			stRerror);
														} else if (Exceptionerror != null) {

															ToastUtil
																	.showLongToast(
																			SplashActivity.this,
																			"网络连接超时，请检查网络设置或IP地址是否正确");
														}
													}

												});

									} else {
										str = "密码已失效,请重新登陆";
										ToastUtil.showLongToast(
												SplashActivity.this, str);
										Intent intent = new Intent(
												SplashActivity.this,
												LoginActivity.class);
										startActivity(intent);
										finish();
									}

								} else if (stRerror != null) {

									str = stRerror;
									ToastUtil.showLongToast(
											SplashActivity.this, str);
									Intent intent = new Intent(
											SplashActivity.this,
											LoginActivity.class);
									startActivity(intent);
									finish();
								} else if (Exceptionerror != null) {

									str = Const.NETWORKERROR + Exceptionerror;
									ToastUtil.showLongToast(
											SplashActivity.this, str);
									Intent intent = new Intent(
											SplashActivity.this,
											LoginActivity.class);
									startActivity(intent);
									finish();
								}

							}
						});

				break;
			case UPDATA_NONEED:
//				ToastUtil.showToast(getApplicationContext(), "不需要更新");
				Intent intent = new Intent(SplashActivity.this,
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
			//	ToastUtil.showToast(getApplicationContext(), "获取服务器更新信息失败");
				Intent intent2 = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(intent2);
				break;
			case DOWN_ERROR:
				// 下载apk失败
				ToastUtil.showToast(getApplicationContext(), "下载新版本失败");
				Intent intent3 = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(intent3);
				break;

			}
		};
	};

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
				URL url = new URL(DemoApplication.getInstance().getUrl()+HttpUrl.VERSIONUPDATE);
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
					 LoginMain();
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
//		dialog.setCancelable(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
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
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
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
	protected void onCreate(Bundle arg0) {
		// setContentView(R.layout.activity_splash);
		setContentView(R.layout.activity_welcome);
		super.onCreate(arg0);
		sevice = Control.getinstance().getUserSevice();
		service = MySharePreferencesService.getInstance(this);
		Intent intent = getIntent();
		if(intent != null) {
			String flag = intent.getStringExtra("mainActivity");
			String msgType = intent.getStringExtra("msgType");
			Log.i("onFailure", "mainActivity status: " + flag);
			Log.i("onFailure", "msgType: " + msgType);
			if(msgType != null && !msgType.equals(""))
			{
				//MainActivity发送通知，让其显示MessageFragment界面
				EventBus.getDefault().post(new mainEvent("t"));
				//给MessageFragment发送通知
				EventBus.getDefault().post(new PushMessageEvent(Integer.parseInt(msgType)));
			}
			if(flag != null && flag.equals("live"))
				finish();
		}
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		loing();
		Log.i("===onCreate()===", "onCreate()");
	}

	@Override
	protected void onStart() {
		super.onStart();
//		loing();
		Log.i("===SplashonStart()===", "onStart()");
	}

	private void loing() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				if (DemoHXSDKHelper.getInstance().isLogined()) {
					// ** 免登陆情况 加载所有本地群和会话
					// 不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
					// 加上的话保证进了主页面会话和群组都已经load完毕
					long start = System.currentTimeMillis();
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					// 等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Message message = new Message();
					message.what = 11;
					handler.sendMessage(message);
					// Intent intent = new Intent(
					// SplashActivity.this,
					// MainActivity.class);
					// startActivity(intent);

				} else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
					finish();
				}
			}
		}).start();

	}

	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		String st = getResources().getString(R.string.Version_number_is_wrong);
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packinfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return st;
		}
	}

}
