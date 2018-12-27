package com.dssm.esc.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dssm.esc.R;
import com.dssm.esc.model.entity.message.HistoryNoticeEntity;
import com.dssm.esc.util.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 通知历史详情界面
 */
@ContentView(R.layout.activity_history_notice_detail)
public class HistoryNoticeDetailActivity extends BaseActivity implements View.OnClickListener {

	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 消息类型 */
	@ViewInject(R.id.message_listview_iv_type)
	private ImageView message_iv_type;
	/** 消息名称 */
	@ViewInject(R.id.message_listview_tv_name)
	private TextView message_tv_name;
	/** 消息发送时间 */
	@ViewInject(R.id.message_listview_tv_time)
	private TextView message_tv_time;
	/** 消息内容 */
	@ViewInject(R.id.message_listview_tv_content)
	private TextView message_tv_content;
	/** 消息接收人 */
	@ViewInject(R.id.history_notice_detail_tv_receiver)
	private TextView message_tv_receiver;

	private HistoryNoticeEntity noticeInfo = new HistoryNoticeEntity();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.history_notice);
		findViewById.setFitsSystemWindows(true);
		initView();
		initData();
	}

	private void initView() {
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		title.setText("通知详情");
		Bundle bundle = getIntent().getExtras();
		noticeInfo = (HistoryNoticeEntity) bundle.getSerializable("noticeInfo");
	}

	private void initData()
	{
		//0系统，1邮件，2短信，3APP
		if ("0".equals(noticeInfo.getSendType())) {
			message_iv_type.setImageResource(R.drawable.system_notice);
			message_tv_name.setText("系统");
		} else if("1".equals(noticeInfo.getSendType())) {
			message_iv_type.setImageResource(R.drawable.email_notice);
			message_tv_name.setText("邮件");
		} else if("2".equals(noticeInfo.getSendType())) {
			message_iv_type.setImageResource(R.drawable.message_notice);
			message_tv_name.setText("短信");
		} else if("3".equals(noticeInfo.getSendType())) {
			message_iv_type.setImageResource(R.drawable.app_notice);
			message_tv_name.setText("APP");
		}
		String creatTime = noticeInfo.getCreateTime();
		long time = Utils.getStringToDate(creatTime, "yyyy-MM-dd HH:mm:ss");
		message_tv_time.setText(Utils.getNewChatTime(time));
		message_tv_content.setText(noticeInfo.getMessage());
		message_tv_receiver.setText(noticeInfo.getReceiver());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.iv_actionbar_back:
				finish();
				break;
		}
	}
}
