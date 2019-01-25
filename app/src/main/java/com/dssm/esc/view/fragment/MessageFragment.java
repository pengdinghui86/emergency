package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.event.Emergenct;
import com.dssm.esc.util.event.MessageCountEvent;
import com.dssm.esc.util.event.My;
import com.dssm.esc.util.event.PushMessageEvent;
import com.dssm.esc.util.event.System;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.widget.RedPointView;

import java.util.Map;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment implements OnClickListener {

	/** 任务碎片 */
	private MessageTaskToastFragment messageTaskToastFragment;
	/** 通知碎片 */
	private MessageSystemToastFragment systemToastFragment;
	/** 通讯（IM） */
	private ConversationListFragment chatHistoryFragment;
	/** 用于区分消息 0,任务；1，通知；2，通讯*/
	public int tag = 0;
	private Context context;
	private RadioButton rb_task, rb_notice, rb_chat;
	/**
	 * 每个用户的每个角色的二张表：任务，通知
	 */
	private String table1 = "";
	private String table2 = "";

	private RedPointView redPointView1;
	private RedPointView redPointView2;
	private RedPointView redPointView3;

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

	public MessageFragment() {

	}
	@SuppressLint("ValidFragment")
	public MessageFragment(Context context, String table1, String table2) {
		if (context == null) {
			this.context = getActivity();

		} else {
			this.context = context;
		}
		this.table1 = table1;
		this.table2 = table2;
	}

	@Override
	protected View getViews() {
		Log.i("context", context + "");
		return view_Parent = LayoutInflater.from(context).inflate(
				R.layout.fragment_message, null);
	}

	@Override
	protected void findViews() {
		rb_task = (RadioButton) view_Parent.findViewById(R.id.rb_task);
		rb_notice = (RadioButton) view_Parent.findViewById(R.id.rb_notice);
		rb_chat = (RadioButton) view_Parent
				.findViewById(R.id.rb_chat);
	}

	@Override
	protected void widgetListener() {
		rb_task.setOnClickListener(this);
		rb_notice.setOnClickListener(this);
		rb_chat.setOnClickListener(this);
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
		MainActivity.imMsgCount = MainActivity.getUnreadMsgCountTotal();
		redPointView1 = remind(rb_task, ((MainActivity) getActivity()).xgTaskMsgCount + "");
		redPointView2 = remind(rb_notice, ((MainActivity) getActivity()).xgSysMsgCount + "");
		redPointView3 = remind(rb_chat, ((MainActivity) getActivity()).imMsgCount + "");
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

		case 2:// 添加环信（消息记录）碎片
			if (MainActivity.imMsgCount > 0) {
				MainActivity.imMsgCount = 0;
			}
			if (chatHistoryFragment == null) {
				chatHistoryFragment = new ConversationListFragment();
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
	 * @param transaction
	 */
	private void hideFragment(FragmentTransaction transaction) {
		if (messageTaskToastFragment != null) {
			transaction.hide(messageTaskToastFragment);
		}
		if (systemToastFragment != null) {
			transaction.hide(systemToastFragment);
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
		MainActivity.imMsgCount = 0;
		if (MainActivity.imMsgCount < 1) {
			redPointView3.hide();
		} else {
			if(MainActivity.imMsgCount > 99)
				redPointView3.setText("99+");
			else
				redPointView3.setText(MainActivity.imMsgCount + "");
			redPointView3.show();
		}
        Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
        intent.putExtra("msgType", "updateMsgCount");
        getActivity().sendBroadcast(intent);
	}

	public void onEvent(My data) {
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
			rb_notice.setChecked(false);
			rb_chat.setChecked(false);
			tag = 0;
			switchView(0);
			break;
		case 2:
			rb_notice.setChecked(true);
			rb_task.setChecked(false);
			rb_chat.setChecked(false);
			tag = 1;
			switchView(1);
			break;
		case 3:
			rb_notice.setChecked(false);
			rb_task.setChecked(false);
			rb_chat.setChecked(true);
			tag = 2;
			switchView(2);
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
		if (MainActivity.imMsgCount < 1) {
			redPointView3.hide();
		} else {
			if(MainActivity.imMsgCount > 99)
				redPointView3.setText("99+");
			else
				redPointView3.setText(MainActivity.imMsgCount + "");
			redPointView3.show();
		}
		Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
		intent.putExtra("msgType", "updateMsgCount");
		getActivity().sendBroadcast(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rb_task:
			tag = 0;
			rb_task.setChecked(true);
			rb_notice.setChecked(false);
			rb_chat.setChecked(false);
			break;
		case R.id.rb_notice:
			tag = 1;
			rb_notice.setChecked(true);
			rb_task.setChecked(false);
			rb_chat.setChecked(false);
			break;
		case R.id.rb_chat:
			tag = 2;
			rb_notice.setChecked(false);
			rb_task.setChecked(false);
			rb_chat.setChecked(true);
			break;
		}
		switchView(tag);
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
			MainActivity.imMsgCount = MainActivity.getUnreadMsgCountTotal();
			refresh(MainActivity.imMsgCount);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("MessageFragment", "onResume");
		if (!hidden && !MainActivity.isConflict) {
			refresh(MainActivity.imMsgCount);
		}
	}

	/**
	 * 刷新页面
	 */
	public void refresh(int count) {
		Log.i("count", count + "");
		if (count > 0) {
			if (chatHistoryFragment != null) {
				chatHistoryFragment.onRefresh();
			}
		}

		switch (tag) {
			case 0:
//				if(messageTaskToastFragment != null)
//					messageTaskToastFragment.onRefresh();
				MainActivity.xgTaskMsgCount = 0;
				break;
			case 1:
				if(systemToastFragment != null)
					systemToastFragment.onRefresh();
				MainActivity.xgSysMsgCount = 0;
				break;
			case 2:
//				if(chatHistoryFragment != null)
//					chatHistoryFragment.refresh();
				MainActivity.imMsgCount = 0;
				break;
		}
		int count1 = MainActivity.xgTaskMsgCount;
		int count2 = MainActivity.xgSysMsgCount;
		int count3 = MainActivity.imMsgCount;

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
		onEvent(new My(""));
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
