package com.dssm.esc.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.ContactListService;
import com.dssm.esc.model.analytical.implSevice.ContactListServiceImpl;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.model.entity.emergency.SendNoticyEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


/**
 * 发送消息界面
 */
@ContentView(R.layout.activity_sendcollaborate_new)
public class SendMessageActivity extends BaseActivity implements
		OnClickListener, MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;

	/** 联系人布局 */
	@ViewInject(R.id.contact_people_ll)
	private LinearLayout contact_people_ll;

	/** 要发送的联系人 */
	@ViewInject(R.id.contact_people)
	private TextView contact_people;

	/** 系统的布局 */
	@ViewInject(R.id.xitong_ll1)
	private LinearLayout xitong_ll;
	/** 短信的布局 */
	@ViewInject(R.id.message_ll1)
	private LinearLayout message_ll;
	/** 邮件的布局 */
	@ViewInject(R.id.email_ll1)
	private LinearLayout email_ll;
	/** APP的布局 */
	@ViewInject(R.id.APP_ll1)
	private LinearLayout APP_ll;
	/** 系统 */
	@ViewInject(R.id.xitong_tg)
	private ToggleButton xitong;
	/** 短信 */
	@ViewInject(R.id.shortmessage_tg)
	private ToggleButton message;
	/** 邮件 */
	@ViewInject(R.id.email_tg)
	private ToggleButton email;
	/** APP */
	@ViewInject(R.id.app_tg)
	private ToggleButton APP;
	@ViewInject(R.id.edit_message)
	private EditText edit_message;

	/** 发送内容 */
	private String content = "";
	String[] sendTypearray = new String[]{"a","a","a","a"};
	private SendNoticyEntity entity = new SendNoticyEntity();

	@ViewInject(R.id.send_tv)
	private TextView send_tv;

	public static final int ADD_CONTACT_PEOPLE = 1;
	private ContactListService contactListService;
	private String title;
	//所选联系人ID
	private String contactIds = "";
	//所选联系人名字
	private String contactNames = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.sendcollaborate);
		findViewById.setFitsSystemWindows(true);
		contactListService = Control.getinstance().getContactSevice();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("tag");
		initView();
	}
	private void initView() {
		if (title.equals("1")) {
			mSelectTypeTitle.setText("应急通讯录");
		} else if (title.equals("2")) {
			mSelectTypeTitle.setText("应急通知组");
		}
		contact_people_ll.setOnClickListener(this);
		xitong.setOnClickListener(this);
		email.setOnClickListener(this);
		message.setOnClickListener(this);
		APP.setOnClickListener(this);
		send_tv.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.iv_actionbar_back:
				finish();
				break;
			case R.id.send_tv://发送
				AlertDialog.Builder adBuilder = new AlertDialog.Builder(SendMessageActivity.this);
				adBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
						sendMessage();
					}
				});
				adBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});
				adBuilder.setTitle("提示");
				adBuilder.setMessage("确定发送消息");
				adBuilder.setCancelable(true);
				adBuilder.show();
				break;
			case R.id.contact_people_ll:
				addContactPeople();
				break;
			case R.id.xitong_tg:// 0系统，1邮件，2短信，3 APP，逗号隔开
				if (xitong.isChecked()) {
					xitong_ll
							.setBackgroundResource(R.drawable.ump_select_back_checked_on);
					xitong.setTextColor(getResources().getColor(
							R.color.textColor_selected));
					sendTypearray[0] = "0";
				} else {
					xitong_ll.setBackgroundResource(R.drawable.tvbk_gray);
					xitong.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					sendTypearray[0] = "a";
				}
				break;
			case R.id.shortmessage_tg:
				if (message.isChecked()) {
					message_ll
							.setBackgroundResource(R.drawable.ump_select_back_checked_on);
					message.setTextColor(getResources().getColor(
							R.color.textColor_selected));
					sendTypearray[1] = "2";
				} else {
					message_ll.setBackgroundResource(R.drawable.tvbk_gray);
					message.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					sendTypearray[1] = "a";
				}
				break;
			case R.id.email_tg:
				if (email.isChecked()) {
					email_ll.setBackgroundResource(R.drawable.ump_select_back_checked_on);
					email.setTextColor(getResources().getColor(
							R.color.textColor_selected));
					sendTypearray[2] = "1";
				} else {
					email_ll.setBackgroundResource(R.drawable.tvbk_gray);
					email.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					sendTypearray[2] = "a";
				}
				break;
			case R.id.app_tg:
				if (APP.isChecked()) {
					APP_ll.setBackgroundResource(R.drawable.ump_select_back_checked_on);
					APP.setTextColor(getResources().getColor(
							R.color.textColor_selected));
					sendTypearray[3] = "3";
				} else {
					APP_ll.setBackgroundResource(R.drawable.tvbk_blue);
					// APP.setTextColor(getResources().getColor(
					// R.color.textColor_unselected));
					sendTypearray[3] = "a";
				}
				break;
		}
	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			GetProjectEveInfoEntity entity = null;
			if (object != null) {
				entity = (GetProjectEveInfoEntity) object;
				edit_message.setText(entity
						.getTradeTypeName());
			} else if (stRerror != null) {
				entity = new GetProjectEveInfoEntity();

			} else if (Exceptionerror != null) {
				entity = new GetProjectEveInfoEntity();
				ToastUtil
						.showToast(
								SendMessageActivity.this,
								Const.NETWORKERROR
										+ ":"
										+ Exceptionerror);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && resultCode == RESULT_OK) {
			switch (requestCode) {
				case ADD_CONTACT_PEOPLE:
					contactIds = data.getExtras().getString("people");
					contactNames = data.getExtras().getString("peopleName");
					contact_people.setText(contactNames);
					break;
			}
		} else {
			Log.i("", "data为空");
		}
	}
	/**
	 * 去掉字符串首尾的逗号
	 */
	public void split(String s) {
		String[] strArray = s.split(",");
		StringBuilder sb = new StringBuilder();
		for(String tmpStr : strArray) {
			if(tmpStr.length()>0)
				sb.append(tmpStr).append(",");
		}
		if(sb.toString().endsWith(",")) sb.deleteCharAt(sb.length()-1);
		System.out.println(sb.toString());
	}

	private void sendMessage() {
		Utils.getInstance().showProgressDialog(
				SendMessageActivity.this, "", Const.SEND_MESSAGE);
		String sendType = "";
		for (int i = 0; i < sendTypearray.length; i++) {
			String string = sendTypearray[i];
			if (!string.equals("a")) {
				sendType = sendType + "," + sendTypearray[i];
			}
		}
		if (sendType.length() > 0 && !sendType.equals("")) {
			split(sendType);
			if (sendType.subSequence(0, 1).equals(",")) {
				sendType = (String) sendType.subSequence(1,
						sendType.length());
			}

			content = edit_message.getText().toString().trim();
			if (!content.equals("") && content.length() > 0) {
				if (!contactIds.equals("") && contactIds.length() > 0) {
					Log.i("sendType", sendType);
					contactListService.sendMessage(contactIds, sendType, content,
							new ContactListServiceImpl.ContactSeviceImplBackBooleanListenser() {

								@Override
								public void setContactSeviceImplListenser(
										Boolean backflag, String stRerror,
										String Exceptionerror) {
									// TODO Auto-generated method stub
									if (backflag) {
										ToastUtil.showToast(
												SendMessageActivity.this,
												stRerror);
										finish();
									} else if (backflag == false) {
										ToastUtil.showToast(
												SendMessageActivity.this,
												stRerror);
									} else if (stRerror != null) {

										ToastUtil.showLongToast(
												SendMessageActivity.this,
												stRerror);
									} else if (Exceptionerror != null) {

										ToastUtil.showLongToast(
												SendMessageActivity.this,
												Const.NETWORKERROR
														+ Exceptionerror);
									}
									Utils.getInstance().hideProgressDialog();
								}
							});
				} else {
					Utils.getInstance().hideProgressDialog();
					ToastUtil.showToast(SendMessageActivity.this,
							"请选择发送对象");
				}
			} else {
				Utils.getInstance().hideProgressDialog();
				ToastUtil.showToast(SendMessageActivity.this, "发送信息不能为空");
			}

		} else {
			Utils.getInstance().hideProgressDialog();
			ToastUtil.showToast(SendMessageActivity.this, "请选择发送方式");
		}
	}

	/***
	 * 添加联系人
	 */
	private void addContactPeople() {
		Intent intent = new Intent(SendMessageActivity.this,
				SelectContactListActivity.class);
		startActivityForResult(intent, ADD_CONTACT_PEOPLE);
	}

	/***
	 * 根据item数量设置listView的高度
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if(listItem == null)
				continue;
			if (listItem instanceof LinearLayout){
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}else {
				try {
					listItem.measure(0, 0);
					totalHeight += listItem.getMeasuredHeight();
				}catch (NullPointerException e){
					totalHeight += 50; //这里随便写个大小做容错处理
				}
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listener = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated
			// method
			// stub
			if (backflag) {
				ToastUtil.showToast(SendMessageActivity.this,
						stRerror);
				SendMessageActivity.this.finish();

			} else if (backflag == false) {
				ToastUtil.showToast(SendMessageActivity.this,
						stRerror);
				resetting();
			} else if (stRerror != null) {

				ToastUtil.showToast(SendMessageActivity.this,
						stRerror);
				resetting();
			} else if (Exceptionerror != null) {

				ToastUtil.showToast(SendMessageActivity.this,
						Exceptionerror);
				resetting();
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void initContent() {
		Utils.getInstance().showProgressDialog(
				SendMessageActivity.this, "",
				Const.SUBMIT_MESSAGE);
		Control.getinstance().getEmergencyService().sendNotice(entity, listener);

	}

	private void resetting() {
		for (int i = 0; i < sendTypearray.length; i++) {
			sendTypearray[i] = "a";
		}
		APP_ll.setBackgroundResource(R.drawable.tvbk_blue);
		APP.setTextColor(getResources().getColor(R.color.textColor_unselected));
		email_ll.setBackgroundResource(R.drawable.tvbk_blue);
		email.setTextColor(getResources()
				.getColor(R.color.textColor_unselected));
		xitong_ll.setBackgroundResource(R.drawable.tvbk_blue);
		xitong.setTextColor(getResources().getColor(
				R.color.textColor_unselected));
		message_ll.setBackgroundResource(R.drawable.tvbk_blue);
		message.setTextColor(getResources().getColor(
				R.color.textColor_unselected));
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initContent();
	}
}
