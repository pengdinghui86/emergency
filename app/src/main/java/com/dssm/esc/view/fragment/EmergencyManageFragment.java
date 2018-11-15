package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.EmergencyMenuEntity;
import com.dssm.esc.model.entity.emergency.PlanCountEntity;
import com.dssm.esc.model.entity.user.MenuEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.activity.AddeValuationActivity;
import com.dssm.esc.view.activity.AutorizationDecisionActivity;
import com.dssm.esc.view.activity.DismissValuationActivity;
import com.dssm.esc.view.activity.DrillSelectActivity;
import com.dssm.esc.view.activity.EventListActivity;
import com.dssm.esc.view.activity.EventPlanListActivity;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.activity.PersonSignInActivity;
import com.dssm.esc.view.activity.PlanExecutionActivity;
import com.dssm.esc.view.adapter.EmergencyMenuRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 应急管理
 */
public class EmergencyManageFragment extends BaseFragment implements
		OnClickListener {
	private TextView title;
	private Context context;
	private RelativeLayout emergency_menu_rl_add_event;
	private RelativeLayout emergency_menu_rl_event_manage;
	private RelativeLayout emergency_menu_rl_plan_manage;
	private RecyclerView emergency_menu_lv_add_event;
	private RecyclerView emergency_menu_lv_event_manage;
	private RecyclerView emergency_menu_lv_plan_manage;
	private RecyclerView emergency_menu_lv_sign_in_assign;
	private RecyclerView emergency_menu_lv_plan_execute;
	private EmergencyMenuRecyclerViewAdapter addEventAdapter;
	private EmergencyMenuRecyclerViewAdapter eventManageAdapter;
	private EmergencyMenuRecyclerViewAdapter planManageAdapter;
	private EmergencyMenuRecyclerViewAdapter signInAssignAdapter;
	private EmergencyMenuRecyclerViewAdapter planExecuteAdapter;
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

		emergency_menu_lv_add_event = (RecyclerView) view_Parent
				.findViewById(R.id.emergency_menu_lv_add_event);
		emergency_menu_lv_event_manage = (RecyclerView) view_Parent
				.findViewById(R.id.emergency_menu_lv_event_manage);
		emergency_menu_lv_plan_manage = (RecyclerView) view_Parent
				.findViewById(R.id.emergency_menu_lv_plan_manage);
		emergency_menu_lv_sign_in_assign = (RecyclerView) view_Parent
				.findViewById(R.id.emergency_menu_lv_sign_in_assign);
		emergency_menu_lv_plan_execute = (RecyclerView) view_Parent
				.findViewById(R.id.emergency_menu_lv_plan_execute);

	}

	@Override
	protected void widgetListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		initGetData();
		getPlanCount();
	}

	@Override
	public void initGetData() {
		// TODO Auto-generated method stub
		if (getActivity() instanceof MainActivity) {
			if (((MainActivity) getActivity()).menu.size() > 0) {
				List<MenuEntity> menu = ((MainActivity) getActivity()).menu;
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
				addEventList.clear();
				eventManageList.clear();
				planManageList.clear();
				signInAssignList.clear();
				planExecuteList.clear();
				EmergencyMenuEntity emergencyMenuEntity = new EmergencyMenuEntity();
				if (!sjpgVisible) {
					emergency_menu_rl_add_event.setVisibility(View.GONE);
					emergency_menu_rl_event_manage.setVisibility(View.GONE);
				} else {
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
					emergencyMenuEntity.setActivity(EventListActivity.class);
					eventManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("event_reject");
					emergencyMenuEntity.setName(getString(R.string.rejected));
					emergencyMenuEntity.setIcon(R.drawable.event_reject);
					emergencyMenuEntity.setActivity(DismissValuationActivity.class);
					eventManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("event_complete");
					emergencyMenuEntity.setName(getString(R.string.execute_complete));
					emergencyMenuEntity.setIcon(R.drawable.event_complete);
					emergencyMenuEntity.setActivity(EventListActivity.class);
					eventManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("");
					emergencyMenuEntity.setName("");
					emergencyMenuEntity.setIcon(0);
					emergencyMenuEntity.setActivity(null);
					eventManageList.add(emergencyMenuEntity);
				}
				if (!yaqdVisible && !jcsqVisible)
					emergency_menu_rl_plan_manage.setVisibility(View.GONE);
				else if (!yaqdVisible) {
					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("wait_authorize");
					emergencyMenuEntity.setName(getString(R.string.wait_authorize));
					emergencyMenuEntity.setIcon(R.drawable.plan_for_authorize);
					emergencyMenuEntity.setActivity(EventPlanListActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("authorized");
					emergencyMenuEntity.setName(getString(R.string.authorized));
					emergencyMenuEntity.setIcon(R.drawable.plan_authorized);
					emergencyMenuEntity.setActivity(EventPlanListActivity.class);
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
				} else if (!jcsqVisible) {
					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("wait_start");
					emergencyMenuEntity.setName(getString(R.string.wait_start));
					emergencyMenuEntity.setIcon(R.drawable.plan_for_start);
					emergencyMenuEntity.setActivity(EventListActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("started");
					emergencyMenuEntity.setName(getString(R.string.started));
					emergencyMenuEntity.setIcon(R.drawable.plan_started);
					emergencyMenuEntity.setActivity(EventPlanListActivity.class);
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
				} else {
					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("wait_start");
					emergencyMenuEntity.setName(getString(R.string.wait_start));
					emergencyMenuEntity.setIcon(R.drawable.plan_for_start);
					emergencyMenuEntity.setActivity(EventListActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("started");
					emergencyMenuEntity.setName(getString(R.string.started));
					emergencyMenuEntity.setIcon(R.drawable.plan_started);
					emergencyMenuEntity.setActivity(EventPlanListActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("wait_authorize");
					emergencyMenuEntity.setName(getString(R.string.wait_authorize));
					emergencyMenuEntity.setIcon(R.drawable.plan_for_authorize);
					emergencyMenuEntity.setActivity(EventPlanListActivity.class);
					planManageList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("authorized");
					emergencyMenuEntity.setName(getString(R.string.authorized));
					emergencyMenuEntity.setIcon(R.drawable.plan_authorized);
					emergencyMenuEntity.setActivity(EventPlanListActivity.class);
					planManageList.add(emergencyMenuEntity);
				}
				if (!personnel_assignment_llVisible) {
					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("people_sign");
					emergencyMenuEntity.setName(getString(R.string.people_sign));
					emergencyMenuEntity.setIcon(R.drawable.person_sign_in);
					emergencyMenuEntity.setActivity(PersonSignInActivity.class);
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
				} else {
					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("people_sign");
					emergencyMenuEntity.setName(getString(R.string.people_sign));
					emergencyMenuEntity.setIcon(R.drawable.person_sign_in);
					emergencyMenuEntity.setActivity(PersonSignInActivity.class);
					signInAssignList.add(emergencyMenuEntity);

					emergencyMenuEntity = new EmergencyMenuEntity();
					emergencyMenuEntity.setId("people_assign");
					emergencyMenuEntity.setName(getString(R.string.people_assign));
					emergencyMenuEntity.setIcon(R.drawable.person_assign);
					emergencyMenuEntity.setActivity(EventPlanListActivity.class);
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
				emergencyMenuEntity.setActivity(EventPlanListActivity.class);
//				emergencyMenuEntity.setActivity(PlanExecutionActivity.class);
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
			showMenuData();
		}
	}

	private void getPlanCount()
	{
		Utils.getInstance().showProgressDialog(
				getActivity(), "",
				Const.LOAD_MESSAGE);
		Control.getinstance().getEmergencyService().GetAllPlanCount(listener);

	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			PlanCountEntity entity = null;
			if (object != null) {
				entity = (PlanCountEntity) object;

			} else if (stRerror != null) {

			} else if (Exceptionerror != null) {
				ToastUtil.showToast(getActivity(),
						Const.NETWORKERROR);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.collaborative_circular_ll:// 协同通告
			Intent intent8 = new Intent(getActivity(),
					AutorizationDecisionActivity.class);
			intent8.putExtra("tags", "4");
			startActivity(intent8);
			break;
		}
	}

	private void showMenuData() {
		addEventAdapter = new EmergencyMenuRecyclerViewAdapter(getContext(), addEventList);
		eventManageAdapter = new EmergencyMenuRecyclerViewAdapter(getContext(), eventManageList);
		planManageAdapter = new EmergencyMenuRecyclerViewAdapter(getContext(), planManageList);
		signInAssignAdapter = new EmergencyMenuRecyclerViewAdapter(getContext(), signInAssignList);
		planExecuteAdapter = new EmergencyMenuRecyclerViewAdapter(getContext(), planExecuteList);
		GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 4);
		GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 4);
		GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getContext(), 4);
		GridLayoutManager gridLayoutManager4 = new GridLayoutManager(getContext(), 4);
		GridLayoutManager gridLayoutManager5 = new GridLayoutManager(getContext(), 4);
		emergency_menu_lv_add_event.setLayoutManager(gridLayoutManager1);
		emergency_menu_lv_event_manage.setLayoutManager(gridLayoutManager2);
		emergency_menu_lv_plan_manage.setLayoutManager(gridLayoutManager3);
		emergency_menu_lv_sign_in_assign.setLayoutManager(gridLayoutManager4);
		emergency_menu_lv_plan_execute.setLayoutManager(gridLayoutManager5);

		addEventAdapter.setmOnItemClickListener(new EmergencyMenuRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (addEventList.get(position).getActivity() == null)
					return;
				Intent intent = new Intent(getActivity(),
						addEventList.get(position).getActivity());
				if(addEventList.get(position).getId().equals("emergency_evaluate"))
				{
					intent.putExtra("type", "0");
					intent.putExtra("tag", "1");
				}
				startActivity(intent);
			}
		});

		eventManageAdapter.setmOnItemClickListener(new EmergencyMenuRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (eventManageList.get(position).getActivity() == null)
					return;
				Intent intent = new Intent(getActivity(),
						eventManageList.get(position).getActivity());
				if(eventManageList.get(position).getId().equals("event_execute"))
				{
					intent.putExtra("tags", "2");
				}
				else if(eventManageList.get(position).getId().equals("event_complete"))
				{
					intent.putExtra("tags", "4");
				}
				startActivity(intent);
			}
		});

		planManageAdapter.setmOnItemClickListener(new EmergencyMenuRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (planManageList.get(position).getActivity() == null)
					return;
				Intent intent = new Intent(getActivity(),
						planManageList.get(position).getActivity());
				if(planManageList.get(position).getId().equals("wait_start"))
				{
					intent.putExtra("tags", "1");
				}
				else if(planManageList.get(position).getId().equals("started"))
				{
					intent.putExtra("tags", "2");
				}
				else if(planManageList.get(position).getId().equals("wait_authorize"))
				{
					intent.putExtra("tags", "1");
				}
				else if(planManageList.get(position).getId().equals("authorized"))
				{
					intent.putExtra("tags", "0");
				}
				startActivity(intent);
			}
		});

		signInAssignAdapter.setmOnItemClickListener(new EmergencyMenuRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (signInAssignList.get(position).getActivity() == null)
					return;
				Intent intent = new Intent(getActivity(),
						signInAssignList.get(position).getActivity());
				if(signInAssignList.get(position).getId().equals("people_assign"))
					intent.putExtra("tags", "3");
				startActivity(intent);
			}
		});

		planExecuteAdapter.setmOnItemClickListener(new EmergencyMenuRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (planExecuteList.get(position).getActivity() == null)
					return;
				Intent intent = new Intent(getActivity(),
						planExecuteList.get(position).getActivity());
                intent.putExtra("tags", "6");
				startActivity(intent);
			}
		});
		emergency_menu_lv_add_event.setAdapter(addEventAdapter);
		emergency_menu_lv_event_manage.setAdapter(eventManageAdapter);
		emergency_menu_lv_plan_manage.setAdapter(planManageAdapter);
		emergency_menu_lv_sign_in_assign.setAdapter(signInAssignAdapter);
		emergency_menu_lv_plan_execute.setAdapter(planExecuteAdapter);
		if (emergency_menu_lv_add_event.getVisibility() == View.INVISIBLE) {
			emergency_menu_lv_add_event.setVisibility(View.VISIBLE);
			emergency_menu_lv_event_manage.setVisibility(View.VISIBLE);
			emergency_menu_lv_plan_manage.setVisibility(View.VISIBLE);
			emergency_menu_lv_sign_in_assign.setVisibility(View.VISIBLE);
			emergency_menu_lv_plan_execute.setVisibility(View.VISIBLE);
		}
	}
}