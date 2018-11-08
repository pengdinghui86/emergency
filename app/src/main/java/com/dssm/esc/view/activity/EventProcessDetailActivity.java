package com.dssm.esc.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.entity.control.ProgressDetailEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.EventProcessDetailListviewAdapter;
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
	@ViewInject(R.id.event_process_detail_rcv)
	private RingChartView ringChartView;
	@ViewInject(R.id.event_process_detail_lv)
	private ListView listView;
	/** 数据源 */
	private List<ProgressDetailEntity.EvenDetail> list = new ArrayList<ProgressDetailEntity.EvenDetail>();
	/** 适配器 */
	private EventProcessDetailListviewAdapter adapter;

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
				ringChartView.setOutProgress(Integer.parseInt(backValue.getProgressNum()));
				List<ProgressDetailEntity.EvenDetail> list = new ArrayList<ProgressDetailEntity.EvenDetail>();
				list.add(backValue.getEveAssess()) ;//事件评估
				list.add(backValue.getPlanStart()) ;//预案启动
				list.add(backValue.getPlanAuth()) ;//决策授权
				list.add(backValue.getPersonSign()) ;//人员签到与指派
				list.add(backValue.getPlanPerform()) ;//预案执行
				list.add(backValue.getEveClose()) ;//时间关闭
				adapter.update(list, backValue.getNowTime());
			}else if (Exceptionerror!=null) {
				Toast.makeText(EventProcessDetailActivity.this, Const.NETWORKERROR, Toast.LENGTH_SHORT).show();
			}
			Utils.getInstance().hideProgressDialog();
			rll_event_process.onCompleteRefresh();
			rll_event_process.setResultSize(0, 3);
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
		adapter = new EventProcessDetailListviewAdapter(this, list);
		listView.setAdapter(adapter);
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}
}
