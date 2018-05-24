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
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.PlanDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanDetailObjEntity;
import com.dssm.esc.model.entity.emergency.PlanSuspandEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.widget.MyScrollView;

import net.tsz.afinal.annotation.view.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * 已启动预案详情界面
 * 
 * @author zsj
 * 
 */
public class PlanSuspandDetilActivity extends BaseActivity implements
		OnClickListener, MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView back;
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

	/** 预案授权人 */
	@ViewInject(id = R.id.planAther)
	private TextView planAther;
	/** 预案授权时间 */
	@ViewInject(id = R.id.planAuthTime)
	private TextView planAuthTime;
	/** 预案授权意见 */
	@ViewInject(id = R.id.planAuthOption)
	private TextView planAuthOption;

	/** 预案类型 */
	@ViewInject(id = R.id.plan_type)
	private TextView plan_type;
	/** 预案摘要 */
	@ViewInject(id = R.id.plan_des)
	private TextView plan_des;
	/** 处理意见 布局 */
	@ViewInject(id = R.id.ed_ll)
	private LinearLayout suggestion_ll;

	/** 处理意见 */
	@ViewInject(id = R.id.plan_donesug)
	private TextView plan_donesug;
	/** 中止按钮 */
	@ViewInject(id = R.id.suspandbt)
	private Button suspandbt;
	/** 中止原因的编辑框 */
	@ViewInject(id = R.id.etc)
	private EditText etc;
	/** 中止原因的删除按钮 */
	@ViewInject(id = R.id.img_delete)
	private ImageView img_delete;
	private PlanDetailEntity planDetailEntity;
	private PlanSuspandEntity suspandEntity;
	/** 1,应急;2,演练 */
	// private String tag;
	private String id;// 预案id
	private String planName = "";// 预案名称
	/** 预案列表是否有启动的权限 */
	private String isStarter = "";
	/** 授权列表列表是否有启动的权限 */
	private String isAuthor = "";
	Intent intent;
	private String stop;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				break;
			case 1:
				PlanDetailEntity planDetailEntity = (PlanDetailEntity) msg.obj;
				PlanDetailObjEntity obj2 = planDetailEntity.getObj();
				plan_detail_name.setText(obj2.getPlanName());
				plan_people.setText(obj2.getPlanStarter());
				String startTime = obj2.getPlanStartTime();
				startime.setText(startTime);
				String nowTime = obj2.getNowTime();
				plan_over_time.setText(Utils.getInstance().getOverTime(nowTime,
						startTime));
				plan_type.setText(obj2.getPlanTypeName());
				plan_des.setText(obj2.getSummary());
				planAther.setText(obj2.getPlanAuthor());
				planAuthTime.setText(obj2.getPlanAuthTime());
				planAuthOption.setText(obj2.getPlanAuthOpition());
				plan_donesug.setText(obj2.getPlanStartOpition());
				planName = obj2.getPlanName();
				// id 预案ID
				// suspendType 中止类型 启动时中止，类型为null
				// planSuspendOpition 中止原因
				// planName 预案名称 发送通知使用
				// planResType 预案来源类型 发送通知使用
				// planId 预案ID 发送通知使用
				// planResName 预案来源名称 发送通知使用
				// tradeTypeId 业务类型ID 发送通知使用
				// eveLevelId 事件等级ID 发送通知使用
				// planStarterId 预案启动人 发送通知使用
				// planAuthorId 预案授权人 发送通知使用
				// submitterId 事件提交人 发送通知使用
				suspandEntity.setId(obj2.getId());
				if (stop.equals("0")) {
					suspandEntity.setSuspendType("auth");
				} else if (stop.equals("1")) {
					suspandEntity.setSuspendType("authSuspend");
				}
				suspandEntity.setPlanSuspendOpition(etc.getText().toString()
						.trim());
				suspandEntity.setPlanName(planName);
				suspandEntity.setPlanResType(obj2.getPlanResType());
				suspandEntity.setPlanId(obj2.getPlanId());
				suspandEntity.setPlanResName(obj2.getPlanResName());
				suspandEntity.setTradeTypeId(obj2.getTradeTypeId());
				suspandEntity.setEveLevelId(obj2.getEveLevelId());
				suspandEntity.setPlanStarterId(obj2.getPlanStarterId());
				String planAuthorId = obj2.getPlanAuthorId();
				if (planAuthorId == null && planAuthorId.equals("")
						&& planAuthorId.equals("null")
						&& planAuthorId.length() == 0) {

					suspandEntity.setPlanAuthorId("");
				}
				suspandEntity.setSubmitterId(obj2.getSubmitterId());

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
		setContentView(R.layout.activity_suspanddetail);
		View findViewById = findViewById(R.id.plan_detail_all);
		findViewById.setFitsSystemWindows(true);
		intent = getIntent();
		// tag = intent.getStringExtra("tag");
		id = intent.getStringExtra("id");
		stop = intent.getStringExtra("stop");
		if (stop.equals("0")) {// 预案列表
			isStarter = intent.getStringExtra("isStarter");
		} else if (stop.equals("1")) {// 授权列表
			isAuthor = intent.getStringExtra("isAuthor");
		}
		suspandEntity = new PlanSuspandEntity();
		initView();
		suspandbt.setOnClickListener(this);
		img_delete.setOnClickListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		back.setVisibility(View.VISIBLE);
		title.setText("预案详情");
		if (isStarter.equals("true")) {
			suspandbt.setVisibility(View.VISIBLE);
			suggestion_ll.setVisibility(View.VISIBLE);
		} else if (isStarter.equals("false")) {
			suspandbt.setVisibility(View.GONE);
			suggestion_ll.setVisibility(View.GONE);
		}
		if (isAuthor.equals("true")) {
			suspandbt.setVisibility(View.VISIBLE);
			suggestion_ll.setVisibility(View.VISIBLE);
		} else if (isAuthor.equals("false")) {
			suspandbt.setVisibility(View.GONE);
			suggestion_ll.setVisibility(View.GONE);
		}
		intData();
		etc.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					img_delete.setVisibility(View.GONE);
				} else {
					img_delete.setVisibility(View.VISIBLE);
				}
			}
		});
		// setNetListener(this);
	}

	private void intData() {
		Utils.getInstance().showProgressDialog(PlanSuspandDetilActivity.this,
				"", Const.SUBMIT_MESSAGE);
		Control.getinstance().getEmergencyService().getPlanDetail(id, new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

			@Override
			public void setEmergencySeviceImplListListenser(Object object,
					String stRerror, String Exceptionerror) {
				// TODO Auto-generated method stub
				if (object != null) {
					planDetailEntity = (PlanDetailEntity) object;

				} else if (stRerror != null) {
					planDetailEntity = new PlanDetailEntity();
					ToastUtil
							.showToast(PlanSuspandDetilActivity.this, stRerror);
				} else if (Exceptionerror != null) {
					planDetailEntity = new PlanDetailEntity();
					ToastUtil.showToast(PlanSuspandDetilActivity.this,
							Const.NETWORKERROR + ":" + Exceptionerror);
				}
				Message message = new Message();
				message.what = 1;
				message.obj = planDetailEntity;
				handler.sendMessage(message);
				// if (Utils.getInstance().progressDialog.isShowing()) {
				Utils.getInstance().hideProgressDialog();
				// }
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
		case R.id.suspandbt://中止
			if (!etc.getText().toString().trim().equals("")) {
				View view=LayoutInflater.from(PlanSuspandDetilActivity.this).inflate(R.layout.editcode, null);
				final EditText et = (EditText) view.findViewById(R.id.vc_code);
				getCode = Utils.getInstance().code();
				new AlertDialog.Builder(PlanSuspandDetilActivity.this)
						.setTitle("验证码：" + getCode)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(view)  
			    .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
			        public void onClick(DialogInterface dialog, int which) {  
			        	String v_code = et.getText().toString()
								.trim();
						if (!v_code.equals(getCode)) {
							Toast.makeText(
									PlanSuspandDetilActivity.this,
									"验证码错误", Toast.LENGTH_SHORT).show();
						} else {
							
							stopPlan();
						}
			        }
			        })  
			    .setNegativeButton("取消", null)  
			    .show();  
			} else {
				ToastUtil.showToast(PlanSuspandDetilActivity.this, "中止原因必填");
			}
			break;
		case R.id.img_delete:// 预案处置的编辑框的删除按钮
			etc.setText("");
			break;
		default:
			break;
		}
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		intData();
	}

	private void stopPlan() {
		if (stop.equals("0")) {// 预案启动列表
			Utils.getInstance().showProgressDialog(
					PlanSuspandDetilActivity.this, "", Const.SUBMIT_MESSAGE);
			Control.getinstance().getEmergencyService().suspand(suspandEntity,
					new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

						@Override
						public void setEmergencySeviceImplListenser(
								Boolean backflag, String stRerror,
								String Exceptionerror) {
							// TODO Auto-generated
							// method stub
							if (backflag) {
								ToastUtil.showToast(
										PlanSuspandDetilActivity.this, stRerror);
								EventBus.getDefault().post(
										new mainEvent("refres"));// 刷新已启动预案列表界面
								finish();
							} else if (backflag == false) {
								ToastUtil.showToast(
										PlanSuspandDetilActivity.this,stRerror);
							} else if (stRerror != null) {

								ToastUtil
										.showToast(
												PlanSuspandDetilActivity.this,
												stRerror);
							} else if (Exceptionerror != null) {

								ToastUtil.showToast(
										PlanSuspandDetilActivity.this,
										Exceptionerror);
							}
							if (Utils.getInstance().progressDialog.isShowing()) {
								Utils.getInstance().hideProgressDialog();
							}
						}
					});

		} else if (stop.equals("1")) {// 预案授权列表
			Utils.getInstance().showProgressDialog(
					PlanSuspandDetilActivity.this, "", Const.SUBMIT_MESSAGE);
			Control.getinstance().getEmergencyService().planSuspand(suspandEntity,
					new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

						@Override
						public void setEmergencySeviceImplListenser(
								Boolean backflag, String stRerror,
								String Exceptionerror) {
							// TODO Auto-generated
							// method stub
							if (backflag) {
								ToastUtil.showToast(
										PlanSuspandDetilActivity.this, stRerror);
								EventBus.getDefault().post(new mainEvent("r"));// 刷新已授权列表界面
								finish();
							} else if (backflag == false) {
								ToastUtil.showToast(
										PlanSuspandDetilActivity.this,stRerror);
							} else if (stRerror != null) {

								ToastUtil
										.showToast(
												PlanSuspandDetilActivity.this,
												stRerror);
							} else if (Exceptionerror != null) {

								ToastUtil.showToast(
										PlanSuspandDetilActivity.this,
										Exceptionerror);
							}
							// if
							// (Utils.getInstance().progressDialog
							// .isShowing()) {
							Utils.getInstance().hideProgressDialog();
							// }
						}
					});
		}
	}
}
