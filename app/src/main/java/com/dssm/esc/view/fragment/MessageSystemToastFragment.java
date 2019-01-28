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
import com.dssm.esc.model.entity.message.MessageInfoEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.System;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.adapter.MessageListAdapter;
import com.dssm.esc.view.adapter.MessageToastAdapter;
import com.dssm.esc.view.widget.AutoListView;
import com.dssm.esc.view.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 通知碎片
 */
public class MessageSystemToastFragment extends BaseFragment implements
		AutoListView.OnRefreshListener, AutoListView.OnLoadListener,MainActivity.onInitNetListener {
	/** 自定义listview */
	private AutoListView listview;
	/** 搜索输入框 */
	private ClearEditText filter_edit;
	/** 页面尾部灰色背景区域 */
	private View message_listview_v_end;
	/**
	 * 无数据显示布局
	 */
	private LinearLayout ll_no_data_page;
	/** 总list */
	private List<MessageInfoEntity> list = new ArrayList<MessageInfoEntity>();
	/** 适配器 */
	private MessageListAdapter adapter;
	/** 当前页数 */
	private int i = 1;
	private Context context;
	private UserSevice sevice;
	/**
	 * 每个用户的每个角色的三张表：系统
	 */
	private String table2 = "";
	/** 每次查询20条数据 */
	private int limt = 20;
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
				listview.onRefreshComplete();
				/** 总集合清理 */
				list.clear();
				/** 总集合添加 */
				list.addAll(result);
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
				list.clear();
				list.addAll(result);
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
	public MessageSystemToastFragment() {

	}
	@SuppressLint("ValidFragment")
	public MessageSystemToastFragment(Context context, String table2) {
		this.context = context;
		sevice = Control.getinstance().getUserSevice();
		this.table2 = table2;
		Log.i("系统通知表名", table2);

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
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			initData();
		}
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
		// TODO Auto-generated method stub;
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
		if (data.getData().equals("2")) {
			
			loadData(0);
		}
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
				EventBus.getDefault().post(new
						System("0"));
			}else if (stRerror != null) {
				ToastUtil
						.showToast(
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
			List<MessageInfoEntity> dataList=null;
			if (object != null) {

				dataList = (List<MessageInfoEntity>) object;
				Log.i("===系统消息刷新条数===", dataList.size() + "");
				if (dataList.size()>0) {
					saveData(dataList);
				}
				sevice.confirMsg("2", listener);

			} else if (stRerror != null) {
				dataList = new ArrayList<MessageInfoEntity>();
			} else if (Exceptionerror != null) {
				dataList = new ArrayList<MessageInfoEntity>();
				ToastUtil.showToast(getActivity(),
						Const.REQUESTERROR);
			}
			getDBData(dataList, curWhat);
		}
	};

	/**
	 * 
	 * 加载数据
	 * @param what
	 */
	private void loadData(final int what) {
		Utils.getInstance().showProgressDialog(getContext(), "", Const.LOAD_MESSAGE);
		if (what == 0 || what == 2) {// 刷新和第一次加载
			curWhat = what;
			sevice.getFrashMessageList(getActivity(), "2", "false","", listListener);

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
			dataBaseUtil.insterUserInfo(table2, list.get(i));
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
			//ToastUtil.showToast(context, "已是最新的数据");
		}
		int size = DataBaseUtil.getList(table2, null, queryStr).size();
		List<MessageInfoEntity> dbList;
		if (limt > size) {
			//ToastUtil.showToast(context, "没有更多的数据了");
			dbList = DataBaseUtil.getList(table2, (limt - 20) + "," + size, queryStr);
		} else {
			dbList = DataBaseUtil.getList(table2, (limt - 20) + "," + limt, queryStr);
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
		int size = DataBaseUtil.getList(table2, null, queryStr).size();
		List<MessageInfoEntity> dbList;
		if ((limt + 20) > size) {
			//ToastUtil.showToast(context, "没有更多的数据了");
			dbList = DataBaseUtil.getList(table2, limt + "," + size, queryStr);
		} else {
			dbList = DataBaseUtil.getList(table2, limt + "," + (limt + 20), queryStr);
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

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 */
	private void filterData(String filterStr) {
		queryStr = filterStr;
		loadData(0);
	}
}
