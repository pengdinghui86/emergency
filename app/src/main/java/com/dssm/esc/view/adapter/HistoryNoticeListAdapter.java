package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.message.HistoryNoticeEntity;
import com.dssm.esc.util.Utils;

import java.util.List;

public class HistoryNoticeListAdapter extends BaseAdapter {
	private List<HistoryNoticeEntity> arraylist;
	private Context context;
	public HistoryNoticeListAdapter(Context context, List<HistoryNoticeEntity> list) {
		this.context = context;
		this.arraylist = list;
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public HistoryNoticeEntity getItem(int position) {
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HistoryNoticeEntity entity = getItem(position);
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
		//0系统，1邮件，2短信，3APP
		if ("0".equals(entity.getSendType())) {
			mhHolder.message_listview_iv_type.setImageResource(R.drawable.system_notice);
			mhHolder.message_listview_tv_name.setText("系统");
		} else if("1".equals(entity.getSendType())) {
			mhHolder.message_listview_iv_type.setImageResource(R.drawable.email_notice);
			mhHolder.message_listview_tv_name.setText("邮件");
		} else if("2".equals(entity.getSendType())) {
			mhHolder.message_listview_iv_type.setImageResource(R.drawable.message_notice);
			mhHolder.message_listview_tv_name.setText("短信");
		} else if("3".equals(entity.getSendType())) {
			mhHolder.message_listview_iv_type.setImageResource(R.drawable.app_notice);
			mhHolder.message_listview_tv_name.setText("APP");
		}
		String creatTime = entity.getCreateTime();
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
