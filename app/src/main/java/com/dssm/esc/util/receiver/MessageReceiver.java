package com.dssm.esc.util.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dssm.esc.util.ActivityCollector;
import com.dssm.esc.util.event.PushMessageEvent;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.MainActivity;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.activity.LoginActivity;
import com.easemob.chatuidemo.activity.SplashActivity;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class MessageReceiver extends XGPushBaseReceiver {
	public static final String LogTag = "TPushReceiver";
//	private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
	public MessageReceiver() {
	}

	private void show(Context context, String text) {
		// Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
		XGNotification notific = new XGNotification();
		notific.setMsg_id(notifiShowedRlt.getMsgId());
		notific.setTitle(notifiShowedRlt.getTitle());
		notific.setContent(notifiShowedRlt.getContent());
		// notificationActionType==1为Activity，2为url，3为intent
		notific.setNotificationActionType(notifiShowedRlt
				.getNotificationActionType());
		// Activity,url,intent都可以通过getActivity()获得
		notific.setActivity(notifiShowedRlt.getActivity());
//		notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//				.format(Calendar.getInstance().getTime()));
//		NotificationService.getInstance(context).save(notific);
//		context.sendBroadcast(intent);
		String msgType = "";
		String customContent = notifiShowedRlt.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("msgType")) {
					msgType = obj.getString("msgType");
					Intent intent = new Intent("com.dssm.esc.RECEIVER");
					intent.putExtra("msgType", msgType);
					DemoApplication.getInstance().sendBroadcast(intent);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "反注册成功";
		} else {
			text = "反注册失败" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"设置成功";
		} else {
			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"删除成功";
		} else {
			text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		Intent intent = new Intent(context, MainActivity.class);
		ActivityCollector.finishSplashActivity();
		if (context.getPackageManager().resolveActivity(intent, 0) == null) {
			// 说明系统中不存在这个activity
			intent.setClass(context, SplashActivity.class);
			intent.putExtra("mainActivity", "unLive");
		}
		else {
			intent.setClass(context, SplashActivity.class);
			intent.putExtra("mainActivity", "live");
		}
		String text = "";
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
			text = "通知被打开 :" + message;
			Log.i("onFailure", text);
			String msgType = "";
			// 判断是否从推送通知栏打开的
			if (message != null) {
				// 获取自定义key-value
				String customContent = message.getCustomContent();
				// 拿到数据自行处理
				if (customContent != null && customContent.length() != 0) {
					try {
						JSONObject obj = new JSONObject(customContent);
						// key1为前台配置的key
						if (!obj.isNull("msgType")) {
							msgType = obj.getString("msgType");
							Log.i("onFailure", "msgType" + msgType);
						}
					} catch (JSONException e) {
						Log.i("onFailure", ":" + e.toString());
						e.printStackTrace();
					}
				}
			}
			intent.putExtra("msgType", msgType);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Log.i("onFailure", "startActivity_splashActivity");
			context.startActivity(intent);
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
			text = "通知被清除 :" + message;
		}

		// 获取自定义key-value
//		String customContent = message.getCustomContent();
//		if (customContent != null && customContent.length() != 0) {
//			try {
//				JSONObject obj = new JSONObject(customContent);
//				// key1为前台配置的key
//				if (!obj.isNull("msgType")) {// 后台直接定义跳到MainActivity
//					String msgType = obj.getString("msgType");
//					//MainActivity发送通知，让其显示MessageFragment界面
//					EventBus.getDefault().post(new mainEvent("t"));
//					//给MessageFragment发送通知
//					EventBus.getDefault().post(new PushMessageEvent(Integer.parseInt(msgType)));
//				}
//				// ...
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
		// APP自主处理的过程。。。
		Log.d(LogTag, text);
		show(context, text);
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
		// TODO Auto-generated method stub
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "注册成功";
			// 在这里拿token
			String token = message.getToken();
		} else {
			text = message + "注册失败，错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);
	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		String text = "收到消息:" + message.toString();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理消息的过程...
		Log.d(LogTag, text);
		show(context, text);
	}

}
