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
import com.dssm.esc.model.entity.message.MessageInfoEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.MessageListAdapter;
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
	public int tag = 0;
	private List<MessageInfoEntity> list = new ArrayList<MessageInfoEntity>();
	private MessageListAdapter adapter;
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
			List<MessageInfoEntity> result = (ArrayList<MessageInfoEntity>) msg.obj;
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
		service.getHistoryNoticeList(tag + "", listListener);
	}

	private void initView() {
		service = Control.getinstance().getUserSevice();
		back.setOnClickListener(this);
		rb_all.setOnClickListener(this);
		rb_msg.setOnClickListener(this);
		rb_sys.setOnClickListener(this);
		rb_email.setOnClickListener(this);
		rb_app.setOnClickListener(this);
		title.setText("通知历史");
		adapter = new MessageListAdapter(context, list);
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

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.history_notice_rb_all:
				tag = 0;
				rb_all.setChecked(true);
				rb_msg.setChecked(false);
				rb_sys.setChecked(false);
				rb_email.setChecked(false);
				rb_app.setChecked(false);
				initData();
				break;
			case R.id.history_notice_rb_msg:
				tag = 1;
				rb_all.setChecked(false);
				rb_msg.setChecked(true);
				rb_sys.setChecked(false);
				rb_email.setChecked(false);
				rb_app.setChecked(false);
				initData();
				break;
			case R.id.history_notice_rb_sys:
				tag = 2;
				rb_all.setChecked(false);
				rb_msg.setChecked(false);
				rb_sys.setChecked(true);
				rb_email.setChecked(false);
				rb_app.setChecked(false);
				initData();
				break;
			case R.id.history_notice_rb_email:
				tag = 3;
				rb_all.setChecked(false);
				rb_msg.setChecked(false);
				rb_sys.setChecked(false);
				rb_email.setChecked(true);
				rb_app.setChecked(false);
				initData();
				break;
			case R.id.history_notice_rb_app:
				tag = 4;
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
			List<MessageInfoEntity> dataList = null;
			if (object != null) {

			} else if (stRerror != null) {
				dataList = new ArrayList<MessageInfoEntity>();
			} else if (Exceptionerror != null) {
				dataList = new ArrayList<MessageInfoEntity>();
				ToastUtil.showToast(HistoryNoticeActivity.this,
						Const.NETWORKERROR);
			} else {
				dataList = new ArrayList<MessageInfoEntity>();
			}
			Message msg = handler.obtainMessage();
			msg.what = 0;
			msg.obj = dataList;
			handler.sendMessage(msg);
		}
	};
}
