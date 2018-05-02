package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.ListviewCheckboxAdapter;

import net.tsz.afinal.annotation.view.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 应急类型，业务类型，事件等级，事件场景，发送对象，阶段界面(单选)
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-10
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class EmergencyTypeActivity extends BaseActivity implements
		OnClickListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 确定 */
	@ViewInject(id = R.id.tv_actionbar_editData, click = "onClick")
	private TextView mSelectConfirm;
	/** 返回 */
	@ViewInject(id = R.id.iv_actionbar_back, click = "onClick")
	private ImageView mBack;
	/** ListView */
	@ViewInject(id = R.id.select_type_listview)
	private ListView mListView;
	/** 适配器 */
	private ListviewCheckboxAdapter mSelectAdapter;
	/** 1,行业类型;2,事件等级;3,事件场景; 4,完成状态;5,发送对象;6,阶段 */
	private String tags;
	//节点类型
	private String nodeStepType = "";
	/** 业务类型、事件等级、事件场景数据源 */
	private List<BusinessTypeEntity> businessTypeList = new ArrayList<BusinessTypeEntity>();
	/** 被选中的事件场景id */
	private String selectedId;
	/** 记住选择 */
	private List<BusinessTypeEntity> arrlist;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<BusinessTypeEntity> businessTypeList = (List<BusinessTypeEntity>) msg.obj;
			switch (msg.what) {
			case 0:
				setData(businessTypeList);
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

		setContentView(R.layout.activity_emergency_type);
		View findViewById = findViewById(R.id.emergency_type);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		tags = intent.getStringExtra("tags");
		if(intent.getStringExtra("nodeStepType") != null &&
				!"".equals(intent.getStringExtra("nodeStepType"))) {
			nodeStepType = intent.getStringExtra("nodeStepType");
		}
		// tag = getIntent().getStringExtra("tag");
		arrlist = (List<BusinessTypeEntity>) intent.getSerializableExtra("arrlist");
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		// if (tag.equals("1")) {

		if (tags.equals("0")) {
			mSelectTypeTitle.setText("应急类型");
		} else if (tags.equals("1")) {
			mSelectTypeTitle.setText("选择业务类型");
		} else if (tags.equals("2")) {
			mSelectTypeTitle.setText("选择事件等级");
		} else if (tags.equals("3")) {
			mSelectTypeTitle.setText("选择事件场景");
		} else if (tags.equals("4")) {
			mSelectTypeTitle.setText("完成状态");
		} else if (tags.equals("5")) {
			mSelectTypeTitle.setText("发送对象");
		} else if (tags.equals("6")) {
			mSelectTypeTitle.setText("阶段");
		}

		mSelectConfirm.setVisibility(View.VISIBLE);
		mSelectConfirm.setText(R.string.sure);
		mBack.setVisibility(View.VISIBLE);

		/**
		 * 为Adapter准备数据
		 */
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
//		if (businessTypeList!=null) {
//			businessTypeList.clear();
//		}
		if (tags.equals("1")) {// 业务类型
			if (businessTypeList!=null&&businessTypeList.size()==0) {//只访问一次网络
				
			
			Utils.getInstance().showProgressDialog(EmergencyTypeActivity.this,
					"", Const.LOAD_MESSAGE);
			esevice.getBusinessType(1, new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

				@Override
				public void setEmergencySeviceImplListListenser(Object object,
						String stRerror, String Exceptionerror) {
					// TODO Auto-generated method stub
					List<BusinessTypeEntity> businessTypeList = null;
					if (object != null) {
						businessTypeList = (List<BusinessTypeEntity>) object;
						Log.i("业务类型businessTypeList", businessTypeList.size()
								+ "");

						// setData(businessTypeList);
					} else if (stRerror != null) {
						businessTypeList = new ArrayList<BusinessTypeEntity>();

					} else if (Exceptionerror != null) {
						businessTypeList = new ArrayList<BusinessTypeEntity>();
						ToastUtil.showToast(EmergencyTypeActivity.this,
								Const.NETWORKERROR + ":" + Exceptionerror);
					}
					Message message = new Message();
					message.what = 0;
					message.obj = businessTypeList;
					handler.sendMessage(message);
//					if (Utils.getInstance().progressDialog.isShowing()) {
						Utils.getInstance().hideProgressDialog();
//					}
				}
			});
			}else if (businessTypeList!=null&&businessTypeList.size()>0) {
				Message message = new Message();
				message.what = 0;
				message.obj = businessTypeList;
				handler.sendMessage(message);
			}
		} else if (tags.equals("2")) {// 事件等级
			if (businessTypeList!=null&&businessTypeList.size()==0) {//只访问一次网络
			Utils.getInstance().showProgressDialog(EmergencyTypeActivity.this,
					"", Const.LOAD_MESSAGE);
			esevice.getBusinessType(2, new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

				@Override
				public void setEmergencySeviceImplListListenser(Object object,
						String stRerror, String Exceptionerror) {
					// TODO Auto-generated method stub
					List<BusinessTypeEntity> businessTypeList = null;
					if (object != null) {
						businessTypeList = (List<BusinessTypeEntity>) object;

						// setData(businessTypeList);
					} else if (stRerror != null) {
						businessTypeList = new ArrayList<BusinessTypeEntity>();

					} else if (Exceptionerror != null) {
						businessTypeList = new ArrayList<BusinessTypeEntity>();
						ToastUtil.showToast(EmergencyTypeActivity.this,
								Const.NETWORKERROR + ":" + Exceptionerror);
					}
					Message message = new Message();
					message.what = 0;
					message.obj = businessTypeList;
					handler.sendMessage(message);
//					if (Utils.getInstance().progressDialog.isShowing()) {
						Utils.getInstance().hideProgressDialog();
//					}
				}
			});
			}else if (businessTypeList!=null&&businessTypeList.size()>0) {
				Message message = new Message();
				message.what = 0;
				message.obj = businessTypeList;
				handler.sendMessage(message);
			}
		} else if (tags.equals("3")) {
			if (businessTypeList!=null&&businessTypeList.size()==0) {//只访问一次网络
			Utils.getInstance().showProgressDialog(EmergencyTypeActivity.this,
					"", Const.LOAD_MESSAGE);
			esevice.getEventScene(new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

				@Override
				public void setEmergencySeviceImplListListenser(Object object,
						String stRerror, String Exceptionerror) {
					// TODO Auto-generated method stub
					List<BusinessTypeEntity> businessTypeList = null;
					if (object != null) {
						businessTypeList = (List<BusinessTypeEntity>) object;

						// setData(businessTypeList);
					} else if (stRerror != null) {
						businessTypeList = new ArrayList<BusinessTypeEntity>();

					} else if (Exceptionerror != null) {
						businessTypeList = new ArrayList<BusinessTypeEntity>();
						ToastUtil.showToast(EmergencyTypeActivity.this,
								Const.NETWORKERROR + ":" + Exceptionerror);
					}
					Message message = new Message();
					message.what = 0;
					message.obj = businessTypeList;
					handler.sendMessage(message);
//					if (Utils.getInstance().progressDialog.isShowing()) {
						Utils.getInstance().hideProgressDialog();
//					}
				}
			});
			}else if (businessTypeList!=null&&businessTypeList.size()>0) {
				Message message = new Message();
				message.what = 0;
				message.obj = businessTypeList;
				handler.sendMessage(message);
			}
		} else if (tags.equals("4")) {
			//判断节点
			if(nodeStepType.equals("ExclusiveGateway")) {
				businessTypeList.clear();
				businessTypeList.addAll(arrlist);
			}
			else {
				for (int i = 0; i < 3; i++) {
					BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
					switch (i) {
						case 0:
							businessTypeEntity.setName("部分完成");
							break;
						case 1:
							businessTypeEntity.setName("全部完成");
							break;
						case 2:
							businessTypeEntity.setName("跳过");
							break;
					}
					businessTypeList.add(businessTypeEntity);
				}
			}
			Message message = new Message();
			message.what = 0;
			message.obj = businessTypeList;
			handler.sendMessage(message);
		} else if (tags.equals("5")) {
			for (int i = 0; i < 3; i++) {
				BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
				switch (i) {
				case 0:
					businessTypeEntity.setName("对外");
					break;
				case 1:
					businessTypeEntity.setName("对内");
					break;
				case 2:
					businessTypeEntity.setName("对监管");
					break;
				}
				businessTypeList.add(businessTypeEntity);
			}
			Message message = new Message();
			message.what = 0;
			message.obj = businessTypeList;
			handler.sendMessage(message);
		} else if (tags.equals("6")) {
			for (int i = 0; i < 4; i++) {
				BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
				switch (i) {
				case 0:
					businessTypeEntity.setName("预案启动");
					break;
				case 1:
					businessTypeEntity.setName("决策授权");
					break;
				case 2:
					businessTypeEntity.setName("业务验证");
					break;
				case 3:
					businessTypeEntity.setName("业务恢复");
					break;
				}
				businessTypeList.add(businessTypeEntity);
			}
			Message message = new Message();
			message.what = 0;
			message.obj = businessTypeList;
			handler.sendMessage(message);
		}
	}

	/**
	 * 给list添加数据
	 */
	private void setData(List<BusinessTypeEntity> businessTypeList) {
		if (businessTypeList == null || businessTypeList.size() == 0)
			return;
		if (arrlist!=null&&arrlist.size()>0) {
			businessTypeList.clear();
			businessTypeList.addAll(arrlist);
			
		}
			// 清除已经选择的项
			mSelectAdapter = new ListviewCheckboxAdapter(
					EmergencyTypeActivity.this, businessTypeList, "");
			mListView.setAdapter(mSelectAdapter);
			mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			mSelectAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_actionbar_editData:
			Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("arrlist", (Serializable) mSelectAdapter.list);
				intent.putExtras(bundle);
				EmergencyTypeActivity.this.setResult(RESULT_OK, intent);
			EmergencyTypeActivity.this.finish();
			break;

		case R.id.iv_actionbar_back:
			// EmergencyTypeActivity.this.setResult(RESULT_CANCELED);
			EmergencyTypeActivity.this.finish();

			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
