package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.PlanNameRowEntity;

import java.util.List;


/**
 * 预案名称选择适配器（带checkbox，多选）
 */
public class ListvCheckboxMulselectAdapter extends BaseAdapter {

	public  List<PlanNameRowEntity> arraylist;
	private Context context;
	private int planTags;
	private String tags;

	public ListvCheckboxMulselectAdapter(Context context,
			List<PlanNameRowEntity> list, int planTags, String tags) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.arraylist = list;
		this.planTags = planTags;
		this.tags = tags;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public PlanNameRowEntity getItem(int position) {
		// TODO Auto-generated method stub
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final PlanNameRowEntity entity = getItem(position);
		final ViewHolderPlan mhHolder;
		if (convertView == null) {
			mhHolder = new ViewHolderPlan();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_planname, null);

			mhHolder.name = (TextView) convertView.findViewById(R.id.name);
			mhHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.planname_type_item_checkbox);

			convertView.setTag(mhHolder);
		} else {
			mhHolder = (ViewHolderPlan) convertView.getTag();
		}
		mhHolder.name.setText(entity.getName()
				+ ((entity.getSceneName() != null
				&& !"".equals(entity.getSceneName()))
				? "-" + entity.getSceneName() : ""));
		mhHolder.checkBox.setChecked(entity.isSelect());
		mhHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				//预案启动时判断是否有启动该预案权限
				if("1".equals(tags) && b && planTags != 5)
				{
					if("true".equals(entity.getHasStartAuth()))
						return;
					else {
						entity.setSelect(false);
						mhHolder.checkBox.setChecked(false);
						Toast.makeText(context, "您对该预案没有启动权限！", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		return convertView;
	}

	public static class ViewHolderPlan {
		TextView name;
		public CheckBox checkBox;

	}
}
