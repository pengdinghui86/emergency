/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dssm.esc.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dssm.esc.R;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.Utils;
import com.dssm.esc.DemoApplication;
import com.dssm.esc.view.activity.ChatActivity;
import com.dssm.esc.util.VoicePlayClickListener;
import com.dssm.esc.util.SmileUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.enums.MessageStatus;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;


public class MessageAdapter extends BaseAdapter{

	private String username;
	private LayoutInflater inflater;
	private WeakReference<Context> wf;
	private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
	private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
	private static final int HANDLER_MESSAGE_SEEK_TO = 2;
	private Conversation conversation;
	List<Message> messages = null;
	private Map<String, Timer> timers = new Hashtable<String, Timer>();

	public MessageAdapter(Context context, String username, int chatType) {
		this.username = username;
		inflater = LayoutInflater.from(context);
		wf = new WeakReference<>(context);
		this.conversation = JMessageClient.getSingleConversation(username);
	}
	
	Handler handler = new Handler() {
		private void refreshList() {
			// UI线程不能直接使用conversation.getAllMessages()
			// 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
			messages = conversation.getAllMessage();
			notifyDataSetChanged();
		}
		
		@Override
		public void handleMessage(android.os.Message message) {
			switch (message.what) {
			case HANDLER_MESSAGE_REFRESH_LIST:
				refreshList();
				break;
			case HANDLER_MESSAGE_SELECT_LAST:
				if (wf.get() != null && wf.get() instanceof ChatActivity) {
					ListView listView = ((ChatActivity) wf.get()).getListView();
					if (messages.size() > 0) {
						listView.setSelection(messages.size() - 1);
					}
				}
				break;
			case HANDLER_MESSAGE_SEEK_TO:
				int position = message.arg1;
				if (wf.get() != null && wf.get() instanceof ChatActivity) {
					ListView listView = ((ChatActivity) wf.get()).getListView();
					listView.setSelection(position);
				}
				break;
			default:
				break;
			}
		}
	};


