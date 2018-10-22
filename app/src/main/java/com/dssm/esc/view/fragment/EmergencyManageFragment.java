package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.entity.emergency.EmergencyMenuEntity;
import com.dssm.esc.model.entity.user.MenuEntity;
import com.dssm.esc.model.entity.user.UserPowerEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.activity.AddeValuationActivity;
import com.dssm.esc.view.activity.AutorizationDecisionActivity;
import com.dssm.esc.view.activity.DismissValuationActivity;
import com.dssm.esc.view.activity.DrillSelectActivity;
import com.dssm.esc.view.activity.PlanExecutionActivity;
import com.dssm.esc.view.activity.PlanStarActivity;
import com.dssm.esc.view.adapter.EmergencyMenuListviewAdapter;
import com.dssm.esc.view.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 应急管理
 */
public class EmergencyManageFragment extends BaseFragment implements
		OnClickListener {
	private TextView title;
	private Context context;

	private UserSevice sevice;
	private RelativeLayout emergency_menu_rl_add_event;
	private RelativeLayout emergency_menu_rl_event_manage;
	private RelativeLayout emergency_menu_rl_plan_manage;
	private RelativeLayout emergency_menu_rl_sign_in_assign;
	private RelativeLayout emergency_menu_rl_plan_execute;
	private ListView emergency_menu_lv_add_event;
	private ListView emergency_menu_lv_event_manage;
	private ListView emergency_menu_lv_plan_manage;
	private ListView emergency_menu_lv_sign_in_assign;
	private ListView emergency_menu_lv_plan_execute;
	private EmergencyMenuListviewAdapter addEventAdapter;
	private EmergencyMenuListviewAdapter eventManageAdapter;
	private EmergencyMenuListviewAdapter planManageAdapter;
	private EmergencyMenuListviewAdapter signInAssignAdapter;
	private EmergencyMenuListviewAdapter planExecuteAdapter;
	private List<EmergencyMenuEntity> addEventList = new ArrayList<>();
	private List<EmergencyMenuEntity> eventManageList = new ArrayList<>();
	private List<EmergencyMenuEntity> planManageList = new ArrayList<>();
	private List<EmergencyMenuEntity> signInAssignList = new ArrayList<>();
	private List<EmergencyMenuEntity> planExecuteList = new ArrayList<>();

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
		emergency_menu_rl_add_event = (RelativeLayout) view_Parent
				.findViewById(R.id.emergency_menu_rl_add_event);
		emergency_menu_rl_event_manage = (RelativeLayout) view_Parent
				.findViewById(R.id.emergency_menu_rl_event_manage);
		emergency_menu_rl_plan_manage = (RelativeLayout) view_Parent
				.findViewById(R.id.emergency_menu_rl_plan_manage);
		emergency_menu_rl_sign_in_assign = (RelativeLayout) view_Parent
				.findViewById(R.id.emergency_menu_rl_sign_in_assign);
		emergency_menu_rl_plan_execute = (RelativeLayout) view_Parent
				.findViewById(R.id.emergency_menu_rl_plan_execute);

		emergency_menu_lv_add_event = (ListView) view_Parent
				.findViewById(R.id.emergency_menu_lv_add_event);
		emergency_menu_lv_event_manage = (ListView) view_Parent
				.findViewById(R.id.emergency_menu_lv_event_manage);
		emergency_menu_lv_plan_manage = (ListView) view_Parent
				.findViewById(R.id.emergency_menu_lv_plan_manage);
		emergency_menu_lv_sign_in_assign = (ListView) view_Parent
				.findViewById(R.id.emergency_menu_lv_sign_in_assign);
		emergency_menu_lv_plan_execute = (ListView) view_Parent
				.findViewById(R.id.emergency_menu_lv_plan_execute);

		emergency_menu_lv_add_event.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(addEventList.get(i).getActivity() == null)
					return;
				Intent intent = new Intent(getActivity(),
						addEventList.get(i).getActivity());
				// intent.putExtra("tag", tag);
				startActivity(intent);
			}
		});

		emergency_menu_lv_event_manage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(eventManageList.get(i).getActivity() == null)
					return;
				Intent intent = new Intent(getActivity(),
						eventManageList.get(i).getActivity());
				// intent.putExtra("tag", tag);
				startActivity(intent);
			}
		});

		emergency_menu_lv_plan_manage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(planManageList.get(i).getActivity() == null)
					return;
				Intent intent = new Intent(getActivity(),
						planManageList.get(i).getActivity());
				// intent.putExtra("tag", tag);
				startActivity(intent);
			}
		});

		emergency_menu_lv_sign_in_assign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(signInAssignList.get(i).getActivity() == null)
					return;
				Intent intent = new Intent(getActivity(),
						signInAssignList.get(i).getActivity());
				// intent.putExtra("tag", tag);
				startActivity(intent);
			}
		});

		emergency_menu_lv_plan_execute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(planExecuteList.get(i).getActivity() == null)
					return;
				Intent intent = new Intent(getActivity(),
						planExecuteList.get(i).getActivity());
				// intent.putExtra("tag", tag);
				startActivity(intent);
			}
		});
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
				}
				EmergencyMenuEntity emergencyMenuEntity = new EmergencyMenuEntity();
				if(!sjpgVisible) {
					emergency_menu_rl_add_event.setVisibility(View.GONE);
					emergency_menu_rl_event_manage.setVisibility(View.GONE);
				}
				else {
					emergencyMenuEntity.setId("emergency_evaluate");
					emergencyMenuEntity.setName(getString(R.string.emergency_evaluation));
					emergencyMenuEntity.setIcon(R.drawable.emergency_evaluate);
					emergencyMenuEntity.setActivity(AddeValuationActivity.class);
					addEventList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("drill_evaluate");
					emergencyMenuEntity.setName(getString(R.string.drill_evaluation));
					emergencyMenuEntity.setIcon(R.drawable.drill_evaluate);
					emergencyMenuEntity.setActivity(DrillSelectActivity.class);
					addEventList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					addEventList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					addEventList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("event_execute");
					emergencyMenuEntity.setName(getString(R.string.executing));
					emergencyMenuEntity.setIcon(R.drawable.event_execute);
					emergencyMenuEntity.setActivity(PlanStarActivity.class);
					eventManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("event_reject");
					emergencyMenuEntity.setName(getString(R.string.rejected));
					emergencyMenuEntity.setIcon(R.drawable.event_reject);
					emergencyMenuEntity.setActivity(PlanStarActivity.class);
					eventManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("event_complete");
					emergencyMenuEntity.setName(getString(R.string.execute_complete));
					emergencyMenuEntity.setIcon(R.drawable.event_complete);
					emergencyMenuEntity.setActivity(PlanStarActivity.class);
					eventManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					eventManageList.add(emergencyMenuEntity);
				}
				if(!yaqdVisible && !jcsqVisible)
					emergency_menu_rl_plan_manage.setVisibility(View.GONE);
				else if(!yaqdVisible)
				{
					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("wait_authorize");
					emergencyMenuEntity.setName(getString(R.string.wait_authorize));
					emergencyMenuEntity.setIcon(R.drawable.plan_for_authorize);
					emergencyMenuEntity.setActivity(AutorizationDecisionActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("authorized");
					emergencyMenuEntity.setName(getString(R.string.authorized));
					emergencyMenuEntity.setIcon(R.drawable.plan_authorized);
					emergencyMenuEntity.setActivity(AutorizationDecisionActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					planManageList.add(emergencyMenuEntity);
				}
				else if(!jcsqVisible)
				{
					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("wait_start");
					emergencyMenuEntity.setName(getString(R.string.wait_start));
					emergencyMenuEntity.setIcon(R.drawable.plan_for_start);
					emergencyMenuEntity.setActivity(PlanStarActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("started");
					emergencyMenuEntity.setName(getString(R.string.started));
					emergencyMenuEntity.setIcon(R.drawable.plan_started);
					emergencyMenuEntity.setActivity(PlanStarActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("wait_authorize");
					emergencyMenuEntity.setName(getString(R.string.wait_authorize));
					emergencyMenuEntity.setIcon(R.drawable.plan_for_authorize);
					emergencyMenuEntity.setActivity(AutorizationDecisionActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("authorized");
					emergencyMenuEntity.setName(getString(R.string.authorized));
					emergencyMenuEntity.setIcon(R.drawable.plan_authorized);
					emergencyMenuEntity.setActivity(AutorizationDecisionActivity.class);
					planManageList.add(emergencyMenuEntity);
				}
				else
				{
					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("wait_start");
					emergencyMenuEntity.setName(getString(R.string.wait_start));
					emergencyMenuEntity.setIcon(R.drawable.plan_for_start);
					emergencyMenuEntity.setActivity(PlanStarActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("started");
					emergencyMenuEntity.setName(getString(R.string.started));
					emergencyMenuEntity.setIcon(R.drawable.plan_started);
					emergencyMenuEntity.setActivity(PlanStarActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					planManageList.add(emergencyMenuEntity);
				}
				if(!personnel_assignment_llVisible)
				{
					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("people_sign");
					emergencyMenuEntity.setName(getString(R.string.people_sign));
					emergencyMenuEntity.setIcon(R.drawable.person_sign_in);
					emergencyMenuEntity.setActivity(AutorizationDecisionActivity.class);
					signInAssignList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					signInAssignList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					signInAssignList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					signInAssignList.add(emergencyMenuEntity);
				}
				else
				{
					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("people_sign");
					emergencyMenuEntity.setName(getString(R.string.people_sign));
					emergencyMenuEntity.setIcon(R.drawable.person_sign_in);
					emergencyMenuEntity.setActivity(AutorizationDecisionActivity.class);
					signInAssignList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName(getString(R.string.people_assign));
					emergencyMenuEntity.setIcon(R.drawable.person_assign);
					emergencyMenuEntity.setActivity(AutorizationDecisionActivity.class);
					signInAssignList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					signInAssignList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					signInAssignList.add(emergencyMenuEntity);
				}
				emergencyMenuEntity = new EmergencyMenuEntity();
				emergencyMenuEntity.setId("plan_execute");
				emergencyMenuEntity.setName(getString(R.string.plan_execute));
				emergencyMenuEntity.setIcon(R.drawable.plan_for_execute);
				emergencyMenuEntity.setActivity(PlanExecutionActivity.class);
				planExecuteList.add(emergencyMenuEntity);

				emergencyMenuEntity = new EmergencyMenuEntity();
				emergencyMenuEntity.setId("");
				emergencyMenuEntity.setName("");
				emergencyMenuEntity.setIcon(0);
				emergencyMenuEntity.setActivity(null);
				planExecuteList.add(emergencyMenuEntity);

				emergencyMenuEntity = new EmergencyMenuEntity();
				emergencyMenuEntity.setId("");
				emergencyMenuEntity.setName("");
				emergencyMenuEntity.setIcon(0);
				emergencyMenuEntity.setActivity(null);
				planExecuteList.add(emergencyMenuEntity);

				emergencyMenuEntity = new EmergencyMenuEntity();
				emergencyMenuEntity.setId("");
				emergencyMenuEntity.setName("");
				emergencyMenuEntity.setIcon(0);
				emergencyMenuEntity.setActivity(null);
				planExecuteList.add(emergencyMenuEntity);
			}

			Utils.getInstance().hideProgressDialog();
			showMenuData();
		}
	};

	private void showMenuData()
	{
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				addEventAdapter = new EmergencyMenuListviewAdapter(getContext(), addEventList);
				emergency_menu_lv_add_event.setAdapter(addEventAdapter);

				eventManageAdapter = new EmergencyMenuListviewAdapter(getContext(), eventManageList);
				emergency_menu_lv_event_manage.setAdapter(eventManageAdapter);

				planManageAdapter = new EmergencyMenuListviewAdapter(getContext(), planManageList);
				emergency_menu_lv_plan_manage.setAdapter(planManageAdapter);

				signInAssignAdapter = new EmergencyMenuListviewAdapter(getContext(), signInAssignList);
				emergency_menu_lv_sign_in_assign.setAdapter(signInAssignAdapter);

				planExecuteAdapter = new EmergencyMenuListviewAdapter(getContext(), planExecuteList);
				emergency_menu_lv_plan_execute.setAdapter(planExecuteAdapter);
			}
		});
	}

	public void getUserPower() {
		Utils.getInstance().showProgressDialog(getActivity(), "", Const.LOAD_MESSAGE);
		sevice.getUserPower(listListener);
	}

}