package com.dssm.esc.view.activity;

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
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.adapter.PlanStarAdapter;
import com.dssm.esc.view.widget.AutoListView;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
/**
 * 预案启动界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-11
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_dismissvaluation)
public class PlanStarActivity extends BaseActivity implements
		AutoListView.OnRefreshListener, AutoListView.OnLoadListener,MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	/** ListView */
	@ViewInject(R.id.dismissv_listview)
	private AutoListView listView;
	/** 适配器 */
	private PlanStarAdapter adapter;
	/** 数据源 */
	private List<PlanStarListEntity> list = new ArrayList<PlanStarListEntity>();
	/** 当前页面 */
	private int i = 1;
	/** 1,应急;2,演练 */
	//private String tag;
	/** 1,待启动事件列表;2,已启动预案列表 */
	private String tags;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			/**
			 * 接收子集合
			 */
			List<PlanStarListEntity> result = (List<PlanStarListEntity>) msg.obj;
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

				// i++;
				listView.onLoadComplete();
				list.addAll(result);
				break;

			default:
				break;
			}
			// 用来显示"加载全部"
			listView.setResultSize(result.size(), i);
			adapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 与驳回事件同用一个布局
//		setContentView(R.layout.activity_dismissvaluation);
		View findViewById = findViewById(R.id.dismissvaluation);
		findViewById.setFitsSystemWindows(true);
		//tag = getIntent().getStringExtra("tag");
		tags = getIntent().getStringExtra("tags");
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
		if (data.getData().equals("refres")) {
			
			initData();
		}
	}
	
	
	private void initview() {
		// TODO Auto-generated method stub
		back.setVisibility(View.VISIBLE);
			if (tags.equals("1")) {
				title.setText("待启动事件");

			} else if (tags.equals("2")) {
				title.setText("已启动预案");
			}
		adapter = new PlanStarAdapter(PlanStarActivity.this, list, tags);
		listView.setAdapter(adapter);
		
		initData();
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);
		listvitemclick();
//		setNetListener(this);
	}

	/**
	 * listview的item监听
	 */
	private void listvitemclick() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (position > 0 && position <= list.size()) {
					//启动中
					if(list.get(position - 1).getState().equals("5")) {
						ToastUtil.showLongToast(PlanStarActivity.this, "预案正在启动中，请稍后刷新列表重试");
						return;
					}
					if (tags.equals("1")) {

						Intent intent = new Intent(PlanStarActivity.this,
								PlanStarDetailActivity.class);
						//intent.putExtra("tag", tag);
						intent.putExtra("id", list.get(position - 1).getId());
						intent.putExtra("name", list.get(position - 1)
								.getEveName());
						intent.putExtra("tag", list.get(position - 1)
								.getEveType());
						intent.putExtra("isStarter", list.get(position-1).getIsStarter());
						startActivity(intent);
						// PlanStarActivity.this.finish();
					} else if (tags.equals("2")) {
						Intent intent = new Intent(PlanStarActivity.this,
								PlanSuspandDetilActivity.class);
						//intent.putExtra("tag", tag);
						intent.putExtra("id", list.get(position - 1).getId());
						intent.putExtra("stop", "0");// 已启动的预案
						intent.putExtra("isStarter",list.get(position - 1).getIsStarter());// 已启动的预案
						startActivity(intent);
						// PlanStarActivity.this.finish();
					}

				}
			}
		});
	}

	private void initData() {
		loadData(AutoListView.REFRESH);
	}

	// 从网络获取的总的list
	private List<PlanStarListEntity> allList = new ArrayList<PlanStarListEntity>();
	private int num = 20;// 每次显示20条

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListenser = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			List<PlanStarListEntity> datalist=null;
			if (object != null) {// 没有做分页，一次性加载所有的数据，本地做分页显示
				datalist = (List<PlanStarListEntity>) object;
				Log.i("待启动预案列表的长度", datalist.size() + "");

			}else if (stRerror!=null) {
				datalist=new ArrayList<PlanStarListEntity>();

			}else if (Exceptionerror!=null) {
				datalist=new ArrayList<PlanStarListEntity>();
				ToastUtil.showToast(PlanStarActivity.this, Const.NETWORKERROR);
			}
			Message message = handler.obtainMessage();
			message.what = 0;
			if (datalist.size() > 20) {// 如果超过20条，则分页
				List<PlanStarListEntity> subList = datalist
						.subList(0, 20);
				message.obj = subList;
			} else {
				message.obj = datalist;
			}
			handler.sendMessage(message);
			allList = datalist;
		}
	};

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			List<PlanStarListEntity> datalist=null;
			if (object != null) {
				datalist = (List<PlanStarListEntity>) object;
				Log.i("已启动预案列表的长度", datalist.size() + "");

			}else if (stRerror!=null) {
				datalist=new ArrayList<PlanStarListEntity>();

			}else if (Exceptionerror!=null) {
				datalist=new ArrayList<PlanStarListEntity>();
				ToastUtil.showToast(PlanStarActivity.this, Const.NETWORKERROR);
			}
			Message message = handler.obtainMessage();
			message.what = 0;
			if (datalist.size() > 20) {// 如果超过20条，则分页
				List<PlanStarListEntity> subList = datalist
						.subList(0, 20);
				message.obj = subList;
			} else {
				message.obj = datalist;
			}
			handler.sendMessage(message);
			allList = datalist;
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void loadData(final int what) {
		if (tags.equals("1")) {
			if (what == 0) {// 刷新和第一次加载
				/*
				 * List<PlanStarListEntity> datalist = new
				 * ArrayList<PlanStarListEntity>(); for (int i = 0; i < 79; i++)
				 * { PlanStarListEntity entity = new PlanStarListEntity();
				 * entity.setEveName("事件" + i); datalist.add(entity); } allList
				 * = datalist; Message message = handler.obtainMessage();
				 * message.what = 0; if (datalist.size() >= 20) {
				 * List<PlanStarListEntity> subList = datalist.subList(0, 20);
				 * message.obj = subList; } else { message.obj = datalist; }
				 * handler.sendMessage(message);
				 */
				Control.getinstance().getEmergencyService().getPlanStarList(listListenser);
			} else if (what == 1) {// 加载更多
				// 本地做分页，加载20条以后的数据，默认每20条分一页
				Log.i("list测试长度", allList.size() + "");
				Log.i("num", num + "");
				List<PlanStarListEntity> datalist2;
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
		} else if (tags.equals("2")) {
			if (what == 0) {// 刷新和第一次加载
				/*
				 * List<PlanStarListEntity> datalist = new
				 * ArrayList<PlanStarListEntity>(); for (int i = 0; i < 42; i++)
				 * { PlanStarListEntity entity = new PlanStarListEntity();
				 * entity.setPlanName("预案" + i); datalist.add(entity); } allList
				 * = datalist; Message message = handler.obtainMessage();
				 * message.what = 0; if (datalist.size() >= 20) {
				 * List<PlanStarListEntity> subList = datalist.subList(0, 20);
				 * message.obj = subList; } else { message.obj = datalist; }
				 * handler.sendMessage(message);
				 */
				Utils.getInstance().showProgressDialog(
						PlanStarActivity.this, "",
						Const.SUBMIT_MESSAGE);
				Control.getinstance().getEmergencyService().getStarList(listListener);
			} else if (what == 1) {
				List<PlanStarListEntity> datalist2;
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
		allList.clear();
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}
	
}