	/**
	 * 获取item数
	 */
	public int getCount() {
		return messages == null ? 0 : messages.size();
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
			return;
		}
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
		handler.sendMessage(msg);
	}
	
	/**
	 * 刷新页面, 选择最后一个
	 */
	public void refreshSelectLast() {
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_LAST));
	}
	
	/**
	 * 刷新页面, 选择Position
	 */
	public void refreshSeekTo(int position) {
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
		msg.arg1 = position;
		handler.sendMessage(msg);
	}

	public Message getItem(int position) {
		if (messages != null && position < messages.size()) {
			return messages.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取item类型
	 */
	public int getItemViewType(int position) {
		Message message = getItem(position);
		if (message == null) {
			return -1;
		}
		if (message.getContentType().equals(ContentType.text))
			return 0;
		return -1;
	}

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Message message = getItem(position);
		ContentType chatType = message.getContentType();
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			if(message.getDirect() == MessageDirect.receive)
				holder.direction = 1;
			else
				holder.direction = 2;
			convertView = createViewByMessage(message);
			if (chatType == ContentType.image) {
				try {
					holder.iv = ((ImageView) convertView.findViewById(R.id.iv_sendPicture));
					holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView.findViewById(R.id.percentage);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
				} catch (Exception e) {

				}

			} else if (chatType == ContentType.text) {

				try {
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
					// 这里是文字内容
					holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
					holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
					
					holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
					holder.tvList = (LinearLayout) convertView.findViewById(R.id.ll_layout);
				} catch (Exception e) {

				}
			} else if (chatType == ContentType.voice) {
				try {
					holder.iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
					holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView.findViewById(R.id.tv_length);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
					holder.iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
				} catch (Exception e) {

				}
			} else if (chatType == ContentType.location) {
				try {
					holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView.findViewById(R.id.tv_location);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
				} catch (Exception e) {

				}
			} else if (chatType == ContentType.video) {
				try {
					holder.iv = ((ImageView) convertView.findViewById(R.id.chatting_content_iv));
					holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView.findViewById(R.id.percentage);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.size = (TextView) convertView.findViewById(R.id.chatting_size_iv);
					holder.timeLength = (TextView) convertView.findViewById(R.id.chatting_length_iv);
					holder.playBtn = (ImageView) convertView.findViewById(R.id.chatting_status_btn);
					holder.container_status_btn = (LinearLayout) convertView.findViewById(R.id.container_status_btn);
					holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);

				} catch (Exception e) {

				}
			} else if (chatType == ContentType.file) {
				try {
					holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
					holder.tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_name);
					holder.tv_file_size = (TextView) convertView.findViewById(R.id.tv_file_size);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_file_download_state = (TextView) convertView.findViewById(R.id.tv_file_state);
					holder.ll_container = (LinearLayout) convertView.findViewById(R.id.ll_file_container);
					// 这里是进度值
					holder.tv = (TextView) convertView.findViewById(R.id.percentage);
				} catch (Exception e) {

				}
				try {
					holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
				} catch (Exception e) {

				}
			}
			convertView.setTag(holder);
		} else {
			ViewHolder holder1 = (ViewHolder) convertView.getTag();
			if((holder1.direction == 1 ? MessageDirect.receive : MessageDirect.send) != message.getDirect())
			{
				holder = new ViewHolder();
				if(message.getDirect() == MessageDirect.receive)
					holder.direction = 1;
				else
					holder.direction = 2;
				convertView = createViewByMessage(message);
				if (chatType == ContentType.image) {
					try {
						holder.iv = ((ImageView) convertView.findViewById(R.id.iv_sendPicture));
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						holder.tv = (TextView) convertView.findViewById(R.id.percentage);
						holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
					} catch (Exception e) {

					}

				} else if (chatType == ContentType.text) {

					try {
						holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						// 这里是文字内容
						holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);

						holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
						holder.tvList = (LinearLayout) convertView.findViewById(R.id.ll_layout);
					} catch (Exception e) {

					}
				} else if (chatType == ContentType.voice) {
					try {
						holder.iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						holder.tv = (TextView) convertView.findViewById(R.id.tv_length);
						holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
						holder.iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
					} catch (Exception e) {

					}
				} else if (chatType == ContentType.location) {
					try {
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						holder.tv = (TextView) convertView.findViewById(R.id.tv_location);
						holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
					} catch (Exception e) {

					}
				} else if (chatType == ContentType.video) {
					try {
						holder.iv = ((ImageView) convertView.findViewById(R.id.chatting_content_iv));
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						holder.tv = (TextView) convertView.findViewById(R.id.percentage);
						holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.size = (TextView) convertView.findViewById(R.id.chatting_size_iv);
						holder.timeLength = (TextView) convertView.findViewById(R.id.chatting_length_iv);
						holder.playBtn = (ImageView) convertView.findViewById(R.id.chatting_status_btn);
						holder.container_status_btn = (LinearLayout) convertView.findViewById(R.id.container_status_btn);
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);

					} catch (Exception e) {

					}
				} else if (chatType == ContentType.file) {
					try {
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						holder.tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_name);
						holder.tv_file_size = (TextView) convertView.findViewById(R.id.tv_file_size);
						holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.tv_file_download_state = (TextView) convertView.findViewById(R.id.tv_file_state);
						holder.ll_container = (LinearLayout) convertView.findViewById(R.id.ll_file_container);
						// 这里是进度值
						holder.tv = (TextView) convertView.findViewById(R.id.percentage);
					} catch (Exception e) {

					}
					try {
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
					} catch (Exception e) {

					}
				}
				convertView.setTag(holder);
			}
			else
				holder = holder1;
		}

		// 如果是发送的消息，显示已读textview
		if (message.getDirect() == MessageDirect.send) {
			holder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
			holder.tv_delivered = (TextView) convertView.findViewById(R.id.tv_delivered);
			if (holder.tv_ack != null) {
				if (message.getStatus() == MessageStatus.receive_success) {
					if (holder.tv_delivered != null) {
						holder.tv_delivered.setVisibility(View.INVISIBLE);
					}
					holder.tv_ack.setVisibility(View.VISIBLE);
				} else {
					holder.tv_ack.setVisibility(View.INVISIBLE);
					// check and display msg delivered ack status
					if (holder.tv_delivered != null) {
						if (message.getStatus() == MessageStatus.send_success) {
							holder.tv_delivered.setVisibility(View.VISIBLE);
						} else {
							holder.tv_delivered.setVisibility(View.INVISIBLE);
						}
					}
				}
			}
		} else {
			try {
				// 发送已读回执
				message.setHaveRead(new BasicCallback() {
					@Override
					public void gotResult(int i, String s) {

					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//设置用户头像
		setUserAvatar(message, holder.iv_avatar);
		if (chatType == ContentType.image) {

		}
		else if (chatType == ContentType.text)
		{
			handleTextMessage(message, holder, position);
		}
		else if (chatType == ContentType.location)
		{

		}
		else if (chatType == ContentType.voice)
		{
			handleVoiceMessage(message, holder);
		}
		else if (chatType == ContentType.video)
		{

		}
		else if (chatType == ContentType.file)
		{

		}
		else
			handleTextMessage(message, holder, position);
		if (message.getDirect() == MessageDirect.send) {
			View statusView = convertView.findViewById(R.id.msg_status);
			if(statusView != null) {
				// 重发按钮点击事件
				statusView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 发送消息
						sendMsgInBackground(message, holder);
					}
				});
			}
		} else {
			if(wf.get() != null && wf.get() instanceof ChatActivity) {
				final String st = wf.get().getResources().getString(R.string.Into_the_blacklist);
				// 长按头像，移入黑名单
				holder.iv_avatar.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						if(wf.get() != null && wf.get() instanceof ChatActivity) {
							Intent intent = new Intent(wf.get(), AlertDialog.class);
							intent.putExtra("msg", st);
							intent.putExtra("cancel", true);
							intent.putExtra("position", position);
							((ChatActivity) wf.get()).startActivityForResult(intent, ChatActivity.REQUEST_CODE_ADD_TO_BLACKLIST);
						}
						return true;
					}
				});
			}
		}
		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
		if (position == 0) {
			timestamp.setText(Utils.getNewChatTime(message.getCreateTime()));
			timestamp.setVisibility(View.VISIBLE);
		} else {
			// 两条消息时间相差5分钟，显示时间
			Message prevMessage = getItem(position - 1);
			if (prevMessage != null && (message.getCreateTime() - prevMessage.getCreateTime()) < 5 * 60 * 1000) {
				timestamp.setVisibility(View.GONE);
			} else {
				timestamp.setText(Utils.getNewChatTime(message.getCreateTime()));
				timestamp.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	private View createViewByMessage(Message message) {
		if (ContentType.location == message.getContentType())
			return message.getDirect() == MessageDirect.receive ?
					inflater.inflate(R.layout.row_received_location,
					null) : inflater.inflate(
					R.layout.row_sent_location, null);
		else if(ContentType.image == message.getContentType())
			return message.getDirect() == MessageDirect.receive ?
					inflater.inflate(R.layout.row_received_picture,
					null) : inflater.inflate(
					R.layout.row_sent_picture, null);
		else if(ContentType.voice == message.getContentType())
			return message.getDirect() == MessageDirect.receive ?
					inflater.inflate(R.layout.row_received_voice,
					null) : inflater.inflate(
					R.layout.row_sent_voice, null);
		else if(ContentType.video == message.getContentType())
			return message.getDirect() == MessageDirect.receive ?
					inflater.inflate(R.layout.row_received_video,
					null) : inflater.inflate(
					R.layout.row_sent_video, null);
		else if(ContentType.file == message.getContentType())
			return message.getDirect() == MessageDirect.receive ?
					inflater.inflate(R.layout.row_received_file,
					null) : inflater.inflate(
					R.layout.row_sent_file, null);
		else
			return message.getDirect() == MessageDirect.receive ?
					inflater.inflate(R.layout.row_received_message,
					null) : inflater.inflate(
					R.layout.row_sent_message, null);
	}
	
	/**
	 * 显示用户头像
	 * @param message
	 * @param imageView
	 */
	private void setUserAvatar(final Message message, ImageView imageView){
		if(wf.get() != null && wf.get() instanceof ChatActivity) {
			if (message.getDirect() == MessageDirect.send) {
				//显示自己头像
				Glide.with(wf.get()).load(R.drawable.man_online).into(imageView);
			} else {
				Glide.with(wf.get()).load(R.drawable.man_online).into(imageView);
			}
		}
	}

	/**
	 * 文本消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 */
	private void handleTextMessage(Message message, ViewHolder holder, final int position) {
		if(wf.get() != null && wf.get() instanceof ChatActivity) {
			TextContent textContent = (TextContent) message.getContent();
			Spannable span = SmileUtils.getSmiledText(wf.get(), textContent.getText());
			// 设置内容
			holder.tv.setText(span, BufferType.SPANNABLE);
		}
		if (message.getDirect() == MessageDirect.send) {
			if(message.getStatus() == MessageStatus.send_success) {
				if(holder.pb != null)
					holder.pb.setVisibility(View.GONE);
				if(holder.staus_iv != null)
					holder.staus_iv.setVisibility(View.GONE);
			}
			else if(message.getStatus() == MessageStatus.send_fail) {
				if(holder.pb != null)
					holder.pb.setVisibility(View.GONE);
				if(holder.staus_iv != null)
					holder.staus_iv.setVisibility(View.VISIBLE);
			}
			else if(message.getStatus() == MessageStatus.send_going) {
				if(holder.pb != null)
					holder.pb.setVisibility(View.VISIBLE);
				if(holder.staus_iv != null)
					holder.staus_iv.setVisibility(View.GONE);
			}
//			else {
//				// 发送消息
//				sendMsgInBackground(message, holder);
//			}
		}
	}

	/**
	 * 发送消息
	 * @param message
	 * @param holder
	 */
	public void sendMsgInBackground(final Message message, final ViewHolder holder) {
		holder.staus_iv.setVisibility(View.GONE);
		holder.pb.setVisibility(View.VISIBLE);
		//对方的username
		String name = username;
		//对方所属应用的appkey，空默认为本应用
		String appkey = "";
		//是否保存离线
		boolean retainOfflineMsg = true;
		//是否显示通知
		boolean showNotification = true;
		//是否开启自定义接收方通知栏
		boolean enableCustomNotify = true;
		//是否需要对方发送已读回执
		boolean needReadReceipt = false;
		//通过username和appkey拿到会话对象，通过指定appkey可以创建一个和跨应用用户的会话对象，从而实现跨应用的消息发送
		Conversation mConversation = JMessageClient.getSingleConversation(name, appkey);
		if (mConversation == null) {
			mConversation = Conversation.createSingleConversation(name, appkey);
		}
		Message message1 = mConversation.createSendMessage(new TextContent(""), "");
		String description = "";
		if(message.getContentType() == ContentType.text) {
			//构造message content对象
			TextContent textContent = (TextContent) message.getContent();
			description = textContent.getText();
			//设置自定义的extra参数
			textContent.setStringExtra("", "");
			message1 = mConversation.createSendMessage(textContent, "");

		}
		else if(message.getContentType() == ContentType.voice) {
			try {
				VoiceContent voiceContent = (VoiceContent) message.getContent();
				description = "[语音]";
				if (!(new File(voiceContent.getLocalPath()).exists())) {
					return;
				}
				File fileMp3 = new File(voiceContent.getLocalPath());
				MediaPlayer player = new MediaPlayer();
				player.setDataSource(String.valueOf(fileMp3));
				player.prepare();
				int duration = player.getDuration();
				message1 = mConversation.createSendVoiceMessage(fileMp3, duration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		message1.setOnSendCompleteCallback(new BasicCallback() {
			@Override
			public void gotResult(int i, String s) {
				if (i == 0) {
					Toast.makeText(DemoApplication.getInstance(), "发送成功", Toast.LENGTH_SHORT).show();
					holder.pb.setVisibility(View.GONE);
					holder.staus_iv.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(DemoApplication.getInstance(), "发送失败", Toast.LENGTH_SHORT).show();
					holder.pb.setVisibility(View.GONE);
					holder.staus_iv.setVisibility(View.VISIBLE);
				}
				updateSendedView(message, holder);
			}
		});
		//上传进度
//            message1.setOnContentUploadProgressCallback(new ProgressUpdateCallback() {
//                @Override
//                public void onProgressUpdate(double v) {
//                    String progressStr = (int) (v * 100) + "%";
//                    mTv_progress.append("上传进度：" + progressStr + "\n");
//                }
//            });
		//设置消息发送时的一些控制参数
		MessageSendingOptions options = new MessageSendingOptions();
		options.setNeedReadReceipt(needReadReceipt);//是否需要对方用户发送消息已读回执
		options.setRetainOffline(retainOfflineMsg);//是否当对方用户不在线时让后台服务区保存这条消息的离线消息
		options.setShowNotification(showNotification);//是否让对方展示sdk默认的通知栏通知
		options.setCustomNotificationEnabled(enableCustomNotify);//是否需要自定义对方收到这条消息时sdk默认展示的通知栏中的文字
		if (enableCustomNotify) {
			options.setNotificationTitle(MySharePreferencesService.getInstance(
					DemoApplication.getInstance().getApplicationContext()).getcontectName("name"));//自定义对方收到消息时通知栏展示的title
			options.setNotificationAtPrefix("");//自定义对方收到消息时通知栏展示的@信息的前缀
			options.setNotificationText(description);//自定义对方收到消息时通知栏展示的text
		}
		//发送消息
		JMessageClient.sendMessage(message, options);
	}

	/**
	 * 语音消息
	 *
	 * @param message
	 * @param holder
	 */
	private void handleVoiceMessage(final Message message, final ViewHolder holder) {
		VoiceContent voiceContent = (VoiceContent) message.getContent();
		int len = voiceContent.getDuration();
		if (len > 0) {
			holder.tv.setText(voiceContent.getDuration() / 1000 + "\"");
			holder.tv.setVisibility(View.VISIBLE);
		} else {
			holder.tv.setVisibility(View.INVISIBLE);
		}
		holder.iv.setOnClickListener(new VoicePlayClickListener(message,
				holder.iv, holder.iv_read_status, this, (Activity) wf.get()));

		if (wf.get() != null && wf.get() instanceof ChatActivity) {

			if (((ChatActivity) wf.get()).playMsgId == message
					.getId() && VoicePlayClickListener.isPlaying) {
				AnimationDrawable voiceAnimation;
				if (message.getDirect() == MessageDirect.receive) {
					holder.iv.setImageResource(R.drawable.voice_from_icon);
				} else {
					holder.iv.setImageResource(R.drawable.voice_to_icon);
				}
				voiceAnimation = (AnimationDrawable) holder.iv.getDrawable();
				voiceAnimation.start();
			} else {
				if (message.getDirect() == MessageDirect.receive) {
					holder.iv.setImageResource(R.drawable.chatfrom_voice_playing);
				} else {
					holder.iv.setImageResource(R.drawable.chatto_voice_playing);
				}
			}
		}

		if (message.getDirect() == MessageDirect.receive) {
			if (message.haveRead()) {
				// 隐藏语音未听标志
				holder.iv_read_status.setVisibility(View.INVISIBLE);
			} else {
				holder.iv_read_status.setVisibility(View.VISIBLE);
			}
			return;
		}

		// until here, deal with send voice msg
		if (MessageStatus.send_success == message.getStatus()) {
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
		} else if (MessageStatus.send_fail == message.getStatus()) {
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
		} else if (MessageStatus.send_going == message.getStatus()) {
			holder.pb.setVisibility(View.VISIBLE);
			holder.staus_iv.setVisibility(View.GONE);
		}
//		else
//			sendMsgInBackground(message, holder);
	}

	/**
	 * 更新ui上消息发送状态
	 * 
	 * @param message
	 * @param holder
	 */
	private void updateSendedView(final Message message, final ViewHolder holder) {
		if (wf.get() != null && wf.get() instanceof ChatActivity) {
			((ChatActivity) wf.get()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (message.getStatus() == MessageStatus.send_success) {
						holder.pb.setVisibility(View.GONE);
						holder.staus_iv.setVisibility(View.VISIBLE);
					}
					else if (message.getStatus() == MessageStatus.send_fail) {
						holder.pb.setVisibility(View.GONE);
						holder.staus_iv.setVisibility(View.VISIBLE);
					}
					notifyDataSetChanged();
				}
			});
		}
	}

	public static class ViewHolder {
		int direction;
		ImageView iv;
		TextView tv;
		ProgressBar pb;
		ImageView staus_iv;
		ImageView iv_avatar;
		TextView tv_usernick;
		ImageView playBtn;
		TextView timeLength;
		TextView size;
		LinearLayout container_status_btn;
		LinearLayout ll_container;
		ImageView iv_read_status;
		// 显示已读回执状态
		TextView tv_ack;
		// 显示送达回执状态
		TextView tv_delivered;

		TextView tv_file_name;
		TextView tv_file_size;
		TextView tv_file_download_state;
		
		TextView tvTitle;
		LinearLayout tvList;
	}
}