package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.MessageToastEntity;

import java.util.List;


/**
 * 我的消息
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-18
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class MyMessagesAdapter extends BaseAdapter {
	private List<MessageToastEntity> arraylist;
	private Context context;

	public MyMessagesAdapter(Context context, List<MessageToastEntity> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.arraylist = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public MessageToastEntity getItem(int position) {
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

		MessageToastEntity entity = getItem(position);
		ViewHolder mhHolder = null;
		if (convertView == null) {
			mhHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_mymessage, null);
			mhHolder.name = (TextView) convertView
					.findViewById(R.id.mymessage_name_tv);
			mhHolder.position = (TextView) convertView
					.findViewById(R.id.mymessage_positon_tv);
			mhHolder.date = (TextView) convertView
					.findViewById(R.id.mymessage_date);
			mhHolder.time = (TextView) convertView
					.findViewById(R.id.mymessage_time);
			mhHolder.content = (TextView) convertView
					.findViewById(R.id.mymessage_content_tv);

			convertView.setTag(mhHolder);
		} else {
			mhHolder = (ViewHolder) convertView.getTag();
		}

		mhHolder.name.setText(entity.getSender());
	//	mhHolder.position.setText(entity.getPosition());
		mhHolder.time.setText(entity.getCreateTime());
		mhHolder.content.setText(entity.getMessage());

		return convertView;
	}

	class ViewHolder {
		private TextView name;
		private TextView position;// 职位
		private TextView date;
		private TextView time;
		private TextView content;

	}

}
