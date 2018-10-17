package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.entity.user.MenuEntity;
import com.dssm.esc.model.entity.user.UserPowerEntity;
import com.dssm.esc.view.activity.AddeValuationActivity;
import com.dssm.esc.view.activity.AutorizationDecisionActivity;
import com.dssm.esc.view.activity.DismissValuationActivity;
import com.dssm.esc.view.activity.DrillSelectActivity;
import com.dssm.esc.view.activity.PlanExecutionActivity;
import com.dssm.esc.view.activity.PlanStarActivity;
import com.dssm.esc.view.widget.CustomDialog;

import java.util.List;


/**
 * 个人中心
 *
 */
public class PersonalCenterFragment extends BaseFragment implements
		OnClickListener {
	private TextView title;
	private Context context;
	private UserSevice sevice;

	public PersonalCenterFragment() {
	}
	@SuppressLint("ValidFragment")
	public PersonalCenterFragment(Context context) {
		this.context = context;
		sevice = Control.getinstance().getUserSevice();
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
		title.setText("我的");

	}

	@Override
	protected void widgetListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

		getUserPower();
	}

	@Override
	public void initGetData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sjpg:
			break;
		}
	}

	private UserSeviceImpl.UserSeviceImplListListenser listListener = new UserSeviceImpl.UserSeviceImplListListenser() {

		@Override
		public void setUserSeviceImplListListenser(Object object,
				String stRerror, String Exceptionerror) {
			// TODO Auto-generated method stub
			if (object != null) {
				UserPowerEntity entity = (UserPowerEntity) object;
				List<MenuEntity> menu = entity.getMenu();
			}
		}
	};

	public void getUserPower() {
		sevice.getUserPower(listListener);
	}

}