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
import com.dssm.esc.model.entity.message.FirstAllMessagesEntity;
import com.dssm.esc.model.entity.message.MessageInfoEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.event.MessagCountEvent;
import com.dssm.esc.util.event.Toast;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.adapter.MessageToastAdapter;
import com.dssm.esc.view.widget.AutoListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 任务通知碎片
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-18
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class MessageTaskToastFragment extends BaseFragment implements
		AutoListView.OnRefreshListener, AutoListView.OnLoadListener, MainActivity.onInitNetListener {
	/** 自定义listview */
	private AutoListView listview;
	/** 总list */
	private List<MessageInfoEntity> list = new ArrayList<MessageInfoEntity>();
	/** 适配器 */
	private MessageToastAdapter adapter;
	/** 当前页数 */
	private int i = 1;
	private Context context;
	private UserSevice sevice;

	// private int page = 1;
	/** 每次查询20条数据 */
	private int limt = 20;
	/** 每次查询数据库的第几条 */
	private int databaselimitn = 0;
	private ArrayList<MessageInfoEntity> dataListAll;
	/**
	 * 每个用户的每个角色的三张表：任务
	 */
	private String table1 = "";
	public String count1 = "0";
	public String count2 = "0";
	public String count3 = "0";
	public String count4 = "0";
	private MessageInfoEntity infoEntity;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			@SuppressWarnings("unchecked")
			List<MessageInfoEntity> result = (ArrayList<MessageInfoEntity>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				Log.i("44", "刷新");
				listview.onRefreshComplete();
				/** 总集合清理 */
				list.clear();
				/** 总集合添加 */
				list.addAll(result);
				break;
			case 2:// 第一次加载
				listview.onRefreshComplete();
				/** 总集合清理 */
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

	public MessageTaskToastFragment() {

	}
	@SuppressLint("ValidFragment")
	public MessageTaskToastFragment(Context context, String table1) {
		this.context = context;
		sevice = Control.getinstance().getUserSevice();
		infoEntity = new MessageInfoEntity();
		this.table1 = table1;
		Log.i("任务通知表名", table1);

	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			initData();
		}
	}

	@Override
	protected View getViews() {
		// TODO Auto-generated method stub
		return view_Parent = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_message_toast, null);
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
		if (what == 2) {// 第一次加载
			// Log.i("getActivity()", getActivity()+"");
			// 任务通知
			sevice.getMessageList("1", "false",
					new UserSeviceImpl.UserSeviceImplListListenser() {

						@Override
						public void setUserSeviceImplListListenser(
								Object object, String stRerror,
								String Exceptionerror) {
							// TODO Auto-generated method stub
							List<MessageInfoEntity> dataList = null;
							if (object != null) {
								ArrayList<FirstAllMessagesEntity> list = (ArrayList<FirstAllMessagesEntity>) object;
								if(list.size() == 0)
									return;
								Log.i("消息类型1", list.get(0).getMsgType());
								Log.i("消息长度1", list.get(0).getList().size()
										+ "");
								Log.i("未读消息长度1", list.get(0).getUnreadCount());
								count1 = list.get(0).getUnreadCount();
								count2 = list.get(1).getUnreadCount();
								count3 = list.get(3).getUnreadCount();// 应急
								count4 = list.get(2).getUnreadCount();// 个人
								dataList = list.get(0).getList();

								// EventBus.getDefault().post(new
								// AllUnReadMessageCount(Integer.parseInt(count2)+Integer.parseInt(count3)));
								if (Integer.parseInt(list.get(0)
										.getUnreadCount()) > 0 && what == 2) {//
									// 保存数据到数据库
									saveData(dataList);
									// 未读消息显示在对应的tab上
									// 有未读消息(点击任务通知按钮的时候)
								}
								sevice.confirMsg(
										"1",
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
															new Toast("0"));
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
							} else {
								dataList = new ArrayList<MessageInfoEntity>();
							}

							getDBData(dataList, what);
							EventBus.getDefault().post(
									new MessagCountEvent(count1, count2,
											count3, count4));
						}
					});

		} else if (what == 0) {// 刷新
			Log.i("44", what + "");
			sevice.getFrashMessageList(getActivity(), "1", "false", "",
					new UserSeviceImpl.UserSeviceImplListListenser() {

						@Override
						public void setUserSeviceImplListListenser(
								Object object, String stRerror,
								String Exceptionerror) {
							// TODO Auto-generated method stub
							List<MessageInfoEntity> dataList = null;
							if (object != null) {
								dataList = (List<MessageInfoEntity>) object;

								EventBus.getDefault().post(new Toast("0"));
							} else if (stRerror != null) {
								dataList = new ArrayList<MessageInfoEntity>();
							} else if (Exceptionerror != null) {
								dataList = new ArrayList<MessageInfoEntity>();
								ToastUtil.showToast(getActivity(),
										Const.NETWORKERROR + ":"
												+ Exceptionerror);
							} else {
								dataList = new ArrayList<MessageInfoEntity>();
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
		// page++;
		loadData(AutoListView.LOAD);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		i = 1;
		Log.i("44", "onRefresh");
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
			dataBaseUtil.insterUserInfo(table1, list.get(i));
		}
	}

	/**
	 * 从数据库读取新添加的数据
	 * 
	 * @param what
	 */
	private void getDBData(List<MessageInfoEntity> dataList, int what) {
		// TODO Auto-generated method stub
		saveData(dataList);
		Log.i("44", "读数据");
		Message msg = handler.obtainMessage();
		msg.what = what;
		// 加载数据库数据
		if (dataList.size() == 0) {
			// ToastUtil.showToast(context, "已是最新的数据");
			Log.i("44", "无数据");
			listview.onRefreshComplete();
		}
		int size = DataBaseUtil.getList(table1, null).size();
		List<MessageInfoEntity> dbList;
		if (limt > size) {
			// ToastUtil.showToast(context, "没有更多的数据了");
			dbList = DataBaseUtil.getList(table1, (limt - 20) + "," + size);
		} else {
			dbList = DataBaseUtil.getList(table1, (limt - 20) + "," + limt);
		}
		Log.i("44", "list的长度为：" + dataList.size());
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
		int size = DataBaseUtil.getList(table1, null).size();
		List<MessageInfoEntity> dbList;
		if ((limt + 20) > size) {
			// ToastUtil.showToast(context, "没有更多的数据了");
			dbList = DataBaseUtil.getList(table1, limt + "," + size);
		} else {
			dbList = DataBaseUtil.getList(table1, limt + "," + (limt + 20));
			limt += 20;
		}
		Message msg = handler.obtainMessage();
		msg.what = what;
		msg.obj = dbList;
		handler.sendMessage(msg);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().finish();
	}

	@Override
	public void initNetData() {
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
		if (data.getData().equals("1")) {

			loadData(0);
		}
	}
}
