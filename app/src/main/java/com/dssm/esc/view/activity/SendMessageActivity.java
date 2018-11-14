package com.dssm.esc.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.ContactListService;
import com.dssm.esc.model.analytical.implSevice.ContactListServiceImpl;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.SendNoticyEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


/**
 * 发送消息界面
 */
@ContentView(R.layout.activity_send_message)
public class SendMessageActivity extends BaseActivity implements
		OnClickListener, MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;

	/** 通知历史信息 */
	@ViewInject(R.id.tv_actionbar_rightimag)
	private ImageView historyNotice;

	/** 联系人布局 */
	@ViewInject(R.id.contact_people_ll)
	private LinearLayout contact_people_ll;

	/** 要发送的联系人 */
	@ViewInject(R.id.contact_people)
	private TextView contact_people;

	/** 系统的布局 */
	@ViewInject(R.id.send_message_rl_sys)
	private RelativeLayout send_message_rl_sys;
	/** 短信的布局 */
	@ViewInject(R.id.send_message_rl_msg)
	private RelativeLayout send_message_rl_msg;
	/** 邮件的布局 */
	@ViewInject(R.id.send_message_rl_email)
	private RelativeLayout send_message_rl_email;
	/** APP的布局 */
	@ViewInject(R.id.send_message_rl_app)
	private RelativeLayout send_message_rl_app;

	@ViewInject(R.id.send_message_iv_sys)
	private ImageView send_message_iv_sys;
	@ViewInject(R.id.send_message_iv_msg)
	private ImageView send_message_iv_msg;
	@ViewInject(R.id.send_message_iv_email)
	private ImageView send_message_iv_email;
	@ViewInject(R.id.send_message_iv_app)
	private ImageView send_message_iv_app;

	@ViewInject(R.id.send_message_iv_sys_check)
	private ImageView send_message_iv_sys_check;
	@ViewInject(R.id.send_message_iv_msg_check)
	private ImageView send_message_iv_msg_check;
	@ViewInject(R.id.send_message_iv_email_check)
	private ImageView send_message_iv_email_check;
	@ViewInject(R.id.send_message_iv_app_check)
	private ImageView send_message_iv_app_check;

	@ViewInject(R.id.send_message_tv_sys)
	private TextView send_message_tv_sys;
	@ViewInject(R.id.send_message_tv_msg)
	private TextView send_message_tv_msg;
	@ViewInject(R.id.send_message_tv_email)
	private TextView send_message_tv_email;
	@ViewInject(R.id.send_message_tv_app)
	private TextView send_message_tv_app;

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
	private String tag;
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
		tag = bundle.getString("tag", "0");
		initView();
	}
	private void initView() {
		if (tag.equals("0")) {
			mSelectTypeTitle.setText("应急通讯录");
		} else if (tag.equals("1")) {
			mSelectTypeTitle.setText("应急通知组");
		}
		contact_people_ll.setOnClickListener(this);
		send_message_rl_sys.setOnClickListener(this);
		send_message_rl_msg.setOnClickListener(this);
		send_message_rl_email.setOnClickListener(this);
		send_message_rl_app.setOnClickListener(this);
		send_tv.setOnClickListener(this);
		mBack.setOnClickListener(this);
		historyNotice.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.iv_actionbar_back:
				finish();
				break;
			case R.id.tv_actionbar_rightimag:
				//通知历史信息
				Intent intent = new Intent(this, HistoryNoticeActivity.class);
				startActivity(intent);
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
			case R.id.send_message_rl_sys:// 0系统，1邮件，2短信，3 APP，逗号隔开
				if (send_message_iv_sys_check.getVisibility() == View.INVISIBLE) {
					send_message_rl_sys.setBackgroundResource(R.drawable.btbg_blue);
					send_message_tv_sys.setTextColor(getResources().getColor(
							R.color.white));
					send_message_iv_sys.setImageResource(R.drawable.system_select);
					send_message_iv_sys_check.setVisibility(View.VISIBLE);
					sendTypearray[0] = "0";
				} else {
					send_message_rl_sys.setBackgroundResource(R.drawable.tvbk_gray);
					send_message_tv_sys.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					send_message_iv_sys.setImageResource(R.drawable.system);
					send_message_iv_sys_check.setVisibility(View.INVISIBLE);
					sendTypearray[0] = "a";
				}
				break;
			case R.id.send_message_rl_msg:
				if (send_message_iv_msg_check.getVisibility() == View.INVISIBLE) {
					send_message_rl_msg
							.setBackgroundResource(R.drawable.btbg_blue);
					send_message_tv_msg.setTextColor(getResources().getColor(
							R.color.white));
					send_message_iv_msg.setImageResource(R.drawable.short_message_select);
					send_message_iv_msg_check.setVisibility(View.VISIBLE);
					sendTypearray[1] = "2";
				} else {
					send_message_rl_msg.setBackgroundResource(R.drawable.tvbk_gray);
					send_message_tv_msg.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					send_message_iv_msg.setImageResource(R.drawable.short_message);
					send_message_iv_msg_check.setVisibility(View.INVISIBLE);
					sendTypearray[1] = "a";
				}
				break;
			case R.id.send_message_rl_email:
				if (send_message_iv_email_check.getVisibility() == View.INVISIBLE) {
					send_message_rl_email.setBackgroundResource(R.drawable.btbg_blue);
					send_message_tv_email.setTextColor(getResources().getColor(
							R.color.white));
					send_message_iv_email.setImageResource(R.drawable.email_select);
					send_message_iv_email_check.setVisibility(View.VISIBLE);
					sendTypearray[2] = "1";
				} else {
					send_message_rl_email.setBackgroundResource(R.drawable.tvbk_gray);
					send_message_tv_email.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					send_message_iv_email.setImageResource(R.drawable.email);
					send_message_iv_email_check.setVisibility(View.INVISIBLE);
					sendTypearray[2] = "a";
				}
				break;
			case R.id.send_message_rl_app:
				if (send_message_iv_app_check.getVisibility() == View.INVISIBLE) {
					send_message_rl_app.setBackgroundResource(R.drawable.btbg_blue);
					send_message_tv_app.setTextColor(getResources().getColor(
							R.color.white));
					send_message_iv_app.setImageResource(R.drawable.app_select);
					send_message_iv_app_check.setVisibility(View.VISIBLE);
					sendTypearray[3] = "3";
				} else {
					send_message_rl_app.setBackgroundResource(R.drawable.tvbk_blue);
					send_message_tv_app.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					send_message_iv_app.setImageResource(R.drawable.app);
					send_message_iv_app_check.setVisibility(View.INVISIBLE);
					sendTypearray[3] = "a";
				}
				break;
		}
	}

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
		intent.putExtra("tag", tag);
		startActivityForResult(intent, ADD_CONTACT_PEOPLE);
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
		send_message_rl_sys.setBackgroundResource(R.drawable.tvbk_blue);
		send_message_tv_sys.setTextColor(getResources().getColor(R.color.textColor_unselected));
		send_message_iv_sys.setImageResource(R.drawable.system);
		send_message_iv_sys_check.setVisibility(View.INVISIBLE);
		send_message_rl_msg.setBackgroundResource(R.drawable.tvbk_blue);
		send_message_tv_msg.setTextColor(getResources()
				.getColor(R.color.textColor_unselected));
		send_message_iv_msg.setImageResource(R.drawable.short_message);
		send_message_iv_msg_check.setVisibility(View.INVISIBLE);
		send_message_rl_email.setBackgroundResource(R.drawable.tvbk_blue);
		send_message_tv_email.setTextColor(getResources().getColor(
				R.color.textColor_unselected));
		send_message_iv_email.setImageResource(R.drawable.email);
		send_message_iv_email_check.setVisibility(View.INVISIBLE);
		send_message_rl_app.setBackgroundResource(R.drawable.tvbk_blue);
		send_message_tv_app.setTextColor(getResources().getColor(
				R.color.textColor_unselected));
		send_message_iv_app.setImageResource(R.drawable.app);
		send_message_iv_app_check.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initContent();
	}
}
