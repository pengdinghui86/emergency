package com.dssm.esc.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.model.entity.message.HistoryNoticeEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.HistoryNoticeListAdapter;
import com.dssm.esc.view.widget.AutoListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知历史界面
 */
@ContentView(R.layout.activity_history_notice)
public class HistoryNoticeActivity extends BaseActivity implements
		AutoListView.OnRefreshListener, AutoListView.OnLoadListener, View.OnClickListener {

	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	@ViewInject(R.id.history_notice_alv)
	private AutoListView listView;
	@ViewInject(R.id.history_notice_rb_all)
	private RadioButton rb_all;
	@ViewInject(R.id.history_notice_rb_msg)
	private RadioButton rb_msg;
	@ViewInject(R.id.history_notice_rb_sys)
	private RadioButton rb_sys;
	@ViewInject(R.id.history_notice_rb_email)
	private RadioButton rb_email;
	@ViewInject(R.id.history_notice_rb_app)
	private RadioButton rb_app;
	//不传查全部，0系统，1邮件，2短信，3APP
	public String tag = "";
	//当前已加载的总条数
	private int num = 0;
	//每次加载20条
	private int perCount = 20;
	private List<HistoryNoticeEntity> list = new ArrayList<HistoryNoticeEntity>();
	//查询的所有列表
	private List<HistoryNoticeEntity> allList = new ArrayList<HistoryNoticeEntity>();
	private HistoryNoticeListAdapter adapter;
	private UserSevice service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.history_notice);
		findViewById.setFitsSystemWindows(true);
		initView();
		initData();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<HistoryNoticeEntity> result = (ArrayList<HistoryNoticeEntity>) msg.obj;
			switch (msg.what) {
				case AutoListView.REFRESH:
					listView.onRefreshComplete();
					list.clear();
					list.addAll(result);
					break;
				case 2:// 第一次加载
					listView.onRefreshComplete();
					list.clear();
					list.addAll(result);
					break;
				case AutoListView.LOAD:
					listView.onLoadComplete();
					list.addAll(result);
					break;
			}
			listView.setResultSize(result.size(), 0);
			adapter.notifyDataSetChanged();
		}
	};

	private ControlServiceImpl.ControlServiceImplBackValueListenser<ProgressDetailEntity> controlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<ProgressDetailEntity>() {

		@Override
		public void setControlServiceImplListenser(ProgressDetailEntity backValue,
				String stRerror, String Exceptionerror) {
			if (backValue != null) {

			}else if (Exceptionerror!=null) {
				Toast.makeText(HistoryNoticeActivity.this, Const.NETWORKERROR, Toast.LENGTH_SHORT).show();
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void initData() {
		Utils.getInstance().showProgressDialog(
				HistoryNoticeActivity.this, "",
				Const.SUBMIT_MESSAGE);
		service.getHistoryNoticeList(tag, listListener);
	}

	private void initView() {
		service = Control.getinstance().getUserSevice();
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		rb_all.setOnClickListener(this);
		rb_msg.setOnClickListener(this);
		rb_sys.setOnClickListener(this);
		rb_email.setOnClickListener(this);
		rb_app.setOnClickListener(this);
		title.setText("通知历史");
		adapter = new HistoryNoticeListAdapter(context, list);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

			}
		});
	}

	@Override
	public void onRefresh() {
		initData();
	}

	@Override
	public void onLoad() {
		loadData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.iv_actionbar_back:
				finish();
				break;
			case R.id.history_notice_rb_all:
				tag = "";
				rb_all.setChecked(true);
				rb_msg.setChecked(false);
				rb_sys.setChecked(false);
				rb_email.setChecked(false);
				rb_app.setChecked(false);
				initData();
				break;
			case R.id.history_notice_rb_msg:
				tag = "2";
				rb_all.setChecked(false);
				rb_msg.setChecked(true);
				rb_sys.setChecked(false);
				rb_email.setChecked(false);
				rb_app.setChecked(false);
				initData();
				break;
			case R.id.history_notice_rb_sys:
				tag = "0";
				rb_all.setChecked(false);
				rb_msg.setChecked(false);
				rb_sys.setChecked(true);
				rb_email.setChecked(false);
				rb_app.setChecked(false);
				initData();
				break;
			case R.id.history_notice_rb_email:
				tag = "1";
				rb_all.setChecked(false);
				rb_msg.setChecked(false);
				rb_sys.setChecked(false);
				rb_email.setChecked(true);
				rb_app.setChecked(false);
				initData();
				break;
			case R.id.history_notice_rb_app:
				tag = "3";
				rb_all.setChecked(false);
				rb_msg.setChecked(false);
				rb_sys.setChecked(false);
				rb_email.setChecked(false);
				rb_app.setChecked(true);
				initData();
				break;
		}
	}

	private UserSeviceImpl.UserSeviceImplListListenser listListener = new UserSeviceImpl.UserSeviceImplListListenser() {

		@Override
		public void setUserSeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			ArrayList<HistoryNoticeEntity> dataList = null;
			if (object != null) {
				dataList = (ArrayList<HistoryNoticeEntity>) object;
			} else if (stRerror != null) {
				dataList = new ArrayList<HistoryNoticeEntity>();
			} else if (Exceptionerror != null) {
				dataList = new ArrayList<HistoryNoticeEntity>();
				ToastUtil.showToast(HistoryNoticeActivity.this,
						Const.NETWORKERROR);
			} else {
				dataList = new ArrayList<HistoryNoticeEntity>();
			}

			Message msg = handler.obtainMessage();
			num = 0;
			if (dataList.size() > num + perCount) {// 如果超过可加载条数，则分页
				ArrayList<HistoryNoticeEntity> subList = (ArrayList) dataList.subList(0, perCount);
				msg.obj = subList;
				num = perCount;
			} else {
				msg.obj = dataList;
				num = dataList.size();
			}
			msg.what = 0;
			allList = dataList;
			handler.sendMessage(msg);
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void loadData()
	{
		ArrayList<HistoryNoticeEntity> datalist2;
		if ((num + perCount) <= allList.size()) {
			datalist2 = (ArrayList) allList.subList(num, num + perCount);
			num += perCount;
		} else {
			datalist2 = (ArrayList) allList.subList(num, allList.size());
			num = allList.size();
		}
		Message message = handler.obtainMessage();
		message.what = 1;
		message.obj = datalist2;
		handler.sendMessage(message);
	}
}
