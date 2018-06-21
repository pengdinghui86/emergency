package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.entity.emergency.PlanDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanDetailObjEntity;
import com.dssm.esc.model.entity.emergency.UserSignInfoEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.ExpanListvSignInAdapter;
import com.dssm.esc.view.widget.MyScrollView;
import com.dssm.esc.view.widget.SegmentControl;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_singin)
public class SignInActivity extends BaseActivity implements OnClickListener,
		OnRefreshListener, MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	/** 1,应急;2,演练 */
	// private String tag;
	/** 1,预案详情;2,签到详情 (SegmentControl点击) */
	private int sem_tags;
	@ViewInject(R.id.segment_control_sign)
	private SegmentControl mSegmentControl;
	/** 预案详情总布局 */
	@ViewInject(R.id.plandesc_ll)
	private MyScrollView plandesc_ll;

	/** 事件名称 */
	@ViewInject(R.id.eventname)
	private TextView eventname;
	// /** 预案场景 */
	// @ViewInject(id = R.id.planbackground)
	// private TextView planbackground;
	/** 事件详情 */
	@ViewInject(R.id.eventdesc)
	private TextView eventdesc;

	/** 事件类型 */
	@ViewInject(R.id.eventType)
	private TextView eventType;
	/** 预案名称 */
	@ViewInject(R.id.planname)
	private TextView planname;
	/** 预案摘要 */
	@ViewInject(R.id.plansummary)
	private TextView plansummary;

	/** 签到按钮 */
	@ViewInject(R.id.sign_in)
	private Button sign_in;
	/** 人员签到总布局 */
	@ViewInject(R.id.sigin_ll)
	private LinearLayout sigin_ll;
	/** 人员签到可扩展listview */
	@ViewInject(R.id.expandable_list_signin)
	private ExpandableListView expandable_list_signin;
	@ViewInject(R.id.emptytv)
	private TextView emptytv;

	private ExpanListvSignInAdapter adapter;
	/** 父list显示组 */
	private List<GroupEntity> groupList = new ArrayList<GroupEntity>();
	/** 子list显示人 */
	private List<ChildEntity> childList = new ArrayList<ChildEntity>();
	private String planId;
	private String id;
	private String signState = "";// 签到状态0:未签到 1：已签到
	private List<UserSignInfoEntity> list;
	private String eventName = "";
	@ViewInject(R.id.id_swipe_ly)
	private SwipeRefreshLayout mSwipeLayout;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				groupList = (List<GroupEntity>) msg.obj;
				Log.i("result", groupList.size() + "");
				adapter = new ExpanListvSignInAdapter(groupList,
						SignInActivity.this, "2");
				if (groupList.size()==0) {
					mSwipeLayout.setVisibility(View.GONE);
					expandable_list_signin.setVisibility(View.GONE);
					emptytv.setVisibility(View.VISIBLE);
				}else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					expandable_list_signin.setVisibility(View.VISIBLE);
					emptytv.setVisibility(View.GONE);
				}
				expandable_list_signin.setAdapter(adapter);
				break;
			case 1:
				List<GroupEntity> result = (List<GroupEntity>) msg.obj;
				groupList.clear();
				groupList.addAll(result);
				adapter.notifyDataSetChanged();
				mSwipeLayout.setRefreshing(false);
				if (groupList.size()==0) {
					mSwipeLayout.setVisibility(View.GONE);
					expandable_list_signin.setVisibility(View.GONE);
					emptytv.setVisibility(View.VISIBLE);
				}else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					expandable_list_signin.setVisibility(View.VISIBLE);
					emptytv.setVisibility(View.GONE);
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
//		setContentView(R.layout.activity_singin);
		View findViewById = findViewById(R.id.singin);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		// tag = intent.getStringExtra("tag");
		planId = intent.getStringExtra("planId");
		id = intent.getStringExtra("id");
		signState = intent.getStringExtra("signState");
		if (!signState.equals("")) {
			if (signState.equals("1")) {// 1：已签到
				sign_in.setBackgroundResource(R.drawable.btbg_gray);
			} else if (signState.equals("0")) {// 0:未签到

				sign_in.setBackgroundResource(R.drawable.btbg_blue);

			}
			Log.i("signState", signState);
		}
		sem_tags = 1;// 默认预案详情
		initview();

		segmentControlListDate();
	}

	private void initview() {
		back.setVisibility(View.VISIBLE);
		title.setText("人员签到");
		expandable_list_signin.setGroupIndicator(null);
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mSwipeLayout.setOnRefreshListener(this);
		initData(sem_tags);
	}

	private void initData(int sem_tags) {
		if (sem_tags == 1) {// 1,预案详情
			plandesc_ll.setVisibility(View.VISIBLE);
			sigin_ll.setVisibility(View.GONE);
			// 初始化，默认加载任务通知界面,避免重复加载
			if (eventName.equals("") || eventName == null || eventName == "") {

				getPlanDetail();
			}

		} else if (sem_tags == 2) {// 2,签到详情
			sigin_ll.setVisibility(View.VISIBLE);
			plandesc_ll.setVisibility(View.GONE);
			// 初始化，默认加载任务通知界面,避免重复加载
			if (groupList.size() == 0) {

				initListData();
			}

		}
		// setNetListener(this);
		sign_in.setOnClickListener(this);
	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(Object object,
				String stRerror, String Exceptionerror) {
			// TODO Auto-generated method stub
			if (object != null) {
				PlanDetailEntity planDetailEntity = (PlanDetailEntity) object;
				PlanDetailObjEntity obj = planDetailEntity.getObj();
				eventName = obj.getEveName();
				eventname.setText(eventName);
				// planbackground.setText(obj.getSceneName());
				eventdesc.setText(obj.getEveDescription());
				String eveType = obj.getEveType();
				if (eveType.equals("1")) {
					eventType.setText("应急");
				} else if (eveType.equals("2")) {
					eventType.setText("演练");
				}
				planname.setText(obj.getPlanName());
				plansummary.setText(obj.getSummary());

			}
		}
	};

	/**
	 * 获取预案详情
	 */
	private void getPlanDetail() {
		Control.getinstance().getEmergencyService().getPlanDetail(id, listListener);
	}

	/**
	 * 
	 * selectButton控制list数据
	 * 
	 * @version 1.0
	 * @createTime 2015-9-7,下午3:23:05
	 * @updateTime 2015-9-7,下午3:23:05
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	private void segmentControlListDate() {
		// TODO Auto-generated method stub
		mSegmentControl
				.setmOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
					@Override
					public void onSegmentControlClick(int index) {
						switch (index) {
						case 0:// 1,预案处置
							sem_tags = 1;
							sign_in.setVisibility(View.VISIBLE);
							break;
						case 1:// 2,事件详情
							sem_tags = 2;
							sign_in.setVisibility(View.GONE);
							break;
						}
						initData(sem_tags);
					}
				});
	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser signEmergencyInfoListener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			List<GroupEntity> dataList = null;
			if (i != 1) {
				if (object != null) {
					dataList = (List<GroupEntity>) object;

				} else if (stRerror != null) {
					dataList = new ArrayList<GroupEntity>();

				} else if (Exceptionerror != null) {
					dataList = new ArrayList<GroupEntity>();
					ToastUtil.showToast(SignInActivity.this,
							Const.NETWORKERROR + ":"
									+ Exceptionerror);
				}
				Message message = new Message();
				message.what = 0;
				message.obj = dataList;
				handler.sendMessage(message);
				// if
				// (Utils.getInstance().progressDialog.isShowing())
				// {
				Utils.getInstance().hideProgressDialog();
				// }
			} else if (i == 1) {
				if (object != null) {
					dataList = (List<GroupEntity>) object;

				} else if (stRerror != null) {
					dataList = new ArrayList<GroupEntity>();

				} else if (Exceptionerror != null) {
					dataList = new ArrayList<GroupEntity>();
					ToastUtil.showToast(SignInActivity.this,
							Const.NETWORKERROR + ":"
									+ Exceptionerror);
				}
				Message message = new Message();
				message.what = 1;
				message.obj = dataList;
				handler.sendMessage(message);
				Utils.getInstance().hideProgressDialog();
			}
		}
	};

	/**
	 * 
	 * 获取签到详情
	 * 
	 * @version 1.0
	 * @createTime 2015-9-8,下午8:38:59
	 * @updateTime 2015-9-8,下午8:38:59
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	private void initListData() {
		Utils.getInstance().showProgressDialog(SignInActivity.this, "",
				Const.LOAD_MESSAGE);
		Control.getinstance().getEmergencyService().getSignEmergencyInfo(id, signEmergencyInfoListener);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sign_in:// 签到
			if (signState.equals("0")) {
				siginin();
			} else if (signState.equals("1")) {
				ToastUtil.showToast(SignInActivity.this, "您已签到，请不要重复签到");
				Log.i("signState", signState);
			}

			break;

		default:
			break;
		}
	}

	private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listener = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(Boolean backflag,
				String stRerror, String Exceptionerror) {
			// TODO Auto-generated method stub
			if (backflag) {
				ToastUtil.showToast(SignInActivity.this, stRerror);
				sign_in.setBackgroundResource(R.drawable.btbg_gray);
				signState = "1";
				initListData();
			} else if (backflag == false) {

				ToastUtil.showToast(SignInActivity.this, stRerror);
			} else if (stRerror != null) {

				ToastUtil.showLongToast(SignInActivity.this, stRerror);
			} else if (Exceptionerror != null) {

				ToastUtil.showLongToast(SignInActivity.this,
						Const.NETWORKERROR + Exceptionerror);
			}
		}
	};

	/**
	 * 签到
	 */
	private void siginin() {
		Control.getinstance().getEmergencyService().signIn(id, listener);
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData(sem_tags);
	}

	int i;

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		i = 1;
		initListData();
	}
}
