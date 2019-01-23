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
import cn.jpush.im.android.api.enums.ContentType;
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
		String title;
		if(message == null || message.getFromUser() == null)
			title = conversation.getTitle();
		else {
			title = message.getFromUser().getNickname();
			if (message.getDirect() == MessageDirect.send) {
				title = ((UserInfo) conversation.getTargetInfo()).getNickname();
			}
		}
		if (title == null)
			title = "";
		String createTime = "";
		if(message != null)
			createTime = Utils.getNewChatTime(message.getCreateTime());
		mhHolder.message_listview_iv_type.setImageResource(R.drawable.emergency_notice);
		mhHolder.message_listview_tv_name.setText(title);
		mhHolder.message_listview_tv_time.setText(createTime);
		mhHolder.message_listview_tv_content.setText(getMessageDigest(message));
		return convertView;
	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * @param message
	 */
	private String getMessageDigest(Message message) {
		String digest = "";
		if(message == null)
			return digest;
		// 位置消息
		if(ContentType.location == message.getContentType())
		{
			if (message.getDirect() == MessageDirect.receive) {
				digest = context.getString(R.string.location_recv);
				digest = String.format(digest, message.getFromUser().getUserName());
				return digest;
			} else {
				digest = context.getString(R.string.location_prefix);
			}
		}
		// 图片消息
		else if(ContentType.image == message.getContentType())
		{
			digest = context.getString(R.string.picture);
			return digest;
		}
		// 语音消息
		else if(ContentType.voice == message.getContentType())
		{
			digest = context.getString(R.string.voice);
			return digest;
		}
		// 普通文件消息
		else if(ContentType.file == message.getContentType())
		{
			digest = context.getString(R.string.file);
			return digest;
		}
		// 视频消息
		else if(ContentType.video == message.getContentType())
		{
			digest = context.getString(R.string.video);
			return digest;
		}
		// 文本消息
		else if(ContentType.text == message.getContentType())
		{
			TextContent textContent = (TextContent) message.getContent();
			digest = textContent.getText();
			return digest;
		}
		return digest;
	}

	class ViewHolder {

		private ImageView message_listview_iv_type;
		private TextView message_listview_tv_name;
		private TextView message_listview_tv_time;
		private TextView message_listview_tv_content;

	}

}
