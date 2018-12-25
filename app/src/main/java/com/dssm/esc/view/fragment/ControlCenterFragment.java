package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.view.activity.EventListActivity;
import com.dssm.esc.view.activity.EventPlanListActivity;


/**
 * 指挥中心
 */
public class ControlCenterFragment extends BaseFragment implements
		OnClickListener {
	private TextView title;
	/** 事件流程布局 */
	private RelativeLayout rl_event_procedure;
	/** 指挥与展示布局 */
	private RelativeLayout rl_command_display;
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
		title = (TextView) view_Parent.findViewById(R.id.tv_actionbar_title);
		title.setText(R.string.controlCenter);
		rl_event_procedure = (RelativeLayout) view_Parent
				.findViewById(R.id.control_center_rl_event_procedure);
		rl_command_display = (RelativeLayout) view_Parent
				.findViewById(R.id.control_center_rl_command_display);
	}

	@Override
	protected void widgetListener() {
		// TODO Auto-generated method stub
		rl_event_procedure.setOnClickListener(this);
		rl_command_display.setOnClickListener(this);
		
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
		case R.id.control_center_rl_event_procedure:// 事件流程布局
			Intent intent = new Intent(getActivity(),
					EventListActivity.class);
			intent.putExtra("tags", "6");
			startActivity(intent);
			break;
		case R.id.control_center_rl_command_display:// 指挥与展示布局
			Intent intent2 = new Intent(getActivity(),
					EventPlanListActivity.class);
			intent2.putExtra("tags", "5");
			startActivity(intent2);
			break;

		default:
			break;
		}
	}
}
