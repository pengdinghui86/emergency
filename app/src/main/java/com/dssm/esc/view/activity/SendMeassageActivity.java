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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.ContactListService;
import com.dssm.esc.model.analytical.implSevice.ContactListServiceImpl;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 发送消息界面
 * 
 * @author zsj
 * 
 */
@ContentView(R.layout.contact_next)
public class SendMeassageActivity extends BaseActivity implements
		OnClickListener, MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;
	/** 系统的布局 */
	private LinearLayout xitong_ll;
	/** 短信的布局 */
	private LinearLayout message_ll;
	/** 邮件的布局 */
	private LinearLayout email_ll;
	/** APP的布局 */
	private LinearLayout APP_ll;
	private ToggleButton xitong, message, email, APP;
	private TextView send;
	private String ids = "";
	// private String sendType = "";
	private String content = "";
	/** 被选中的人员的id */
	public List<String> selectId = new ArrayList<String>();
	/** 信息输入框 */
	private EditText edit_message;
	private ContactListService contactListService;
	private String title;
	String[] sendTypearray = new String[4];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.contact_next);
		View findViewById = findViewById(R.id.sendmessage);
		findViewById.setFitsSystemWindows(true);
		contactListService = Control.getinstance().getContactSevice();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("tag");
		selectId = (List<String>) bundle.getSerializable("selectId");
		// selectId = intent.getStringArrayListExtra("selectId");
		// title = intent.getStringExtra("tag");
		for (int i = 0; i < sendTypearray.length; i++) {
			sendTypearray[i] = "a";
		}
		initView();
	}

	private void initView() {
		mBack.setVisibility(View.VISIBLE);
		if (title.equals("1")) {
			mSelectTypeTitle.setText("应急通讯录");
		} else if (title.equals("2")) {
			mSelectTypeTitle.setText("应急通知");
		}
		edit_message = (EditText) findViewById(R.id.edit_message);
		xitong_ll = (LinearLayout) findViewById(R.id.xitong_ll);
		message_ll = (LinearLayout) findViewById(R.id.message_ll);
		email_ll = (LinearLayout) findViewById(R.id.email_ll);
		APP_ll = (LinearLayout) findViewById(R.id.APP_ll);
		xitong = (ToggleButton) findViewById(R.id.xitong_tg);
		message = (ToggleButton) findViewById(R.id.shortmessage_tg);
		email = (ToggleButton) findViewById(R.id.email_tg);
		APP = (ToggleButton) findViewById(R.id.app_tg);
		send = (TextView) findViewById(R.id.send);
		xitong.setOnClickListener(this);
		email.setOnClickListener(this);
		message.setOnClickListener(this);
		APP.setOnClickListener(this);
		send.setOnClickListener(this);

	}
	/**
	 * 去掉字符串首尾的逗号
	 */
	public  void split(String s) {
	    String[] strArray = s.split(",");
	    StringBuilder sb = new StringBuilder();
	    for(String tmpStr : strArray) {
	        if(tmpStr.length()>0)
	            sb.append(tmpStr).append(",");
	    }
	    if(sb.toString().endsWith(",")) sb.deleteCharAt(sb.length()-1);
	    System.out.println(sb.toString());
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.xitong_tg:
			// 0系统，1邮件，2短信，3 APP，逗号隔开
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

				APP_ll.setBackgroundResource(R.drawable.tvbk_gray);
				APP.setTextColor(getResources().getColor(
						R.color.textColor_unselected));
				sendTypearray[3] = "a";
			}
			break;
		case R.id.send:
			AlertDialog.Builder adBuilder = new AlertDialog.Builder(SendMeassageActivity.this);
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
		}

	}

	private void sendMessage() {
		Utils.getInstance().showProgressDialog(
				SendMeassageActivity.this, "", Const.SEND_MESSAGE);
		if (selectId.size() > 0) {
			for (int i = 0; i < selectId.size(); i++) {
				ids = ids + "," + selectId.get(i);
			}
			if (ids.subSequence(0, 1).equals(",")) {
				ids = (String) ids.subSequence(1, ids.length());
			}
		}
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
				if (!ids.equals("") && ids.length() > 0) {

					Log.i("sendType", sendType);
					contactListService.sendMessage(ids, sendType, content,
							new ContactListServiceImpl.ContactSeviceImplBackBooleanListenser() {

								@Override
								public void setContactSeviceImplListenser(
										Boolean backflag, String stRerror,
										String Exceptionerror) {
									// TODO Auto-generated method stub
									if (backflag) {
										ToastUtil.showToast(
												SendMeassageActivity.this,
												stRerror);
										finish();
									} else if (backflag == false) {
										ToastUtil.showToast(
												SendMeassageActivity.this,
												stRerror);
									} else if (stRerror != null) {

										ToastUtil.showLongToast(
												SendMeassageActivity.this,
												stRerror);
									} else if (Exceptionerror != null) {

										ToastUtil.showLongToast(
												SendMeassageActivity.this,
												Const.NETWORKERROR
														+ Exceptionerror);
									}
									Utils.getInstance().hideProgressDialog();
								}
							});
				} else {
					Utils.getInstance().hideProgressDialog();
					ToastUtil.showToast(SendMeassageActivity.this,
							"请选择发送对象");
				}
			} else {
				Utils.getInstance().hideProgressDialog();
				ToastUtil.showToast(SendMeassageActivity.this, "发送信息不能为空");
			}

		} else {
			Utils.getInstance().hideProgressDialog();
			ToastUtil.showToast(SendMeassageActivity.this, "请选择发送方式");
		}

		Log.i("APP_ll", "APP_ll" + selectId.size());
		for (int i = 0; i < selectId.size(); i++) {
			Log.i("被选联系人ID" + i, selectId.get(i));
		}
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub

	}

}
