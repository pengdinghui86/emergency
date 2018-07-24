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
 * 应急管理
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-6
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class EmergencyManageFragment extends BaseFragment implements
		OnClickListener {
	private TextView title;
	private Context context;
	/** 事件评估 */
	private LinearLayout eventevaluation_ll;
	/** 事件评估箭头 */
	private ImageView eventevaluation_img;
	/** 添加评估和驳回事件 */
	private LinearLayout eventevaluation_child_ll;
	/** 添加评估 */
	private LinearLayout addevaluation_ll;
	/** 驳回事件 */
	private LinearLayout dismissvaluation_ll;
	/** 预案启动 */
	private LinearLayout planstar_ll;
	/** 预案启动箭头 */
	private ImageView planstar_img;
	/** 预案启动和预案中止 */
	private LinearLayout people_start_child_ll;
	/** 预案启动 */
	private LinearLayout plan_start_ll;
	/** 预案中止 */
	private LinearLayout plan_stop_ll;

	/** 授权决策 */
	private LinearLayout authorization_decision_ll;
	/** 授权决策 箭头 */
	private ImageView authorization_img;
	/** 授权决策 和预案中止 */
	private LinearLayout authorization_child_ll;
	/** 授权决策 */
	private LinearLayout authorization_ll;
	/** 预案中止 */
	private LinearLayout plan_authorization_stop_ll;
	/** 人员签到 */
	private LinearLayout people_sign_in_ll;
	/** 人员签到 箭头 */
	private ImageView people_sign_in_img;
	/** 人员签到 和人员指派 */
	private LinearLayout people_sign_in_child_ll;
	/** 签到 */
	private LinearLayout sign_in_ll;
	/** 人员指派 */
	private LinearLayout personnel_assignment_ll;
	/** 预案执行 */
	private LinearLayout plan_execution_ll;
	/** 协同通告 */
	private LinearLayout collaborative_circular_ll;
	// /** 1,应急；2，演练 */
	// public String tag;
	private UserSevice sevice;
	private LinearLayout sjpg, yaqd, jcsq;
	private View jcsq_line, xttg;

	public EmergencyManageFragment() {
	}
	@SuppressLint("ValidFragment")
	public EmergencyManageFragment(Context context) {
		this.context = context;
		sevice = Control.getinstance().getUserSevice();
	}

	@Override
	protected View getViews() {
		// TODO Auto-generated method stub
		return view_Parent = LayoutInflater.from(context).inflate(
				R.layout.fragment_emergency, null);
	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		title = (TextView) view_Parent.findViewById(R.id.tv_actionbar_title);
		title.setText("应急管理");
		sjpg = (LinearLayout) view_Parent.findViewById(R.id.sjpg);
		yaqd = (LinearLayout) view_Parent.findViewById(R.id.yaqd);
		jcsq = (LinearLayout) view_Parent.findViewById(R.id.jcsq);
		xttg = view_Parent.findViewById(R.id.xttg);
		jcsq_line = view_Parent.findViewById(R.id.jcsq_line);
		eventevaluation_ll = (LinearLayout) view_Parent
				.findViewById(R.id.eventevaluation_ll);
		eventevaluation_img = (ImageView) view_Parent
				.findViewById(R.id.eventevaluation_img);
		eventevaluation_child_ll = (LinearLayout) view_Parent
				.findViewById(R.id.eventevaluation_child_ll);
		addevaluation_ll = (LinearLayout) view_Parent
				.findViewById(R.id.addevaluation_ll);
		dismissvaluation_ll = (LinearLayout) view_Parent
				.findViewById(R.id.dismissvaluation_ll);
		planstar_ll = (LinearLayout) view_Parent.findViewById(R.id.planstar_ll);
		planstar_img = (ImageView) view_Parent.findViewById(R.id.planstar_img);
		people_start_child_ll = (LinearLayout) view_Parent
				.findViewById(R.id.people_start_child_ll);
		plan_start_ll = (LinearLayout) view_Parent
				.findViewById(R.id.plan_start_ll);
		plan_stop_ll = (LinearLayout) view_Parent
				.findViewById(R.id.plan_stop_ll);

		authorization_decision_ll = (LinearLayout) view_Parent
				.findViewById(R.id.authorization_decision_ll);
		authorization_img = (ImageView) view_Parent
				.findViewById(R.id.authorization_img);
		authorization_child_ll = (LinearLayout) view_Parent
				.findViewById(R.id.authorization_child_ll);
		authorization_ll = (LinearLayout) view_Parent
				.findViewById(R.id.authorization_ll);
		plan_authorization_stop_ll = (LinearLayout) view_Parent
				.findViewById(R.id.plan_authorization_stop_ll);

		people_sign_in_ll = (LinearLayout) view_Parent
				.findViewById(R.id.people_sign_in_ll);
		people_sign_in_img = (ImageView) view_Parent
				.findViewById(R.id.people_sign_in_img);
		people_sign_in_child_ll = (LinearLayout) view_Parent
				.findViewById(R.id.people_sign_in_child_ll);
		sign_in_ll = (LinearLayout) view_Parent.findViewById(R.id.sign_in_ll);
		personnel_assignment_ll = (LinearLayout) view_Parent
				.findViewById(R.id.personnel_assignment_ll);
		plan_execution_ll = (LinearLayout) view_Parent
				.findViewById(R.id.plan_execution_ll);
		collaborative_circular_ll = (LinearLayout) view_Parent
				.findViewById(R.id.collaborative_circular_ll);
	}

	@Override
	protected void widgetListener() {
		// TODO Auto-generated method stub
		sjpg.setOnClickListener(this);
		addevaluation_ll.setOnClickListener(this);
		dismissvaluation_ll.setOnClickListener(this);
		yaqd.setOnClickListener(this);
		plan_start_ll.setOnClickListener(this);
		plan_stop_ll.setOnClickListener(this);
		jcsq.setOnClickListener(this);
		authorization_ll.setOnClickListener(this);
		plan_authorization_stop_ll.setOnClickListener(this);
		people_sign_in_ll.setOnClickListener(this);
		people_sign_in_img.setOnClickListener(this);
		people_sign_in_child_ll.setOnClickListener(this);
		sign_in_ll.setOnClickListener(this);
		personnel_assignment_ll.setOnClickListener(this);
		plan_execution_ll.setOnClickListener(this);
		collaborative_circular_ll.setOnClickListener(this);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		// sjpg.setVisibility(View.VISIBLE);
		// yaqd.setVisibility(View.VISIBLE);
		// jcsq.setVisibility(View.VISIBLE);
		// collaborative_circular_ll.setVisibility(View.VISIBLE);

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
		case R.id.sjpg:// 事件评估

			if (eventevaluation_child_ll.getVisibility() == View.VISIBLE) {// 0,可见;4,不可见;8,gone;
				eventevaluation_child_ll.setVisibility(View.GONE);
				eventevaluation_img.setImageResource(R.drawable.right);
			} else {
				eventevaluation_child_ll.setVisibility(View.VISIBLE);
				eventevaluation_img.setImageResource(R.drawable.expand);
			}
			break;
		case R.id.addevaluation_ll:// 添加评估
			CustomDialog.Builder builder = new CustomDialog.Builder(
					getActivity());
			 builder.setMessage("请选择事件类型：");
			builder.setNegativeButton("演练",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// 设置你的操作事项
							Intent intent2 = new Intent(getActivity(),
									DrillSelectActivity.class);
							startActivity(intent2);
						}
					});

			builder.setPositiveButton("应急",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent intent2 = new Intent(getActivity(),
									AddeValuationActivity.class);
							intent2.putExtra("type", "0");
							intent2.putExtra("tag", "1");
							startActivity(intent2);
						}
					});
			builder.create().show();
			break;
		case R.id.dismissvaluation_ll:// 驳回 事件
			Intent intent = new Intent(getActivity(),
					DismissValuationActivity.class);
			// intent.putExtra("tag", tag);
			startActivity(intent);
			break;
		case R.id.yaqd:// 预案启动
			if (people_start_child_ll.getVisibility() == View.VISIBLE) {// 0,可见;4,不可见;8,gone;
				people_start_child_ll.setVisibility(View.GONE);
				planstar_img.setImageResource(R.drawable.right);
			} else {
				people_start_child_ll.setVisibility(View.VISIBLE);
				planstar_img.setImageResource(R.drawable.expand);
			}
			break;
		case R.id.plan_start_ll:// 预案待启动事件列表
			Intent intent3 = new Intent(getActivity(), PlanStarActivity.class);
			// intent3.putExtra("tag", tag);
			intent3.putExtra("tags", "1");
			startActivity(intent3);
			break;
		case R.id.plan_stop_ll:// 预案已启动列表
			Intent intent32 = new Intent(getActivity(), PlanStarActivity.class);
			// intent32.putExtra("tag", tag);
			intent32.putExtra("tags", "2");
			startActivity(intent32);
			break;

		case R.id.jcsq:// 授权决策
			if (authorization_child_ll.getVisibility() == View.VISIBLE) {// 0,可见;4,不可见;8,gone;
				authorization_child_ll.setVisibility(View.GONE);
				authorization_img.setImageResource(R.drawable.right);
			} else {
				authorization_child_ll.setVisibility(View.VISIBLE);
				authorization_img.setImageResource(R.drawable.expand);
			}
			break;
		case R.id.authorization_ll:// 授权决策列表
			Intent intent4 = new Intent(getActivity(),
					AutorizationDecisionActivity.class);
			// intent4.putExtra("tag", tag);
			intent4.putExtra("tags", "1");
			startActivity(intent4);
			break;
		case R.id.plan_authorization_stop_ll:// 已授权
			Intent intent41 = new Intent(getActivity(),
					AutorizationDecisionActivity.class);
			// intent41.putExtra("tag", tag);
			intent41.putExtra("tags", "0");
			startActivity(intent41);
			break;
		case R.id.people_sign_in_ll:// 人员签到
			if (people_sign_in_child_ll.getVisibility() == View.VISIBLE) {// 0,可见;4,不可见;8,gone;
				people_sign_in_child_ll.setVisibility(View.GONE);
				people_sign_in_img.setImageResource(R.drawable.right);
			} else {
				people_sign_in_child_ll.setVisibility(View.VISIBLE);
				people_sign_in_img.setImageResource(R.drawable.expand);
			}

			break;

		case R.id.sign_in_ll:// 签到
			Intent intent5 = new Intent(getActivity(),
					AutorizationDecisionActivity.class);
			// intent5.putExtra("tag", tag);
			intent5.putExtra("tags", "2");
			startActivity(intent5);

			break;
		case R.id.personnel_assignment_ll:// 人员指派
			Intent intent6 = new Intent(getActivity(),
					AutorizationDecisionActivity.class);
			// intent6.putExtra("tag", tag);
			intent6.putExtra("tags", "3");
			startActivity(intent6);
			break;
		case R.id.plan_execution_ll:// 预案执行
			Intent intent7 = new Intent(getActivity(),
					PlanExecutionActivity.class);
			// intent7.putExtra("tag", tag);
			startActivity(intent7);
			break;
		case R.id.collaborative_circular_ll:// 协同通告
			Intent intent8 = new Intent(getActivity(),
					AutorizationDecisionActivity.class);
			// intent8.putExtra("tag", tag);
			intent8.putExtra("tags", "4");
			startActivity(intent8);
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
				// 事件评估：SJPG
				// 预案启动：YAQDCD
				// 决策授权：JCSQCD
				// 人员指派：RYZP
				// 协同与通告:XTYTG
				// 指挥与展示启动终止按钮：BTN_QDZZ
				boolean sjpgVisible = false;
				boolean yaqdVisible = false;
				boolean jcsqVisible = false;
				boolean personnel_assignment_llVisible = false;
				boolean collaborative_circular_llVisible = false;
				for (int i = 0; i < menu.size(); i++) {
					MenuEntity menuEntity = menu.get(i);
					String mark = menuEntity.getMark();
					if (mark.equals("SJPG")) {
						sjpgVisible = true;
					}
					if (mark.equals("YAQD")) {
						yaqdVisible = true;
					}
					if (mark.equals("JCSQ")) {
						jcsqVisible = true;
					}
					if (mark.equals("RYZP")) {
						personnel_assignment_llVisible = true;
					}
					if (mark.equals("XTYTG")) {
						collaborative_circular_llVisible = true;
					}
				}
				if(!sjpgVisible)
					sjpg.setVisibility(View.GONE);
				if(!yaqdVisible)
					yaqd.setVisibility(View.GONE);
				if(!jcsqVisible)
					jcsq.setVisibility(View.GONE);
				if(!personnel_assignment_llVisible)
					personnel_assignment_ll.setVisibility(View.GONE);
				if(!collaborative_circular_llVisible)
					collaborative_circular_ll.setVisibility(View.GONE);
				if (yaqd.getVisibility() == View.GONE) {
					jcsq_line.setVisibility(View.VISIBLE);
				}
				else
					jcsq_line.setVisibility(View.GONE);
				if (xttg.getVisibility() == View.VISIBLE) {
					xttg.setVisibility(View.VISIBLE);
				}
			}
		}
	};

	public void getUserPower() {
//		sjpg.setVisibility(View.GONE);
//		yaqd.setVisibility(View.GONE);
//		jcsq.setVisibility(View.GONE);
//		personnel_assignment_ll.setVisibility(View.GONE);
		//collaborative_circular_ll.setVisibility(View.GONE);
		sevice.getUserPower(listListener);
	}

}