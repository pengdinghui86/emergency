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
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.view.adapter.EventProcessListviewAdapter;
import com.dssm.esc.view.widget.AutoListView;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 事件流程界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-15
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class EventProcessActivity extends BaseActivity implements
		AutoListView.OnRefreshListener, AutoListView.OnLoadListener,MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView back;
	/** ListView */
	@ViewInject(id = R.id.eventprocess_listview)
	AutoListView listView;
	/** 适配器 */
	private EventProcessListviewAdapter adapter;
	/** 数据源 */
	private List<BoHuiListEntity> list = new ArrayList<BoHuiListEntity>();
	/** 当前页面 */
	private int i = 1;
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
		setContentView(R.layout.activity_eventprocess);
		View findViewById = findViewById(R.id.eventprocess);
		findViewById.setFitsSystemWindows(true);
		initview();
		initData();
	}

	private void initview() {
		// TODO Auto-generated method stub
		
		back.setVisibility(View.VISIBLE);

		title.setText(R.string.event_process);

		adapter = new EventProcessListviewAdapter(
				EventProcessActivity.this, list,"6");
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				if (position > 0 && position <= list.size()) {
				
						Intent intent = new Intent(EventProcessActivity.this,EventProcessDetailActivity.class);
						intent.putExtra("BoHuiListEntity", adapter.getItem(position-1));
					startActivity(intent);
						
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
	private void loadData(final int what) {
		if (what==0) {
		csevice.getEvalist(new ControlServiceImpl.ControlServiceImplBackValueListenser<List<BoHuiListEntity>>() {

			@Override
			public void setControlServiceImplListenser(
					List<BoHuiListEntity> backValue, String stRerror,
					String Exceptionerror) {
				// TODO Auto-generated method stub
                //List<EvaProgressEntity>list;
				Message message = handler.obtainMessage();
				List<BoHuiListEntity> list =null;
				if (backValue!=null) {
					list=backValue;
				}else if (Exceptionerror!=null) {
					list=new ArrayList<BoHuiListEntity>();
				Toast.makeText(EventProcessActivity.this, Const.NETWORKERROR+Exceptionerror, Toast.LENGTH_SHORT).show();
			}
				message.what = 0;
				if (list.size() > 20) {// 如果超过20条，则分页
					List<BoHuiListEntity> subList = list.subList(0,
							20);
					message.obj = subList;
				} else {
					message.obj = list;
				}
				handler.sendMessage(message);
				allList = list;
			}
		});
		}else if (what==1) {
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
		i=1;
		loadData(AutoListView.REFRESH);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		list.clear();
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}
}
