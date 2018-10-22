package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.EmergencyMenuEntity;

import java.util.List;

/**
 * 应急管理页面的适配器
 */
public class EmergencyMenuListviewAdapter extends BaseAdapter {
	// 填充数据
	private List<EmergencyMenuEntity> list;
	// 上下文
	private Context context;
	// 用来导入布局
	private LayoutInflater inflater = null;

	public EmergencyMenuListviewAdapter(Context context,
                                        List<EmergencyMenuEntity> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_listview_emergency_menu,
					null);
			holder.name = (TextView) convertView.findViewById(R.id.item_list_view_emergency_menu_tv);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_list_view_emergency_menu_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(list.get(position).getIcon() > 0)
			holder.imageView.setImageResource(list.get(position).getIcon());
		holder.name.setText(list.get(position).getName());

		return convertView;
	}

	class ViewHolder {
		private ImageView imageView;
		private TextView name;
	}

}
