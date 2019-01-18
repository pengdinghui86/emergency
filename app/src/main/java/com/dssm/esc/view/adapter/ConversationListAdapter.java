package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dssm.esc.R;
import com.dssm.esc.util.Utils;
import java.util.List;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

public class ConversationListAdapter extends BaseAdapter {
	private List<Conversation> arraylist;
	private Context context;
	public ConversationListAdapter(Context context, List<Conversation> list) {
		this.context = context;
		this.arraylist = list;
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public Conversation getItem(int position) {
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Conversation conversation = getItem(position);
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
		Message message = conversation.getLatestMessage();
		String title = message.getFromUser().getNickname();
		if(message.getDirect() == MessageDirect.send)
		{
			title = ((UserInfo) conversation.getTargetInfo()).getNickname();
		}
		String createTime = Utils.getNewChatTime(message.getCreateTime());
		TextContent textContent = (TextContent) message.getContent();
		mhHolder.message_listview_iv_type.setImageResource(R.drawable.emergency_notice);
		mhHolder.message_listview_tv_name.setText(title);
		mhHolder.message_listview_tv_time.setText(createTime);
		mhHolder.message_listview_tv_content.setText(textContent.getText());
		return convertView;
	}

	class ViewHolder {

		private ImageView message_listview_iv_type;
		private TextView message_listview_tv_name;
		private TextView message_listview_tv_time;
		private TextView message_listview_tv_content;

	}

}
