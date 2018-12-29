package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.entity.emergency.PlanTreeEntity;
import com.dssm.esc.util.CharacterParser;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.treeview.TreeNode;
import com.dssm.esc.util.treeview.TreeView;
import com.dssm.esc.util.treeview.view.MyNodeViewFactory;
import com.dssm.esc.view.widget.ClearEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_group_sign_detail)
public class GroupSignDetailActivity extends BaseActivity implements
		SwipeRefreshLayout.OnRefreshListener, MainActivity.onInitNetListener, View.OnClickListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
    /** 返回按钮 */
	@ViewInject(R.id.filter_edit)
	private ClearEditText filter_edit;
	/** 暂无数据 */
	@ViewInject(R.id.ll_no_data)
	private LinearLayout ll_no_data;
	/** 全部数据 */
	private List<PlanTreeEntity> planTreeList = new ArrayList<>();
	/** 搜索过滤后数据 */
	private List<PlanTreeEntity> searchPlanTreeList = new ArrayList<>();
	/** 汉字转换成拼音的类 */
	private CharacterParser characterParser = CharacterParser.getInstance();
	/** 预案执行编号 */
	private String id;
	@ViewInject(R.id.id_swipe_ly)
	private SwipeRefreshLayout mSwipeLayout;

	private TreeNode root;
	private TreeView treeView;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				planTreeList = (List<PlanTreeEntity>) msg.obj;
				addTreeView();
				if (planTreeList.size() == 0) {
					mSwipeLayout.setVisibility(View.GONE);
					ll_no_data.setVisibility(View.VISIBLE);
				} else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					ll_no_data.setVisibility(View.GONE);
				}
				i = 1;
				break;
			case 1:
				List<PlanTreeEntity> result = (List<PlanTreeEntity>) msg.obj;
				planTreeList.clear();
				planTreeList.addAll(result);
				mSwipeLayout.setRefreshing(false);
				if (planTreeList.size() == 0) {
					mSwipeLayout.setVisibility(View.GONE);
					ll_no_data.setVisibility(View.VISIBLE);
				} else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					ll_no_data.setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.groupsignin);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		initview();
		initListData();
		mSwipeLayout.setOnRefreshListener(this);
	}

	private void initview() {
		back.setVisibility(View.VISIBLE);
		title.setText("签到详情");
		back.setOnClickListener(this);
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		// 初始化，默认加载任务通知界面
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

	private void initData() {
		root.getChildren().clear();
		buildTree(searchPlanTreeList);
		treeView.refreshTreeView();
		treeView.expandAll();
	}

	private void addTreeView() {
		root = TreeNode.root();
		buildTree(planTreeList);
		treeView = new TreeView(root, this, new MyNodeViewFactory(), "2");
		View view = treeView.getView();
		view.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		mSwipeLayout.addView(view);
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

	private ControlServiceImpl.ControlServiceImplBackValueListenser<List<PlanTreeEntity>> controlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<List<PlanTreeEntity>>() {
		@Override
		public void setControlServiceImplListenser(
				List<PlanTreeEntity> backValue, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			List<PlanTreeEntity> dataList = null;
			if (i != 1) {
				if (backValue != null) {
					dataList = backValue;
				} else if (stRerror != null) {
					dataList = new ArrayList<>();
				} else if (Exceptionerror != null) {
					dataList = new ArrayList<>();
					ToastUtil.showToast(GroupSignDetailActivity.this,
							Const.NETWORKERROR);
				}
				Message message = handler.obtainMessage();
				message.what = 0;
				message.obj = dataList;
				handler.sendMessage(message);
			} else if (i == 1) {
				if (backValue != null) {
					dataList = backValue;
				} else if (stRerror != null) {
					dataList = new ArrayList<>();
				} else if (Exceptionerror != null) {
					dataList = new ArrayList<>();
					ToastUtil.showToast(GroupSignDetailActivity.this,
							Const.NETWORKERROR);
				}
				Message message = handler.obtainMessage();
				message.what = 1;
				message.obj = dataList;
				handler.sendMessage(message);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void initListData() {
		Utils.getInstance().showProgressDialog(GroupSignDetailActivity.this, "",
				Const.LOAD_MESSAGE);
		Control.getinstance().getControlSevice().getSignDetailInfo(id, controlServiceImplBackValueListenser);
	}

	private int i = 0;

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		i = 1;
		initListData();
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initListData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.iv_actionbar_back:
				finish();
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
			mSwipeLayout.setVisibility(View.GONE);
			ll_no_data.setVisibility(View.VISIBLE);
		}
		else {
			searchPlanTreeList = filterList;
			initData();
			mSwipeLayout.setVisibility(View.VISIBLE);
			ll_no_data.setVisibility(View.GONE);
		}
	}

}
