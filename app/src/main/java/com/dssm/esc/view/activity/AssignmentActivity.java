package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.entity.emergency.PlanProcessEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.adapter.ExpanListCheckboxAdapter;
import com.dssm.esc.view.adapter.ListviewCheckboxAdapter;
import com.dssm.esc.view.widget.SegmentControl;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 指派界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-12
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class AssignmentActivity extends BaseActivity implements
		MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView title;
	/** 返回 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView mBack;
	/** 确定 */
	@ViewInject(id = R.id.tv_actionbar_editData)
	private TextView sure;
	/** 1，应急；2，演练 */
	// private String tag;
	/** 1，相关人员；2，应急小组 (SegmentControl点击) */
	private int sem_tags;
	@ViewInject(id = R.id.segment_control_assign)
	private SegmentControl mSegment;
	/** ListView */
	@ViewInject(id = R.id.assign_listview)
	private ListView mListView;
	/** 类型数据 */
	private ArrayList<BusinessTypeEntity> list = new ArrayList<BusinessTypeEntity>();
	/** ListView适配器 */
	private ListviewCheckboxAdapter mSelectAdapter = null;
	/** ListView被选中的选项 */
	//private ArrayList<BusinessTypeEntity> beSelectedData = new ArrayList<BusinessTypeEntity>();
	/** ListView用来控制CheckBox的选中状况 */
//	private HashMap<Integer, Boolean> isSelected;
	/** 可扩展ListView */
	@ViewInject(id = R.id.assign_expandlistview)
	private ExpandableListView expandableList;
	/** 可扩展ListView适配器 */
	private ExpanListCheckboxAdapter adapter;
	/** 父list显示组 */
	private List<GroupEntity> groupList = new ArrayList<GroupEntity>();
	/** 子list显示人 */
	private List<ChildEntity> childList = new ArrayList<ChildEntity>();
	/** 可扩展ListView用来控制CheckBox的选中状况 */
	private HashMap<String, Boolean> statusHashMap;
	/** 被选的人的和name */
	public String beSelectId []= new String [2];
	// MyExpandListAdapterTest adapter;
	private String id = "";// 流程步骤id
	private String planInfoId = ""; // 预案执行id
	// private String executePeopleId = ""; // 执行人id
	private String executePeople = ""; // 执行人姓名
	private PlanProcessEntity entity;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mSelectAdapter = new ListviewCheckboxAdapter(
						AssignmentActivity.this, list,
						"7");
				mListView.setAdapter(mSelectAdapter);
				mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				mSelectAdapter.notifyDataSetChanged();
				break;
			case 1:
				groupList = (List<GroupEntity>) msg.obj;
				Log.i("result", groupList.size() + "");
				adapter = new ExpanListCheckboxAdapter(AssignmentActivity.this,
						statusHashMap, groupList); // 实例化适配器
				expandableList.setAdapter(adapter); // 设置适配器
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
		setContentView(R.layout.activity_assignment);
		View findViewById = findViewById(R.id.assignment);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		// tag = intent.getStringExtra("tag");
		planInfoId = intent.getStringExtra("planInfoId");
		id = intent.getStringExtra("id");
		entity = (PlanProcessEntity) intent.getSerializableExtra("entity");
		initview();
		sure.setVisibility(View.VISIBLE);
		sure.setText("确定");
		segmentControlListDate();
	}

	private void initview() {
		mBack.setVisibility(View.VISIBLE);
		// if (tag.equals("1")) {

		title.setText("指派其他");
		sem_tags = 1;// 默认相关人员
		statusHashMap = new HashMap<String, Boolean>();
		/**
		 * 为Adapter准备数据
		 */
		initListviewData(sem_tags);
		expandableList.setGroupIndicator(null);
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// id 流程步骤id
				// planInfoId 预案执行id
				// executePeopleId 执行人id
				// executePeople 执行人姓名
				Log.i("id", id);
				Log.i("planInfoId", planInfoId);
				String executePeopleId = ""; // 执行人id
				if (mSelectAdapter!=null) {
					
					if (mSelectAdapter.beSelectedData.size() > 0) {
						
						executePeopleId = mSelectAdapter.beSelectedData.get(0).getId();
						executePeople = mSelectAdapter.beSelectedData.get(0).getName();
					}
				}
				if (beSelectId[0]!=null&&beSelectId[1]!=null) {
					executePeopleId = beSelectId[0];
					executePeople = beSelectId[1];
				}
				Log.i("executePeopleId", executePeopleId);
				Log.i("executePeople", executePeople);
				// if (!executePeopleId.equals("")) {
				//
				// }
				if (!executePeopleId.equals("")) {
					Utils.getInstance().showProgressDialog(
							AssignmentActivity.this, "", Const.SUBMIT_MESSAGE);
					Control.getinstance().getEmergencyService().assign(id, planInfoId, executePeopleId,
							executePeople,
							new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

								@Override
								public void setEmergencySeviceImplListenser(
										Boolean backflag, String stRerror,
										String Exceptionerror) {
									// TODO Auto-generated method stub
									if (backflag) {
										ToastUtil
												.showToast(
														AssignmentActivity.this,
														stRerror);
										EventBus.getDefault().post(
												new mainEvent("refr"));//刷新指派列表
										finish();
									} else if (backflag == false) {

										ToastUtil
												.showToast(
														AssignmentActivity.this,
														stRerror);
									}
									if (Utils.getInstance().progressDialog
											.isShowing()) {
										Utils.getInstance()
												.hideProgressDialog();
									}
								}
							});

				} else {
					ToastUtil.showToast(AssignmentActivity.this, "请选择指派人员");
				}
				// Log.i("executePeopleId", executePeopleId);
				// Log.i("executePeople", executePeople);

			}
		});
