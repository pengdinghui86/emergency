package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dssm.esc.R;

import java.util.HashMap;


public class MyExpandListAdapterTest extends BaseExpandableListAdapter {
	private Context context = null;
	public String[] groups = { "扑克", "麻将" }; // 组名称
	public String[][] children = { { "斗地主", "炸金花", "推火车" },
			{ "四川麻将", "北京麻将", "湖南麻将" } }; // 定义子选项
	private HashMap<String, Boolean> statusHashMap;

	public MyExpandListAdapterTest(Context context,
			HashMap<String, Boolean> statusHashMap) {
		this.context = context;
		this.statusHashMap = statusHashMap;
		for (int i = 0; i < children.length; i++) {// 初始时,让所有的子选项均未被选中
			for (int a = 0; a < children[i].length; a++) {
				statusHashMap.put(children[i][a], false);
			}
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) { // 取得指定的子项
		return this.children[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) { // 取得子项ID
		return childPosition;
	}

	public TextView buildTextView() { // 自定义方法，建立文本.用于显示组
		AbsListView.LayoutParams param = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, 35); // 指定布局参数
		TextView textView = new TextView(this.context); // 创建TextView
		textView.setLayoutParams(param); // 设置布局参数
		textView.setTextSize(14.0f); // 设置文字大小
		textView.setGravity(Gravity.LEFT); // 左对齐
		textView.setPadding(40, 8, 3, 3); // 间距
		return textView; // 返回组件
	}

	// 点击事件发生后:先执行事件监听,然后调用此getChildView()
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {// 返回子项组件
	
		if (convertView == null) {// 第一次的时候convertView是空,所以要生成convertView
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_test, null);
		}

//		TextView childTextView = (TextView) convertView.findViewById(R.id.textView_test);
//		childTextView
//				.setText(getChild(groupPosition, childPosition).toString());
//		CheckBox childBox = (CheckBox) convertView.findViewById(R.id.checkBox_test);
//		Boolean nowStatus = statusHashMap
//				.get(children[groupPosition][childPosition]);// 当前状态
//		childBox.setChecked(nowStatus);
		return convertView;
	}

	

	@Override
	public int getChildrenCount(int groupPosition) { // 取得子项个数
		return this.children[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) { // 取得组对象
		return this.groups[groupPosition];
	}

	@Override
	public int getGroupCount() { // 取得组个数
		return this.groups.length;
	}

	@Override
	public long getGroupId(int groupPosition) { // 取得组ID
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {// 取得组显示组件
		TextView textView = buildTextView(); // 建立组件
		textView.setText(this.getGroup(groupPosition).toString()); // 设置文字
		return textView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void notifyDataSetChanged() {// 通知数据发生变化
		super.notifyDataSetChanged();
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}