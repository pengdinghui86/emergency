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
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
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
import com.dssm.esc.view.widget.SegmentControl;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 决策授权详情
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class AutorizateDecDetailActivity extends BaseActivity implements
		OnClickListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView back;
	@ViewInject(id = R.id.segment_control_plandetail)
	SegmentControl mSegmentControl;
	/** 事件编号 */
	@ViewInject(id = R.id.event_number)
	private TextView event_number;
	/** 事件名称 */
	@ViewInject(id = R.id.event_name)
	private TextView event_name;
	/** 事件提交人 */
	@ViewInject(id = R.id.event_people)
	private TextView eventpeople;
	/** 提交时间 */
	@ViewInject(id = R.id.event_time)
	private TextView event_time;
	/** 已用时 */
	@ViewInject(id = R.id.event_over_time)
	private TextView event_over_time;

	/** 行业类型 */
	@ViewInject(id = R.id.business_type)
	private TextView business_type;
	/** 事件等级 */
	@ViewInject(id = R.id.event_level)
	private TextView event_level;
	/** 事件类型 */
	@ViewInject(id = R.id.event_type)
	private TextView event_type;
	/** 事件场景 */
	@ViewInject(id = R.id.event_background)
	private TextView event_background;
	/** 预案名称 */
	@ViewInject(id = R.id.plan_name)
	private TextView plan_name;
	/** 事件描述 */
	@ViewInject(id = R.id.event_des)
	private TextView event_des;
	/** 应对建议 */
	@ViewInject(id = R.id.suggestion)
	private TextView suggestion;
	/** 事件详情的总布局 */
	@ViewInject(id = R.id.event_detail_ll)
	MyScrollView event_detail_ll;
	/** 预案详情的总布局 */
	@ViewInject(id = R.id.plan_detail_ll)
	MyScrollView plan_detail_ll;
	/** 预案名称 */
	@ViewInject(id = R.id.plan_detail_name)
	private TextView plan_detail_name;
	/** 预案启动人 */
	@ViewInject(id = R.id.plan_people)
	private TextView plan_people;
	/** 启动时间 */
	@ViewInject(id = R.id.startime)
	private TextView startime;
	/** 已用时 */
	@ViewInject(id = R.id.plan_over_time)
	private TextView plan_over_time;
	/** 预案类型 */
	@ViewInject(id = R.id.plan_type)
	private TextView plan_type;
	/** 预案摘要 */
	@ViewInject(id = R.id.plan_des)
	private TextView plan_des;
	/** 处理意见 */
	@ViewInject(id = R.id.plan_donesug)
	private TextView plan_donesug;
	/** 预案授权的总布局 */
	@ViewInject(id = R.id.plan_autro_ll)
	private LinearLayout plan_autro_ll;
	/** 预案授权的编辑框 */
	@ViewInject(id = R.id.etc_plan)
	private EditText etc_plan;
	/** 预案授权的编辑框的删除按钮 */
	@ViewInject(id = R.id.img_delete_plan)
	private ImageView img_delete_plan;
	/** 终止按钮 */
	@ViewInject(id = R.id.stop)
	private Button stop;
	/** 授权按钮 */
	@ViewInject(id = R.id.auth)
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
					name = name + "," + list.get(i).getName();
				}
				if (name.subSequence(0, 1).equals(",")) {
					name = (String) name.subSequence(1, name.length());
				}
				event_number.setText(obj.getEveCode());
				event_name.setText(obj.getEveName());
				eventpeople.setText(obj.getSubmitter());
				String subTime = obj.getSubTime();
				event_time.setText(subTime);
				String nowTime = obj.getNowTime();
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
				event_background.setText(obj.getEveScenarioName());
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
				plan_detail_name.setText(obj2.getPlanName());
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
				planName = obj2.getPlanName();
				suspandEntity.setId(obj2.getId());
				suspandEntity.setSuspendType("authSuspend");
				suspandEntity.setPlanSuspendOpition(planSuspendOpition2);
				suspandEntity.setPlanName(planName);
				planResType = obj2.getPlanResType();
				plan_type.setText(planTypeName);
				if (planResType.equals("1")) {
					planResType="应急";
				} else if (planResType.equals("2")) {
					planResType="演练";
				}
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
		setContentView(R.layout.activity_autorizatdetail);
		View findViewById = findViewById(R.id.autorizatdetail);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		planId = intent.getStringExtra("planId");
		planResId = intent.getStringExtra("planResId");
		//isAuthor= intent.getStringExtra("isAuthor");
		initview();
		suspandEntity = new PlanSuspandEntity();
		segmentControlListDate();
		sem_tags = 3;// 默认预案处置
		initData(sem_tags);

	}

	private void initview() {
		// TODO Auto-generated method stub
		back.setVisibility(View.VISIBLE);
		title.setText("决策授权");

	}

	private void initData(int sem_tags) {
		if (sem_tags == 1) {// 1,事件详情

			event_detail_ll.setVisibility(View.VISIBLE);
			plan_detail_ll.setVisibility(View.GONE);
			plan_autro_ll.setVisibility(View.GONE);
			if (planResName == "" || planResName.equals("")
					|| planResName == null) {
				getEventDetail();
			}
		} else if (sem_tags == 2) {// 2,预案详情
			plan_detail_ll.setVisibility(View.VISIBLE);
			event_detail_ll.setVisibility(View.GONE);
			plan_autro_ll.setVisibility(View.GONE);
			if (planName == "" || planName.equals("") || planName == null) {
				getPlanDetail();
			}
		} else if (sem_tags == 3) {// 3预案授权
			plan_autro_ll.setVisibility(View.VISIBLE);
			plan_detail_ll.setVisibility(View.GONE);
			event_detail_ll.setVisibility(View.GONE);
			if (planName == "" || planName.equals("") || planName == null) {
				getPlanDetail();
			}
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

	/**
	 * 
	 * selectButton控制list数据
	 * 
	 * @version 1.0
	 * @createTime 2015-9-7,下午3:23:05
	 * @updateTime 2015-9-7,下午3:23:05
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	private void segmentControlListDate() {
		// TODO Auto-generated method stub
		mSegmentControl
				.setmOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
					@Override
					public void onSegmentControlClick(int index) {
						switch (index) {
						case 0:// 1,预案授权
							sem_tags = 3;

							break;
						case 1:// 2预案详情
							sem_tags = 2;
							break;
						case 2:
							sem_tags = 1;// 3,事件详情
							break;
						}
						initData(sem_tags);
					}
				});
	}
	private AlertDialog selfdialog;
	private String getCode =null;
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.stop:// 中止
			planSuspendOpition2 = etc_plan.getText().toString().trim();
			if (!planSuspendOpition2.equals("")) {
				View view=LayoutInflater.from(AutorizateDecDetailActivity.this).inflate(R.layout.editcode, null);
				final EditText et = (EditText) view.findViewById(R.id.vc_code);
				getCode = Utils.getInstance().code();
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
				
			} else {
				ToastUtil.showToast(AutorizateDecDetailActivity.this, "意见必填");
			}
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

	/**
	 * 预案中止
	 */
	private void planSuspand() {
		Utils.getInstance().showProgressDialog(
				AutorizateDecDetailActivity.this, "", Const.SUBMIT_MESSAGE);
		esevice.planSuspand(suspandEntity,
				new EmergencySeviceImplBackBooleanListenser() {

					@Override
					public void setEmergencySeviceImplListenser(
							Boolean backflag, String stRerror,
							String Exceptionerror) {
						// TODO Auto-generated method stub
						if (backflag) {
							ToastUtil.showToast(
									AutorizateDecDetailActivity.this, stRerror);
							EventBus.getDefault().post(
									new mainEvent("r"));//刷新列表界面
							finish();
						} else if (backflag == false) {
							ToastUtil.showToast(AutorizateDecDetailActivity.this,
									stRerror);
						} else if (stRerror != null) {

							ToastUtil.showLongToast(AutorizateDecDetailActivity.this,
									stRerror);
						} else if (Exceptionerror != null) {

							ToastUtil.showLongToast(AutorizateDecDetailActivity.this,
									Const.NETWORKERROR + Exceptionerror);
						}
//						if (Utils.getInstance().progressDialog.isShowing()) {
							Utils.getInstance().hideProgressDialog();
//						}
					}
				});

	}

	/**
	 * 预案授权
	 */
	private void planAuth() {
		Utils.getInstance().showProgressDialog(
				AutorizateDecDetailActivity.this, "", Const.SUBMIT_MESSAGE);
		esevice.planAuth(id, planSuspendOpition2, planName, planResName,
				planResType, planId, planStarterId, submitterId,
				new EmergencySeviceImplBackBooleanListenser() {

					@Override
					public void setEmergencySeviceImplListenser(
							Boolean backflag, String stRerror,
							String Exceptionerror) {
						// TODO Auto-generated method stub
						if (backflag) {
							ToastUtil.showToast(
									AutorizateDecDetailActivity.this, stRerror);
							EventBus.getDefault().post(
									new mainEvent("r"));//刷新列表界面
							finish();
						} else if (backflag == false) {
							ToastUtil.showToast(AutorizateDecDetailActivity.this,
									stRerror);
						} else if (stRerror != null) {

							ToastUtil.showLongToast(AutorizateDecDetailActivity.this,
									stRerror);
						} else if (Exceptionerror != null) {

							ToastUtil.showLongToast(AutorizateDecDetailActivity.this,
									Const.NETWORKERROR + Exceptionerror);
						}
//						if (Utils.getInstance().progressDialog.isShowing()) {
							Utils.getInstance().hideProgressDialog();
//						}
					}
				});

	}

	/**
	 * 获取预案详情
	 */
	private void getPlanDetail() {
		// TODO Auto-generated method stub
		Utils.getInstance().showProgressDialog(AutorizateDecDetailActivity.this, "", Const.LOAD_MESSAGE);
		esevice.getPlanDetail(id, new EmergencySeviceImplListListenser() {

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
							Const.NETWORKERROR + ":" + Exceptionerror);
				}
				Message message = new Message();
				message.what = 1;
				message.obj = planDetailEntity;
				handler.sendMessage(message);
//				if (Utils.getInstance().progressDialog.isShowing()) {
					Utils.getInstance().hideProgressDialog();
//				}
			}
		});
	}

	/**
	 * 获取事件详情
	 */
	private void getEventDetail() {
		// TODO Auto-generated method stub
		esevice.getPlanStarListDetail(planResId,
				new EmergencySeviceImplListListenser() {

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
									Const.NETWORKERROR + ":" + Exceptionerror);
						}
						Message message = new Message();
						message.what = 0;
						message.obj = detailEntity;
						handler.sendMessage(message);
					}
				});
	}

}
