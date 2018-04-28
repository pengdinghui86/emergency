package com.dssm.esc.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.event.mainEvent;

import net.tsz.afinal.annotation.view.ViewInject;

import de.greenrobot.event.EventBus;
import com.easemob.chatuidemo.activity.ChatActivity;

/**
 * 个人简介界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-9
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class IntroductionActivity extends BaseActivity {
	/** 从上一级传过来的值 */
	private String id = "";
	private String name="";
	private String gwei="";
	private String bmen="";
	private String phonenumberOne="";
	private String phonenumberTwo="";
	private String sex="";
	private String email="";
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView back;
	/** 名称 */
	@ViewInject(id = R.id.name)
	private TextView nametv;
	/** 性别 */
	@ViewInject(id = R.id.sex)
	private TextView sextv;
	/** 岗位 */
	@ViewInject(id = R.id.gangwei)
	private TextView gangwei;
	/** 联系电话 */
	@ViewInject(id = R.id.phonenumber)
	private TextView phonenumber;
	/** 备用电话 */
	@ViewInject(id = R.id.phonenumber2)
	private TextView phonenumber2;
	/** 邮箱 */
	@ViewInject(id = R.id.email)
	private TextView emailtv;
	/** 部门 */
	@ViewInject(id = R.id.bumen)
	private TextView bumen;
	/** 发消息 */
	@ViewInject(id = R.id.send_message)
	private TextView send_message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduce);
		if (useEventBus()) {
			EventBus.getDefault().register(this);
		}
		View findViewById = findViewById(R.id.introduce);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		name = intent.getStringExtra("name");
		phonenumberOne = intent.getStringExtra("phonenumberOne");
		phonenumberTwo = intent.getStringExtra("phonenumberTwo");
		gwei = intent.getStringExtra("gangwei");
		bmen = intent.getStringExtra("bumen");
		sex = intent.getStringExtra("sex");
		email = intent.getStringExtra("email");
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		back.setVisibility(View.VISIBLE);
		title.setText(R.string.introduce);
		nametv.setText(name);
		if (phonenumberOne.equals("null")) {
			phonenumberOne="";
		}
		phonenumber.setText(phonenumberOne);
		if (gwei.equals("null")) {
			gwei="";
		}
		gangwei.setText(gwei);
		if (bmen.equals("null")) {
			bmen="";
		}
		bumen.setText(bmen);
		if (sex.equals("null")) {
			sex="";
		}
		sextv.setText(sex);
		if (phonenumberTwo.equals("null")) {
			phonenumberTwo="";
		}
		phonenumber2.setText(phonenumberTwo);
		if (email.equals("null")) {
			email="";
		}
		emailtv.setText(email);
	}

	/**
	 * 
	 * 监听事件
	 * 
	 * @version 1.0
	 * @createTime 2015-9-10,上午10:59:58
	 * @updateTime 2015-9-10,上午10:59:58
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param view
	 */
	public void clickButton(View view) {
		switch (view.getId()) {

		case R.id.call:// 联系电话
			if(phonenumber.equals(""))
				return;
			Intent intent = new Intent(Intent.ACTION_DIAL);
			Uri data = Uri.parse("tel:" + phonenumberOne);
			intent.setData(data);
			startActivity(intent);
			break;
		case R.id.call2:// 备用电话
			if(phonenumberTwo.equals(""))
				return;
			Intent intent2 = new Intent(Intent.ACTION_DIAL);
			Uri data2 = Uri.parse("tel:" + phonenumberTwo);
			intent2.setData(data2);
			startActivity(intent2);
			break;
		case R.id.send_message:// 发消息
			Intent intent3 = new Intent(IntroductionActivity.this,
					ChatActivity.class);
			Log.i("userId", id);

			if (!id.equals("")) {
				if (!service.getPreferences().get("userId").equals(id)) {
					String hxuserid = id.replace("-", "_");

					intent3.putExtra("userId", hxuserid);// 环信的用户id:李佳的用户id，去掉中划线
					intent3.putExtra("name", name);// 环信用户名：李佳

					startActivity(intent3);
					service.saveContactName(id, name);// 将此联系人存到本地
					EventBus.getDefault().post(new mainEvent("userid"));
				} else {
					ToastUtil.showToast(context, "不能跟自己聊天");
				}
			} else {
				ToastUtil.showToast(context, "该用户不是系统用户，不能进行即时通讯");
			}

			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (useEventBus()) {
			EventBus.getDefault().unregister(this);
		}
		super.onDestroy();
	}
}
