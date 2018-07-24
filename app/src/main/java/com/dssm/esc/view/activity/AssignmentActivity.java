package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.PlanProcessEntity;
import com.dssm.esc.model.entity.emergency.PlanTreeEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.util.treeview.TreeNode;
import com.dssm.esc.util.treeview.TreeView;
import com.dssm.esc.util.treeview.view.MyNodeViewFactory;
import com.dssm.esc.view.adapter.ListviewCheckboxAdapter;
import com.dssm.esc.view.widget.SegmentControl;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
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
@ContentView(R.layout.activity_assignment)
public class AssignmentActivity extends BaseActivity implements
		MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;
	/** 确定 */
	@ViewInject(R.id.tv_actionbar_editData)
	private TextView sure;
	/** 1，相关人员；2，应急小组 (SegmentControl点击) */
	private int sem_tags;
	@ViewInject(R.id.segment_control_assign)
	private SegmentControl mSegment;
	/** ListView */
	@ViewInject(R.id.assign_listview)
	private ListView mListView;
	/** 类型数据 */
	private ArrayList<BusinessTypeEntity> list = new ArrayList<>();
	private ListviewCheckboxAdapter mSelectAdapter;
	@ViewInject(R.id.ll_contact_list)
	private LinearLayout expandableLayout;
	/** 父list显示组 */
	private List<PlanTreeEntity> planTreeList = new ArrayList<>();
	private String id = "";// 流程步骤id
	private String planInfoId = ""; // 预案执行id
	private String executePeople = ""; // 执行人姓名
	private PlanProcessEntity entity;

	private TreeNode root;
	private TreeView treeView;

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
				planTreeList = (List<PlanTreeEntity>) msg.obj;
				Log.i("result", planTreeList.size() + "");
				addTreeView();
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
//		setContentView(R.layout.activity_assignment);
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

	private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listener = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			if (backflag) {
				ToastUtil.showToast(AssignmentActivity.this, "操作成功");
				//刷新指派列表
				EventBus.getDefault().post(new mainEvent("refr"));
				finish();
			} else if (backflag == false) {
				ToastUtil.showToast(AssignmentActivity.this, "操作失败");
			}
			if (Utils.getInstance().progressDialog.isShowing()) {
				Utils.getInstance().hideProgressDialog();
			}
		}
	};

	private void initview() {
		mBack.setVisibility(View.VISIBLE);
		title.setText("指派其他");
		sem_tags = 1;// 默认相关人员
		/**
		 * 为Adapter准备数据
		 */
		initListviewData(sem_tags);
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
				if (mSelectAdapter != null) {
					if(mSelectAdapter.beSelectedData.size() > 0) {
						executePeopleId = mSelectAdapter.beSelectedData.get(0).getId();
						executePeople = mSelectAdapter.beSelectedData.get(0).getName();
					}
				}
				List<TreeNode> treeNodes = treeView.getSelectedNodes();
				for(TreeNode treeNode : treeNodes) {
					if(treeNode.getValue() instanceof ChildEntity) {
						executePeopleId = ((ChildEntity) treeNode.getValue()).getChild_id();
						executePeople = ((ChildEntity) treeNode.getValue()).getName();
					}
				}
				Log.i("executePeopleId", executePeopleId);
				Log.i("executePeople", executePeople);
				if (!executePeopleId.equals("")) {
					Utils.getInstance().showProgressDialog(
							AssignmentActivity.this, "", Const.SUBMIT_MESSAGE);
					Control.getinstance().getEmergencyService().assign(id, planInfoId, executePeopleId,
							executePeople, listener);
				} else {
					ToastUtil.showToast(AssignmentActivity.this, "请选择指派人员");
				}
			}
		});
	}

	private void initListviewData(int sem_tags) {
		if (sem_tags == 1) {
			// 相关人员
			expandableLayout.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			// 清除应急小组已经选择的项
			if(treeView != null)
				treeView.deselectAll();
			initListData();

		} else if (sem_tags == 2) {
			// 应急小组
			mListView.setVisibility(View.GONE);
			expandableLayout.setVisibility(View.VISIBLE);
			
			// 初始化，默认加载任务通知界面,避免重复加载
			if (planTreeList.size() == 0)
				initExpandListData();
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

	private void addTreeView() {
		root = TreeNode.root();
		buildTree(planTreeList);
		//tag = 3,CheckBox只能单选
		treeView = new TreeView(root, this, new MyNodeViewFactory(), "3");
		View view = treeView.getView();
		view.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		expandableLayout.addView(view);
	}

	private void buildTree(List<PlanTreeEntity> planTreeList) {
		for (int i = 0; i < planTreeList.size(); i++) {
			TreeNode treeNode = new TreeNode(planTreeList.get(i).getName());
			treeNode.setLevel(0);
			for (int j = 0; j < planTreeList.get(i).getEmeGroups().size(); j++) {
				TreeNode treeNode1 = new TreeNode(planTreeList.get(i).getEmeGroups().get(j).getGroupname());
				treeNode1.setLevel(1);
				for (int k = 0; k < planTreeList.get(i).getEmeGroups().get(j).getcList().size(); k++) {
					TreeNode treeNode2 = new TreeNode(planTreeList.get(i).getEmeGroups().get(j).getcList().get(k));
					treeNode2.setLevel(2);
					treeNode1.addChild(treeNode2);
				}
				treeNode.addChild(treeNode1);
			}
			root.addChild(treeNode);
		}
	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			List<PlanTreeEntity> dataList = null;
			if (object != null) {
				dataList = (List<PlanTreeEntity>) object;

			} else if (stRerror != null) {
				dataList = new ArrayList<>();

			} else if (Exceptionerror != null) {
				dataList = new ArrayList<>();
				ToastUtil.showToast(AssignmentActivity.this,
						Const.NETWORKERROR);
			}
			Message message = new Message();
			message.what = 1;
			message.obj = dataList;
			handler.sendMessage(message);
			Utils.getInstance().hideProgressDialog();
		}
	};

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
		Control.getinstance().getEmergencyService().getSignEmergencyInfo(planInfoId, listListener);
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
