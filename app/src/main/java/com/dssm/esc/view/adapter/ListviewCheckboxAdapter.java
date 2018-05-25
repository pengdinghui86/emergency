package com.dssm.esc.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 应急类型和行业类型，事件等级和事件背景的适配器（带checkbox，单选）
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-10
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class ListviewCheckboxAdapter extends BaseAdapter {
	/** 数据源 */
	public static List<BusinessTypeEntity> list = new ArrayList<>();
	/** 用来控制CheckBox的选中状况 */
	public static HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
	private Context context;
	private LayoutInflater inflater = null;
	/** 被选中的选项 */
	public ArrayList<BusinessTypeEntity> beSelectedData = new ArrayList<BusinessTypeEntity>();
	private String tags;

	public ListviewCheckboxAdapter(Context context,
			List<BusinessTypeEntity> list, String tags) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.tags = tags;

		setchek();
	}

	public static void setchek() {

		for (int i = 0; i < list.size(); i++) {
			isSelected.put(i, false);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position1, View convertView, ViewGroup parent) {
		final BusinessTypeEntity businessTypeEntity = list.get(position1);
		final BusinessTypeEntity selectBusinessTypeEntity = new BusinessTypeEntity();
		final ViewHolder holder;
		final int position = position1;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_listview_type, null);
			holder.typeTv = (TextView) convertView
					.findViewById(R.id.select_type_item_type);
			holder.typeTv2 = (TextView) convertView
					.findViewById(R.id.select_type_item_type2);
			holder.typeTv3 = (TextView) convertView
					.findViewById(R.id.select_type_item_type3);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.select_type_item_checkbox);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String name = businessTypeEntity.getName();
		holder.typeTv.setText(name);
		if (tags.equals("7")) {// 人员指派
			holder.typeTv2.setVisibility(View.VISIBLE);
			holder.typeTv3.setVisibility(View.VISIBLE);
			holder.typeTv2.setText(businessTypeEntity.getRole());
			String signState2 = businessTypeEntity.getSignState();
			if (signState2.equals("1")) {

				holder.typeTv3.setText("已签到");
				holder.typeTv3.setTextColor(Color.GREEN);
				holder.checkBox.setVisibility(View.VISIBLE);
				holder.checkBox.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// 当前点击的CB
						boolean cu = !isSelected.get(position);
						// 先将所有的置为FALSE
						for (Integer p : isSelected.keySet()) {
							isSelected.put(p, false);
						}
						// 再将当前选择CB的实际状态
						setchek();
						isSelected.put(position, cu);
						beSelectedData.clear();
						if (cu) {
							selectBusinessTypeEntity.setId(businessTypeEntity
									.getId());
							selectBusinessTypeEntity.setName(businessTypeEntity
									.getName());
							selectBusinessTypeEntity.setSelect(cu);
							beSelectedData.add(selectBusinessTypeEntity);
							ExpanListCheckboxAdapter.setCheck();
						}
						ListviewCheckboxAdapter.this.notifyDataSetChanged();
					}
				});

				// 根据isSelected来设置checkbox的选中状况
				holder.checkBox.setChecked(isSelected.get(position));
			} else if (signState2.equals("0")) {
				holder.typeTv3.setText("未签到");
				holder.typeTv3.setTextColor(Color.RED);
				holder.checkBox.setVisibility(View.INVISIBLE);
				holder.checkBox.setChecked(false);
				holder.checkBox.setClickable(false);
			}

		} else {
			holder.typeTv2.setVisibility(View.GONE);
			holder.typeTv3.setVisibility(View.GONE);
			holder.checkBox.setClickable(true);
			holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					
					if (arg1) {
						for (int i = 0; i < list.size(); i++) {
							list.get(i).setSelect(false);
						}
						ListviewCheckboxAdapter.this.notifyDataSetChanged();
						businessTypeEntity.setSelect(true);
					}else {
						businessTypeEntity.setSelect(false);
					}
				}
			});
			//businessTypeEntity.s
			// 根据isSelected来设置checkbox的选中状况
			holder.checkBox.setChecked(businessTypeEntity.isSelect());
		}

		return convertView;
	}

	class ViewHolder {
		private TextView typeTv;
		private TextView typeTv2;
		private TextView typeTv3;
		private CheckBox checkBox;
	}

}
