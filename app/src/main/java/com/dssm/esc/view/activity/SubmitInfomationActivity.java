package com.dssm.esc.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;

import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 提交信息界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-14
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class SubmitInfomationActivity extends BaseActivity implements
		OnClickListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView back;
	/** 确定 */
	@ViewInject(id = R.id.tv_actionbar_editData)
	private TextView mSelectConfirm;
	/** 1，预案执行5,控制中心 */
	private String tag;
	/** 提交信息的编辑框 */
	@ViewInject(id = R.id.etc_submit)
	private EditText etc_submit;
	/** 提交信息的编辑框的删除按钮 */
	@ViewInject(id = R.id.img_delete_submit)
	private ImageView img_delete_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submitinfomation);
		View findViewById = findViewById(R.id.submitinfomation);
		findViewById.setFitsSystemWindows(true);
		tag = getIntent().getStringExtra("tag");
		initView();
	}

	private void initView() {
		mSelectConfirm.setVisibility(View.VISIBLE);
		mSelectConfirm.setText(R.string.sure);
		if (tag.equals("5")) {
			back.setVisibility(View.INVISIBLE);
			back.setClickable(false);
		} else {

			back.setVisibility(View.VISIBLE);
		}
		if (tag.equals("1")) {
			title.setText("完成情况");
			// } else if (tag.equals("2")) {
			// title.setText("演练-提交信息");
		} else if (tag.equals("5")) {
			title.setText("中止原因");
		}
		mSelectConfirm.setOnClickListener(this);
		etc_submit.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					img_delete_submit.setVisibility(View.GONE);
				} else {
					img_delete_submit.setVisibility(View.VISIBLE);
				}
			}
		});
		img_delete_submit.setOnClickListener(this);
	}

	private String getCode = null;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_actionbar_editData:// 确定
			final Intent intent = new Intent();
			if (tag.equals("5")) {
				if (!etc_submit.getText().toString().trim().equals("")) {
					View view=LayoutInflater.from(SubmitInfomationActivity.this).inflate(R.layout.editcode, null);
					final EditText et = (EditText) view.findViewById(R.id.vc_code);
					getCode = Utils.getInstance().code();
//					getCode = String.valueOf(Math.random() * 9000 + 1000);
					new AlertDialog.Builder(SubmitInfomationActivity.this)
							.setTitle("验证码：" + getCode)
							.setIcon(android.R.drawable.ic_dialog_info)
							.setView(view)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											String v_code = et.getText()
													.toString().trim();
											if (!v_code.equals(getCode)) {
												Toast.makeText(
														SubmitInfomationActivity.this,
														"验证码错误", Toast.LENGTH_SHORT).show();
											} else {

												intent.putExtra("info",
														etc_submit.getText()
																.toString()
																.trim());
												SubmitInfomationActivity.this
														.setResult(RESULT_OK,
																intent);
												SubmitInfomationActivity.this
														.finish();
											}
										}
									}).setNegativeButton("取消", null).show();

				} else {
					ToastUtil.showToast(SubmitInfomationActivity.this,
							"请输入中止原因");
				}
			} else {

				intent.putExtra("info", etc_submit.getText().toString().trim());
				SubmitInfomationActivity.this.setResult(RESULT_OK, intent);
				SubmitInfomationActivity.this.finish();
			}
			break;

		case R.id.img_delete_submit:// 编辑框的删除按钮
			etc_submit.setText("");
			break;
		}
	}
}
