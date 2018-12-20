package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.DrillProcationDetailEntity;
import com.dssm.esc.model.entity.emergency.DrillProcationDetailObjEntity;
import com.dssm.esc.model.entity.emergency.DrillProjectDetailObjPreinfoEntity;
import com.dssm.esc.model.entity.emergency.DrillProjectNameEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.adapter.DrillselectListviewAdapter;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * 演练界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_drillselect)
public class DrillSelectActivity extends BaseActivity implements
		MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;
	/** ListView */
	@ViewInject(R.id.drill_type_listview)
	private ListView mListView;
	/** 暂无数据 */
	@ViewInject(R.id.ll_no_data)
	private LinearLayout ll_no_data;
	/** 类型数据 */
	private ArrayList<DrillProjectNameEntity> list = new ArrayList<DrillProjectNameEntity>();
	/** 适配器 */
	private DrillselectListviewAdapter mSelectAdapter;
	/** 被选中的list */
	private ArrayList<DrillProjectNameEntity> type = new ArrayList<DrillProjectNameEntity>();

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<DrillProjectNameEntity> result = (ArrayList<DrillProjectNameEntity>) msg.obj;
			switch (msg.what) {
			case 0:
				list.clear();
				list.addAll(result);

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

//		setContentView(R.layout.activity_drillselect);
		View findViewById = findViewById(R.id.drillselect);
		findViewById.setFitsSystemWindows(true);
		initView();

		lvListener();

	}

	/***
	 * 打开EventBus开关
	 */
	protected boolean useEventBus() {
		return true;
	}

	/**
	 * 刷新界面
	 * 
	 * @param data
	 */
	public void onEvent(mainEvent data) {
		if (data.getData().equals("ref")) {

//			initData();
			finish();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub

		mSelectTypeTitle.setText("选择演练计划");
		mBack.setVisibility(View.VISIBLE);
		mListView.setEmptyView(ll_no_data);
		initData();
		// setNetListener(this);
	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser emergencySeviceImplListListenser = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(Object object,
				String stRerror, String Exceptionerror) {
			// TODO Auto-generated method stub
			if (object != null) {
				list = (ArrayList<DrillProjectNameEntity>) object;
			} else if (stRerror != null) {
				list = new ArrayList<DrillProjectNameEntity>();

			} else if (Exceptionerror != null) {
				list = new ArrayList<DrillProjectNameEntity>();
				ToastUtil.showToast(DrillSelectActivity.this,
						Const.NETWORKERROR);

			}
			mSelectAdapter = new DrillselectListviewAdapter(
					DrillSelectActivity.this, list);
			mListView.setAdapter(mSelectAdapter);
			mSelectAdapter.notifyDataSetChanged();
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void initData() {
		// TODO Auto-generated method stub
		Utils.getInstance().showProgressDialog(
				DrillSelectActivity.this, "",
				Const.SUBMIT_MESSAGE);
		Control.getinstance().getEmergencyService().getDrillProjectName(emergencySeviceImplListListenser);

	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser detailEmergencySeviceImplListListenser = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			if (object != null) {
				DrillProcationDetailEntity detailEntity = (DrillProcationDetailEntity) object;
				DrillProcationDetailObjEntity obj = detailEntity
						.getObj();
				List<DrillProjectDetailObjPreinfoEntity> preInfo = obj
						.getPreInfo();
				Intent intent = new Intent(
						DrillSelectActivity.this,
						AddeValuationActivity.class);
				intent.putExtra("type", "0");// 添加评估
				intent.putExtra("tag", "2");
				intent.putExtra("emergType",
						obj.getEmergType());
				intent.putExtra("drillPlanId",
						obj.getDrillPlanId());
				intent.putExtra("drillPlanName",
						obj.getDrillPlanName());
				String precautionName = "";
				String precautionId = obj.getReferPlan();
				precautionName = obj.getPrecautionList().replace("[", "")
						.replace("]", "")
						.replace("\"", "");
				intent.putExtra("precautionName",
						precautionName);// 预案名称
				intent.putExtra("precautionId",
						precautionId);// 预案编号
				intent.putExtra("exPlanId",
						obj.getExPlanId());
				intent.putExtra("referProcess",
						obj.getReferProcess());
				Log.i("演练预案名称", precautionName);
				Log.i("演练预案名称id", precautionId);
				startActivity(intent);
			} else if (stRerror != null) {

			} else if (Exceptionerror != null) {
				ToastUtil.showToast(
						DrillSelectActivity.this,
						Const.NETWORKERROR);
			}
		}
	};

	/**
	 * 监听ListView
	 */
	private void lvListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				// emergType 演练类型
				// drillPlanId 演练详细计划ID
				// drillPlanName 演练详细计划名称
				// exPlanId;//演练初始计划id
				DrillProjectNameEntity drillProjectNameEntity = list
						.get(position);
				String drillPlanId = drillProjectNameEntity.getId();
				String drillPlanName = drillProjectNameEntity.getName();
				try {
					URLEncoder.encode(URLEncoder.encode(drillPlanName), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Control.getinstance().getEmergencyService().getDrillProjectNameDetail(drillPlanId, drillPlanName, detailEmergencySeviceImplListListenser);
			}
		});

	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}

}
