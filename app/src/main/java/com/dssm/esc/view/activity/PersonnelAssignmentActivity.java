package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.PlanProcessEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.adapter.PersonAssignListvAdapter;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员指派界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_person_assignment)
public class PersonnelAssignmentActivity extends BaseActivity implements MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;
	/** 1，应急；2，演练 */
	// private String tag;
	/** ListView */
	@ViewInject(R.id.assign_step_listview)
	private ListView listView;
	/** 暂无数据 */
	@ViewInject(R.id.pemptytv)
	private TextView emptytv;
	/** 类型数据 */
	private ArrayList<PlanProcessEntity> list = new ArrayList<>();
	private ArrayList<PlanProcessEntity> entities = new ArrayList<>();
	/** 适配器 */
	private PersonAssignListvAdapter mSelectAdapter;
	private String id;// 流程步骤id
	private String planInfoId;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<PlanProcessEntity> result = (ArrayList<PlanProcessEntity>) msg.obj;
			switch (msg.what) {
			case 0:

				list.clear();
				list.addAll(result);
				mSelectAdapter = new PersonAssignListvAdapter(planInfoId,
						PersonnelAssignmentActivity.this, list);
				listView.setAdapter(mSelectAdapter);
				mSelectAdapter.notifyDataSetChanged();

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

//		setContentView(R.layout.activity_person_assignment);
		View findViewById = findViewById(R.id.person_assignment);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		// tag = intent.getStringExtra("tag");
		planInfoId = intent.getStringExtra("id");
		initview();
		lvListener();

	}

	private void initview() {
		mBack.setVisibility(View.VISIBLE);
		title.setText("人员指派");
		listView.setEmptyView(emptytv);
		initData();
//		setNetListener(this);

	}
	/***
	 * 打开EventBus开关
	 */
	protected boolean useEventBus() {
		return true;
	}

	/**
	 * 刷新界面
	 * @param data
	 */
	public void onEvent(mainEvent data) {
		if (data.getData().equals("refr")) {
			
			initData();
		}
	}

	private void addIndex(List<PlanProcessEntity> result) {
		for(PlanProcessEntity planProcessEntity : result) {
			if(planProcessEntity.getParentProcessStepId() == null || planProcessEntity.getParentProcessStepId().equals("null"))
				planProcessEntity.setParentProcessStepId("");
			int index = 0;
			String parentOrderNum = "";
			String parentId = planProcessEntity.getParentProcessStepId();
			while (parentId != null && !"".equals(parentId)) {
				index++;
				int i = 0;
				for (PlanProcessEntity planProcessEntity1 : result) {
					if(planProcessEntity1.getId().equals(parentId)) {
						parentId = planProcessEntity1.getParentProcessStepId();
						String num = "";
						if (!planProcessEntity1.getOrderNum().equals("null")) {
							num = planProcessEntity1.getOrderNum() + ".";
						}
						else if (!planProcessEntity1.getEditOrderNum().equals("null")) {
							num = planProcessEntity1.getEditOrderNum() + ".";
						}
						parentOrderNum = num + parentOrderNum;
						i++;
						break;
					}
				}
				if(i == 0)
					break;
			}
			planProcessEntity.setIndex(index);
			planProcessEntity.setParentProcessNumber(parentOrderNum);
		}
	}

	private void reSort(List<PlanProcessEntity> result, String parentId, int index) {
		for(PlanProcessEntity planProcessEntity : result) {
			if(planProcessEntity.getIndex() == index
					&& parentId.equals(planProcessEntity.getParentProcessStepId())
					&& !planProcessEntity.getNodeStepType().equals("drillNew")) {
				entities.add(planProcessEntity);
				if("CallActivity".equals(planProcessEntity.getNodeStepType())) {
					reSort(result, planProcessEntity.getId(), index + 1);
				}
			}
		}
	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			ArrayList<PlanProcessEntity> result = null;
			if (object != null) {

				result = (ArrayList<PlanProcessEntity>) object;

			} else if (stRerror != null) {
				result = new ArrayList<PlanProcessEntity>();

			} else if (Exceptionerror != null) {
				result = new ArrayList<PlanProcessEntity>();
				ToastUtil.showToast(
						PersonnelAssignmentActivity.this,
						Const.NETWORKERROR);
			}
			entities.clear();
			//增加子预案层级标记
			addIndex(result);
			//根据子预案层级关系重新排序
			reSort(result, "", 0);
			result.clear();
			result.addAll(entities);

			Message msg = new Message();
			msg.what = 0;
			msg.obj = result;
			handler.sendMessage(msg);
//						if (Utils.getInstance().progressDialog.isShowing()) {
			Utils.getInstance().hideProgressDialog();
//						}
		}
	};

	private void initData() {
		// TODO Auto-generated method stub
		Utils.getInstance().showProgressDialog(
				PersonnelAssignmentActivity.this, "", Const.LOAD_MESSAGE);
		Control.getinstance().getEmergencyService().getPlanProcessList(planInfoId, listListener);

	}

	/**
	 * 监听ListView
	 */
	private void lvListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}

}
