package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.control.EventProgressEntity;
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
	private List<EventProgressEntity> list = new ArrayList<>();
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
	/** 应急处置流程 */
	@ViewInject(R.id.emergency_disposal_process)
	private TextView emergency_disposal_process;
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
	/**
	 * 暂无数据布局
	 */
	@ViewInject(R.id.ll_no_data_page)
	private LinearLayout ll_no_data_page;
	private String status = "";// 事件状态
	private String closeTime = "";// 事件关闭时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.event_process_detail);
		findViewById.setFitsSystemWindows(true);
		eventId = getIntent().getStringExtra("id");
		eventName = getIntent().getStringExtra("name");
		status = getIntent().getStringExtra("status");
		closeTime = getIntent().getStringExtra("closeTime");
		initView();
		initData();
	}

	private ControlServiceImpl.ControlServiceImplBackValueListenser<List<EventProgressEntity>> controlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<List<EventProgressEntity>>() {

		@Override
		public void setControlServiceImplListenser(List<EventProgressEntity> backValue,
				String stRerror, String Exceptionerror) {
			List<EventProgressEntity> eventProgressEntities = null;
			ll_no_data_page.setVisibility(View.VISIBLE);
			if (backValue != null) {
				eventProgressEntities = (List<EventProgressEntity>) backValue;
				if(eventProgressEntities != null && eventProgressEntities.size() > 0)
				{
					ll_no_data_page.setVisibility(View.GONE);
					eventProgressEntities = processData(eventProgressEntities);
					list.clear();
					list.addAll(eventProgressEntities);
					adapter.notifyDataSetChanged();
				}
			}else if (Exceptionerror != null) {
				Toast.makeText(EventProcessDetailActivity.this, Const.NETWORKERROR, Toast.LENGTH_SHORT).show();
			}
			Utils.getInstance().hideProgressDialog();
			rll_event_process.onCompleteRefresh();
			rll_event_process.setResultSize(0, 3);
		}
	};

	private List<EventProgressEntity> processData(List<EventProgressEntity> list)
	{
		List<EventProgressEntity> result = new ArrayList<>();
		int index = 0;
		for(EventProgressEntity entity : list)
		{
			//步骤类型：0：事件发生，1：事件评估，2：事件通告，
			// 3：事件升级，4：事件降级，5：预案启动，6：预案授权，
			// 7：预案中止，8：预案完成，9：事件关闭
			String stepName = entity.getStep();
			String stepType = entity.getStepType();
			String planName = entity.getPlanName();
			String operatorName = entity.getOperatorName();
			String operationTime = entity.getOperationTime();
			String eveLevelName = entity.getEveLevelName();
			String content = "null".equals(entity.getContent()) ? "" : entity.getContent();
			String appendContent = "";
			//合并标记
			boolean flag = false;
			//发生时间点一致的步骤进行显示合并
			while (index + 1 < list.size()
					? operationTime.equals(
							list.get(index + 1).getOperationTime())
					: false) {
				String nextStepType = list.get(index + 1).getStepType();
				stepName += "&" + list.get(index + 1).getStep();
				appendContent = createContent(appendContent, content, nextStepType,
						operatorName, eveLevelName, planName);
				flag = true;
				index++;
			}
			if(index >= list.size()) {
				EventProgressEntity item = new EventProgressEntity();
				item.setId(entity.getId());
				item.setDiscoveryTime(entity.getDiscoveryTime());
				item.setOperationTime(operationTime);
				if(appendContent.length() > 0)
                    appendContent = appendContent.substring(1, appendContent.length());
				item.setContent(appendContent);
				item.setStepType(stepType);
				item.setStep(stepName);
				item.setOperatorName(operatorName);
				result.add(item);
				break;
			}
			if(flag) {
				index++;
				continue;
			}
			appendContent = createContent(appendContent, content, stepType,
					operatorName, eveLevelName, planName);
			EventProgressEntity item = new EventProgressEntity();
			item.setId(entity.getId());
			item.setDiscoveryTime(entity.getDiscoveryTime());
			item.setOperationTime(operationTime);
            if(appendContent.length() > 0)
                appendContent = appendContent.substring(1, appendContent.length());
            item.setContent(appendContent);
			item.setStepType(stepType);
			item.setStep(stepName);
			item.setOperatorName(operatorName);
			result.add(item);
			index++;
		}
		return result;
	}

	private String createContent(String appendContent, String content,
							   String type, String operatorName,
							   String eveLevelName, String planName)
	{
		String ressult = appendContent;
		if ("0".equals(type)){
			ressult += "；发现人：" + operatorName;
		}else if("1".equals(type)){
			ressult += "；评估人：" + operatorName
					+ "，预判事件等级：" + eveLevelName;
		}else if("2".equals(type)){
			ressult += "；" + content;
		}else if("3".equals(type)){
			ressult += "；" + content;
		}else if("4".equals(type)){
			ressult += "；" + content;
		}else if("5".equals(type)){
			ressult += "；启动：" + planName + "，启动人："
					+ operatorName;
		}else if("6".equals(type)){
			ressult += "；授权：" + planName + "，授权人：" + operatorName;
		}else if("7".equals(type)){
			ressult += "；中止：" + planName + "，中止人：" + operatorName;
		}else if("8".equals(type)){
			ressult += "；" + planName + "执行完成";
		}else if("9".equals(type)){
			ressult += "；关闭人:" + operatorName;
		}
		return ressult;
	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListenser = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			PlanStarListDetailEntity detailEntity = null;
			ll_no_data_page.setVisibility(View.GONE);
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
				name = name + "," + list.get(i).getName() + "-" + list.get(i).getSceneName();
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
			String emergencyProcess = "";
			if("1".equals(obj.getIsPreStart()))
				emergencyProcess += ",预案启动";
			if("1".equals(obj.getIsAuthori()))
				emergencyProcess += ",预案授权";
			if("1".equals(obj.getIsSign()))
				emergencyProcess += ",人员签到";
			if (emergencyProcess.length() > 0) {
				emergencyProcess = emergencyProcess.substring(1, emergencyProcess.length());
			}
			emergency_disposal_process.setText(emergencyProcess);
			String nowTime = obj.getNowTime();
			//事件已结束
			if("3".equals(status) && closeTime != null
					&& !"".equals(closeTime)
					&& !"null".equals(closeTime))
			{
				nowTime = closeTime;
			}
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
			rll_event_process.onCompleteRefresh();
			rll_event_process.setResultSize(0, 3);
			Utils.getInstance().hideProgressDialog();
		}
	};

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			List<PlanStarListEntity> dataList = null;
			ll_no_data_page.setVisibility(View.VISIBLE);
			if (object != null) {
				dataList = (List<PlanStarListEntity>) object;
				if(dataList.size() > 0)
				    ll_no_data_page.setVisibility(View.GONE);
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
			rll_event_process.onCompleteRefresh();
			rll_event_process.setResultSize(0, 3);
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
				listView.setVisibility(View.GONE);
				planListView.setVisibility(View.GONE);
				event_detail_ll.setVisibility(View.VISIBLE);
				initData();
				break;
		}
	}
}