//		setNetListener(this);
	}

	private void initListviewData(int sem_tags) {
		if (sem_tags == 1) {// 相关人员
			expandableList.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			// 清除已经选择的项
			if (beSelectId[0]!=null&& beSelectId[1]!=null) {// 应急小组若有已选的，则清空
				if(mSelectAdapter != null) {
					mSelectAdapter.beSelectedData.clear();
					mSelectAdapter.notifyDataSetChanged();// 通知数据发生了变化
				}
				// statusHashMap.put(adapter.getGroup(i).getcList().get(k)
				// .getChild_id(), false);
			}
			initListData();

		} else if (sem_tags == 2) {// 应急小组
			mListView.setVisibility(View.GONE);
			expandableList.setVisibility(View.VISIBLE);
			
			// 初始化，默认加载任务通知界面,避免重复加载
			if (groupList.size() == 0) {

				initExpandListData();

			}else{
				adapter.notifyDataSetChanged();	
			}

		}

	}

	/**
	 * 
	 * 初始化数据（listview）
	 * 
	 * @version 1.0
	 * @createTime 2015-9-12,下午2:44:40
	 * @updateTime 2015-9-12,下午2:44:40
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	private void initListData() {
		// TODO Auto-generated method stub

		if (list == null || list.size() == 0) {
			int length = 0;
			if (!entity.getExecutorA().equals("")
					&& entity.getExecutorA().length() > 0
					&& entity.getExecutorA() != null
					&& !entity.getExecutorA().equals("null")) {
				BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
				length++;
				Log.i("executorA指派界面", entity.getExecutorA());
				Log.i("executorAName指派界面", entity.getExecutorAName());
				businessTypeEntity.setId(entity.getExecutorA());
				businessTypeEntity.setSignState(entity.getSignStateA());
				businessTypeEntity.setRole("A角");
				businessTypeEntity.setName(entity.getExecutorAName());
				list.add(businessTypeEntity);
			}
			if (!entity.getExecutorB().equals("")
					&& entity.getExecutorB().length() > 0
					&& entity.getExecutorB() != null
					&& !entity.getExecutorB().equals("null")) {
				BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
				length++;
				Log.i("ExecutorB指派界面", entity.getExecutorB());
				Log.i("executorBName指派界面", entity.getExecutorBName());
				businessTypeEntity.setId(entity.getExecutorB());
				businessTypeEntity.setSignState(entity.getSignStateB());
				businessTypeEntity.setRole("B角");
				businessTypeEntity.setName(entity.getExecutorBName());
				list.add(businessTypeEntity);
			}
			if (!entity.getExecutorC().equals("")
					&& entity.getExecutorC().length() > 0
					&& entity.getExecutorC() != null
					&& !entity.getExecutorC().equals("null")) {
				BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
				length++;
				Log.i("executorC指派界面", entity.getExecutorC());
				Log.i("executorCName指派界面", entity.getExecutorCName());
				businessTypeEntity.setId(entity.getExecutorC());
				businessTypeEntity.setId(entity.getExecutorC());
				businessTypeEntity.setSignState(entity.getSignStateC());
				businessTypeEntity.setRole("C角");
				businessTypeEntity.setName(entity.getExecutorCName());
				list.add(businessTypeEntity);
			}

			if (length > 0) {

				Message msg = new Message();
				msg.what = 0;
				msg.obj = list;
				handler.sendMessage(msg);
			
			}

		}

	}

	/**
	 * 
	 * 初始化数据（expandlistview）
	 * 
	 * @version 1.0
	 * @createTime 2015-9-8,下午8:38:59
	 * @updateTime 2015-9-8,下午8:38:59
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	private void initExpandListData() {
		Utils.getInstance().showProgressDialog(AssignmentActivity.this, "",
				Const.LOAD_MESSAGE);
		Control.getinstance().getEmergencyService().getSignEmergencyInfo(planInfoId,
				new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

					@Override
					public void setEmergencySeviceImplListListenser(
							Object object, String stRerror,
							String Exceptionerror) {
						// TODO Auto-generated method stub
						List<GroupEntity> dataList = null;
						if (object != null) {
							dataList = (List<GroupEntity>) object;

						} else if (stRerror != null) {
							dataList = new ArrayList<GroupEntity>();

						} else if (Exceptionerror != null) {
							dataList = new ArrayList<GroupEntity>();
							ToastUtil.showToast(AssignmentActivity.this,
									Const.NETWORKERROR + ":" + Exceptionerror);
						}
						Message message = new Message();
						message.what = 1;
						message.obj = dataList;
						handler.sendMessage(message);
//						if (Utils.getInstance().progressDialog.isShowing()) {
							Utils.getInstance().hideProgressDialog();
//						}
					}
				});
	}

	/**
	 * 
	 * selectButton控制界面
	 * 
	 * @version 1.0
	 * @createTime 2015-9-12,下午2:22:10
	 * @updateTime 2015-9-12,下午2:22:10
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	private void segmentControlListDate() {
		// TODO Auto-generated method stub
		mSegment.setmOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
			@Override
			public void onSegmentControlClick(int index) {
				switch (index) {
				case 0:// 1,1事件详情
					sem_tags = 1;

					break;	
				case 1:// 2预案详情
					sem_tags = 2;
					break;
				
				}
				initListviewData(sem_tags);
			}
		});
	}

	
	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initListviewData(sem_tags);
	}
}
