package com.dssm.esc.view.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.EventProcessRecyclerViewAdapter;
import com.dssm.esc.view.widget.RefreshLinearLayout;
import com.dssm.esc.view.widget.RingChartView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 事件流程图界面
 */
@ContentView(R.layout.activity_event_process_detail)
public class EventProcessDetailActivity extends BaseActivity implements MainActivity.onInitNetListener {
	/** 下拉刷新控件 */
	@ViewInject(R.id.rll_event_process)
	private RefreshLinearLayout rll_event_process;
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	/** 传过来的事件编号 */
	private String eventId = "";
	/** 传过来的事件名称 */
	private String eventName = "";
	/** 进度条 */
	@ViewInject(R.id.event_process_detail_rcv)
	private RingChartView ringChartView;
	/** 时间轴 */
	@ViewInject(R.id.event_process_detail_rv)
	private RecyclerView recyclerView;
	/** 时间轴数据源 */
	private List<ProgressDetailEntity.EvenDetail> list = new ArrayList<ProgressDetailEntity.EvenDetail>();
	/** 时间轴适配器 */
	private EventProcessRecyclerViewAdapter adapter;
	/** 进度条的进度（从网络获取） */
	//private String progress="79";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.event_process_detail);
		findViewById.setFitsSystemWindows(true);
		eventId = getIntent().getStringExtra("id");
		eventName = getIntent().getStringExtra("name");
		initView();
		initData();
	}

	private ControlServiceImpl.ControlServiceImplBackValueListenser<ProgressDetailEntity> controlServiceImplBackValueListenser = new ControlServiceImpl.ControlServiceImplBackValueListenser<ProgressDetailEntity>() {

		@Override
		public void setControlServiceImplListenser(ProgressDetailEntity backValue,
				String stRerror, String Exceptionerror) {
			// TODO Auto-generated method stub
			if (backValue != null) {
				ringChartView.setInnerProgress(Integer.parseInt(backValue.getProgressNum()));
//				overtime.setText(Utils.getInstance().setTtimeline(backValue.getNowTime(),backValue.getEveStartTime()));
				list.add(backValue.getEveAssess()) ;//事件评估
				list.add(backValue.getPlanStart()) ;//预案启动
				list.add(backValue.getPlanAuth()) ;//决策授权
				list.add(backValue.getPersonSign()) ;//人员签到与指派
				list.add(backValue.getPlanPerform()) ;//预案执行
				list.add(backValue.getEveClose()) ;//时间关闭
				adapter = new EventProcessRecyclerViewAdapter(EventProcessDetailActivity.this,
						list, backValue.getNowTime());
                //设置布局管理器
                recyclerView.setLayoutManager(new LinearLayoutManager(EventProcessDetailActivity.this));//设置布局管理器
                recyclerView.setItemAnimator(new DefaultItemAnimator());//设置控制Item增删的动画
				recyclerView.setAdapter(adapter);
			}else if (Exceptionerror!=null) {
				Toast.makeText(EventProcessDetailActivity.this, Const.NETWORKERROR, Toast.LENGTH_SHORT).show();
			}
			Utils.getInstance().hideProgressDialog();
			rll_event_process.onCompleteRefresh();
			rll_event_process.setResultSize(0, 2);
		}
	};

	private void initData() {
		Utils.getInstance().showProgressDialog(
				EventProcessDetailActivity.this, "",
				Const.SUBMIT_MESSAGE);
		Control.getinstance().getControlSevice().getProgressDetail(eventId, controlServiceImplBackValueListenser);
		
	}

	private void initView() {
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		rll_event_process.setOnRefreshListener(new RefreshLinearLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
			}
		});
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}
}
