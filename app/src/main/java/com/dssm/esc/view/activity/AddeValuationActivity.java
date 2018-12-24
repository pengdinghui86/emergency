package com.dssm.esc.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;
import com.dssm.esc.model.entity.emergency.EmergencyPlanEvaAddEntity;
import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.model.entity.emergency.PlanNameRowEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.widget.TimePickerDialog;
import com.easemob.chatuidemo.DemoApplication;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 添加评估界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-10
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_addvaluation)
public class AddeValuationActivity extends BaseActivity implements
		OnClickListener, TimePickerDialog.TimePickerDialogInterface{
	/** 事件名称布局 */
	@ViewInject(R.id.add_valuation_ll_event_name)
	private LinearLayout event_name_ll;
	/** 事件描述布局 */
	@ViewInject(R.id.event_des_ll)
	private LinearLayout event_des_ll;
	/** 应对建议布局 */
	@ViewInject(R.id.suggestion_ll)
	private LinearLayout suggestion_ll;
	/** 事件名称 */
	@ViewInject(R.id.event_name)
	private TextView event_name;
	/** 行业类型布局 */
	@ViewInject(R.id.business_type_ll)
	private LinearLayout business_type_ll;
	/** 行业类型 */
	@ViewInject(R.id.business_type)
	private TextView business_type;
	/** 事件等级布局 */
	@ViewInject(R.id.event_level_ll)
	private LinearLayout event_level_ll;
	/** 事件等级 */
	@ViewInject(R.id.event_level)
	private TextView event_level;
	/** 事件发生时间布局 */
	@ViewInject(R.id.event_happen_time_ll)
	private LinearLayout event_happen_time_ll;
	/** 事件发生时间 */
	@ViewInject(R.id.event_happen_time)
	private TextView event_happen_time;
	/** 事件发生时间下方的分隔线 */
	@ViewInject(R.id.event_happen_time_view)
	private View event_happen_time_view;

	/** 事件发现人布局 */
	@ViewInject(R.id.event_discover_ll)
	private LinearLayout event_discover_ll;
	/** 事件发现人 */
	@ViewInject(R.id.event_discover)
	private TextView event_discover;

	/** 应急预案布局 */
	@ViewInject(R.id.plan_ll)
	private LinearLayout plan_ll;
	/** 演练预案布局 */
	@ViewInject(R.id.emergType_ll)
	private LinearLayout emergType_ll;
	/** 演练预案名称 */
	@ViewInject(R.id.emergType)
	private TextView emergTypetv;
	/** 演练类型布局 */
	@ViewInject(R.id.plan_ll_2)
	private LinearLayout plan_ll_2;
	/** 演练类型 */
	@ViewInject(R.id.plan_name)
	private TextView plan_name;
	@ViewInject(R.id.line)
	private View line;

	/** 参考预案布局 */
	@ViewInject(R.id.referPlan_name_ll)
	private LinearLayout referPlan_name_ll;
	/** 参考预案 */
	@ViewInject(R.id.referPlan_name)
	private TextView referPlan_name;
	/** 应急处置流程布局 */
	@ViewInject(R.id.emergency_disposal_process_ll)
	private LinearLayout emergency_disposal_process_ll;
	/** 应急处置流程 */
	@ViewInject(R.id.emergency_disposal_process)
	private TextView emergency_disposal_process;
	/** 其他预案布局 */
	@ViewInject(R.id.otherReferPlan_name_ll)
	private LinearLayout otherReferPlan_name_ll;
	/** 其他预案 */
	@ViewInject(R.id.otherReferPlan_name)
	private TextView otherReferPlan_name;
	/** 分类预案布局 */
	@ViewInject(R.id.categoryPlan_name_ll)
	private LinearLayout categoryPlan_name_ll;
	/** 分类预案 */
	@ViewInject(R.id.categoryPlan_name)
	private TextView categoryPlan_name;
	/** 事件描述 */
	@ViewInject(R.id.event_des)
	private TextView event_des;
	/** 应对建议 */
	@ViewInject(R.id.suggestion)
	private TextView suggestion;
	/** 1,应急;2,演练 */
	private String tag = "";
	/** 提交 */
	@ViewInject(R.id.submittv)
	private TextView submit;
	/** 业务类型 */
	private String tradeType = "";
	private String tradeTypeId = "";
	/** 事件等级 */
	private String eveLevel = "";
	private String eveLevelId = "";
	/** 事件描述 */
	private String eveDescription = "";
	/** 事件发现人 */
	private String eveDiscover = "";
	/** 事件发生时间 */
	private String eveDiscoveryTime = "";
	/** 演练类型 */
	private String emergType = "";
	/** 事件名称 */
	private String eveName = "";
	/** 处置建议 */
	private String dealAdvice = "";
	/** 参考预案 可以多选，以“|”隔开 */
	private String referPlan = "";
	/** 应急处置流程 可以多选，以“|”隔开 */
	private String emergencyDisposalProcess = "";
	/** 参考预案下面的场景预案 可以多选，以“|”隔开 */
	private String referProcess = "";
	/** 其他预案 可以多选，以“|”隔开 */
	private String otherReferPlan = "";
	/** 分类预案 可以多选，以“|”隔开 */
	private String categoryPlan = "";
	/** 事件类型 1为应急，2为演练 */
	private String eveType;
	/** 演练详细计划ID eveType为2时传入 */
	private String drillPlanId = "";
	/** 演练详细计划名称 eveType为2时传入 */
	private String drillPlanName = "";
	/** 演练初始计划id */
	private String exPlanId = "";
	/** 预案场景id */
	private String id = "";
	/** 演练预案计划名称 */
	private String precautionName = "";
	/** 演练预案计划id */
	private String precautionId = "";
	private TimePickerDialog mTimePickerDialog;
	Intent intent;
	/** 1,重新评估0，添加评估 */
	private String type;
	EmergencyPlanEvaAddEntity addEntity = new EmergencyPlanEvaAddEntity();
	private GetProjectEveInfoEntity entity;
	private String referPlanNames = "";
	private String referPlanIds = "";
	private String referIds = "";
	private String otherPlanNames = "";
	private String otherPlanIds = "";
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	/** 被选的预案id */
	private ArrayList<PlanNameRowEntity> selectedIds = new ArrayList<PlanNameRowEntity>();
    public static int EVENT_NAME_INPUT = 1001;
    public static int EVENT_DISCOVER_INPUT = 1004;
    public static int EVENT_DES_INPUT = 1002;
    public static int EVENT_SUGGESTION_INPUT = 1003;
	private MySharePreferencesService preferencesService;
	private Map<String, String> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_addvaluation);
		View findViewById = findViewById(R.id.addvaluation);
		findViewById.setFitsSystemWindows(true);
		intent = getIntent();
		mTimePickerDialog = new TimePickerDialog(AddeValuationActivity.this);
		type = intent.getStringExtra("type");
		precautionName = intent.getStringExtra("precautionName");
		precautionId = intent.getStringExtra("precautionId");
		emergType = intent.getStringExtra("emergType");
		initview();
		switchView(tag);
	}

	private void initview() {
		// TODO Auto-generated method stub
		preferencesService = new MySharePreferencesService(DemoApplication.applicationContext);
		map = preferencesService.getPreferences();
		back.setVisibility(View.VISIBLE);
		event_name.setFocusable(true);
		event_name.setFocusableInTouchMode(true);
		event_name.requestFocus();
		event_discover.setText("");

		if (type.equals("1")) {
			entity = (GetProjectEveInfoEntity) intent
					.getSerializableExtra("entity");
			tag = entity.getEveType();
			String eveType2 = entity.getEveType();
			if (eveType2.equals("1")) {
				title.setText("应急-重新评估");
				emergency_disposal_process_ll.setVisibility(View.VISIBLE);
			} else if (eveType2.equals("2")) {
				title.setText("演练-重新评估");
				emergency_disposal_process_ll.setVisibility(View.VISIBLE);
			}

		} else {
			tag = intent.getStringExtra("tag");
			if (tag.equals("1")) {
				title.setText("应急-新增评估");
				emergency_disposal_process_ll.setVisibility(View.VISIBLE);
				emergency_disposal_process_ll.setOnClickListener(this);
			} else if (tag.equals("2")) {
				emergency_disposal_process_ll.setVisibility(View.GONE);
				title.setText("演练-新增评估");
			}
			event_name_ll.setOnClickListener(this);
			event_discover_ll.setOnClickListener(this);
			otherReferPlan_name_ll.setOnClickListener(this);// 其他预案布局
			categoryPlan_name_ll.setOnClickListener(this);// 分类预案布局
			business_type_ll.setOnClickListener(this);// 行业类型布局
			event_happen_time_ll.setOnClickListener(this);// 事件发生时间布局
			referPlan_name_ll.setOnClickListener(this);// 参考预案布局
		}
		otherReferPlan_name_ll.setVisibility(View.GONE);
		event_level_ll.setOnClickListener(this);// 事件等级布局
		submit.setOnClickListener(this);// 提交数据
		event_des_ll.setOnClickListener(this);
		suggestion_ll.setOnClickListener(this);
	}

	public void switchView(String tag) {
		if (tag.equals("1")) {// 应急
			plan_ll.setVisibility(View.VISIBLE);
			plan_ll_2.setVisibility(View.GONE);
			emergType_ll.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			if (type.equals("1")) {// 重新评估
				eveName = entity.getEveName();
				tradeTypeId = entity.getTradeTypeId();
				eveLevelId = entity.getEveLevelId();
				eveDiscover = entity.getEveDiscover();
				id = eveDiscover;
				eveDiscoveryTime = entity.getEveDiscoveryTime();
				eveDescription = entity.getEveDescription();
				dealAdvice = entity.getDealAdvice();
				event_name.setText(eveName);
				business_type.setText(entity.getTradeTypeName());
				event_level.setText(entity.getEveLevelName());
				event_discover.setText(eveDiscover);
				event_happen_time.setText(eveDiscoveryTime);
				List<Map<String, String>> referPlan2 = entity.getReferPlan();
				if (referPlan2 != null && referPlan2.size() > 0) {

					for (int i = 0; i < referPlan2.size(); i++) {
						Map<String, String> map = referPlan2.get(i);
						String id = map.get("id");
						String name = map.get("name");
						referPlanIds = referPlanIds + "|" + id;
						referPlanNames = referPlanNames + "," + name;
					}
					if (referPlanIds.subSequence(0, 1).equals("|")) {
						referPlanIds = (String) referPlanIds.subSequence(1,
								referPlanIds.length());
					}
					if (referPlanNames.subSequence(0, 1).equals(",")) {
						referPlanNames = (String) referPlanNames.subSequence(1,
								referPlanNames.length());
					}
				}
				referPlan = referPlanIds;
				referPlan_name.setText(referPlanNames);
				List<Map<String, String>> otherPlan2 = entity
						.getOtherReferPlan();
				if (otherPlan2 != null && otherPlan2.size() > 0) {

					for (int i = 0; i < otherPlan2.size(); i++) {
						Map<String, String> map = otherPlan2.get(i);
						String id = map.get("id");
						String name = map.get("name");
						otherPlanIds = otherPlanIds + "|" + id;
						otherPlanNames = otherPlanNames + "," + name;
					}
					if (otherPlanIds.subSequence(0, 1).equals("|")) {
						otherPlanIds = (String) otherPlanIds.subSequence(1,
								otherPlanIds.length());
					}
					if (otherPlanNames.subSequence(0, 1).equals(",")) {
						otherPlanNames = (String) otherPlanNames.subSequence(1,
								otherPlanNames.length());
					}
				}
				otherReferPlan = otherPlanIds;
				otherReferPlan_name.setText(otherPlanNames);
				event_des.setText(entity.getEveDescription());
				suggestion.setText(entity.getDealAdvice());
			}
		} else if (tag.equals("2")) {// 演练
			plan_ll.setVisibility(View.GONE);
			plan_ll_2.setVisibility(View.VISIBLE);
			emergType_ll.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
			if (type.equals("1")) {// 重新评估
				tradeTypeId = entity.getTradeTypeId();// 行业类型id
				eveLevelId = entity.getEveLevelId();// 事件等级id
				referPlan = precautionId;// 预案id
				referProcess = id;// 场景流程id
				eveDiscover = entity.getEveDiscover();// 事件场景id

				event_name.setText(entity.getEveName());
				business_type.setText(entity.getTradeTypeName());
				event_level.setText(entity.getEveLevelName());
				event_discover.setText(entity.getEveDiscover());
				event_happen_time.setText(entity.getEveDiscoveryTime());
				event_des.setText(entity.getEveDescription());
				suggestion.setText(entity.getDealAdvice());
				List<Map<String, String>> referPlan2 = entity.getReferPlan();
				if (referPlan2.size() > 0) {

					for (int i = 0; i < referPlan2.size(); i++) {
						Map<String, String> map = referPlan2.get(i);
						String id = map.get("id");
						String name = map.get("name");
						referPlanIds = referPlanIds + "|" + id;
						referPlanNames = referPlanNames + "," + name;
					}
					if (referPlanIds.subSequence(0, 1).equals("|")) {
						referPlanIds = (String) referPlanIds.subSequence(1,
								referPlanIds.length());
					}
					if (referPlanNames.subSequence(0, 1).equals(",")) {
						referPlanNames = (String) referPlanNames.subSequence(1,
								referPlanNames.length());
					}
				}
				plan_name.setText(referPlanNames);
				emergTypetv.setText(entity.getEmergType());
			} else {
				emergTypetv.setText(emergType);
				plan_name.setText(precautionName);

			}
		}
	}

	/** 带回来的集合 */
	List<BusinessTypeEntity> resutList1 = new ArrayList<BusinessTypeEntity>();
	List<BusinessTypeEntity> resutList2 = new ArrayList<BusinessTypeEntity>();
	List<BusinessTypeEntity> resutList3 = new ArrayList<BusinessTypeEntity>();
	private List<PlanNameRowEntity> resutList4 = new ArrayList<PlanNameRowEntity>();
	private List<PlanNameRowEntity> resutList5 = new ArrayList<PlanNameRowEntity>();
	private List<PlanNameRowEntity> resutList6 = new ArrayList<PlanNameRowEntity>();
	private List<PlanNameRowEntity> resutList7 = new ArrayList<PlanNameRowEntity>();

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (view.getId()) {
			case R.id.business_type_ll: // 业务类型布局
				intent = new Intent(AddeValuationActivity.this,
						EmergencyTypeActivity.class);
				// intent.putExtra("tag", tag);
				Bundle bundle = new Bundle();
				bundle.putSerializable("arrlist", (Serializable) resutList1);
				bundle.putString("tags", "1");
				intent.putExtras(bundle);
				startActivityForResult(intent, 1);
				break;
			case R.id.event_level_ll: // 事件等级布局
				intent = new Intent(AddeValuationActivity.this,
						EmergencyTypeActivity.class);
				// intent.putExtra("tag", tag);
				Bundle bundle2 = new Bundle();
				bundle2.putSerializable("arrlist", (Serializable) resutList2);
				bundle2.putString("tags", "2");
				intent.putExtras(bundle2);
				startActivityForResult(intent, 2);
				break;
//			case R.id.event_background_ll: // 事件场景布局
//				intent = new Intent(AddeValuationActivity.this,
//						EmergencyTypeActivity.class);
//				// intent.putExtra("tag", tag);
//				Bundle bundle3 = new Bundle();
//				bundle3.putSerializable("arrlist", (Serializable) resutList3);
//				bundle3.putString("tags", "3");
//				intent.putExtras(bundle3);
//				startActivityForResult(intent, 3);
//				break;

			case R.id.referPlan_name_ll: // 涉及预案布局
				intent = new Intent(AddeValuationActivity.this,
						PlanNameActivity.class);
				Bundle bundle4 = new Bundle();
				bundle4.putInt("plantags", 1);
				bundle4.putString("tags", "2");
				bundle4.putString("businessType", tradeTypeId);
				bundle4.putSerializable("arrlist", (Serializable) resutList4);
				intent.putExtras(bundle4);
				startActivityForResult(intent, 4);
				break;
				case R.id.emergency_disposal_process_ll: // 应急处置流程布局
				intent = new Intent(AddeValuationActivity.this,
						PlanNameActivity.class);
				Bundle bundle7 = new Bundle();
				bundle7.putInt("plantags", 5);
				bundle7.putString("tags", "2");
				bundle7.putSerializable("arrlist", (Serializable) resutList7);
				intent.putExtras(bundle7);
				startActivityForResult(intent, 7);
				break;
			case R.id.otherReferPlan_name_ll: // 其他预案布局

				intent = new Intent(AddeValuationActivity.this,
						PlanNameActivity.class);
				Bundle bundle5 = new Bundle();
				bundle5.putString("id", id);
				bundle5.putInt("plantags", 2);
				bundle5.putString("tags", "2");
				bundle5.putSerializable("arrlist", (Serializable) resutList5);
				intent.putExtras(bundle5);
				startActivityForResult(intent, 5);
				break;
			case R.id.categoryPlan_name_ll: // 分类预案布局
				intent = new Intent(AddeValuationActivity.this,
						PlanNameActivity.class);
				Bundle bundle6 = new Bundle();
				bundle6.putString("id", tradeTypeId);// 根据业务类型来过滤
				bundle6.putInt("plantags", 3);
				bundle6.putString("tags", "2");
				bundle6.putSerializable("arrlist", (Serializable) resutList6);
				intent.putExtras(bundle6);
				startActivityForResult(intent, 6);
				break;
			case R.id.submittv:// 提交数据
				AlertDialog.Builder adBuilder = new AlertDialog.Builder(AddeValuationActivity.this);
				adBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
						addEventValuation();
					}
				});
				adBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});
				adBuilder.setTitle("提示");
				adBuilder.setMessage("确定添加事件评估");
				adBuilder.setCancelable(true);
				adBuilder.show();
				break;
			case R.id.add_valuation_ll_event_name:
				intent = new Intent(AddeValuationActivity.this,
						InputTextActivity.class);
				intent.putExtra("title", "请输入名称");
				intent.putExtra("content", event_name.getText().toString());
				startActivityForResult(intent, EVENT_NAME_INPUT);
				break;
			case R.id.event_happen_time_ll:
				mTimePickerDialog.showDateAndTimePickerDialog();
				break;
			case R.id.event_discover_ll:
				intent = new Intent(AddeValuationActivity.this,
						InputTextActivity.class);
				intent.putExtra("title", "请输入事件发现人");
				intent.putExtra("content", event_discover.getText().toString());
				startActivityForResult(intent, EVENT_DISCOVER_INPUT);
				break;
			case R.id.event_des_ll:
				intent = new Intent(AddeValuationActivity.this,
						InputTextActivity.class);
				intent.putExtra("title", "请输入描述");
				intent.putExtra("content", event_des.getText().toString());
				startActivityForResult(intent, EVENT_DES_INPUT);
				break;
			case R.id.suggestion_ll:
				intent = new Intent(AddeValuationActivity.this,
						InputTextActivity.class);
				intent.putExtra("title", "请输入建议");
				intent.putExtra("content", suggestion.getText().toString());
				startActivityForResult(intent, EVENT_SUGGESTION_INPUT);
				break;
		}
	}

	private void addEventValuation() {
		eveName = event_name.getText().toString().trim();
		eveDescription = event_des.getText().toString().trim();
		dealAdvice = suggestion.getText().toString().trim();
		if (tag.equals("1")) {
			eveType = "1";
		} else if (tag.equals("2")) {
			eveType = "2";
			Intent intent2 = getIntent();
			emergType = intent2.getStringExtra("emergType");
			drillPlanId = intent2.getStringExtra("drillPlanId");
			drillPlanName = intent2.getStringExtra("drillPlanName");
			exPlanId = intent2.getStringExtra("exPlanId");
			referPlan = precautionId;
			referProcess = intent2.getStringExtra("referProcess");
		}
		if (type.equals("1")) {// 重新评估
			entity.setTradeTypeId(tradeTypeId);// 行业类型
			entity.setEveLevelId(eveLevelId);// 事件等级
			entity.setEveDescription(eveDescription);// 事件描述
			entity.setEveScenarioId(eveDiscover);// 事件场景
			entity.setEveScenarioName(eveDiscoveryTime);// 事件场景名称
			entity.setEveName(eveName);// 事件名称
			entity.setEveDiscover(event_discover.getText().toString());// 事件发现人
			entity.setReferPlanIds(referPlan);// 涉及预案
			entity.setDealAdvice(dealAdvice);// 处置建议
			entity.setOtherReferPlanIds(otherReferPlan);// 其他预案
			entity.setCategoryPlanIds(categoryPlan);// 分类预案
			String planall = "";
			planall = referPlan + otherReferPlan + categoryPlan;
			if (!tradeTypeId.equals("") && !eveLevelId.equals("")
					&& !eveDescription.equals("") && !dealAdvice.equals("")
					&& !eveName.equals("") && !planall.equals("")) {
				reValuation();

			} else {
				ToastUtil.showToast(AddeValuationActivity.this, "信息不完整");
			}
		} else if (type.equals("0")) {// 添加评估
			addEntity.setTradeType(tradeTypeId);// 行业类型
			addEntity.setEveLevel(eveLevelId);// 事件等级
			addEntity.setEveDescription(eveDescription);// 事件描述
			addEntity.setEveScenarioId(eveDiscover);// 事件场景
			addEntity.setEveScenarioName(eveDiscoveryTime);// 事件场景名称
			addEntity.setEmergType(emergType);// 演练类型
			addEntity.setEveName(eveName);// 事件名称
			addEntity.setEventDiscover(event_discover.getText().toString());// 事件发现人
			addEntity.setEventHappenTime(event_happen_time.getText().toString());// 事件发生时间
			addEntity.setRefPlanId(referPlan);// 涉及预案
			addEntity.setDealAdvice(dealAdvice);// 处置建议
			addEntity.setReferPlan(referPlan);// 参考预案
			addEntity.setReferProcess(referProcess);// 参考预案
			addEntity.setOtherReferPlan(otherReferPlan);// 其他预案
			addEntity.setCategoryPlan(categoryPlan);// 分类预案
			addEntity.setEveType(eveType);// 事件类型
			addEntity.setDrillPlanId(drillPlanId);// 演练详细计划ID
			addEntity.setDrillPlanName(drillPlanName);// 演练详细计划名称
			addEntity.setExPlanId(exPlanId);// 演练初始计划id
			//预案启动
			if(emergencyDisposalProcess.indexOf("1") > -1)
			{
				addEntity.setIsPreStart("1");
			}
			else
				addEntity.setIsPreStart("0");
			//预案授权
			if(emergencyDisposalProcess.indexOf("2") > -1)
			{
				addEntity.setIsAuthori("1");
			}
			else
				addEntity.setIsAuthori("0");
			//人员签到
			if(emergencyDisposalProcess.indexOf("3") > -1)
			{
				addEntity.setIsSign("1");
			}
			else
				addEntity.setIsSign("0");
			String planall = "";
			planall = referPlan + otherReferPlan + categoryPlan;
			Log.i("referPlan", referPlan);
			Log.i("otherReferPlan", otherReferPlan);
			Log.i("categoryPlan", categoryPlan);
			Log.i("planall", planall);
			if (tag.equals("1")) {
				if (!tradeTypeId.equals("") && !eveLevelId.equals("")
						&& !eveDescription.equals("")
						&& !dealAdvice.equals("") && !eveName.equals("")
						&& !planall.equals("")
						&& event_discover.getText().toString() != null
						&& !"".equals(event_discover.getText().toString())
						&& event_happen_time.getText().toString() != null
						&& !"".equals(event_happen_time.getText().toString())) {

					addValuation();

				} else {
					ToastUtil
							.showToast(AddeValuationActivity.this, "信息不完整");
				}

			} else if (tag.equals("2")) {
				if (!tradeTypeId.equals("") && !eveLevelId.equals("")
						&& !eveDescription.equals("")
						&& !dealAdvice.equals("") && !eveName.equals("")
						&& !precautionId.equals("")
						&& event_discover.getText().toString() != null
						&& !"".equals(event_discover.getText().toString())
						&& event_happen_time.getText().toString() != null
						&& !"".equals(event_happen_time.getText().toString())) {
					addValuation();
				} else {
					ToastUtil
							.showToast(AddeValuationActivity.this, "信息不完整");
				}
			}

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 1:
			tradeType = "";
			if (data != null && resultCode == RESULT_OK) {

				resutList1 = (ArrayList<BusinessTypeEntity>) data
						.getSerializableExtra("arrlist");
				ArrayList<BusinessTypeEntity> typelist1 = new ArrayList<BusinessTypeEntity>();
				if (resutList1 != null && resutList1.size() > 0) {
					typelist1.clear();
					typelist1.addAll(resutList1);
					for (int i = 0; i < typelist1.size(); i++) {
						BusinessTypeEntity businessTypeEntity = typelist1
								.get(i);
						boolean select = businessTypeEntity.isSelect();
						if (select) {
							tradeType = businessTypeEntity.getName();
							tradeTypeId = businessTypeEntity.getId();
						}
					}
					if (resutList6 != null && resutList6.size() > 0) {

						resutList6.clear();
						categoryPlan_name.setText("");
					}
					if (selectedIds != null && selectedIds.size() > 0) {

						selectedIds.clear();
					}
				}
				business_type.setText(tradeType);
			}

			break;
		case 2:
			eveLevel = "";
			if (data != null && resultCode == RESULT_OK) {
				resutList2 = (ArrayList<BusinessTypeEntity>) data
						.getSerializableExtra("arrlist");
				ArrayList<BusinessTypeEntity> typelist2 = new ArrayList<BusinessTypeEntity>();
				if (resutList2 != null && resutList2.size() > 0) {
					typelist2.clear();
					typelist2.addAll(resutList2);
					for (int i = 0; i < typelist2.size(); i++) {
						BusinessTypeEntity businessTypeEntity = typelist2
								.get(i);
						boolean select = businessTypeEntity.isSelect();
						if (select) {
							eveLevel = businessTypeEntity.getName();
							eveLevelId = businessTypeEntity.getId();
						}
					}
				}
				event_level.setText(eveLevel);
			}
			break;
		case 3:
			eveDiscoveryTime = "";
			if (data != null && resultCode == RESULT_OK) {
				resutList3 = (ArrayList<BusinessTypeEntity>) data
						.getSerializableExtra("arrlist");
				ArrayList<BusinessTypeEntity> typelist3 = new ArrayList<BusinessTypeEntity>();
				if (resutList3 != null && resutList3.size() > 0) {
					typelist3.clear();
					typelist3.addAll(resutList3);
					for (int i = 0; i < typelist3.size(); i++) {
						BusinessTypeEntity businessTypeEntity = typelist3
								.get(i);
						boolean select = businessTypeEntity.isSelect();
						if (select) {
							eveDiscoveryTime = businessTypeEntity.getName();
							eveDiscover = businessTypeEntity.getId();
							id = businessTypeEntity.getId();
						}
					}
					if (resutList4 != null && resutList4.size() > 0) {
						resutList4.clear();
						referPlan_name.setText("");

					}
					if (resutList5 != null && resutList5.size() > 0) {
						resutList5.clear();
						otherReferPlan_name.setText("");

					}
					if (selectedIds != null && selectedIds.size() > 0) {

						selectedIds.clear();
					}
				}
				event_happen_time.setText(eveDiscoveryTime);
			}

			break;
		case 4:
			String str4 = "";
			selectedIds.clear();
			referPlan = "";
			if (data != null && resultCode == RESULT_OK) {
				// Bundle bundle = data.getExtras();
				// resutList4 = (List<PlanNameRowEntity>)
				// bundle.getSerializable("arrlist");
				resutList4 = (ArrayList<PlanNameRowEntity>) data
						.getSerializableExtra("arrlist");
				ArrayList<PlanNameRowEntity> typelist4 = new ArrayList<PlanNameRowEntity>();
				if (resutList4 != null && resutList4.size() > 0) {
					typelist4.clear();
					typelist4.addAll(resutList4);
					if (typelist4.size() > 0) {
						for (int i = 0; i < typelist4.size(); i++) {
							if (typelist4.get(i).isSelect()) {

								str4 = str4 + "," + typelist4.get(i).getName()
										+ (typelist4.get(i).getSceneName() != null
										&& !"".equals(typelist4.get(i).getSceneName())
										? "-" + typelist4.get(i).getSceneName() : "");
								referPlan = referPlan + "|"
										+ typelist4.get(i).getPrecautionId();
								referProcess = referProcess + "|"
										+ typelist4.get(i).getId();
								PlanNameRowEntity entity = new PlanNameRowEntity();
								entity.setId(typelist4.get(i).getId());
								selectedIds.add(entity);
							}
						}
						if (referPlan.subSequence(0, 1).equals("|")) {
							referPlan = (String) referPlan.subSequence(1,
									referPlan.length());
						}
						if (referProcess.subSequence(0, 1).equals("|")) {
							referProcess = (String) referProcess.subSequence(1,
									referProcess.length());
						}
					}
				}
			}
			if (str4.length() > 0) {

				referPlan_name.setText(str4.substring(1, str4.length()));
			}
//			else {
//				referPlan_name.setText(str4);
//			}
			break;
		case 5: // 其他预案布局
			String str5 = "";
			selectedIds.clear();
			otherReferPlan = "";
			if (data != null && resultCode == RESULT_OK) {
				resutList5 = (ArrayList<PlanNameRowEntity>) data
						.getSerializableExtra("arrlist");
				ArrayList<PlanNameRowEntity> typelist5 = new ArrayList<PlanNameRowEntity>();
				if (resutList5 != null && resutList5.size() > 0) {
					typelist5.clear();
					typelist5.addAll(resutList5);

					if (typelist5.size() > 0) {
						for (int i = 0; i < typelist5.size(); i++) {
							if (typelist5.get(i).isSelect()) {
								str5 = str5 + "," + typelist5.get(i).getName();
								otherReferPlan = otherReferPlan + "|"
										+ typelist5.get(i).getId();
								PlanNameRowEntity entity = new PlanNameRowEntity();
								entity.setId(typelist5.get(i).getId());
								selectedIds.add(entity);
							}
						}
						if (otherReferPlan.subSequence(0, 1).equals("|")) {
							otherReferPlan = (String) otherReferPlan
									.subSequence(1, otherReferPlan.length());
						}
					}

				}
			}
			if (str5.length() > 0) {

				otherReferPlan_name.setText(str5.substring(1, str5.length()));
			}
//			else {
//				otherReferPlan_name.setText(str5);
//			}
			break;

		case 6:
			String str6 = "";
			selectedIds.clear();
			categoryPlan = "";
			if (data != null && resultCode == RESULT_OK) {
				resutList6 = (ArrayList<PlanNameRowEntity>) data
						.getSerializableExtra("arrlist");
				ArrayList<PlanNameRowEntity> typelist6 = new ArrayList<PlanNameRowEntity>();
				if (resutList6 != null && resutList6.size() > 0) {
					typelist6.clear();
					typelist6.addAll(resutList6);
					if (typelist6.size() > 0) {
						for (int i = 0; i < typelist6.size(); i++) {
							if (typelist6.get(i).isSelect()) {
								str6 = str6 + "," + typelist6.get(i).getName();
								categoryPlan = categoryPlan + "|"
										+ typelist6.get(i).getId();
								PlanNameRowEntity entity = new PlanNameRowEntity();
								entity.setId(typelist6.get(i).getId());
								selectedIds.add(entity);
							}
						}
						if (categoryPlan.subSequence(0, 1).equals("|")) {
							categoryPlan = (String) categoryPlan.subSequence(1,
									categoryPlan.length());
						}

					}
				}
			}
			if (str6.length() > 0) {
				categoryPlan_name.setText(str6.substring(1, str6.length()));
			}
//			else {
//				categoryPlan_name.setText(str6);
//			}

			break;
			case 7:
				String str7 = "";
				selectedIds.clear();
				emergencyDisposalProcess = "";
				if (data != null && resultCode == RESULT_OK) {
					resutList7 = (ArrayList<PlanNameRowEntity>) data
							.getSerializableExtra("arrlist");
					ArrayList<PlanNameRowEntity> typelist7 = new ArrayList<PlanNameRowEntity>();
					if (resutList7 != null && resutList7.size() > 0) {
						typelist7.clear();
						typelist7.addAll(resutList7);
						for (int i = 0; i < typelist7.size(); i++) {
							if (typelist7.get(i).isSelect()) {
								str7 = str7 + "," + typelist7.get(i).getName();
								emergencyDisposalProcess = emergencyDisposalProcess + "|"
										+ typelist7.get(i).getId();
								PlanNameRowEntity entity = new PlanNameRowEntity();
								entity.setId(typelist7.get(i).getId());
								selectedIds.add(entity);
							}
						}
						if (emergencyDisposalProcess.subSequence(0, 1).equals("|")) {
							emergencyDisposalProcess = (String) emergencyDisposalProcess.subSequence(1,
									emergencyDisposalProcess.length());
						}
					}
				}
				if (str7.length() > 0) {
					emergency_disposal_process.setText(str7.substring(1, str7.length()));
				}
				break;
			case 1001:
				if (data != null && resultCode == RESULT_OK) {
					event_name.setText(data.getExtras().get("content").toString());
				}
				break;
			case 1002:
				if (data != null && resultCode == RESULT_OK) {
					event_des.setText(data.getExtras().get("content").toString());
				}
				break;
			case 1003:
				if (data != null && resultCode == RESULT_OK) {
					suggestion.setText(data.getExtras().get("content").toString());
				}
				break;
			case 1004:
				if (data != null && resultCode == RESULT_OK) {
					event_discover.setText(data.getExtras().get("content").toString());
				}
				break;
		}

	}

	/**
	 * @Description:把list转换为一个用“|”分隔的字符串
	 */
	public static String listToString(List list) {
		StringBuilder sb = new StringBuilder();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i < list.size() - 1) {
					sb.append(list.get(i) + "|");
				} else {
					sb.append(list.get(i));
				}
			}
		}
		return sb.toString();
	}

	private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listenser = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			String str = "";
			if (backflag) {
				ToastUtil.showToast(AddeValuationActivity.this,
						"操作成功");
				if (tag.equals("2")) {
					EventBus.getDefault()
							.post(new mainEvent("ref"));// 刷新演练列表

				}
				Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
				intent.putExtra("msgType", "updatePlanCount");
				sendBroadcast(intent);
				finish();
				// } else if (backflag == false) {
				// ToastUtil.showToast(AddeValuationActivity.this,
				// stRerror);
			} else if (stRerror != null) {

				ToastUtil.showLongToast(AddeValuationActivity.this,
						stRerror);
			} else if (Exceptionerror != null) {

				ToastUtil.showLongToast(AddeValuationActivity.this,
						Const.NETWORKERROR);
			}
			// if (Utils.getInstance().progressDialog.isShowing()) {
			Utils.getInstance().hideProgressDialog();
			// }
		}
	};

	/**
	 * 添加评估
	 */
	private void addValuation() {
		Utils.getInstance().showProgressDialog(AddeValuationActivity.this, "",
				Const.SUBMIT_MESSAGE);
		Log.i("添加评估referplan", referPlan);
		Log.i("添加评估otherReferPlan", otherReferPlan);
		Log.i("添加评估categoryPlan", categoryPlan);
		Control.getinstance().getEmergencyService().addEmergencyPlanevent(tag, addEntity, listenser);
	}

	private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser eventListener = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			if (backflag) {
				ToastUtil.showToast(AddeValuationActivity.this,
						"操作成功");
				EventBus.getDefault().post(new mainEvent("re"));// 刷新驳回事件的列表
				Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
				intent.putExtra("msgType", "updatePlanCount");
				sendBroadcast(intent);
				finish();
			} else if (backflag == false) {
				ToastUtil.showToast(AddeValuationActivity.this,
						"操作失败");
			} else if (stRerror != null) {

				ToastUtil.showLongToast(AddeValuationActivity.this,
						stRerror);
			} else if (Exceptionerror != null) {

				ToastUtil.showLongToast(AddeValuationActivity.this,
						Const.NETWORKERROR);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	/**
	 * 重新评估
	 */
	private void reValuation() {
		Utils.getInstance().showProgressDialog(AddeValuationActivity.this, "",
				Const.SUBMIT_MESSAGE);
		Control.getinstance().getEmergencyService().reValuationEvent(entity, eventListener);
	}

	//时间选择器----------确定
	@Override
	public void positiveListener() {
		int year = mTimePickerDialog.getYear();
		int month = mTimePickerDialog.getMonth();
		int day = mTimePickerDialog.getDay();
		int hour = mTimePickerDialog.getHour();
		int minute = mTimePickerDialog.getMinute();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String dateString = formatter.format(currentTime);
		String time = year + "-"
				+ (month < 10 ? "0" + month : month) + "-"
				+ (day < 10 ? "0" + day : day) + " "
				+ (hour < 10 ? "0" + hour : hour) + ":"
				+ (minute < 10 ? "0" + minute : minute) + ":"
				+ "00";

		event_happen_time.setText(time);
	}
	//时间选择器----------取消
	@Override
	public void negativeListener() {

	}
}
