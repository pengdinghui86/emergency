package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.control.PlanEntity;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjListEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.dssm.esc.view.adapter.EventProcessDetailListviewAdapter;
import com.dssm.esc.view.adapter.EventProcessPlanListViewAdapter;
import com.dssm.esc.view.widget.MyScrollView;
import com.dssm.esc.view.widget.RefreshLinearLayout;
import com.dssm.esc.view.widget.RingChartView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件流程图界面
 */
@ContentView(R.layout.activity_event_process_detail)
public class EventProcessDetailActivity extends BaseActivity implements MainActivity.onInitNetListener, View.OnClickListener {
	/** 下拉刷新控件 */
	@ViewInject(R.id.rll_event_process)
	private RefreshLinearLayout rll_event_process;
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	/** 传过来的事件编号 */
	private String eventId = "";
	/** 传过来的事件名称 */
	private String eventName = "";
	@ViewInject(R.id.event_process_detail_rcv)
	private RingChartView ringChartView;
	@ViewInject(R.id.event_process_detail_lv)
	private ListView listView;
	@ViewInject(R.id.event_process_detail_lv_plan)
	private ListView planListView;
	//0=事件流程图，1=预案列表，2=事件详情
	private String tag = "0";
	@ViewInject(R.id.rb_process)
	private RadioButton rb_process;
	@ViewInject(R.id.rb_plan)
	private RadioButton rb_plan;
	@ViewInject(R.id.rb_detail)
	private RadioButton rb_detail;
	/** 数据源 */
	private List<ProgressDetailEntity.EvenDetail> list = new ArrayList<ProgressDetailEntity.EvenDetail>();
	private List<PlanStarListEntity> planList = new ArrayList<PlanStarListEntity>();
	/** 适配器 */
	private EventProcessDetailListviewAdapter adapter;
	private EventProcessPlanListViewAdapter planAdapter;

	/**
	 * 事件编号
	 */
	@ViewInject(R.id.event_number)
	private TextView event_number;
	/**
	 * 事件名称
	 */
	@ViewInject(R.id.event_name)
	private TextView event_name;
	/**
	 * 事件提交人
	 */
	@ViewInject(R.id.event_people)
	private TextView eventpeople;
	/**
	 * 提交时间
	 */
	@ViewInject(R.id.event_time)
	private TextView event_time;
	/**
	 * 事件发现人
	 */
	@ViewInject(R.id.event_discoverer)
	private TextView event_discoverer;
	/**
	 * 事件发生时间
	 */
	@ViewInject(R.id.event_discovery_time)
	private TextView event_discovery_time;
	/**
	 * 已用时
	 */
	@ViewInject(R.id.event_over_time)
	private TextView event_over_time;

	/**
	 * 行业类型
	 */
	@ViewInject(R.id.business_type)
	private TextView business_type;
	/**
	 * 事件等级
	 */
	@ViewInject(R.id.event_level)
	private TextView event_level;
	/**
	 * 事件类型
	 */
	@ViewInject(R.id.event_type)
	private TextView event_type;

