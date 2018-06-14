package com.dssm.esc.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dssm.esc.R;
import com.dssm.esc.model.entity.control.FlowChartPlanEntity;
import com.dssm.esc.view.widget.MyFlowView;
import com.dssm.esc.view.widget.NSSetPointValueToSteps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程监控子预案流程图界面
 */
@ContentView(R.layout.activity_process_monitoring_sub_mission)
public class ProcessMonitoringSubMissionActivity extends BaseActivity {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	private MyFlowView my_flow_view;
	private String name = "";
	private String stepId = "";
	private NSSetPointValueToSteps nsSetPointValueToSteps = new NSSetPointValueToSteps();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_process_monitoring_sub_mission);
		View findViewById = findViewById(R.id.ll_sub_mission);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		if(intent != null) {
			name = intent.getStringExtra("name");
			stepId = intent.getStringExtra("stepId");
		}
		SharedPreferences sp = getSharedPreferences("SP_MISSION_LIST", Activity.MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
		String listJson = sp.getString("KEY_MISSION_LIST_DATA","");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
		nsSetPointValueToSteps.steplist = new ArrayList<>();
		nsSetPointValueToSteps.subFlowChart = new ArrayList<>();
		if(listJson != "")  //防空判断
		{
			Gson gson = new Gson();
			List<FlowChartPlanEntity.FlowChart> list = gson.fromJson(listJson, new TypeToken<List<FlowChartPlanEntity.FlowChart>>() {}.getType()); //将json字符串转换成List集合
			nsSetPointValueToSteps.exampleSteps(list, stepId);
			nsSetPointValueToSteps.proveStepPosition();
		}
		initView();
		my_flow_view.post(new Runnable() {
			@Override
			public void run() {
				initData();
			}
		});
	}

	private void initView() {
		my_flow_view = (MyFlowView) findViewById(R.id.my_flow_view);
		back.setVisibility(View.VISIBLE);
		title.setText(name);
	}

	private void initData() {
		my_flow_view.setData(nsSetPointValueToSteps);
	}
}
