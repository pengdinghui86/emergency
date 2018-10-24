package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.dssm.esc.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 文本输入界面
 */

@ContentView(R.layout.activity_input_text)
public class InputTextActivity extends BaseActivity{
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;
	/** 文本输入框 */
	@ViewInject(R.id.input_text_et)
	private EditText input_text_et;
	/** 确定 */
	@ViewInject(R.id.input_text_bt)
	private Button input_text_bt;
	/** 传递过来的标题 */
	private String title = "输入";
	/** 传递过来的文本内容 */
	private String content = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.input_text_ll);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		if(intent.getStringExtra("title") != null &&
				!"".equals(intent.getStringExtra("title"))) {
			title = intent.getStringExtra("title");
		}
		if(intent.getStringExtra("content") != null &&
				!"".equals(intent.getStringExtra("content"))) {
			content = intent.getStringExtra("content");
		}
		initView();
	}

	private void initView() {
		mSelectTypeTitle.setText(title);
		mBack.setVisibility(View.VISIBLE);
		input_text_et.setText(content);
	}

	@Event(type = OnClickListener.class,
			value = {R.id.input_text_bt, R.id.iv_actionbar_back})
	private void onClick(View v){
		switch (v.getId()) {
			case R.id.input_text_bt:
				Intent intent = new Intent();
				content = input_text_et.getText().toString();
				intent.putExtra("content", content);
				InputTextActivity.this.setResult(RESULT_OK, intent);
				InputTextActivity.this.finish();
				break;

			case R.id.iv_actionbar_back:
				InputTextActivity.this.finish();
				break;
			default:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
