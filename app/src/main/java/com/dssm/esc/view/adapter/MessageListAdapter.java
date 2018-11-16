package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.message.MessageInfoEntity;
import com.dssm.esc.model.entity.message.MessageStatusEnum;
import com.dssm.esc.util.Utils;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {
	private List<MessageInfoEntity> arraylist;
	private Context context;
	public MessageListAdapter(Context context, List<MessageInfoEntity> list) {
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
					R.layout.item_listview_message, null);

			mhHolder.message_listview_iv_type = convertView.findViewById(R.id.message_listview_iv_type);
			mhHolder.message_listview_tv_name = convertView.findViewById(R.id.message_listview_tv_name);
			mhHolder.message_listview_tv_time = convertView
					.findViewById(R.id.message_listview_tv_time);
			mhHolder.message_listview_tv_content = convertView
					.findViewById(R.id.message_listview_tv_content);
			convertView.setTag(mhHolder);
		} else {
			mhHolder = (ViewHolder) convertView.getTag();
		}
		//应急通知
		if(MessageStatusEnum.emergencyNotice.getId().equals(
				entity.getModelFlag()) || MessageStatusEnum.noticeAnnouncement.getId().equals(
				entity.getModelFlag()) || MessageStatusEnum.collaborationAnnouncement.getId().equals(
				entity.getModelFlag())|| MessageStatusEnum.emergencyCommunication.getId().equals(
				entity.getModelFlag()))
		{
			mhHolder.message_listview_iv_type.setImageResource(R.drawable.emergency_notice);
			mhHolder.message_listview_tv_name.setText(entity.getSender());
		}
		else {
			if ("1".equals(entity.getEveType()))
				mhHolder.message_listview_iv_type.setImageResource(R.drawable.emergency_type);
			else
				mhHolder.message_listview_iv_type.setImageResource(R.drawable.drill_type);
			String title = entity.getEveName();
			if(!"".equals(entity.getPlanName()))
				title += "-" + entity.getPlanName();
			mhHolder.message_listview_tv_name.setText(title);
		}
		String creatTime = entity.getTime();
		long time = Utils.getStringToDate(creatTime, "yyyy-MM-dd HH:mm:ss");
		mhHolder.message_listview_tv_time.setText(Utils.getNewChatTime(time));
		mhHolder.message_listview_tv_content.setText(entity.getMessage());
		return convertView;
	}

	class ViewHolder {

		private ImageView message_listview_iv_type;
		private TextView message_listview_tv_name;
		private TextView message_listview_tv_time;
		private TextView message_listview_tv_content;

	}

}
