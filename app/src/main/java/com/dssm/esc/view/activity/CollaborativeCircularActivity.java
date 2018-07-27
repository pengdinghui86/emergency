package com.dssm.esc.view.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplListListenser;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.entity.emergency.PlanTreeEntity;
import com.dssm.esc.model.entity.emergency.SendNoticyEntity;
import com.dssm.esc.util.CharacterParser;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.treeview.TreeNode;
import com.dssm.esc.util.treeview.TreeView;
import com.dssm.esc.util.treeview.view.MyNodeViewFactory;
import com.dssm.esc.view.activity.BaseActivity.onInitNetListener;
import com.dssm.esc.view.widget.ClearEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 协同通告界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-14
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_collaborativecircular)
public class CollaborativeCircularActivity extends BaseActivity implements
		OnClickListener, onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 确定 */
	@ViewInject(R.id.tv_actionbar_editData)
	private TextView mSelectConfirm;
	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;
	/** 可扩展ListView */
	@ViewInject(R.id.ll_contact_list)
	private LinearLayout expandableLayout;
	/** 父list显示预案 */
	private List<PlanTreeEntity> planTreeList = new ArrayList<>();
	/** 被选中的人员的id */
	public List<String> selectId = new ArrayList<>();
	/** 可清除的EditText */
	@ViewInject(R.id.filter_edit_c)
	private ClearEditText myClearEt;
	/** 显示未搜索到 */
	@ViewInject(R.id.no_search_result_tv_c)
	private TextView noSearchResultTv;
	private String planInfoId = "";
	private String precautionId = "";
	private SendNoticyEntity entity;
	private String ids = "";
	/** 汉字转换成拼音的类 */
	private CharacterParser characterParser = CharacterParser.getInstance();

	private TreeNode root;
	private TreeView treeView;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				planTreeList = (List<PlanTreeEntity>) msg.obj;
				addTreeView();
				if (planTreeList.size() == 0) {
					expandableLayout.setVisibility(View.GONE);
					noSearchResultTv.setVisibility(View.VISIBLE);
				} else {
					expandableLayout.setVisibility(View.VISIBLE);
					noSearchResultTv.setVisibility(View.GONE);
				}

				for (int i = 0; i < planTreeList.size(); i++) {
					PlanTreeEntity planTreeEntity = planTreeList.get(i);
					List<GroupEntity> groupEntityList = planTreeEntity.getEmeGroups();
					for (int j = 0; j < groupEntityList.size(); j++) {
						GroupEntity groupEntity = groupEntityList.get(j);
						List<ChildEntity> childEntityList = groupEntity.getcList();
						for (int k = 0; k < childEntityList.size(); k++) {
							ChildEntity childEntity = childEntityList.get(k);
							searchChildItemData(childEntity);
						}
					}
				}
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
//		setContentView(R.layout.activity_collaborativecircular);
		View findViewById = findViewById(R.id.collaborativecircular);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		entity = (SendNoticyEntity) intent.getSerializableExtra("entity");
		planInfoId = intent.getStringExtra("planInfoId");
		precautionId = intent.getStringExtra("precautionId");
		initView();
	}

	private void initView() {
		mSelectTypeTitle.setText("协同通告");
		mSelectConfirm.setVisibility(View.VISIBLE);
		mSelectConfirm.setText("发送");
		mBack.setVisibility(View.VISIBLE);
		mSelectConfirm.setOnClickListener(this);
		if (planTreeList.size() == 0) {
			initData();
		}
		// 初始化，默认加载任务通知界面
		myClearEt.addTextChangedListener(new TextWatcher() {

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
				// TODO Auto-generated method stub
				filterData(s.toString());
			}
		});
	}

	private void addTreeView() {
		root = TreeNode.root();
		buildTree(planTreeList);
		treeView = new TreeView(root, this, new MyNodeViewFactory(), "");
		View view = treeView.getView();
		view.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		expandableLayout.addView(view);
	}

	private void buildTree(List<PlanTreeEntity> planTreeList) {
		for (int i = 0; i < planTreeList.size(); i++) {
			TreeNode treeNode = new TreeNode(planTreeList.get(i).getName());
			treeNode.setLevel(0);
			if("".equals(planTreeList.get(i).getTreeId())) {
				for (int k = 0; k < planTreeList.get(i).getEmeGroups().get(0).getcList().size(); k++) {
					TreeNode treeNode2 = new TreeNode(planTreeList.get(i).getEmeGroups().get(0).getcList().get(k));
					treeNode2.setLevel(2);
					treeNode.addChild(treeNode2);
				}
			}
			else {
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
			}
			root.addChild(treeNode);
		}
	}

	/**
	 * 
	 * 子list正则过滤，用于搜索
	 * 
	 * @version 1.0
	 * @createTime 2015-9-9,下午5:49:34
	 * @updateTime 2015-9-9,下午5:49:34
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param childrenItem
	 */
	private void searchChildItemData(ChildEntity childrenItem) {
		// TODO Auto-generated method stub
		String name = characterParser.getSelling(childrenItem.getName());
		String flag = characterParser.getSelling(childrenItem.getZhiwei());
		String sortString = name.substring(0, 1).toUpperCase();
		String sortString2 = flag.substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortString.matches("[A-Z]")) {
			childrenItem.setSortLetters(sortString.toUpperCase());
		} else {
			childrenItem.setSortLetters("#");
		}
		// 正则表达式，判断首字母是否是英文字母
		if (sortString2.matches("[A-Z]")) {
			childrenItem.setSortLetters(sortString2.toUpperCase());
		} else {
			childrenItem.setSortLetters("#");
		}
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<PlanTreeEntity> planTreeFilterList = new ArrayList<>();
		List<GroupEntity> groupFilterList;
		List<ChildEntity> childFilterList;
		if (TextUtils.isEmpty(filterStr)) {
			planTreeFilterList = planTreeList;
			noSearchResultTv.setVisibility(View.GONE);
		} else {
			planTreeFilterList.clear();
			for (int i = 0; i < planTreeList.size(); i++) {
				PlanTreeEntity sortModel = planTreeList.get(i);
				groupFilterList = new ArrayList<>();
				for (int j = 0; j < sortModel.getEmeGroups().size(); j++) {
					GroupEntity sortModel1 = sortModel.getEmeGroups().get(j);
					childFilterList = new ArrayList<>();
					for (int k = 0; k < sortModel1.getcList().size(); k++) {
						ChildEntity sortChildModel = sortModel1.getcList().get(k);
						String childName = sortChildModel.getName();
						String flag = sortChildModel.getZhiwei();
						// child有字符直接加入，其父也加入
						if (childName.indexOf(filterStr.toString()) != -1
								|| characterParser.getSelling(childName)
								.startsWith(filterStr.toString())
								|| flag.indexOf(filterStr.toString()) != -1
								|| characterParser.getSelling(flag)
								.startsWith(filterStr.toString())) {
							childFilterList.add(sortChildModel);
						}
					}
					if (childFilterList.size() > 0) {
						GroupEntity entity = new GroupEntity();
						entity.setGroup_id(sortModel1.getGroup_id());
						entity.setGroupname(sortModel1.getGroupname());
						entity.setSortLetters(sortModel1.getSortLetters());
						entity.setcList(childFilterList);
						groupFilterList.add(entity);
					}
				}
				if (groupFilterList.size() > 0) {
					PlanTreeEntity entity = new PlanTreeEntity();
					entity.setTreeId(sortModel.getTreeId());
					entity.setName(sortModel.getName());
					entity.setEmeGroups(groupFilterList);
					planTreeFilterList.add(entity);
				}

			}
		}

		// 如果查询的结果为0时，显示为搜索到结果的提示
		if (planTreeFilterList.size() == 0) {
			noSearchResultTv.setVisibility(View.VISIBLE);
			expandableLayout.setVisibility(View.GONE);
		} else {
			noSearchResultTv.setVisibility(View.GONE);
			expandableLayout.setVisibility(View.VISIBLE);
			root.getChildren().clear();
			buildTree(planTreeFilterList);
			treeView.expandAll();
		}
	}

	private EmergencySeviceImplListListenser listListener = new EmergencySeviceImplListListenser() {

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
				ToastUtil.showToast(
						CollaborativeCircularActivity.this,
						Const.NETWORKERROR);
			}
			Message message = new Message();
			message.what = 0;
			message.obj = dataList;
			handler.sendMessage(message);
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void initData() {
		Utils.getInstance().showProgressDialog(
				CollaborativeCircularActivity.this, "", Const.LOAD_MESSAGE);
		Control.getinstance().getEmergencyService().getEmergencyGropData(planInfoId, precautionId, listListener);
	}

	/**
	 * 
	 * 所选列表值的id
	 * 
	 * @version 1.0
	 * @createTime 2015-9-8,下午8:42:01
	 * @updateTime 2015-9-8,下午8:42:01
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	private List<String> showCheckedItems() {
		String checkedItems = "";
		List<String> checkedChildren = new ArrayList<>();
		List<TreeNode> treeNodes = treeView.getSelectedNodes();
		for(TreeNode treeNode : treeNodes) {
			if(treeNode.getValue() instanceof ChildEntity) {
				checkedChildren.add(((ChildEntity) treeNode.getValue()).getChild_id());
			}
		}
		if (checkedChildren != null && checkedChildren.size() > 0) {
			for (String child : checkedChildren) {
				if (checkedItems.length() > 0) {
					checkedItems += "\n";
				}

				checkedItems += child;
			}
		}

		return checkedChildren;
	}

	private EmergencySeviceImplBackBooleanListenser listener = new EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			if (backflag) {
				ToastUtil.showToast(
						CollaborativeCircularActivity.this,
						stRerror);
				finish();
			} else if (backflag == false) {
				ToastUtil.showToast(CollaborativeCircularActivity.this,
						stRerror);
			} else if (stRerror != null) {

				ToastUtil.showLongToast(CollaborativeCircularActivity.this,
						stRerror);
			} else if (Exceptionerror != null) {

				ToastUtil.showLongToast(CollaborativeCircularActivity.this,
						Const.NETWORKERROR);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_actionbar_editData:// 确定
			selectId = showCheckedItems();
			if (selectId.size() > 0) {
				for (int i = 0; i < selectId.size(); i++) {
					ids = ids + "," + selectId.get(i);
				}
				if (ids.subSequence(0, 1).equals(",")) {
					ids = (String) ids.subSequence(1, ids.length());
				}
			}
			if (!ids.equals("")) {

				entity.setId(ids);
				Utils.getInstance().showProgressDialog(
						CollaborativeCircularActivity.this, "",
						Const.SUBMIT_MESSAGE);
				Control.getinstance().getEmergencyService().sendNotice(entity, listener);
			} else {
				ToastUtil.showToast(CollaborativeCircularActivity.this,
						"发送人不能为空");
			}
			break;

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		planTreeList.clear();
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}

}
