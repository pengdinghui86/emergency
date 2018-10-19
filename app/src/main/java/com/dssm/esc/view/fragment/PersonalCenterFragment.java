package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.util.MySharePreferencesService;
import com.easemob.chatuidemo.DemoApplication;

import java.util.Map;


/**
 * 个人中心
 *
 */
public class PersonalCenterFragment extends BaseFragment implements
		OnClickListener {
	private TextView title;
	private TextView name;
	private TextView personal_center_tv_role;
	private TextView personal_center_tv_post;
	private TextView personal_center_tv_department;
	private TextView personal_center_tv_email;
	private Button personal_center_bt_change_role;
	private Button personal_center_bt_logout;
	private Context context;
	private MySharePreferencesService preferencesService;
	private Map<String, String> map;

	public PersonalCenterFragment() {
	}
	@SuppressLint("ValidFragment")
	public PersonalCenterFragment(Context context) {
		this.context = context;
		preferencesService = new MySharePreferencesService(DemoApplication.applicationContext);
		// 回显
		map = preferencesService.getPreferences();
	}

	@Override
	protected View getViews() {
		// TODO Auto-generated method stub
		return view_Parent = LayoutInflater.from(context).inflate(
				R.layout.fragment_personal_center, null);
	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		title = (TextView) view_Parent.findViewById(R.id.tv_actionbar_title);
		name = (TextView) view_Parent.findViewById(R.id.personal_center_tv_name);
		personal_center_tv_role = (TextView) view_Parent.findViewById(R.id.personal_center_tv_role);
		personal_center_tv_post = (TextView) view_Parent.findViewById(R.id.personal_center_tv_post);
		personal_center_tv_department = (TextView) view_Parent.findViewById(R.id.personal_center_tv_department);
		personal_center_tv_email = (TextView) view_Parent.findViewById(R.id.personal_center_tv_email);
		personal_center_bt_change_role = (Button) view_Parent.findViewById(R.id.personal_center_bt_change_role);
		personal_center_bt_logout = (Button) view_Parent.findViewById(R.id.personal_center_bt_logout);
		title.setText("我的");

	}

	@Override
	protected void widgetListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		name.setText(map.get("name").toString());
		personal_center_tv_role.setText(map.get("selectedRolemName").toString());
		personal_center_tv_post.setText("");
		personal_center_tv_department.setText("");
		personal_center_tv_email.setText("");

	}

	@Override
	public void initGetData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.personal_center_bt_change_role:
				break;
			case R.id.personal_center_bt_logout:
				break;
		}
	}
}