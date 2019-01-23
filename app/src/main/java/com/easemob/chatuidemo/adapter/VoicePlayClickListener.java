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
package com.easemob.chatuidemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dssm.esc.R;
import com.easemob.chatuidemo.activity.ChatActivity;

import java.io.File;

import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.enums.MessageStatus;
import cn.jpush.im.android.api.model.Message;


public class VoicePlayClickListener implements View.OnClickListener {
	Message message;
	VoiceContent voiceContent;
	ImageView voiceIconView;

	private AnimationDrawable voiceAnimation = null;
	MediaPlayer mediaPlayer = null;
	ImageView iv_read_status;
	Activity activity;
	private BaseAdapter adapter;

	public static boolean isPlaying = false;
	public static VoicePlayClickListener currentPlayListener = null;

	public VoicePlayClickListener(Message message, ImageView v, ImageView iv_read_status, BaseAdapter adapter, Activity activity) {
		this.message = message;
		voiceContent = (VoiceContent) message.getContent();
		this.iv_read_status = iv_read_status;
		this.adapter = adapter;
		voiceIconView = v;
		this.activity = activity;
	}

	public void stopPlayVoice() {
		voiceAnimation.stop();
		if (message.getDirect() == MessageDirect.receive) {
			voiceIconView.setImageResource(R.drawable.chatfrom_voice_playing);
		} else {
			voiceIconView.setImageResource(R.drawable.chatto_voice_playing);
		}
		// stop play voice
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		isPlaying = false;
		((ChatActivity) activity).playMsgId = 0;
		adapter.notifyDataSetChanged();
	}

	public void playVoice(String filePath) {
		if (!(new File(filePath).exists())) {
			return;
		}
		((ChatActivity) activity).playMsgId = message.getId();
		AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

		mediaPlayer = new MediaPlayer();
		if (true) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			audioManager.setSpeakerphoneOn(true);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		} else {
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			// 把声音设定成Earpiece（听筒）出来，设定为正在通话中
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
		}
		try {
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mediaPlayer.release();
					mediaPlayer = null;
					stopPlayVoice(); // stop animation
				}
			});
			isPlaying = true;
			currentPlayListener = this;
			mediaPlayer.start();
			showAnimation();

			// 如果是接收的消息
			if (message.getDirect() == MessageDirect.receive) {
				iv_read_status.setVisibility(View.INVISIBLE);
			}

		} catch (Exception e) {

		}
	}

	// show the voice playing animation
	private void showAnimation() {
		// play voice, and start animation
		if (message.getDirect() == MessageDirect.receive) {
			voiceIconView.setImageResource(R.drawable.voice_from_icon);
		} else {
			voiceIconView.setImageResource(R.drawable.voice_to_icon);
		}
		voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
		voiceAnimation.start();
	}

	@Override
	public void onClick(View v) {
		String st = activity.getResources().getString(R.string.Is_download_voice_click_later);
		if (isPlaying) {
			if (((ChatActivity) activity).playMsgId == message.getId()) {
				currentPlayListener.stopPlayVoice();
				return;
			}
			currentPlayListener.stopPlayVoice();
		}

		if (message.getDirect() == MessageDirect.send) {
			// for sent msg, we will try to play the voice file directly
			playVoice(voiceContent.getLocalPath());
		} else {
			if (message.getStatus() == MessageStatus.receive_success) {
				File file = new File(voiceContent.getLocalPath());
				if (file.exists() && file.isFile())
					playVoice(voiceContent.getLocalPath());
			} else if (message.getStatus() == MessageStatus.receive_going) {

			} else if (message.getStatus() == MessageStatus.receive_fail) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
                        voiceContent.downloadVoiceFile(message,
                                new DownloadCompletionCallback() {
                                    @Override
                                    public void onComplete(int i, String s, File file) {
                                        if (file.exists() && file.isFile())
                                            playVoice(file.getPath());
                                    }
                                });
					    return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						adapter.notifyDataSetChanged();
					}

				}.execute();

			}
		}
	}
}