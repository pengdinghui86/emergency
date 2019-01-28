package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.database.DataBaseUtil;
import com.dssm.esc.model.entity.message.FirstAllMessagesEntity;
import com.dssm.esc.model.entity.message.MessageInfoEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.MessageCountEvent;
import com.dssm.esc.util.event.Toast;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.adapter.MessageListAdapter;
import com.dssm.esc.view.widget.AutoListView;
import com.dssm.esc.view.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 任务通知碎片
 */
public class MessageTaskToastFragment extends BaseFragment implements
		AutoListView.OnRefreshListener, AutoListView.OnLoadListener, MainActivity.onInitNetListener {
	/** 自定义listview */
	private AutoListView listview;
	/** 当前页展示list */
	private List<MessageInfoEntity> list = new ArrayList<MessageInfoEntity>();
	/** 总list */
	private List<MessageInfoEntity> totalList = new ArrayList<MessageInfoEntity>();
	/** 搜索过滤后list */
	private List<MessageInfoEntity> searchList = new ArrayList<MessageInfoEntity>();
	/** 搜索输入框 */
	private ClearEditText filter_edit;
	/** 页面尾部灰色背景区域 */
	private View message_listview_v_end;
	/**
	 * 无数据显示布局
	 */
	private LinearLayout ll_no_data_page;
	/** 适配器 */
	private MessageListAdapter adapter;
	/** 当前页数 */
	private int i = 0;
	private Context context;
	private UserSevice sevice;

	/** 当前数据数量 */
	private int limt = 0;
	/**
	 * 每个用户的每个角色的三张表：任务
	 */
	private String table1 = "";
	public String count1 = "0";
	public String count2 = "0";
	/**
	 * 搜索字符串
	 */
	public String queryStr = "";

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
//				android.widget.Toast.makeText(getContext(), "" + list.size(), android.widget.Toast.LENGTH_SHORT).show();
				if(result.size() > 0) {
					ll_no_data_page.setVisibility(View.GONE);
					listview.setVisibility(View.VISIBLE);
					message_listview_v_end.setVisibility(View.VISIBLE);
				} else {
					ll_no_data_page.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
					message_listview_v_end.setVisibility(View.GONE);
				}
				break;
			case 2:// 第一次加载
				listview.onRefreshComplete();
				/** 总集合清理 */
				list.clear();
				list.addAll(result);
//				android.widget.Toast.makeText(getContext(), "" + list.size(), android.widget.Toast.LENGTH_SHORT).show();
				if(result.size() > 0) {
					ll_no_data_page.setVisibility(View.GONE);
					listview.setVisibility(View.VISIBLE);
					message_listview_v_end.setVisibility(View.VISIBLE);
				} else {
					ll_no_data_page.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
					message_listview_v_end.setVisibility(View.GONE);
				}
				break;
			case AutoListView.LOAD:
				listview.onLoadComplete();
				list.addAll(result);

				break;
			}
			listview.setResultSize(result.size(), i);
			adapter.notifyDataSetChanged();
			Utils.getInstance().hideProgressDialog();
		}
	};

	public MessageTaskToastFragment() {

	}
	@SuppressLint("ValidFragment")
	public MessageTaskToastFragment(Context context, String table1) {
		this.context = context;
		sevice = Control.getinstance().getUserSevice();
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
		filter_edit = (ClearEditText) view_Parent.findViewById(R.id.filter_edit);
		message_listview_v_end = (View) view_Parent.findViewById(R.id.message_listview_v_end);
		ll_no_data_page = (LinearLayout) view_Parent.findViewById(R.id.ll_no_data_page);
	}

	@Override
	protected void widgetListener() {
		// TODO Auto-generated method stub
		// 初始化，默认加载任务通知界面

		listview.setOnRefreshListener(this);
		listview.setOnLoadListener(this);
//		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//				if(i > list.size())
//					return;
//				Intent intent = new Intent();
//				//事件已评估待启动
//				if (MessageStatusEnum.eventEvaluation.getId().equals(
//						list.get(i-1).getModelFlag()))
//				{
//					intent.setClass(getActivity(), EventListActivity.class);
//					intent.putExtra("tags", "1");
//				}
//				//预案已启动待签到
//				else if (MessageStatusEnum.planStarted.getId().equals(
//						list.get(i-1).getModelFlag()) || MessageStatusEnum.personSignIn.getId().equals(
//						list.get(i-1).getModelFlag()))
//				{
//					intent.setClass(getActivity(), PersonSignInActivity.class);
//				}
//				//预案已启动待授权
//				else if (MessageStatusEnum.planAuthorize.getId().equals(
//						list.get(i-1).getModelFlag()))
//				{
//					intent.setClass(getActivity(), EventPlanListActivity.class);
//					intent.putExtra("tags", "1");
//				}
//				//事件被驳回待重新评估
//				else if (MessageStatusEnum.eventReject.getId().equals(
//						list.get(i-1).getModelFlag()))
//				{
//					intent.setClass(getActivity(), DismissValuationActivity.class);
//					intent.putExtra("tags", "2");
//				}
//				//预案执行
//				else if (MessageStatusEnum.planExecute.getId().equals(
//						list.get(i-1).getModelFlag()))
//				{
//					intent.setClass(getActivity(), EventPlanListActivity.class);
//					intent.putExtra("tags", "6");
//				}
//				else
//					return;
//				startActivity(intent);
//			}
//		});
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		adapter = new MessageListAdapter(context, list);
		listview.setAdapter(adapter);
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

	@Override
	public void initGetData() {
		// TODO Auto-generated method stub
//		initData();
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
		limt = 0;
		loadData(2);

	}

	private UserSeviceImpl.UserSeviceImplBackBooleanListenser listener = new UserSeviceImpl.UserSeviceImplBackBooleanListenser() {
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
								Const.REQUESTERROR
										+ ":"
										+ Exceptionerror);
			}
		}
	};

	private int curWhat = 2;
	private UserSeviceImpl.UserSeviceImplListListenser listListener = new UserSeviceImpl.UserSeviceImplListListenser() {

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
				count1 = list.get(0).getUnreadCount();//任务
				count2 = list.get(1).getUnreadCount();//通知
				dataList = list.get(0).getList();

				// EventBus.getDefault().post(new
				// AllUnReadMessageCount(Integer.parseInt(count2)+Integer.parseInt(count3)));
				if (Integer.parseInt(list.get(0)
						.getUnreadCount()) > 0 && curWhat == 2) {//
					// 保存数据到数据库
					saveData(dataList);
					// 未读消息显示在对应的tab上
					// 有未读消息(点击任务通知按钮的时候)
				}
				sevice.confirMsg("1", listener);

			} else if (stRerror != null) {
				dataList = new ArrayList<MessageInfoEntity>();
			} else if (Exceptionerror != null) {
				dataList = new ArrayList<MessageInfoEntity>();
				ToastUtil.showToast(getActivity(),
						Const.REQUESTERROR);
			} else {
				dataList = new ArrayList<MessageInfoEntity>();
			}
			getDBData(dataList, curWhat);
			EventBus.getDefault().post(
					new MessageCountEvent(count1, count2,
							"0", "0"));
		}
	};

	private int curMessageListWhat;
	private UserSeviceImpl.UserSeviceImplListListenser curMessageListListener = new UserSeviceImpl.UserSeviceImplListListenser() {

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
						Const.REQUESTERROR);
			} else {
				dataList = new ArrayList<MessageInfoEntity>();
			}

			getDBData(dataList, curMessageListWhat);
		}
	};

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
		curMessageListWhat = what;
		Utils.getInstance().showProgressDialog(getContext(), "", Const.LOAD_MESSAGE);
		if (what == 2) {// 第一次加载
			// Log.i("getActivity()", getActivity()+"");
			// 任务通知
			sevice.getMessageList("1", "false", listListener);

		} else if (what == 0) {// 刷新
			Log.i("44", what + "");
			curWhat = what;
			sevice.getFrashMessageList(getActivity(), "1", "false", "", curMessageListListener);

		} else if (what == 1) {// 加载更多(从数据库中拿数据)
			onLoadDta(what);
		}
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
        i++;
		loadData(AutoListView.LOAD);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		i = 1;
		limt = 0;
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
		int size = DataBaseUtil.getList(table1, null, queryStr).size();
		List<MessageInfoEntity> dbList;
		if (limt + 20 > size) {
			// ToastUtil.showToast(context, "没有更多的数据了");
			dbList = DataBaseUtil.getList(table1, limt + "," + size, queryStr);
			limt = size;
		} else {
			dbList = DataBaseUtil.getList(table1, limt + "," + (limt + 20), queryStr);
			limt = limt + 20;
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
		int size = DataBaseUtil.getList(table1, null, queryStr).size();
		List<MessageInfoEntity> dbList;
		if ((limt + 20) > size) {
			// ToastUtil.showToast(context, "没有更多的数据了");
			dbList = DataBaseUtil.getList(table1, limt + "," + (size - limt), queryStr);
			limt = size;
		} else {
			dbList = DataBaseUtil.getList(table1, limt + "," + 20, queryStr);
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

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 */
	private void filterData(String filterStr) {
		queryStr = filterStr;
		initData();
	}
}
