package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.database.DataBaseUtil;
import com.dssm.esc.model.entity.message.MessageInfoEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.event.Emergenct;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.adapter.MessageToastAdapter;
import com.dssm.esc.view.widget.AutoListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 紧急通知碎片
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-18
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class MessageEmergencyToastFragment extends BaseFragment implements
		AutoListView.OnRefreshListener, AutoListView.OnLoadListener, MainActivity.onInitNetListener {
	/** 自定义listview */
	private AutoListView listview;
	/** 总list */
	private List<MessageInfoEntity> list = new ArrayList<MessageInfoEntity>();
	/** 适配器 */
	// private EmergencyToastAdapter adapter;
	private MessageToastAdapter adapter;
	/** 当前页数 */
	private int i = 1;
	private Context context;
	private UserSevice sevice;
	/** 每次查询20条数据 */
	private int limt = 20;
	/**
	 * 每个用户的每个角色的三张表：紧急
	 */
	private String table3 = "";
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			@SuppressWarnings("unchecked")
			List<MessageInfoEntity> result = (ArrayList<MessageInfoEntity>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				listview.onRefreshComplete();
				/** 总集合清理 */
				list.clear();
				/** 总集合添加 */
				list.addAll(result);
				break;
			case 2:// 第一次加载
				listview.onRefreshComplete();
				list.clear();
				list.addAll(result);
				break;
			case AutoListView.LOAD:
				listview.onLoadComplete();
				list.addAll(result);
				break;
			}
			listview.setResultSize(result.size(), i);
			adapter.notifyDataSetChanged();

		};
	};

	public MessageEmergencyToastFragment() {
	}
	@SuppressLint("ValidFragment")
	public MessageEmergencyToastFragment(Context context, String table3) {
		this.context = context;
		sevice = Control.getinstance().getUserSevice();
		this.table3 = table3;
		Log.i("紧急通知表名", table3);
	}

	@Override
	protected View getViews() {
		// TODO Auto-generated method stub
		return view_Parent = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_message_toast, null);
	}
	/**
	 * 页面隐藏后再显示回调此方法，刷新界面
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			initData();
		}
	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		listview = (AutoListView) view_Parent
				.findViewById(R.id.message_listview_toast);
		listview.setDividerHeight(0);
	}

	@Override
	protected void widgetListener() {
		// TODO Auto-generated method stub
		// 初始化，默认加载任务通知界面

		listview.setOnRefreshListener(this);
		listview.setOnLoadListener(this);

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		adapter = new MessageToastAdapter(context, list);
		listview.setAdapter(adapter);

	}

	@Override
	public void initGetData() {
		// TODO Auto-generated method stub
		initData();
	}
	/***
	 * 打开EventBus开关
	 */
	protected boolean useEventBus() {
		return true;
	}

	/**
	 * 接收到推送消息，刷新界面
	 * 
	 * @param data
	 */
	public void onEvent(mainEvent data) {
		if (data.getData().equals("3")) {
			
			loadData(0);
		}
	}
	/**
	 * 
	 * 初始化数据
	 * 
	 * @version 1.0
	 * @createTime 2015-9-7,下午3:12:30
	 * @updateTime 2015-9-7,下午3:12:30
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	public void initData() {
		i = 1;
		loadData(2);

	}

	/**
	 * 
	 * 加载数据
	 * 
	 * @version 1.0
	 * @createTime 2015-9-7,下午3:12:24
	 * @updateTime 2015-9-7,下午3:12:24
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param what
	 */
	private void loadData(final int what) {
		// 系统通知
		if (what == 0 || what == 2) {// 刷新和第一次加载
			sevice.getFrashMessageList(getActivity(), "4", "false", "",
					new UserSeviceImpl.UserSeviceImplListListenser() {

						@Override
						public void setUserSeviceImplListListenser(
								Object object, String stRerror,
								String Exceptionerror) {
							// TODO Auto-generated method stub
							List<MessageInfoEntity> dataList = null;
							if (object != null) {
								dataList = (List<MessageInfoEntity>) object;
								Log.i("===紧急消息刷新条数===", dataList.size() + "");
								if (dataList.size() > 0) {
									saveData(dataList);// 保存到数据库
								}
								sevice.confirMsg(
										"4",
										new UserSeviceImpl.UserSeviceImplBackBooleanListenser() {
											@Override
											public void setUserSeviceImplListenser(
													Boolean backflag,
													String stRerror,
													String Exceptionerror) {
												// TODO Auto-generated
												// method stub
												if (backflag) {
													listview.onRefreshComplete();
													EventBus.getDefault().post(
															new Emergenct("0"));
												} else if (backflag == false) {

												} else if (stRerror != null) {
													ToastUtil.showToast(
															getActivity(),
															stRerror);
												} else if (Exceptionerror != null) {
													ToastUtil
															.showToast(
																	getActivity(),
																	Const.NETWORKERROR
																			+ ":"
																			+ Exceptionerror);
												}
											}

										});

							} else if (stRerror != null) {
								dataList = new ArrayList<MessageInfoEntity>();
							} else if (Exceptionerror != null) {
								dataList = new ArrayList<MessageInfoEntity>();
								ToastUtil.showToast(getActivity(),
										Const.NETWORKERROR + ":"
												+ Exceptionerror);
							}
							getDBData(dataList, what);
						}
					});
		} else if (what == 1) {// 加载更多(从数据库中拿数据)
			onLoadDta(what);
		}
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		loadData(AutoListView.LOAD);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		i = 1;
		loadData(AutoListView.REFRESH);
	}

	/**
	 * 缓存数据
	 * 
	 * @param list
	 */
	private void saveData(List<MessageInfoEntity> list) {
		// 倒叙List集合
		// Collections.reverse(list);
		DataBaseUtil dataBaseUtil = new DataBaseUtil();
		Log.i("list长度", list.size() + "");
		for (int i = 0; i < list.size(); i++) {
			// Log.i("id", list.get(i).getId());
			Log.i("messageId", list.get(i).getMessageId());
			Log.i("message", list.get(i).getMessage());
			Log.i("tiem", list.get(i).getTime());
			dataBaseUtil.insterUserInfo(table3, list.get(i));
		}
	}

	/**
	 * 从数据库读取新添加的数据
	 * 
	 * @param what
	 */
	private void getDBData(List<MessageInfoEntity> dataList, int what) {
		// TODO Auto-generated method stub

		Message msg = handler.obtainMessage();
		msg.what = what;
		// 加载数据库数据
		if (dataList.size() == 0) {
			// ToastUtil.showToast(context, "已是最新的数据");
		}
		int size = DataBaseUtil.getList(table3, null).size();
		List<MessageInfoEntity> dbList;
		if (limt > size) {
			// ToastUtil.showToast(context, "没有更多的数据了");
			dbList = DataBaseUtil.getList(table3, (limt - 20) + "," + size);
		} else {
			dbList = DataBaseUtil.getList(table3, (limt - 20) + "," + limt);
		}
		msg.obj = dbList;
		handler.sendMessage(msg);
	}

	/**
	 * 上拉加载更多数据
	 * 
	 * @param what
	 */
	private void onLoadDta(int what) {
		// 判断数据库数据的长度
		int size = DataBaseUtil.getList(table3, null).size();
		List<MessageInfoEntity> dbList;
		if ((limt + 20) > size) {
			// ToastUtil.showToast(context, "没有更多的数据了");
			dbList = DataBaseUtil.getList(table3, limt + "," + size);
		} else {
			dbList = DataBaseUtil.getList(table3, limt + "," + (limt + 20));
			limt += 20;
		}
		Message msg = handler.obtainMessage();
		msg.what = what;
		msg.obj = dbList;
		handler.sendMessage(msg);
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}

}
