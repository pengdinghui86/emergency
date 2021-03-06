package com.dssm.esc.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.TimeLineListviewAdapter;
import com.dssm.esc.view.widget.MyProgressBar;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 事件流程图界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-15
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class EventProcessDetailActivity extends BaseActivity implements MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView back;
	/** 传过来的事件实体 */
	BoHuiListEntity boHuiListEntity  ;
	/** 进度条 */
	@ViewInject(id = R.id.progressBar)
	private MyProgressBar progressBar;
	/** 已用时 */
	@ViewInject(id = R.id.overtime)
	private TextView overtime;
	/** 时间轴 */
	@ViewInject(id = R.id.listview)
	private ListView listview;
	/** 时间轴数据源 */
	private List<ProgressDetailEntity.EvenDetail> list = new ArrayList<ProgressDetailEntity.EvenDetail>();
	/** 时间轴适配器 */
	private TimeLineListviewAdapter adapter;
	/** 进度条的进度（从网络获取） */
	//private String progress="79";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventprocessdetail);
		View findViewById = findViewById(R.id.eventprocessdetail);
		findViewById.setFitsSystemWindows(true);
		boHuiListEntity = (BoHuiListEntity) getIntent().getSerializableExtra("BoHuiListEntity");
		initView();
		initData();
	}

	private void initData() {
		csevice.getProgressDetail(boHuiListEntity.getId(), new ControlServiceImpl.ControlServiceImplBackValueListenser<ProgressDetailEntity>() {
			
			@Override
			public void setControlServiceImplListenser(ProgressDetailEntity backValue,
					String stRerror, String Exceptionerror) {
				// TODO Auto-generated method stub
				if (backValue!=null) {
					progressBar.setMax(6);
					progressBar.setProgress(Integer.parseInt(backValue.getProgressNum()));
					overtime.setText(Utils.getInstance().setTtimeline(backValue.getNowTime(),backValue.getEveStartTime()));
				   list.add(backValue.getEveAssess()) ;//事件评估
				   list.add(backValue.getPlanStart()) ;//预案启动
				   list.add(backValue.getPlanAuth()) ;//决策授权
				   list.add(backValue.getPersonSign()) ;//人员签到与指派
				   list.add(backValue.getPlanPerform()) ;//预案执行
				   list.add(backValue.getEveClose()) ;//时间关闭
				   adapter = new TimeLineListviewAdapter(EventProcessDetailActivity.this,
							list,backValue.getNowTime());
					listview.setAdapter(adapter);
				}else if (Exceptionerror!=null) {
					Toast.makeText(EventProcessDetailActivity.this, Const.NETWORKERROR+Exceptionerror, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}

	private void initView() {
		back.setVisibility(View.VISIBLE);
		title.setText(boHuiListEntity.getEveName());
//		setNetListener(this);
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}
	
	
}
