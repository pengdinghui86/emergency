package com.dssm.esc.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.adapter.DismissValuationListviewAdapter;
import com.dssm.esc.view.widget.AutoListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 驳回事件页面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-10
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_dismissvaluation)
public class DismissValuationActivity extends BaseActivity implements
		AutoListView.OnRefreshListener, AutoListView.OnLoadListener,MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	/** ListView */
	@ViewInject(R.id.dismissv_listview)
	AutoListView listView;
	/** 适配器 */
	private DismissValuationListviewAdapter adapter;
	/** 数据源 */
	private List<BoHuiListEntity> list = new ArrayList<BoHuiListEntity>();
	private GetProjectEveInfoEntity entity;
	/** 当前页面 */
	private int i = 1;
	/** 1，应急；2，演练 */
	//private String tag;
	/** 事件编号 */
	private String id;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			/**
			 * 接收子集合
			 */
			List<BoHuiListEntity> result = (List<BoHuiListEntity>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				listView.onRefreshComplete();
				/**
				 * 总集合清理
				 */
				list.clear();
				/**
				 * 总集合添加
				 */
				list.addAll(result);
				break;

			case AutoListView.LOAD:
				listView.onLoadComplete();
				list.addAll(result);
				break;

			default:
				break;
			}
			listView.setResultSize(result.size(), i);
			adapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_dismissvaluation);
		View findViewById = findViewById(R.id.dismissvaluation);
		findViewById.setFitsSystemWindows(true);
		//tag = getIntent().getStringExtra("tag");
		initview();
	}
	/***
	 * 打开EventBus开关
	 */
	protected boolean useEventBus() {
		return true;
	}

	/**
	 * 刷新界面
	 * @param data
	 */
	public void onEvent(mainEvent data) {
		if (data.getData().equals("re")) {
			
			initData();
		}
	}
	

	private void initview() {
		// TODO Auto-generated method stub
		back.setVisibility(View.VISIBLE);
		
			title.setText(R.string.dismissvaluation);
		adapter = new DismissValuationListviewAdapter(
				DismissValuationActivity.this, list,"7");
		listView.setAdapter(adapter);
		initData();
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				if (position > 0 && position <= list.size()) {
					Dialog dialog = new AlertDialog.Builder(
							DismissValuationActivity.this)
							.setMessage(
									adapter.getItem(position - 1).getEveName())

							// 设置LOGO
							.setPositiveButton("删除",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

											deleteData(position);
										}
									})
							.setNeutralButton("重新评估",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											getValuation(position);

										}
									})

							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).create(); // 创建对话框
					dialog.show(); // 显示对话框

				}
			}
		});
//		setNetListener(this);
	}

	private void initData() {
		loadData(AutoListView.REFRESH);
	}

	// 从网络获取的总的list
	private List<BoHuiListEntity> allList = new ArrayList<BoHuiListEntity>();
	private int num = 20;// 每次显示20条

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListenser = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(Object object,
				String stRerror, String Exceptionerror) {
			// TODO Auto-generated method stub
			List<BoHuiListEntity> dataList=null;
			Message message = handler.obtainMessage();
			if (object != null) {
				dataList = (List<BoHuiListEntity>) object;
				Log.i("驳回事件的长度", dataList.size() + "");

			}else if (stRerror!=null) {
				dataList=new ArrayList<BoHuiListEntity>();

			}else if (Exceptionerror!=null) {
				dataList=new ArrayList<BoHuiListEntity>();
				ToastUtil.showToast(DismissValuationActivity.this, Const.NETWORKERROR);
			}
			message.what = 0;
			if (dataList.size() > 20) {// 如果超过20条，则分页
				List<BoHuiListEntity> subList = dataList.subList(0,
						20);
				message.obj = subList;
			} else {
				message.obj = dataList;
			}
			handler.sendMessage(message);
			allList = dataList;
		}
	};

	private void loadData(final int what) {
		// for (int i = 0; i < 5; i++) {
		// dataList.add("驳回事件" + (i + 1));
		// }
		if (what == 0) {// 刷新和第一次加载
			Control.getinstance().getEmergencyService().getBoHuiList(listListenser);
		} else if (what == 1) {// 加载更多
			// 本地做分页，加载20条以后的数据，默认每20条分一页
			Log.i("list测试长度", allList.size() + "");
			Log.i("num", num + "");
			List<BoHuiListEntity> datalist2;
			if ((num + 20) <= allList.size()) {
				datalist2 = allList.subList(num, num + 20);
				num += 20;
			} else {
				datalist2 = allList.subList(num, allList.size());
			}

			Message message = handler.obtainMessage();
			message.what = 1;
			message.obj = datalist2;
			handler.sendMessage(message);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		list.clear();
		adapter=null;
		allList.clear();
	}

	private int curPosition = 1;
	private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser deleteEventListener = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {
			// TODO
			// Auto-generated
			// method stub
			if (backflag) {
				list.remove(curPosition - 1);
				adapter.notifyDataSetChanged();
				ToastUtil.showToast(DismissValuationActivity.this,
						stRerror);
			} else if (backflag == false) {
				ToastUtil.showToast(DismissValuationActivity.this,
						stRerror);
			} else if (stRerror != null) {

				ToastUtil.showLongToast(DismissValuationActivity.this,
						stRerror);
			} else if (Exceptionerror != null) {

				ToastUtil.showLongToast(DismissValuationActivity.this,
						Const.NETWORKERROR);
			}
		}
	};

	/**
	 * 删除驳回事件
	 */
	private void deleteData(final int position) {
		curPosition = position;
		Control.getinstance().getEmergencyService().deleteEvent(list.get(position - 1).getId(), deleteEventListener);

	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			if (object != null) {
				entity = (GetProjectEveInfoEntity) object;
				Intent intent = new Intent(
						DismissValuationActivity.this,
						AddeValuationActivity.class);
				//	intent.putExtra("tag", tag);
				intent.putExtra("type", "1");// 重新评估
				intent.putExtra("entity", entity);
				startActivity(intent);
			}else if (stRerror!=null) {
				entity = new GetProjectEveInfoEntity();

			}else if (Exceptionerror!=null) {
				entity = new GetProjectEveInfoEntity();
				ToastUtil.showToast(DismissValuationActivity.this, Const.NETWORKERROR);
			}
		}
	};

	/**
	 * 获取评估信息
	 */
	private void getValuation(int position) {
		// 获取评估信息
		Control.getinstance().getEmergencyService().getEventInfo(list.get(position - 1).getId(), listener);

	}
	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}
	
}
