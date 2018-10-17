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
import android.widget.TextView;

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
import com.dssm.esc.view.widget.SegmentControl;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_groupsignin)
public class GroupSigninDetail extends BaseActivity implements
		SwipeRefreshLayout.OnRefreshListener, MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	@ViewInject(R.id.segment_control_sign_grop)
	private SegmentControl mSegmentControl;
	@ViewInject(R.id.segment_control_sign_grop2)
	private SegmentControl mSegmentControl2;
	/** 暂无数据 */
	@ViewInject(R.id.emyptytv)
	private TextView emyptytv;

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
					emyptytv.setVisibility(View.VISIBLE);
				} else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					emyptytv.setVisibility(View.GONE);
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
					emyptytv.setVisibility(View.VISIBLE);
				} else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					emyptytv.setVisibility(View.GONE);
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
//		setContentView(R.layout.activity_groupsignin);
		View findViewById = findViewById(R.id.groupsignin);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		tag = intent.getStringExtra("tag");
		id = intent.getStringExtra("id");
		initview();
//		expandable_list_signin.setGroupIndicator(null);
		segmentControlListDate();
		mSwipeLayout.setOnRefreshListener(this);
	}

	private void initview() {
		back.setVisibility(View.VISIBLE);
		if (tag.equals("1")) {// 1,应急通知接收详情
			mSegmentControl2.setVisibility(View.GONE);
			mSegmentControl.setVisibility(View.VISIBLE);
			title.setText("应急通知接收详情");
		} else if (tag.equals("2")) {// 2,应急小组签到情况
			mSegmentControl.setVisibility(View.GONE);
			mSegmentControl2.setVisibility(View.VISIBLE);
			title.setText("应急小组签到情况");
		}
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		sem_tags = 1;// 默认预案详情
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
				emyptytv.setVisibility(View.VISIBLE);
				treeView.refreshTreeView();
			} else {
				mSwipeLayout.setVisibility(View.VISIBLE);
				emyptytv.setVisibility(View.GONE);
				treeView.refreshTreeView();
			}
		} else if (sem_tags == 3) {
			root.getChildren().clear();
			buildTree(list0);
			if (list0.size() == 0) {
				mSwipeLayout.setVisibility(View.GONE);
				emyptytv.setVisibility(View.VISIBLE);
			} else {
				mSwipeLayout.setVisibility(View.VISIBLE);
				emyptytv.setVisibility(View.GONE);
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

	private void segmentControlListDate() {
		// TODO Auto-generated method stub
		mSegmentControl
				.setmOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
					@Override
					public void onSegmentControlClick(int index) {
						switch (index) {
							case 0:// 1,全部
								sem_tags = 1;

								break;
							case 1:// 2,已
								sem_tags = 2;
								break;
							case 2:// 2,未
								sem_tags = 3;
								break;
						}
						initListData();
					}
				});
		mSegmentControl2
				.setmOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
					@Override
					public void onSegmentControlClick(int index) {
						switch (index) {
							case 0:// 1,全部
								sem_tags = 1;

								break;
							case 1:// 2,已
								sem_tags = 2;
								break;
							case 2:// 2,未
								sem_tags = 3;
								break;
						}
						initListData();
					}
				});
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
			// if (Utils.getInstance().progressDialog.isShowing()) {
			Utils.getInstance().hideProgressDialog();
			// }
		}
	};

	/**
	 * 
	 * 初始化数据
	 * 
	 * @version 1.0
	 * @createTime 2015-9-8,下午8:38:59
	 * @updateTime 2015-9-8,下午8:38:59
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
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
}
