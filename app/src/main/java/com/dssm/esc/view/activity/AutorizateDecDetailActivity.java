package com.dssm.esc.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplListListenser;
import com.dssm.esc.model.entity.emergency.PlanDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanDetailObjEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjListEntity;
import com.dssm.esc.model.entity.emergency.PlanSuspandEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.widget.MyScrollView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 决策授权详情
 */
@ContentView(R.layout.activity_autorizatdetail)
public class AutorizateDecDetailActivity extends BaseActivity implements
		OnClickListener {

	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	@ViewInject(R.id.rb_plan_authorize)
	private RadioButton rb_plan_authorize;
	@ViewInject(R.id.rb_plan_detail)
	private RadioButton rb_plan_detail;
	@ViewInject(R.id.rb_event_detail)
	private RadioButton rb_event_detail;
	/** 事件编号 */
	@ViewInject(R.id.event_number)
	private TextView event_number;
	/** 事件名称 */
	@ViewInject(R.id.event_name)
	private TextView event_name;
	/** 事件提交人 */
	@ViewInject(R.id.event_people)
	private TextView eventpeople;
	/** 提交时间 */
	@ViewInject(R.id.event_time)
	private TextView event_time;
	/** 事件发现人 */
	@ViewInject(R.id.event_discoverer)
	private TextView event_discoverer;
	/** 发生时间 */
	@ViewInject(R.id.event_discovery_time)
	private TextView event_discovery_time;
	/** 应急处置流程 */
	@ViewInject(R.id.emergency_disposal_process)
	private TextView emergency_disposal_process;
	/** 已用时 */
	@ViewInject(R.id.event_over_time)
	private TextView event_over_time;

	/** 行业类型 */
	@ViewInject(R.id.business_type)
	private TextView business_type;
	/** 事件等级 */
	@ViewInject(R.id.event_level)
	private TextView event_level;
	/** 事件类型 */
	@ViewInject(R.id.event_type)
	private TextView event_type;
	/** 预案名称 */
	@ViewInject(R.id.plan_name)
	private TextView plan_name;
	/** 事件描述 */
	@ViewInject(R.id.event_des)
	private TextView event_des;
	/** 应对建议 */
	@ViewInject(R.id.suggestion)
	private TextView suggestion;
	/** 事件详情的总布局 */
	@ViewInject(R.id.event_detail_ll)
	MyScrollView event_detail_ll;
	/** 预案详情的总布局 */
	@ViewInject(R.id.plan_detail_ll)
	MyScrollView plan_detail_ll;
	/** 预案名称 */
	@ViewInject(R.id.plan_detail_name)
	private TextView plan_detail_name;
	/** 预案启动人 */
	@ViewInject(R.id.plan_people)
	private TextView plan_people;
	/** 启动时间 */
	@ViewInject(R.id.startime)
	private TextView startime;
	/** 已用时 */
	@ViewInject(R.id.plan_over_time)
	private TextView plan_over_time;
	/** 预案类型 */
	@ViewInject(R.id.plan_type)
	private TextView plan_type;
	/** 预案摘要 */
	@ViewInject(R.id.plan_des)
	private TextView plan_des;
	/** 处理意见 */
	@ViewInject(R.id.plan_donesug)
	private TextView plan_donesug;
	/** 预案授权的总布局 */
	@ViewInject(R.id.plan_autro_ll)
	private LinearLayout plan_autro_ll;
	/** 预案授权的编辑框 */
	@ViewInject(R.id.etc_plan)
	private EditText etc_plan;
	/** 预案授权的编辑框的删除按钮 */
	@ViewInject(R.id.img_delete_plan)
	private ImageView img_delete_plan;
	/** 终止按钮 */
	@ViewInject(R.id.stop)
	private Button stop;
	/** 授权按钮 */
	@ViewInject(R.id.auth)
	private Button auth;
	/** 1,应急;2,演练 */
	// private String tag;
	/** 预案名称 */
	private String name = "";
	/** 1事件详情2预案详情3预案授权 (SegmentControl点击) */
	private int sem_tags;
	/** 事件id */
	private String id;
	/** 预案编号 */
	private String planId;
	private String status = "";// 事件状态
	private String closeTime = "";// 事件关闭时间
	/** 事件详情id */
	private String planResId;
	private String planResType;// 预案来源类型 1，应急 2，演练（发送通知使用）
	private String planStarterId;// 预案启动人 （发送通知使用）
	private String submitterId;// 事件提交人 （发送通知使用）
	private PlanStarListDetailEntity detailEntity;
	private PlanStarListDetailObjEntity obj;
	private PlanDetailEntity planDetailEntity;
	private PlanDetailObjEntity entity;
	private String overTime = "";
	private String planName = "";
	private String planResName = "";
	private String drillPrecautionId = "";
	private String sceneName = "";
	/** 中止原因 */
	private String planSuspendOpition2 = "";
	/**是否有中止的权限*/
//private String isAuthor="";
	private PlanSuspandEntity suspandEntity;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				PlanStarListDetailEntity detailEntity = (PlanStarListDetailEntity) msg.obj;
				obj = detailEntity.getObj();
				List<PlanStarListDetailObjListEntity> list = obj.getList();
				for (int i = 0; i < list.size(); i++) {
					name = name + "," + list.get(i).getName() + "-" + list.get(i).getSceneName();
				}
				if (name.subSequence(0, 1).equals(",")) {
					name = (String) name.subSequence(1, name.length());
				}
				event_number.setText(obj.getEveCode());
				event_name.setText(obj.getEveName());
				eventpeople.setText(obj.getSubmitter());
				String subTime = obj.getSubTime();
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
				overTime = Utils.getInstance().getOverTime(nowTime, subTime);
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
				planResName = obj.getEveName();
//				String eveType2 = obj.getEveType();
//				plan_type.setText(obj.getPlanTypeName());
				break;
			case 1:
				PlanDetailEntity planDetailEntity = (PlanDetailEntity) msg.obj;
				PlanDetailObjEntity obj2 = planDetailEntity.getObj();
				if (planName == "" || planName.equals("") || planName == null) {
					planName = obj2.getPlanName();
				}
				plan_detail_name.setText(planName);
				plan_people.setText(obj2.getPlanStarter());
				String startTime = obj2.getPlanStartTime();
				startime.setText(startTime);
				String nowTime2 = obj2.getNowTime();
				plan_over_time.setText(Utils.getInstance().getOverTime(
						nowTime2, startTime));
				String planTypeName = obj2.getPlanTypeName();
				//plan_type.setText();
				plan_des.setText(obj2.getSummary());
				plan_donesug.setText(obj2.getPlanStartOpition());
				suspandEntity.setId(obj2.getId());
				suspandEntity.setSuspendType("authSuspend");
				suspandEntity.setPlanSuspendOpition(planSuspendOpition2);
				suspandEntity.setPlanName(planName);
				planResType = obj2.getPlanResType();
				plan_type.setText(planTypeName);
//				if (planResType.equals("1")) {
//					planResType="应急";
//				} else if (planResType.equals("2")) {
//					planResType="演练";
//				}
				suspandEntity.setPlanResType(planResType);
				suspandEntity.setPlanId(obj2.getPlanId());
				suspandEntity.setPlanResName(obj2.getPlanResName());
				suspandEntity.setTradeTypeId(obj2.getTradeTypeId());
				suspandEntity.setEveLevelId(obj2.getEveLevelId());
				planStarterId = obj2.getPlanStarterId();
				suspandEntity.setPlanStarterId(planStarterId);
				String planAuthorId = obj2.getPlanAuthorId();
				if (planAuthorId == null && planAuthorId.equals("")
						&& planAuthorId.equals("null")
						&& planAuthorId.length() == 0) {

					suspandEntity.setPlanAuthorId("");
				}
				submitterId = obj2.getSubmitterId();
				suspandEntity.setSubmitterId(submitterId);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_autorizatdetail);
		View findViewById = findViewById(R.id.autorizatdetail);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		planId = intent.getStringExtra("planId");
		planName = intent.getStringExtra("name");
		planResId = intent.getStringExtra("planResId");
	    sceneName = intent.getStringExtra("sceneName");
		planResType = intent.getStringExtra("planResType");
		planResName = intent.getStringExtra("planResName");
		drillPrecautionId = intent.getStringExtra("drillPrecautionId");
		planStarterId = intent.getStringExtra("planStarterId");
		status = intent.getStringExtra("status");
		closeTime = intent.getStringExtra("closeTime");
		submitterId = intent.getStringExtra("submitterId");
		initview();
		suspandEntity = new PlanSuspandEntity();
		sem_tags = 3;// 默认预案处置
		initData(sem_tags);
	}

	private void initview() {
		back.setOnClickListener(this);
		rb_plan_authorize.setOnClickListener(this);
		rb_plan_detail.setOnClickListener(this);
		rb_event_detail.setOnClickListener(this);
	}

	private void initData(int sem_tags) {
		if (sem_tags == 1) {// 1,事件详情

			event_detail_ll.setVisibility(View.VISIBLE);
			plan_detail_ll.setVisibility(View.GONE);
			plan_autro_ll.setVisibility(View.GONE);
            getEventDetail();
		} else if (sem_tags == 2) {// 2,预案详情
			plan_detail_ll.setVisibility(View.VISIBLE);
			event_detail_ll.setVisibility(View.GONE);
			plan_autro_ll.setVisibility(View.GONE);
			getPlanDetail();
		} else if (sem_tags == 3) {// 3预案授权
			plan_autro_ll.setVisibility(View.VISIBLE);
			plan_detail_ll.setVisibility(View.GONE);
			event_detail_ll.setVisibility(View.GONE);
			getPlanDetail();
			etc_plan.addTextChangedListener(new TextWatcher() {

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
				}

				public void afterTextChanged(Editable s) {
					if (s.length() == 0) {
						img_delete_plan.setVisibility(View.GONE);
					} else {
						img_delete_plan.setVisibility(View.VISIBLE);
					}
				}
			});
			img_delete_plan.setOnClickListener(this);
			stop.setOnClickListener(this);
			auth.setOnClickListener(this);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.iv_actionbar_back:
			finish();
			break;
		case R.id.rb_plan_authorize:
			rb_plan_authorize.setChecked(true);
			rb_plan_detail.setChecked(false);
			rb_event_detail.setChecked(false);
			sem_tags = 3;
			initData(sem_tags);
			break;
		case R.id.rb_plan_detail:
			rb_plan_authorize.setChecked(false);
			rb_plan_detail.setChecked(true);
			rb_event_detail.setChecked(false);
			sem_tags = 2;
			initData(sem_tags);
			break;
		case R.id.rb_event_detail:
			rb_plan_authorize.setChecked(false);
			rb_plan_detail.setChecked(false);
			rb_event_detail.setChecked(true);
			sem_tags = 1;
			initData(sem_tags);
			break;
		case R.id.stop:// 中止
			View view2 = LayoutInflater.from(AutorizateDecDetailActivity.this).inflate(R.layout.edit_info, null);
			final EditText et = (EditText) view2.findViewById(R.id.et_info);
			new AlertDialog.Builder(AutorizateDecDetailActivity.this)
					.setTitle("请输入中止原因")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(view2)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							String info = et.getText().toString()
									.trim();
							if (info.equals("")) {
								Toast.makeText(
										AutorizateDecDetailActivity.this,
										"中止原因不能为空", Toast.LENGTH_SHORT).show();
							} else {
								suspandEntity.setPlanSuspendOpition(info);
								View view=LayoutInflater.from(AutorizateDecDetailActivity.this).inflate(R.layout.editcode, null);
								final EditText et = (EditText) view.findViewById(R.id.vc_code);
								final String getCode = Utils.getInstance().code();
								new AlertDialog.Builder(AutorizateDecDetailActivity.this)
										.setTitle("验证码：" + getCode)
										.setIcon(android.R.drawable.ic_dialog_info)
										.setView(view)
										.setPositiveButton("确定", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												String v_code = et.getText().toString()
														.trim();
												if (!v_code.equals(getCode)) {
													Toast.makeText(
															AutorizateDecDetailActivity.this,
															"验证码错误", Toast.LENGTH_SHORT).show();
												} else {
													planSuspand();
												}
											}
										})
										.setNegativeButton("取消", null)
										.show();
							}
						}
					})
					.setNegativeButton("取消", null)
					.show();
			break;
		case R.id.auth:// 授权
			planSuspendOpition2 = etc_plan.getText().toString().trim();
			if (!planSuspendOpition2.equals("")) {
				planAuth();
			} else {
				ToastUtil.showToast(AutorizateDecDetailActivity.this, "意见必填");
			}

			break;
		case R.id.img_delete_plan:
			etc_plan.setText("");
			break;
		}
	}

	private EmergencySeviceImplBackBooleanListenser listener = new EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			if (backflag) {
				ToastUtil.showToast(
						AutorizateDecDetailActivity.this, "操作成功");
				EventBus.getDefault().post(
						new mainEvent("r"));//刷新列表界面
				Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
				intent.putExtra("msgType", "updatePlanCount");
				sendBroadcast(intent);
				finish();
			} else if (backflag == false) {
				ToastUtil.showToast(AutorizateDecDetailActivity.this,
						"操作失败");
			} else if (stRerror != null) {

				ToastUtil.showLongToast(AutorizateDecDetailActivity.this,
						stRerror);
			} else if (Exceptionerror != null) {

				ToastUtil.showLongToast(AutorizateDecDetailActivity.this,
						Const.NETWORKERROR);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	/**
	 * 预案中止
	 */
	private void planSuspand() {
		Utils.getInstance().showProgressDialog(
				AutorizateDecDetailActivity.this, "", Const.SUBMIT_MESSAGE);
		Control.getinstance().getEmergencyService().planSuspand(suspandEntity, listener);

	}

	private EmergencySeviceImplBackBooleanListenser planAuthListener = new EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			if (backflag) {
				ToastUtil.showToast(
						AutorizateDecDetailActivity.this, "操作成功");
				EventBus.getDefault().post(
						new mainEvent("r"));//刷新列表界面
				Intent intent = new Intent("com.dssm.esc.push.RECEIVER");
				intent.putExtra("msgType", "updatePlanCount");
				sendBroadcast(intent);
				finish();
			} else if (backflag == false) {
				ToastUtil.showToast(AutorizateDecDetailActivity.this,
						"操作失败");
			} else if (stRerror != null) {

				ToastUtil.showLongToast(AutorizateDecDetailActivity.this,
						stRerror);
			} else if (Exceptionerror != null) {

				ToastUtil.showLongToast(AutorizateDecDetailActivity.this,
						Const.NETWORKERROR);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	/**
	 * 预案授权
	 */
	private void planAuth() {
		Utils.getInstance().showProgressDialog(
				AutorizateDecDetailActivity.this, "", Const.SUBMIT_MESSAGE);
		Control.getinstance().getEmergencyService().planAuth(id, planSuspendOpition2, planName, planResName,
				planResType, planId, planStarterId, submitterId, drillPrecautionId, planAuthListener);
	}

	private EmergencySeviceImplListListenser listListener = new EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(Object object,
				String stRerror, String Exceptionerror) {
			// TODO Auto-generated method stub
			if (object != null) {
				planDetailEntity = (PlanDetailEntity) object;

			} else if (stRerror != null) {
				planDetailEntity = new PlanDetailEntity();

			} else if (Exceptionerror != null) {
				planDetailEntity = new PlanDetailEntity();
				ToastUtil.showToast(AutorizateDecDetailActivity.this,
						Const.NETWORKERROR);
			}
			Message message = new Message();
			message.what = 1;
			message.obj = planDetailEntity;
			handler.sendMessage(message);
			Utils.getInstance().hideProgressDialog();
		}
	};

	/**
	 * 获取预案详情
	 */
	private void getPlanDetail() {
		// TODO Auto-generated method stub
		Utils.getInstance().showProgressDialog(AutorizateDecDetailActivity.this, "", Const.LOAD_MESSAGE);
		Control.getinstance().getEmergencyService().getPlanDetail(id, listListener);
	}

	private EmergencySeviceImplListListenser listListenser = new EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			if (object != null) {
				detailEntity = (PlanStarListDetailEntity) object;

			} else if (stRerror != null) {
				detailEntity = new PlanStarListDetailEntity();

			} else if (Exceptionerror != null) {
				detailEntity = new PlanStarListDetailEntity();
				ToastUtil.showToast(
						AutorizateDecDetailActivity.this,
						Const.NETWORKERROR);
			}
			Message message = new Message();
			message.what = 0;
			message.obj = detailEntity;
			handler.sendMessage(message);
		}
	};

	/**
	 * 获取事件详情
	 */
	private void getEventDetail() {
		// TODO Auto-generated method stub
		Control.getinstance().getEmergencyService().getPlanStarListDetail(planResId, listListenser);
	}

}
