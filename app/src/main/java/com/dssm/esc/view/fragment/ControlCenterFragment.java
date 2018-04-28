package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.view.activity.AutorizationDecisionActivity;
import com.dssm.esc.view.activity.EventProcessActivity;


/**
 * 指挥中心
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-6
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class ControlCenterFragment extends BaseFragment implements
		OnClickListener {
	private TextView title;
	/** 事件流程布局 */
	private LinearLayout event_prcess_ll;
	/** 指挥与展示布局 */
	private LinearLayout command_display_ll;
	private Context context;
	public ControlCenterFragment( ) {

	}
	@SuppressLint("ValidFragment")
	public ControlCenterFragment(Context context) {
		this.context = context;
		
	}

	@Override
	protected View getViews() {
		// TODO Auto-generated method stub
		return view_Parent = LayoutInflater.from(context).inflate(
				R.layout.fragment_control, null);
	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
//		exit = (ImageView) view_Parent.findViewById(R.id.iv_actionbar_back);
//		exit.setVisibility(View.VISIBLE);
	//	exit.setImageResource(R.drawable.settings_normal);
		title = (TextView) view_Parent.findViewById(R.id.tv_actionbar_title);
		title.setText(R.string.controlCenter);
		event_prcess_ll = (LinearLayout) view_Parent
				.findViewById(R.id.event_prcess_ll);
		command_display_ll = (LinearLayout) view_Parent
				.findViewById(R.id.command_display_ll);
	}

	@Override
	protected void widgetListener() {
		// TODO Auto-generated method stub
		event_prcess_ll.setOnClickListener(this);
		command_display_ll.setOnClickListener(this);
		
	}

	@Override
	protected void init() {
		
		

	}

	@Override
	public void initGetData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.event_prcess_ll:// 事件流程布局
			Intent intent = new Intent(getActivity(),
					EventProcessActivity.class);
			startActivity(intent);
			break;
		case R.id.command_display_ll:// 指挥与展示布局
			Intent intent2 = new Intent(getActivity(),
					AutorizationDecisionActivity.class);
			intent2.putExtra("tags", "5");
			//intent2.putExtra("tag", "0");
			startActivity(intent2);
			break;

		default:
			break;
		}
	}

	
	
}
