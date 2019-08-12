package com.dssm.esc.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.event.mainEvent;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import de.greenrobot.event.EventBus;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 联系人详细信息界面
 */
@ContentView(R.layout.activity_personal_details)
public class IntroductionActivity extends BaseActivity {
	/** 从上一级传过来的值 */
	private String id = "";
	private String name="";
	private String post ="";
	private String department ="";
	private String mobileNumber ="";
	private String telephoneNumber ="";
	private String sex = "";
	private String email = "";
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	/** 名称 */
	@ViewInject(R.id.personal_details_tv_name)
	private TextView personal_details_tv_name;
	/** 岗位 */
	@ViewInject(R.id.personal_details_tv_post)
	private TextView personal_details_tv_post;
	/** 手机 */
	@ViewInject(R.id.personal_details_tv_mobile)
	private TextView personal_details_tv_mobile;
	/** 电话 */
	@ViewInject(R.id.personal_details_tv_telephone)
	private TextView personal_details_tv_telephone;
	/** 邮箱 */
	@ViewInject(R.id.personal_details_tv_email)
	private TextView personal_details_tv_email;
	/** 部门 */
	@ViewInject(R.id.personal_details_tv_department)
	private TextView personal_details_tv_department;
	/** 发消息 */
	@ViewInject(R.id.send_message)
	private TextView send_message;

	@ViewInject(R.id.personal_details_rl_head)
	private RelativeLayout personal_details_rl_head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (useEventBus()) {
			EventBus.getDefault().register(this);
		}
		View findViewById = findViewById(R.id.personal_details_ll);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		name = intent.getStringExtra("name");
		mobileNumber = intent.getStringExtra("mobileNumber");
		telephoneNumber = intent.getStringExtra("telephoneNumber");
		post = intent.getStringExtra("post");
		department = intent.getStringExtra("department");
		sex = intent.getStringExtra("sex");
		email = intent.getStringExtra("email");
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		back.setVisibility(View.VISIBLE);
		title.setText(R.string.personal_details);
		personal_details_tv_name.setText(name);
		if (mobileNumber.equals("null")) {
			mobileNumber = "";
		}
		personal_details_tv_mobile.setText(mobileNumber);
		if (post.equals("null")) {
			post ="";
		}
		personal_details_tv_post.setText(post);
		if (department.equals("null")) {
			department ="";
		}
		personal_details_tv_department.setText(department);
		if (sex.equals("null")) {
			sex = "";
		}
		if("女".equals(sex))
			personal_details_rl_head.setBackgroundResource(R.drawable.personal_details_woman);
		else
			personal_details_rl_head.setBackgroundResource(R.drawable.personal_details_man);
		if (telephoneNumber.equals("null")) {
			telephoneNumber = "";
		}
		personal_details_tv_telephone.setText(telephoneNumber);
		if (email.equals("null")) {
			email = "";
		}
		personal_details_tv_email.setText(email);
	}

	/**
	 * 监听事件
	 */
	public void clickButton(View view) {
		switch (view.getId()) {
			case R.id.iv_actionbar_back:
				finish();
				break;
			case R.id.personal_details_iv_mobile:// 手机
				if(personal_details_tv_mobile.equals(""))
					return;
				Intent intent = new Intent(Intent.ACTION_DIAL);
				Uri data = Uri.parse("tel:" + mobileNumber);
				intent.setData(data);
				startActivity(intent);
				break;
			case R.id.personal_details_iv_telephone:// 电话
				if(telephoneNumber.equals(""))
					return;
				Intent intent2 = new Intent(Intent.ACTION_DIAL);
				Uri data2 = Uri.parse("tel:" + telephoneNumber);
				intent2.setData(data2);
				startActivity(intent2);
				break;
			case R.id.send_message:// 发消息
				Log.i("userId", id);
				if (!id.equals("")) {
					if (!MySharePreferencesService.getInstance(getApplicationContext()).getPreferences().get("userId").equals(id)) {
						final String userId = id.replace("-", "_");
						//判断该用户是否登录过app
						JMessageClient.getUserInfo(userId, new GetUserInfoCallback() {
							@Override
							public void gotResult(int i, String s, UserInfo userInfo) {
								if(userInfo != null) {
									Intent intent3 = new Intent(IntroductionActivity.this,
											ChatActivity.class);
									intent3.putExtra("userId", userId);
									intent3.putExtra("name", name);

									startActivity(intent3);
									MySharePreferencesService.getInstance(getApplicationContext()).saveContactName(id, name);// 将此联系人存到本地
									EventBus.getDefault().post(new mainEvent("userid"));
								}
								else {
									ToastUtil.showToast(context, "该用户从未登录过APP");
								}
							}
						});
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
