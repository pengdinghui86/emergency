package com.dssm.esc.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.message.MessageInfoEntity;

import java.util.List;

/**
 * 任务通知，系统通知适配器
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-18
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class MessageToastAdapter extends BaseAdapter {
	private List<MessageInfoEntity> arraylist;
	private Context context;
	public MessageToastAdapter(Context context, List<MessageInfoEntity> list) {
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
	public MessageInfoEntity getItem(int position) {
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

		MessageInfoEntity entity = getItem(position);
		ViewHolder mhHolder = null;
		if (convertView == null) {
			mhHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_messagetoast, null);

			mhHolder.date = (TextView) convertView.findViewById(R.id.date);
			mhHolder.time = (TextView) convertView.findViewById(R.id.time);
			mhHolder.content = (TextView) convertView
					.findViewById(R.id.content);
			mhHolder.message_buboom_ll = (LinearLayout) convertView
					.findViewById(R.id.message_buboom_ll);
			convertView.setTag(mhHolder);
		} else {
			mhHolder = (ViewHolder) convertView.getTag();
		}
		if (position % 2 == 0) {
			mhHolder.message_buboom_ll
					.setBackgroundResource(R.drawable.buboom_bule);
		} else {
			mhHolder.message_buboom_ll
					.setBackgroundResource(R.drawable.buboom_gray);
		}
		// 任务通知
		String creatTime = entity.getTime();
		String[] split = creatTime.trim().split(" ");
		for (int i = 0; i < split.length; i++) {
			
			Log.i("date_time", split[i]);
		}
		Log.i("creatTime", creatTime);
		mhHolder.date.setText(split[0]);
		mhHolder.time.setText(split[1]);
//		if (tag.equals("4")) {//个人
//			mhHolder.content.setText("【"+entity.getSender()+"】"+entity.getMessage());
//		}else {
			mhHolder.content.setText(entity.getMessage());
		//}

		return convertView;
	}

	class ViewHolder {

		private TextView date;
		private TextView time;
		private TextView content;
		private LinearLayout message_buboom_ll;

	}

}
