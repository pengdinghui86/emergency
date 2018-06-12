package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.ExpanListvSignInAdapter;
import com.dssm.esc.view.widget.SegmentControl;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;


public class GroupSigninDetail extends BaseActivity implements
		SwipeRefreshLayout.OnRefreshListener, MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView back;
	@ViewInject(id = R.id.segment_control_sign_grop)
	private SegmentControl mSegmentControl;
	@ViewInject(id = R.id.segment_control_sign_grop2)
	private SegmentControl mSegmentControl2;
	/** 人员签到可扩展listview */
	@ViewInject(id = R.id.expandable_signin_grop)
	private ExpandableListView expandable_list_signin;
	private ExpanListvSignInAdapter adapter;
	/** 暂无数据 */
	@ViewInject(id = R.id.emyptytv)
	private TextView emyptytv;

	/** 签到，未签到列表 */
	private List<GroupEntity> list1 = new ArrayList<GroupEntity>();
	private List<GroupEntity> list0 = new ArrayList<GroupEntity>();
	/** 父list显示组 */
	private List<GroupEntity> groupList = new ArrayList<GroupEntity>();
	/** 子list显示人 */
	private List<ChildEntity> childList = new ArrayList<ChildEntity>();
	/** 1,应急通知接收详情;2,应急小组签到情况 */
	private String tag = "";
	/** 1,全部;2,已接受3，未接收 (SegmentControl点击) */
	private int sem_tags = 1;
	/** 预案执行编号 */
	private String id;
	@ViewInject(id = R.id.id_swipe_ly)
	private SwipeRefreshLayout mSwipeLayout;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				groupList = (List<GroupEntity>) msg.obj;
				adapter.updateListView(groupList, tag);
				adapter.notifyDataSetChanged();
				if (groupList.size() == 0) {
					mSwipeLayout.setVisibility(View.GONE);
					expandable_list_signin.setVisibility(View.GONE);
					emyptytv.setVisibility(View.VISIBLE);
				} else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					expandable_list_signin.setVisibility(View.VISIBLE);
					emyptytv.setVisibility(View.GONE);
				}
				for (int i = 0; i < groupList.size(); i++) {
					GroupEntity groupEntity = groupList.get(i);
					List<ChildEntity> getcList = groupEntity.getcList();
					GroupEntity groupEntity1 = new GroupEntity();
					GroupEntity groupEntity0 = new GroupEntity();
					List<ChildEntity> getcList1 = new ArrayList<ChildEntity>();
					List<ChildEntity> getcList0 = new ArrayList<ChildEntity>();
					for (int j = 0; j < getcList.size(); j++) {
						ChildEntity childEntity = getcList.get(j);
						ChildEntity childEntity1 = new ChildEntity();
						ChildEntity childEntity0 = new ChildEntity();
						if (tag.equals("2")) {
							String signin = childEntity.getSignin();

							if (signin.equals("1")) {
								childEntity1 = childEntity;
								getcList1.add(childEntity1);
								groupEntity1.setGroup_id(groupEntity
										.getGroup_id());
								groupEntity1.setGroupname(groupEntity
										.getGroupname());
								groupEntity1.setcList(getcList1);

							} else if (signin.equals("0")) {
								childEntity0 = childEntity;
								getcList0.add(childEntity0);
								groupEntity0.setGroup_id(groupEntity
										.getGroup_id());
								groupEntity0.setGroupname(groupEntity
										.getGroupname());
								groupEntity0.setcList(getcList0);

							}
						} else if (tag.equals("1")) {
							String notice = childEntity.getNoticeState();

							if (notice.equals("1")) {
								childEntity1 = childEntity;
								getcList1.add(childEntity1);
								groupEntity1.setGroup_id(groupEntity
										.getGroup_id());
								groupEntity1.setGroupname(groupEntity
										.getGroupname());
								groupEntity1.setcList(getcList1);

							} else {
								childEntity0 = childEntity;
								getcList0.add(childEntity0);
								groupEntity0.setGroup_id(groupEntity
										.getGroup_id());
								groupEntity0.setGroupname(groupEntity
										.getGroupname());
								groupEntity0.setcList(getcList0);

							}
						}
					}
					if (getcList1.size() > 0) {

						list1.add(groupEntity1);
					}
					if (getcList0.size() > 0) {

						list0.add(groupEntity0);
					}
				}
				break;
			case 1:
				List<GroupEntity> result = (List<GroupEntity>) msg.obj;
				groupList.clear();
				groupList.addAll(result);
				adapter.notifyDataSetChanged();
				Log.i("========", "handler");
				mSwipeLayout.setRefreshing(false);
				if (groupList.size() == 0) {
					mSwipeLayout.setVisibility(View.GONE);
					expandable_list_signin.setVisibility(View.GONE);
					emyptytv.setVisibility(View.VISIBLE);
				} else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					expandable_list_signin.setVisibility(View.VISIBLE);
					emyptytv.setVisibility(View.GONE);
				}
				if (list0.size() > 0) {
					list0.clear();

				}
				if (list1.size() > 0) {
					list1.clear();

				}
				for (int i = 0; i < groupList.size(); i++) {
					GroupEntity groupEntity = groupList.get(i);
					List<ChildEntity> getcList = groupEntity.getcList();
					GroupEntity groupEntity1 = new GroupEntity();
					GroupEntity groupEntity0 = new GroupEntity();
					List<ChildEntity> getcList1 = new ArrayList<ChildEntity>();
					List<ChildEntity> getcList0 = new ArrayList<ChildEntity>();
					for (int j = 0; j < getcList.size(); j++) {
						ChildEntity childEntity = getcList.get(j);
						ChildEntity childEntity1 = new ChildEntity();
						ChildEntity childEntity0 = new ChildEntity();
						if (tag.equals("2")) {
							String signin = childEntity.getSignin();

							if (signin.equals("1")) {
								childEntity1 = childEntity;
								getcList1.add(childEntity1);
								groupEntity1.setGroup_id(groupEntity
										.getGroup_id());
								groupEntity1.setGroupname(groupEntity
										.getGroupname());
								groupEntity1.setcList(getcList1);

							} else if (signin.equals("0")) {
								childEntity0 = childEntity;
								getcList0.add(childEntity0);
								groupEntity0.setGroup_id(groupEntity
										.getGroup_id());
								groupEntity0.setGroupname(groupEntity
										.getGroupname());
								groupEntity0.setcList(getcList0);

							}
						} else if (tag.equals("1")) {
							String notice = childEntity.getNoticeState();

							if (notice.equals("1")) {
								childEntity1 = childEntity;
								getcList1.add(childEntity1);
								groupEntity1.setGroup_id(groupEntity
										.getGroup_id());
								groupEntity1.setGroupname(groupEntity
										.getGroupname());
								groupEntity1.setcList(getcList1);

							} else {
								childEntity0 = childEntity;
								getcList0.add(childEntity0);
								groupEntity0.setGroup_id(groupEntity
										.getGroup_id());
								groupEntity0.setGroupname(groupEntity
										.getGroupname());
								groupEntity0.setcList(getcList0);

							}
						}
					}
					if (getcList1.size() > 0) {

						list1.add(groupEntity1);
					}
					if (getcList0.size() > 0) {

						list0.add(groupEntity0);
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
		setContentView(R.layout.activity_groupsignin);
		View findViewById = findViewById(R.id.groupsignin);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		tag = intent.getStringExtra("tag");
		id = intent.getStringExtra("id");
		initview();
		expandable_list_signin.setGroupIndicator(null);
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
		expandable_list_signin.setEmptyView(emyptytv);
		adapter = new ExpanListvSignInAdapter(groupList,
				GroupSigninDetail.this, tag);
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		expandable_list_signin.setAdapter(adapter);
		sem_tags = 1;// 默认预案详情
		initData(sem_tags, tag);

		// setNetListener(this);
	}

	private void initData(int sem_tags, String tag) {
		if (sem_tags == 1) {
			if (groupList.size() == 0) {
				initListData();
			} else {
				adapter.updateListView(groupList, tag);
			}
		} else if (sem_tags == 2) {
			adapter.updateListView(list1, tag);
			if (list1.size() == 0) {
				mSwipeLayout.setVisibility(View.GONE);
				expandable_list_signin.setVisibility(View.GONE);
				emyptytv.setVisibility(View.VISIBLE);
			} else {
				mSwipeLayout.setVisibility(View.VISIBLE);
				expandable_list_signin.setVisibility(View.VISIBLE);
				emyptytv.setVisibility(View.GONE);
			}
		} else if (sem_tags == 3) {
			adapter.updateListView(list0, tag);
			if (list0.size() == 0) {
				mSwipeLayout.setVisibility(View.GONE);
				expandable_list_signin.setVisibility(View.GONE);
				emyptytv.setVisibility(View.VISIBLE);
			} else {
				mSwipeLayout.setVisibility(View.VISIBLE);
				expandable_list_signin.setVisibility(View.VISIBLE);
				emyptytv.setVisibility(View.GONE);
			}
		}
		adapter.notifyDataSetChanged();

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
						initData(sem_tags, tag);
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
						initData(sem_tags, tag);
					}
				});
	}

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
		Control.getinstance().getControlSevice().getSignDetailInfo(id,
				new ControlServiceImpl.ControlServiceImplBackValueListenser<List<GroupEntity>>() {
					@Override
					public void setControlServiceImplListenser(
							List<GroupEntity> backValue, String stRerror,
							String Exceptionerror) {
						// TODO Auto-generated method stub
						List<GroupEntity> dataList = null;
						if (i != 1) {
							Log.i("========", "initListData()0");
							if (backValue != null) {
								dataList = backValue;
							} else if (stRerror != null) {
								dataList = new ArrayList<GroupEntity>();
							} else if (Exceptionerror != null) {
								dataList = new ArrayList<GroupEntity>();
								ToastUtil.showToast(GroupSigninDetail.this,
										Const.NETWORKERROR + ":"
												+ Exceptionerror);
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
								dataList = new ArrayList<GroupEntity>();
							} else if (Exceptionerror != null) {
								dataList = new ArrayList<GroupEntity>();
								ToastUtil.showToast(GroupSigninDetail.this,
										Const.NETWORKERROR + ":"
												+ Exceptionerror);
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
				});
	}

	int i;

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		i = 1;
		initListData();
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData(sem_tags, tag);
	}
}
