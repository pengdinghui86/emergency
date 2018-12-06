package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.entity.emergency.PlanTreeEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.treeview.TreeNode;
import com.dssm.esc.util.treeview.TreeView;
import com.dssm.esc.util.treeview.view.MyNodeViewFactory;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_groupsignin)
public class GroupSigninDetail extends BaseActivity implements
		SwipeRefreshLayout.OnRefreshListener, MainActivity.onInitNetListener, View.OnClickListener {
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	@ViewInject(R.id.segment_control_sign_grop)
	private RadioGroup mSegmentControl;
	@ViewInject(R.id.segment_control_sign_grop2)
	private RadioGroup mSegmentControl2;

	@ViewInject(R.id.rb_receive_all)
	private RadioButton rb_receive_all;
	@ViewInject(R.id.rb_received)
	private RadioButton rb_received;
	@ViewInject(R.id.rb_not_receive)
	private RadioButton rb_not_receive;

	@ViewInject(R.id.rb_sign_all)
	private RadioButton rb_sign_all;
	@ViewInject(R.id.rb_signed)
	private RadioButton rb_signed;
	@ViewInject(R.id.rb_not_sign)
	private RadioButton rb_not_sign;

	/** 暂无数据 */
	@ViewInject(R.id.ll_no_data)
	private LinearLayout ll_no_data;

	/** 签到，未签到列表 */
	private List<PlanTreeEntity> list1 = new ArrayList<>();
	private List<PlanTreeEntity> list0 = new ArrayList<>();
	/** 父list显示预案 */
	private List<PlanTreeEntity> planTreeList = new ArrayList<>();
	/** 子list显示组 */
	private List<GroupEntity> groupList = new ArrayList<>();
	/** 子list显示人 */
	private List<ChildEntity> childList = new ArrayList<>();
	/** 1,应急通知接收详情;2,应急小组签到情况 */
	private String tag = "";
	/** 1,全部;2,已接受3，未接收 (SegmentControl点击) */
	private int sem_tags = 1;
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
				separateData();
				i = 1;
				break;
			case 1:
				List<PlanTreeEntity> result = (List<PlanTreeEntity>) msg.obj;
				planTreeList.clear();
				planTreeList.addAll(result);
				Log.i("========", "handler");
				mSwipeLayout.setRefreshing(false);
				if (planTreeList.size() == 0) {
					mSwipeLayout.setVisibility(View.GONE);
					ll_no_data.setVisibility(View.VISIBLE);
				} else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					ll_no_data.setVisibility(View.GONE);
				}
				if (list0.size() > 0) {
					list0.clear();
				}
				if (list1.size() > 0) {
					list1.clear();
				}
				separateData();
				initData(sem_tags, tag);
				break;
			default:
				break;
			}
		}
	};

	//分离已接收和未接收或已签到和未签到数据
	private void separateData() {
		for (int i = 0; i < planTreeList.size(); i++) {
			PlanTreeEntity planTreeEntity1 = new PlanTreeEntity();
			PlanTreeEntity planTreeEntity0 = new PlanTreeEntity();
			List<GroupEntity> groupEntityList = planTreeList.get(i).getEmeGroups();
			List<GroupEntity> groupEntityList1 = new ArrayList<>();
			List<GroupEntity> groupEntityList0 = new ArrayList<>();
			for (int j = 0; j < groupEntityList.size(); j++) {
				GroupEntity groupEntity = groupEntityList.get(j);
				List<ChildEntity> getcList = groupEntity.getcList();
				GroupEntity groupEntity1 = new GroupEntity();
				GroupEntity groupEntity0 = new GroupEntity();
				List<ChildEntity> getcList1 = new ArrayList<>();
				List<ChildEntity> getcList0 = new ArrayList<>();
				for (int k = 0; k < getcList.size(); k++) {
					ChildEntity childEntity = getcList.get(k);
					if (tag.equals("2")) {
						String signin = childEntity.getSignin();
						if (signin.equals("1")) {
							getcList1.add(childEntity);
							groupEntity1.setGroup_id(groupEntity
									.getGroup_id());
							groupEntity1.setGroupname(groupEntity
									.getGroupname());

						} else if (signin.equals("0")) {
							getcList0.add(childEntity);
							groupEntity0.setGroup_id(groupEntity
									.getGroup_id());
							groupEntity0.setGroupname(groupEntity
									.getGroupname());
						}
					} else if (tag.equals("1")) {
						String notice = childEntity.getNoticeState();

						if (notice.equals("1")) {
							getcList1.add(childEntity);
							groupEntity1.setGroup_id(groupEntity
									.getGroup_id());
							groupEntity1.setGroupname(groupEntity
									.getGroupname());
						} else {
							getcList0.add(childEntity);
							groupEntity0.setGroup_id(groupEntity
									.getGroup_id());
							groupEntity0.setGroupname(groupEntity
									.getGroupname());
						}
					}
				}
				if(getcList1.size() > 0) {
					groupEntity1.setcList(getcList1);
					groupEntityList1.add(groupEntity1);
				}
				if(getcList0.size() > 0) {
					groupEntity0.setcList(getcList0);
					groupEntityList0.add(groupEntity0);
				}
			}
			if(groupEntityList1.size() > 0) {
				planTreeEntity1.setName(planTreeList.get(i).getName());
				planTreeEntity1.setEmeGroups(groupEntityList1);
				list1.add(planTreeEntity1);
			}
			if(groupEntityList0.size() > 0) {
				planTreeEntity0.setName(planTreeList.get(i).getName());
				planTreeEntity0.setEmeGroups(groupEntityList0);
				list0.add(planTreeEntity0);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.groupsignin);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		tag = intent.getStringExtra("tag");
		id = intent.getStringExtra("id");
		initview();
		mSwipeLayout.setOnRefreshListener(this);
	}

	private void initview() {
		back.setOnClickListener(this);
		rb_receive_all.setOnClickListener(this);
		rb_received.setOnClickListener(this);
		rb_not_receive.setOnClickListener(this);
		rb_sign_all.setOnClickListener(this);
		rb_signed.setOnClickListener(this);
		rb_not_sign.setOnClickListener(this);
		if (tag.equals("1")) {// 1,应急通知接收详情
			mSegmentControl2.setVisibility(View.GONE);
			mSegmentControl.setVisibility(View.VISIBLE);
		} else if (tag.equals("2")) {// 2,应急小组签到情况
			mSegmentControl.setVisibility(View.GONE);
			mSegmentControl2.setVisibility(View.VISIBLE);
		}
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		sem_tags = 1;
		initListData();
	}

	private void initData(int sem_tags, String tag) {
		if (sem_tags == 1) {
			root.getChildren().clear();
			buildTree(planTreeList);
			treeView.refreshTreeView();
		} else if (sem_tags == 2) {
			root.getChildren().clear();
			buildTree(list1);
			if (list1.size() == 0) {
				mSwipeLayout.setVisibility(View.GONE);
				ll_no_data.setVisibility(View.VISIBLE);
				treeView.refreshTreeView();
			} else {
				mSwipeLayout.setVisibility(View.VISIBLE);
				ll_no_data.setVisibility(View.GONE);
				treeView.refreshTreeView();
			}
		} else if (sem_tags == 3) {
			root.getChildren().clear();
			buildTree(list0);
			if (list0.size() == 0) {
				mSwipeLayout.setVisibility(View.GONE);
				ll_no_data.setVisibility(View.VISIBLE);
			} else {
				mSwipeLayout.setVisibility(View.VISIBLE);
				ll_no_data.setVisibility(View.GONE);
				treeView.refreshTreeView();
			}
		}
	}

	private void addTreeView() {
		root = TreeNode.root();
		buildTree(planTreeList);
		treeView = new TreeView(root, this, new MyNodeViewFactory(), tag);
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
				Log.i("========", "initListData()0");
				if (backValue != null) {
					dataList = backValue;
				} else if (stRerror != null) {
					dataList = new ArrayList<>();
				} else if (Exceptionerror != null) {
					dataList = new ArrayList<>();
					ToastUtil.showToast(GroupSigninDetail.this,
							Const.NETWORKERROR);
				}
				Message message = handler.obtainMessage();
				message.what = 0;
				message.obj = dataList;
				handler.sendMessage(message);
			} else if (i == 1) {
				Log.i("=========", "initListData()1");
				if (backValue != null) {
					dataList = backValue;
				} else if (stRerror != null) {
					dataList = new ArrayList<>();
				} else if (Exceptionerror != null) {
					dataList = new ArrayList<>();
					ToastUtil.showToast(GroupSigninDetail.this,
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
		Utils.getInstance().showProgressDialog(GroupSigninDetail.this, "",
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
			case R.id.rb_receive_all:
				rb_receive_all.setChecked(true);
				rb_received.setChecked(false);
				rb_not_receive.setChecked(false);
				sem_tags = 1;
				initListData();
				break;
			case R.id.rb_received:
				rb_receive_all.setChecked(false);
				rb_received.setChecked(true);
				rb_not_receive.setChecked(false);
				sem_tags = 2;
				initListData();
				break;
			case R.id.rb_not_receive:
				rb_receive_all.setChecked(false);
				rb_received.setChecked(false);
				rb_not_receive.setChecked(true);
				sem_tags = 3;
				initListData();
				break;
			case R.id.rb_sign_all:
				rb_sign_all.setChecked(true);
				rb_signed.setChecked(false);
				rb_not_sign.setChecked(false);
				sem_tags = 1;
				initListData();
				break;
			case R.id.rb_signed:
				rb_sign_all.setChecked(false);
				rb_signed.setChecked(true);
				rb_not_sign.setChecked(false);
				sem_tags = 2;
				initListData();
				break;
			case R.id.rb_not_sign:
				rb_sign_all.setChecked(false);
				rb_signed.setChecked(false);
				rb_not_sign.setChecked(true);
				sem_tags = 3;
				initListData();
				break;
		}
	}
}
