package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.event.Emergenct;
import com.dssm.esc.util.event.MessageCountEvent;
import com.dssm.esc.util.event.My;
import com.dssm.esc.util.event.PushMessageEvent;
import com.dssm.esc.util.event.System;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.widget.RedPointView;
import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.ChatAllHistoryFragment;
import com.easemob.chatuidemo.activity.LoginActivity;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * 消息
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-6
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class MessageFragment extends BaseFragment implements OnClickListener {

	/** 标题 */
	// private TextView title;
	/** 可选的button */
	// private SegmentControl mSegmentControl;
	/** 任务通知碎片 */
	private MessageTaskToastFragment messageTaskToastFragment;
	/** 系统通知碎片 */
	private MessageSystemToastFragment systemToastFragment;
	/** 紧急通知碎片 */
	private MessageEmergencyToastFragment emergencyToastFragment;
	/** 个人消息碎片 */
	private MessageMyMessagesFragment myMessagesFragment;
	/** 即时（环信） */
	private ChatAllHistoryFragment chatHistoryFragment;
	/** 用于区分消息 0,任务通知；1，系统通知；2，紧急通知；3，我的消息 */
	public int tag = 0;
	private Context context;
	private RadioButton rb_task, rb_system, rb_emergency, rb_mymessage;
	/**
	 * 每个用户的每个角色的三张表：任务，系统，紧急,个人
	 */
	private String table1 = "";
	private String table2 = "";
	private String table3 = "";
	private String table4 = "";

	private RedPointView redPointView1;
	private RedPointView redPointView2;
	private RedPointView redPointView3;
	private RedPointView redPointView4;
	private RedPointView redPointView5;

	private ImageView setting;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;

	private ProgressDialog pd;
	// 用户角色
	private String[] identity;
	// 用户角色id
	private String[] rolesId;
	/** 用户角色编号 */
	private String[] roleCodes;
	/** 保存用户信息 */
	private MySharePreferencesService service;
	/** 保存用户名和密码的map集合 */
	private Map<String, String> map;
	/** 当前被选中的角色id */
	private String selectedRolem;
	/** 当前被选中的角色名称 */
	private String selectedRolemName;
	/** 当前被选中的角色编号 */
	private String roleCode;
	private String loginName;
	private String name;// 用户姓名
	private UserSevice userSevice;
	private ImageView message;
	private TextView t1, t2;

	private DrawerLayout mDrawerLayout;
	private RelativeLayout mRelativeLayout;

	public MessageFragment() {

	}
	@SuppressLint("ValidFragment")
	public MessageFragment(Context context, String table1, String table2,
			String table3, String table4) {
		if (context == null) {
			this.context = getActivity();

		} else {
			this.context = context;
		}
		this.table1 = table1;
		this.table2 = table2;
		this.table3 = table3;
		this.table4 = table4;
	}

	@Override
	protected View getViews() {
		Log.i("context", context + "");
		return view_Parent = LayoutInflater.from(context).inflate(
				R.layout.fragment_message, null);
	}

	@Override
	protected void findViews() {
		initPop();

		mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.id_drawerlayout);
		mRelativeLayout= (RelativeLayout) getActivity().findViewById(R.id.nav_view);

		message = (ImageView) view_Parent.findViewById(R.id.message);
		setting = (ImageView) view_Parent.findViewById(R.id.setting);
		rb_task = (RadioButton) view_Parent.findViewById(R.id.rb_task);
		rb_system = (RadioButton) view_Parent.findViewById(R.id.rb_system);
		rb_emergency = (RadioButton) view_Parent
				.findViewById(R.id.rb_emergency);
		rb_mymessage = (RadioButton) view_Parent
				.findViewById(R.id.rb_mymessage);
	}

	@Override
	protected void widgetListener() {
		rb_task.setOnClickListener(this);
		rb_system.setOnClickListener(this);
		rb_emergency.setOnClickListener(this);
		rb_mymessage.setOnClickListener(this);
		setting.setOnClickListener(this);
		message.setOnClickListener(this);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		// 初始化，默认加载任务通知界面

		userSevice = Control.getinstance().getUserSevice();
		service = MySharePreferencesService.getInstance(context);
		// 回显
		map = service.getPreferences();
		Log.i("角色", map.get("roleNames"));
		selectedRolem = map.get("selectedRolem");
		selectedRolemName = map.get("selectedRolemName");
		identity = convertStrToArray(map.get("roleNames"));
		rolesId = convertStrToArray(map.get("roleIds"));
		roleCodes = convertStrToArray(map.get("roleCodes"));
		loginName = map.get("loginName");
		name = map.get("name");
	}

	@Override
	public void initGetData() {
		// TODO Auto-generated method stub
		redPointView1 = remind(rb_task, ((MainActivity) getActivity()).xgTaskMsgCount + "");
		redPointView2 = remind(rb_system, ((MainActivity) getActivity()).xgSysMsgCount + "");
		redPointView3 = remind(rb_emergency, ((MainActivity) getActivity()).xgEmergencyMsgCount + "");
		redPointView4 = remind(rb_mymessage, ((MainActivity) getActivity()).xgPersonalMsgCount + "");
		redPointView5 = remind(message, "");
		Log.i("onFailure", "MessageFragment: " + tag);
		switchView(tag);
		int position = 0;
		switch (tag) {
			case 0:
				position = 1;
				break;
			case 1:
				position = 2;
				break;
			case 2:
				position = 4;
				break;
			case 3:
				position = 3;
				break;
		}
		onEvent(new PushMessageEvent(position));
	}

	/**
	 * RedPointView具体的使用
	 * 
	 * @param view
	 */
	public RedPointView remind(View view, String count) {
		RedPointView redPointView = new RedPointView(getActivity(), view);
		LayoutParams lP = (LayoutParams) new LayoutParams(
				20, 20);
		redPointView.setLayoutParams(lP);
		redPointView.setText(count);// 需要显示的提醒类容
		redPointView.setPosition(0, Gravity.RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
		redPointView.setTextSize(12); // 文本大小
		// redPointView.show();// 只有显示
		redPointView.hide();// 先默认隐藏
		return redPointView;
	}

	

	/**
	 * 选择界面
	 * 
	 * @version 1.0
	 * @createTime 2015-8-12,下午3:20:06
	 * @updateTime 2015-8-12,下午3:20:06
	 * @createAuthor XiaoHuan
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param position
	 */
	public void switchView(int position) {
		Log.i("onFailure", "switchView: " + position);
		// 获取Fragment的操作对象
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		hideFragment(transaction);
		switch (position) {
		case 0:// 添加 并展示 任务通知 碎片
			if (messageTaskToastFragment == null) {
				messageTaskToastFragment = new MessageTaskToastFragment(
						getActivity(), table1);
				transaction.add(R.id.view_message, messageTaskToastFragment);

				// messageTaskToastFragment.initGetData();
			} else {
				transaction.show(messageTaskToastFragment);
				// messageTaskToastFragment.initGetData();
			}
			// messageTaskToastFragment.loadData(0);
			break;
		case 1:// 添加 并展示 系统通知 碎片
			if (systemToastFragment == null) {
				systemToastFragment = new MessageSystemToastFragment(
						getActivity(), table2);
				transaction.add(R.id.view_message, systemToastFragment);
				// systemToastFragment.initGetData();
			} else {
				transaction.show(systemToastFragment);
			}
			break;

		case 2:// 添加 并展示 紧急通知 碎片
			if (emergencyToastFragment == null) {
				emergencyToastFragment = new MessageEmergencyToastFragment(
						getActivity(), table3);
				transaction.add(R.id.view_message, emergencyToastFragment);
				// systemToastFragment.initGetData();
			} else {
				transaction.show(emergencyToastFragment);
				// systemToastFragment.initGetData();
			}
			// systemToastFragment.loadData(0);
			break;

		case 3:// 添加 并展示我的消息碎片
				// 显示所有人消息记录的fragment
			if (myMessagesFragment == null) {
				myMessagesFragment = new MessageMyMessagesFragment(
						getActivity(), table4);
				transaction.add(R.id.view_message, myMessagesFragment);
			} else {
				transaction.show(myMessagesFragment);
				// myMessagesFragment.initGetData();
			}
			// myMessagesFragment.loadData(0);
			break;
		case 4:// 环信（消息记录）
			if (MainActivity.hxMsgCount > 0) {
				MainActivity.hxMsgCount = 0;
			}
			if (chatHistoryFragment == null) {
				chatHistoryFragment = new ChatAllHistoryFragment();
				transaction.add(R.id.view_message, chatHistoryFragment);
			} else {
				transaction.show(chatHistoryFragment);
			}
			break;
		}

		transaction.commitAllowingStateLoss();
	}

	/**
	 * 隐藏所有Fragment
	 * 
	 * @version 1.0
	 * @createTime 2015-8-12,下午3:20:32
	 * @updateTime 2015-8-12,下午3:20:32
	 * @createAuthor XiaoHuan
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param transaction
	 */
	private void hideFragment(FragmentTransaction transaction) {
		if (messageTaskToastFragment != null) {
			transaction.hide(messageTaskToastFragment);
		}
		if (systemToastFragment != null) {
			transaction.hide(systemToastFragment);
		}
		if (emergencyToastFragment != null) {
			transaction.hide(emergencyToastFragment);
		}
		if (myMessagesFragment != null) {
			transaction.hide(myMessagesFragment);
		}
		if (chatHistoryFragment != null) {
			transaction.hide(chatHistoryFragment);
		}
	}

	/***
	 * 打开EventBus开关
	 */
	protected boolean useEventBus() {
		return true;
	}

	/**
	 * 各个消息界面刷新和确认成功传来的数据
	 * 
	 * @param data
	 */
	public void onEvent(com.dssm.esc.util.event.Toast data) {
		MainActivity.xgTaskMsgCount = Integer.parseInt(data.count1);

		if (MainActivity.xgTaskMsgCount < 1) {
			redPointView1.hide();
		} else {
			if(MainActivity.xgTaskMsgCount > 99)
				redPointView1.setText("99+");
			else
				redPointView1.setText(MainActivity.xgTaskMsgCount + "");
			redPointView1.show();
		}
        Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
        intent.putExtra("msgType", "updateMsgCount");
        getActivity().sendBroadcast(intent);
	}

	public void onEvent(System data) {
		MainActivity.xgSysMsgCount = Integer.parseInt(data.count2);

		if (MainActivity.xgSysMsgCount  < 1) {
			redPointView2.hide();
		} else {
			if(MainActivity.xgSysMsgCount > 99)
				redPointView2.setText("99+");
			else
				redPointView2.setText(MainActivity.xgSysMsgCount + "");
			redPointView2.show();
		}
        Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
        intent.putExtra("msgType", "updateMsgCount");
        getActivity().sendBroadcast(intent);
	}

	public void onEvent(Emergenct data) {
		MainActivity.xgEmergencyMsgCount  = Integer.parseInt(data.count3);
		if (MainActivity.xgEmergencyMsgCount < 1) {
			redPointView3.hide();
		} else {
			if(MainActivity.xgEmergencyMsgCount > 99)
				redPointView3.setText("99+");
			else
				redPointView3.setText(MainActivity.xgEmergencyMsgCount + "");
			redPointView3.show();
		}
        Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
        intent.putExtra("msgType", "updateMsgCount");
        getActivity().sendBroadcast(intent);
	}

	public void onEvent(My data) {
		MainActivity.xgPersonalMsgCount = Integer.parseInt(data.count4);

		if (MainActivity.xgPersonalMsgCount < 1) {
			redPointView4.hide();
		} else {
			if(MainActivity.xgPersonalMsgCount > 99)
				redPointView4.setText("99+");
			else
				redPointView4.setText(MainActivity.xgPersonalMsgCount + "");
			redPointView4.show();
		}
        Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
        intent.putExtra("msgType", "updateMsgCount");
        getActivity().sendBroadcast(intent);
	}

	/**
	 * 接收MainActivity发送过来的消息类型，展示对应的消息界面
	 * 
	 * @param data
	 */
	public void onEvent(PushMessageEvent data) {
		int index = data.index;
		Log.i("onFailure", "msgType: " + index);
		switch (index) {
		case 1:
			rb_task.setChecked(true);
			rb_system.setChecked(false);
			rb_emergency.setChecked(false);
			rb_mymessage.setChecked(false);
			tag = 0;
			switchView(0);
			break;
		case 2:
			rb_system.setChecked(true);
			rb_task.setChecked(false);
			rb_emergency.setChecked(false);
			rb_mymessage.setChecked(false);
			tag = 1;
			switchView(1);
			break;
		case 4:
			rb_emergency.setChecked(true);
			rb_system.setChecked(false);
			rb_task.setChecked(false);
			rb_mymessage.setChecked(false);
			tag = 2;
			switchView(2);
			break;
		case 3:
			rb_mymessage.setChecked(true);
			rb_system.setChecked(false);
			rb_emergency.setChecked(false);
			rb_task.setChecked(false);
			tag = 3;
			switchView(3);
			break;
		}

	}

	/**
	 * 接收任务通知界面传来的各个消息未读条数（第一次获取数据）
	 * 
	 * @param data
	 */
	public void onEvent(MessageCountEvent data) {

		MainActivity.xgTaskMsgCount = Integer.parseInt(data.count1);
		MainActivity.xgSysMsgCount = Integer.parseInt(data.count2);
		MainActivity.xgEmergencyMsgCount = Integer.parseInt(data.count3);
		MainActivity.xgPersonalMsgCount = Integer.parseInt(data.count4);

		if (MainActivity.xgTaskMsgCount < 1) {
			redPointView1.hide();
		} else {
			if(MainActivity.xgTaskMsgCount > 99)
				redPointView1.setText("99+");
			else
				redPointView1.setText(MainActivity.xgTaskMsgCount + "");
			redPointView1.show();
		}
		if (MainActivity.xgSysMsgCount < 1) {
			redPointView2.hide();
		} else {
			if(MainActivity.xgSysMsgCount > 99)
				redPointView2.setText("99+");
			else
				redPointView2.setText(MainActivity.xgSysMsgCount + "");
			redPointView2.show();
		}
		if (MainActivity.xgEmergencyMsgCount < 1) {
			redPointView3.hide();
		} else {
			if(MainActivity.xgEmergencyMsgCount > 99)
				redPointView3.setText("99+");
			else
				redPointView3.setText(MainActivity.xgEmergencyMsgCount + "");
			redPointView3.show();
		}
		if (MainActivity.xgPersonalMsgCount < 1) {
			redPointView4.hide();
		} else {
			if(MainActivity.xgPersonalMsgCount > 99)
				redPointView4.setText("99+");
			else
				redPointView4.setText(MainActivity.xgPersonalMsgCount + "");
			redPointView4.show();
		}
		Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
		intent.putExtra("msgType", "");
		getActivity().sendBroadcast(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rb_task:
			tag = 0;
			rb_task.setChecked(true);
			rb_system.setChecked(false);
			rb_emergency.setChecked(false);
			rb_mymessage.setChecked(false);
			message.setImageResource(R.drawable.message);
			break;
		case R.id.rb_system:
			tag = 1;
			rb_system.setChecked(true);
			rb_task.setChecked(false);
			rb_emergency.setChecked(false);
			rb_mymessage.setChecked(false);
			message.setImageResource(R.drawable.message);
			break;
		case R.id.rb_emergency:
			tag = 2;
			rb_emergency.setChecked(true);
			rb_system.setChecked(false);
			rb_task.setChecked(false);
			rb_mymessage.setChecked(false);
			message.setImageResource(R.drawable.message);
			break;
		case R.id.rb_mymessage:
			tag = 3;
			rb_mymessage.setChecked(true);
			rb_system.setChecked(false);
			rb_emergency.setChecked(false);
			rb_task.setChecked(false);
			message.setImageResource(R.drawable.message);
			break;
		case R.id.setting:

			if (!mDrawerLayout.isDrawerOpen(mRelativeLayout)) {
				mDrawerLayout.openDrawer(mRelativeLayout);
			}

/*			ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),
					R.anim.push_bottom_in));
			t1.setText(name);
			t2.setText(selectedRolemName);
			pop.showAtLocation(view_Parent, Gravity.BOTTOM, 0, 0);
			message.setBackgroundResource(R.drawable.message);*/
			break;
		case R.id.message:
			tag = 4;
			rb_mymessage.setChecked(false);
			rb_system.setChecked(false);
			rb_emergency.setChecked(false);
			rb_task.setChecked(false);

			message.setImageResource(R.drawable.message2);
			break;

		}
		switchView(tag);
	}

	/**
	 * 弹出退出登录的PopupWindow
	 */
	public void initPop() {
		pop = new PopupWindow(getActivity());

		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_exit);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_exchangerples);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		t1 = (TextView) view.findViewById(R.id.item_popupwindows_info);
		t2 = (TextView) view.findViewById(R.id.item_popupwindows_info2);

		t1.setText(name);
		t2.setText(selectedRolemName);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				logout();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeRoles();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

	}

	/**
	 * 退出登录
	 */
	private void logout() {
		final ProgressDialog pd = new ProgressDialog(getActivity());
		String st = getResources().getString(R.string.Are_logged_out);
		pd.setMessage(st);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		DemoHXSDKHelper.getInstance().logout(true, new EMCallBack() {

			@Override
			public void onSuccess() {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						pd.dismiss();
						/** 推送在退出登录时解除账号绑定 */
						JPushInterface.stopPush(context);
						JPushInterface.deleteAlias(context, 2018);
						// 重新显示登录页面
						getActivity().finish();
						startActivity(new Intent(getActivity(),
								LoginActivity.class));

					}
				});
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						pd.dismiss();
						Toast.makeText(getActivity(),
								"unbind devicetokens failed",
								Toast.LENGTH_SHORT).show();
						// service.save(map.get("loginName"), "",
						// map.get("roleIds"),
						// map.get("roleNames"),map.get("roleCodes") ,"",
						// "", map.get("userId"), "",map.get("roleCode"));
					}
				});
			}
		});
	}

	private int curWhich;
	private UserSeviceImpl.UserSeviceImplBackBooleanListenser listener = new UserSeviceImpl.UserSeviceImplBackBooleanListenser() {

		@Override
		public void setUserSeviceImplListenser(
				Boolean backflag,
				String stRerror,
				String Exceptionerror) {
			String str = null;
			if (backflag) {
				str = stRerror;
				// 被选中的角色id,再次保存
				selectedRolem = rolesId[curWhich];
				selectedRolemName = identity[curWhich];
				roleCode = roleCodes[curWhich];
				// service.save(selectedRolem);
				// String
				// loginName,
				// String
				// password,
				// String
				// roleIds,
				// String
				// roleNames,
				// String
				// roleCodes,String
				// selectedRolem,
				// String
				// postFlag,
				// String
				// userId,
				// String
				// selectedRolemName,
				// String
				// roleCode)
				service.save(
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
				Log.i("messageFragment被选中的角色id",
						selectedRolem);
				Log.i("messageFragment被选中的角色名称",
						selectedRolemName);
				t1.setText(name);
				// t1.setText(loginName);
				t2.setText(selectedRolemName);
			} else if (stRerror != null) {
				if (pd != null) {
					pd.dismiss();
				}
				str = stRerror;
				ToastUtil
						.showLongToast(
								context,
								str);
			} else if (Exceptionerror != null) {
				if (pd != null) {
					pd.dismiss();
				}
				str = Const.NETWORKERROR
						+ Exceptionerror;
				ToastUtil
						.showLongToast(
								context,
								str);
			}

		}
	};

	/**
	 * 切换角色
	 */
	private void changeRoles() {
		if (identity.length > 1) {

			new AlertDialog.Builder(context)
					.setTitle("当前角色：" + selectedRolemName)

					// .setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(identity, -1,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										final int which) {
									dialog.dismiss();
									Toast.makeText(context,
											"你选择了: " + identity[which], Toast.LENGTH_SHORT)
											.show();
									Log.i("rolesId[which]", rolesId[which]);
									curWhich = which;
									// 选择角色
									userSevice.loginRole(rolesId[which], listener);

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

	boolean hidden;

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh(MainActivity.hxMsgCount);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("MessageFragment", "onResume");
		if (!hidden && !MainActivity.isConflict) {
			refresh(MainActivity.hxMsgCount);
		}
	}

	/**
	 * 刷新页面
	 */
	public void refresh(int count) {
		Log.i("count", count + "");
		if (count > 0) {
			if(count > 99)
				redPointView5.setText("99+");
			else
				redPointView5.setText(count + "");
			redPointView5.show();
			if (chatHistoryFragment != null) {
				chatHistoryFragment.refresh();
			}
		} else {
			redPointView5.hide();
		}

		switch (tag) {
			case 0:
				if(messageTaskToastFragment != null)
					messageTaskToastFragment.onRefresh();
				MainActivity.xgTaskMsgCount = 0;
				break;
			case 1:
				if(systemToastFragment != null)
					systemToastFragment.onRefresh();
				MainActivity.xgSysMsgCount = 0;
				break;
			case 2:
				if(emergencyToastFragment != null)
					emergencyToastFragment.onRefresh();
				MainActivity.xgEmergencyMsgCount = 0;
				break;
			case 3:
				if(myMessagesFragment != null)
					myMessagesFragment.onRefresh();
				MainActivity.xgPersonalMsgCount = 0;
				break;
		}
		int count1 = MainActivity.xgTaskMsgCount;
		int count2 = MainActivity.xgSysMsgCount;
		int count3 = MainActivity.xgEmergencyMsgCount;
		int count4 = MainActivity.xgPersonalMsgCount;

		if(count1 > 0) {
			if(count1 > 99)
				redPointView1.setText("99+");
			else
				redPointView1.setText(count1 + "");
			redPointView1.show();
		}
		else
			redPointView1.hide();
		if(count2 > 0) {
			if(count2 > 99)
				redPointView2.setText("99+");
			else
				redPointView2.setText(count2 + "");
			redPointView2.show();
		}
		else
			redPointView2.hide();
		if(count3 > 0) {
			if(count3 > 99)
				redPointView3.setText("99+");
			else
				redPointView3.setText(count3 + "");
			redPointView3.show();
		}
		else
			redPointView3.hide();
		if(count4 > 0) {
			if(count1 > 99)
				redPointView4.setText("99+");
			else
				redPointView4.setText(count4 + "");
			redPointView4.show();
		}
		else
			redPointView4.hide();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (pop != null) {

			pop.dismiss();
		}
		getActivity().finish();
	}

}