	/**
	 * 预案名称
	 */
	@ViewInject(R.id.plan_name)
	private TextView plan_name;
	/**
	 * 事件描述
	 */
	@ViewInject(R.id.event_des)
	private TextView event_des;
	/**
	 * 应对建议
	 */
	@ViewInject(R.id.suggestion)
	private TextView suggestion;
	/**
	 * 事件详情的总布局
	 */
	@ViewInject(R.id.event_detail_ll)
	private MyScrollView event_detail_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.event_process_detail);
		findViewById.setFitsSystemWindows(true);
		eventId = getIntent().getStringExtra("id");
		eventName = getIntent().getStringExtra("name");
		initView();
		initData();
	}

	private ControlServiceImpl.ControlServiceImplBackValueListenser<ProgressDetailEntity> controlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<ProgressDetailEntity>() {

		@Override
		public void setControlServiceImplListenser(ProgressDetailEntity backValue,
				String stRerror, String Exceptionerror) {
			// TODO Auto-generated method stub
			if (backValue != null) {
				ringChartView.setOutProgress(Integer.parseInt(backValue.getProgressNum()) / 6f * 100);
				List<ProgressDetailEntity.EvenDetail> list = new ArrayList<ProgressDetailEntity.EvenDetail>();
				list.add(backValue.getEveAssess()) ;//事件评估
				list.add(backValue.getPlanStart()) ;//预案启动
				list.add(backValue.getPlanAuth()) ;//决策授权
				list.add(backValue.getPersonSign()) ;//人员签到与指派
				list.add(backValue.getPlanPerform()) ;//预案执行
				list.add(backValue.getEveClose()) ;//时间关闭
				adapter.update(list, backValue.getNowTime());
			}else if (Exceptionerror!=null) {
				Toast.makeText(EventProcessDetailActivity.this, Const.NETWORKERROR, Toast.LENGTH_SHORT).show();
			}
			Utils.getInstance().hideProgressDialog();
			rll_event_process.onCompleteRefresh();
			rll_event_process.setResultSize(0, 3);
		}
	};

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListenser = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			PlanStarListDetailEntity detailEntity = null;
			if (object != null) {
				detailEntity = (PlanStarListDetailEntity) object;

			} else if (stRerror != null) {
				detailEntity = new PlanStarListDetailEntity();

			} else if (Exceptionerror != null) {
				detailEntity = new PlanStarListDetailEntity();
				ToastUtil.showToast(EventProcessDetailActivity.this,
						Const.NETWORKERROR);
			}
			String name = "", overTime;
			PlanStarListDetailObjEntity obj = new PlanStarListDetailObjEntity();
			if(detailEntity != null) {
				if (detailEntity.getObj() != null)
					obj = detailEntity.getObj();
			}
			else
				return;
			List<PlanStarListDetailObjListEntity> list = obj.getList();
			for (int i = 0; i < list.size(); i++) {
				name = name + "," + list.get(i).getName();
				if (name.subSequence(0, 1).equals(",")) {
					name = (String) name.subSequence(1, name.length());
				}
			}
			event_number.setText(obj.getEveCode());
			event_name.setText(obj.getEveName());
			eventpeople.setText(obj.getSubmitter());
			final String subTime = obj.getSubTime();
			event_time.setText(subTime);
			event_discoverer.setText(obj.getDiscoverer());
			event_discovery_time.setText(obj.getDiscoveryTime());
			String nowTime = obj.getNowTime();
			if(nowTime != null && subTime != null)
				overTime = Utils.getInstance().getOverTime(nowTime, subTime);
			else
				overTime = "";
			event_over_time.setText(overTime);
			business_type.setText(obj.getTradeType());
			event_level.setText(obj.getEveLevel());
			String eveType = obj.getEveType();
			if (eveType.equals("1")) {
				event_type.setText("应急");
			} else if (eveType.equals("2")) {
				event_type.setText("演练");
			}
			plan_name.setText(name);
			event_des.setText(obj.getEveDescription());
			suggestion.setText(obj.getDealAdvice());
			Utils.getInstance().hideProgressDialog();
		}
	};

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			List<PlanStarListEntity> dataList = null;
			if (object != null) {
				dataList = (List<PlanStarListEntity>) object;

			} else if (stRerror != null) {
				dataList = new ArrayList<PlanStarListEntity>();

			} else if (Exceptionerror != null) {
				dataList = new ArrayList<PlanStarListEntity>();
				ToastUtil.showToast(EventProcessDetailActivity.this,
						Const.NETWORKERROR);
			}
			planList.clear();
			planList.addAll(dataList);
			planAdapter.update(planList);
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void initData() {
		Utils.getInstance().showProgressDialog(
				EventProcessDetailActivity.this, "",
				Const.SUBMIT_MESSAGE);
		if("0".equals(tag))
			Control.getinstance().getControlSevice().getProgressDetail(eventId, controlServiceImplBackValueListenser);
		else if("1".equals(tag))
			Control.getinstance().getEmergencyService().getPlanListByEventId(eventId, listener);
		else if("2".equals(tag))
			Control.getinstance().getEmergencyService().getPlanStarListDetail(eventId, listListenser);
	}

	private void initView() {
		back.setOnClickListener(this);
		rb_process.setOnClickListener(this);
		rb_plan.setOnClickListener(this);
		rb_detail.setOnClickListener(this);
		rll_event_process.setOnRefreshListener(new RefreshLinearLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
			}
		});
		adapter = new EventProcessDetailListviewAdapter(this, list);
		planAdapter = new EventProcessPlanListViewAdapter(this, planList);
		listView.setAdapter(adapter);
		planListView.setAdapter(planAdapter);
		planListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent = new Intent(EventProcessDetailActivity.this,
						ControlActivity.class);
				intent.putExtra("PlanTreeEntity", convert2PlanEntity(planList.get(i)));
				startActivity(intent);
			}
		});
	}

	private PlanEntity convert2PlanEntity(PlanStarListEntity planStarListEntity)
	{
		PlanEntity planEntity = new PlanEntity();
		planEntity.setId(planStarListEntity.getId());
		planEntity.setPlanName(planStarListEntity.getPlanName());
		planEntity.setPlanResName(planStarListEntity.getPlanResName());
		planEntity.setPlanResType(planStarListEntity.getPlanResType());
		planEntity.setState(planStarListEntity.getState());
		planEntity.setPlanId(planStarListEntity.getPlanId());
		planEntity.setPlanResId(planStarListEntity.getPlanResId());
		return planEntity;
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
			case R.id.iv_actionbar_back:
				finish();
				break;
			case R.id.rb_process:
				tag = "0";
				rb_process.setChecked(true);
				rb_plan.setChecked(false);
				rb_detail.setChecked(false);
				ringChartView.setVisibility(View.VISIBLE);
				listView.setVisibility(View.VISIBLE);
				planListView.setVisibility(View.GONE);
				event_detail_ll.setVisibility(View.GONE);
				initData();
				break;
			case R.id.rb_plan:
				tag = "1";
				rb_process.setChecked(false);
				rb_plan.setChecked(true);
				rb_detail.setChecked(false);
				ringChartView.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				planListView.setVisibility(View.VISIBLE);
				event_detail_ll.setVisibility(View.GONE);
				initData();
				break;
			case R.id.rb_detail:
				tag = "2";
				rb_process.setChecked(false);
				rb_plan.setChecked(false);
				rb_detail.setChecked(true);
				ringChartView.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				planListView.setVisibility(View.GONE);
				event_detail_ll.setVisibility(View.VISIBLE);
				initData();
				break;
		}
	}
}
