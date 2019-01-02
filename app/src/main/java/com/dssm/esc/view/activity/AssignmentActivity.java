package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.entity.emergency.PlanProcessEntity;
import com.dssm.esc.model.entity.emergency.PlanTreeEntity;
import com.dssm.esc.util.CharacterParser;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.util.treeview.TreeNode;
import com.dssm.esc.util.treeview.TreeView;
import com.dssm.esc.util.treeview.view.MyNodeViewFactory;
import com.dssm.esc.view.adapter.ListviewCheckboxAdapter;
import com.dssm.esc.view.widget.ClearEditText;
import com.dssm.esc.view.widget.SegmentControl;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 指派界面
 */
@ContentView(R.layout.activity_assignment)
public class AssignmentActivity extends BaseActivity implements
		MainActivity.onInitNetListener, OnClickListener {

	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;
	/** 确定 */
	@ViewInject(R.id.tv_actionbar_editData)
	private TextView sure;
	/** 1，相关人员；2，应急小组 (SegmentControl点击) */
	private int sem_tags;
	@ViewInject(R.id.rb_relation_people)
	private RadioButton rb_relation_people;
    @ViewInject(R.id.rb_emergency_group)
    private RadioButton rb_emergency_group;
	/** ListView */
	@ViewInject(R.id.assign_listview)
	private ListView mListView;
	/** 搜索布局 */
	@ViewInject(R.id.filter_edit_ll)
	private LinearLayout filter_edit_ll;
	/** 搜索输入框 */
	@ViewInject(R.id.filter_edit)
	private ClearEditText filter_edit;
	/** 类型数据 */
	private ArrayList<BusinessTypeEntity> list = new ArrayList<>();
	private ListviewCheckboxAdapter mSelectAdapter;
	@ViewInject(R.id.ll_contact_list)
	private LinearLayout expandableLayout;
	/** 暂无数据 */
	@ViewInject(R.id.ll_no_data)
	private LinearLayout ll_no_data;
	/** 全部数据 */
	private List<PlanTreeEntity> planTreeList = new ArrayList<>();
	/** 搜索过滤后数据 */
	private List<PlanTreeEntity> searchPlanTreeList = new ArrayList<>();
	/** 汉字转换成拼音的类 */
	private CharacterParser characterParser = CharacterParser.getInstance();
	private String id = "";// 流程步骤id
	private String planInfoId = ""; // 预案执行id
	private String isSign = "1"; // 指派操作是否需要人员签到，当值为“0”时能指派给未签到人员
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
		View findViewById = findViewById(R.id.assignment);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		// tag = intent.getStringExtra("tag");
		planInfoId = intent.getStringExtra("planInfoId");
		isSign = intent.getStringExtra("isSign");
		id = intent.getStringExtra("id");
		entity = (PlanProcessEntity) intent.getSerializableExtra("entity");
		initview();
		sure.setText("确定");
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
		mBack.setOnClickListener(this);
        rb_relation_people.setOnClickListener(this);
        rb_emergency_group.setOnClickListener(this);
		sure.setOnClickListener(this);
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
				if(treeView != null) {
					List<TreeNode> treeNodes = treeView.getSelectedNodes();
					for (TreeNode treeNode : treeNodes) {
						if (treeNode.getValue() instanceof ChildEntity) {
							executePeopleId = ((ChildEntity) treeNode.getValue()).getPostFlag();
							executePeople = ((ChildEntity) treeNode.getValue()).getName();
						}
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

		filter_edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				filterData(s.toString());
			}
		});
	}

	private void initListviewData(int sem_tags) {
		if (sem_tags == 1) {
			// 相关人员
			expandableLayout.setVisibility(View.GONE);
			filter_edit_ll.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			// 清除应急小组已经选择的项
			if(treeView != null)
				treeView.deselectAll();
			initListData();

		} else if (sem_tags == 2) {
			// 应急小组
			mListView.setVisibility(View.GONE);
			filter_edit_ll.setVisibility(View.VISIBLE);
			expandableLayout.setVisibility(View.VISIBLE);

			// 初始化，默认加载任务通知界面,避免重复加载
			if (planTreeList.size() == 0)
				initExpandListData();
		}

	}

	/**
	 * 
	 * 初始化数据（listview）
	 */
	private void initListData() {
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
		//tag = 3,CheckBox只能单选、isSign="1";tag = 4,CheckBox只能单选、isSign="0"
		if("0".equals(isSign))
			treeView = new TreeView(root, this, new MyNodeViewFactory(), "4");
		else
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
				int total = 0;
				int signCount = 0;
				for (int k = 0; k < planTreeList.get(i).getEmeGroups().get(j).getcList().size(); k++) {
					TreeNode treeNode2 = new TreeNode(planTreeList.get(i).getEmeGroups().get(j).getcList().get(k));
					treeNode2.setLevel(2);
					treeNode1.addChild(treeNode2);
					total++;
					if("1".equals(planTreeList.get(i).getEmeGroups().get(j).getcList().get(k).getSignin()))
						signCount++;
				}
				treeNode1.setValue(planTreeList.get(i).getEmeGroups().get(j).getGroupname()
						+"（" + signCount + "/" + total + "）");
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
	 */
	private void initExpandListData() {
		Utils.getInstance().showProgressDialog(AssignmentActivity.this, "",
				Const.LOAD_MESSAGE);
		Control.getinstance().getEmergencyService().getSignEmergencyInfo(planInfoId, listListener);
	}
	
	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initListviewData(sem_tags);
	}

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_actionbar_back:
                finish();
                break;
            case R.id.tv_actionbar_editData:
                finish();
                break;
            case R.id.rb_relation_people:
                sem_tags = 1;
                rb_relation_people.setChecked(true);
                rb_emergency_group.setChecked(false);
                initListviewData(sem_tags);
                break;
            case R.id.rb_emergency_group:
                sem_tags = 2;
                rb_relation_people.setChecked(false);
                rb_emergency_group.setChecked(true);
                initListviewData(sem_tags);
                break;
        }
    }

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 */
	private void filterData(String filterStr) {
		List<PlanTreeEntity> filterList = new ArrayList<PlanTreeEntity>();
		if (TextUtils.isEmpty(filterStr)) {
			filterList = planTreeList;
		} else {
			filterList.clear();
			for (int i = 0; i < planTreeList.size(); i++) {
				PlanTreeEntity first = new PlanTreeEntity();
				List<GroupEntity> second = new ArrayList<>();
				List<GroupEntity> emeGroups = planTreeList.get(i).getEmeGroups();
				for (int j = 0; j < emeGroups.size(); j++) {
					GroupEntity groupEntity = new GroupEntity();
					List<ChildEntity> cList = emeGroups.get(j).getcList();
					List<ChildEntity> third = new ArrayList<>();
					for (int k = 0; k < cList.size(); k++) {
						if (cList.get(k).getName().indexOf(filterStr.toString()) != -1
								|| characterParser.getSelling(cList.get(k).getName()).startsWith(
								filterStr.toString())
								|| cList.get(k).getEmergTeam().indexOf(filterStr.toString()) != -1
								|| characterParser.getSelling(cList.get(k).getEmergTeam()).startsWith(
								filterStr.toString())) {
							third.add(cList.get(k));
						}
					}
					if(third.size() > 0)
					{
						groupEntity.setGroup_id(emeGroups.get(j).getGroup_id());
						groupEntity.setGroupname(emeGroups.get(j).getGroupname());
						groupEntity.setcList(third);
						second.add(groupEntity);
					}
				}
				if(second.size() > 0) {
					first.setTreeId(planTreeList.get(i).getTreeId());
					first.setName(planTreeList.get(i).getName());
					first.setEmeGroups(second);
					filterList.add(first);
				}
			}
			// 根据a-z进行排序
			// Collections.sort(groupList, pinyinComparator);
		}

		// 如果查询的结果为0时，显示为搜索到结果的提示
		if (filterList.size() == 0) {
			expandableLayout.setVisibility(View.GONE);
			ll_no_data.setVisibility(View.VISIBLE);
		}
		else {
			searchPlanTreeList = filterList;
			initData();
			expandableLayout.setVisibility(View.VISIBLE);
			ll_no_data.setVisibility(View.GONE);
		}
	}

	private void initData() {
		root.getChildren().clear();
		buildTree(searchPlanTreeList);
		treeView.refreshTreeView();
		treeView.expandAll();
	}
}
