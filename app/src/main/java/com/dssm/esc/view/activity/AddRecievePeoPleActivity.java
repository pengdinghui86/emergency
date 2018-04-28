package com.dssm.esc.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;

import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 新增联系人界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-15
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class AddRecievePeoPleActivity extends BaseActivity implements
		OnClickListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 确定 */
	@ViewInject(id = R.id.tv_actionbar_editData)
	private TextView mSelectConfirm;
	/** 返回 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView mBack;
	/** 1,应急;2,演练 */
	// private String tag;
	/** 姓名 */
	@ViewInject(id = R.id.name)
	private EditText name;
	/** 电话 */
	@ViewInject(id = R.id.phonenumber)
	private EditText phonenumber;
	/** 邮箱 */
	@ViewInject(id = R.id.email)
	private EditText email;
	/** 要传送过去的数据 */
	private String data;
	/** name */
	private String n = "";
	/** phonenumber */
	private String p = "";
	/** email */
	private String e = "";
	private String tag = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addrecieve);
		View findViewById = findViewById(R.id.addrecieve);
		findViewById.setFitsSystemWindows(true);
		Bundle bundle = getIntent().getExtras();
		tag = bundle.getString("tag");
		if (tag.equals("2")) {
			n = bundle.getString("name");
			p = bundle.getString("phone");
			e = bundle.getString("email");
		}
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		name.setText(n);
		phonenumber.setText(p);
		email.setText(e);
		mSelectTypeTitle.setText("新增接收人");
		mSelectConfirm.setVisibility(View.VISIBLE);
		mSelectConfirm.setText(R.string.sure);
		mBack.setVisibility(View.VISIBLE);
		mSelectConfirm.setOnClickListener(this);

	}

	boolean email2 = false;
	boolean mobileNo = false;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_actionbar_editData:
			Intent intent = new Intent();
			String n = name.getText().toString().trim();
			String	p = phonenumber.getText().toString().trim();
			String	e = email.getText().toString().trim();

			if (!n.equals("")) {

				if (p.equals("") && e.equals("")) {
					ToastUtil.showToast(AddRecievePeoPleActivity.this,
							"手机号或邮箱不能为空");
				} else if (p.equals("") && !e.equals("")) {
					email2 = Utils.getInstance().isEmail(e);
					data = n + "," + e;
					if (email2 == false) {
						ToastUtil.showToast(AddRecievePeoPleActivity.this,
								"邮箱格式不正确");
					} else {
						intent.putExtra("data", data);
						AddRecievePeoPleActivity.this.setResult(RESULT_OK,
								intent);
						AddRecievePeoPleActivity.this.finish();
					}
				} else if (!p.equals("") && e.equals("")) {
					data = n + "," + p;
					mobileNo = Utils.getInstance().isMobileNo(p);
					if (mobileNo == false) {
						ToastUtil.showToast(AddRecievePeoPleActivity.this,
								"手机号格式不正确");

					} else {
						intent.putExtra("data", data);
						AddRecievePeoPleActivity.this.setResult(RESULT_OK,
								intent);
						AddRecievePeoPleActivity.this.finish();
					}
				} else if (!p.equals("") && !e.equals("")) {
					data = n + "," + p + "," + e;
					mobileNo = Utils.getInstance().isMobileNo(p);
					email2 = Utils.getInstance().isEmail(e);
					if (mobileNo == true && email2 == true) {
						intent.putExtra("data", data);
						AddRecievePeoPleActivity.this.setResult(RESULT_OK,
								intent);
						AddRecievePeoPleActivity.this.finish();
					} else if (mobileNo == false && email2 == true) {
						ToastUtil.showToast(AddRecievePeoPleActivity.this,
								"手机号格式不正确");
					} else if (mobileNo == true && email2 == false) {
						ToastUtil.showToast(AddRecievePeoPleActivity.this,
								"邮箱格式不正确");
					} else if (email2 == false && mobileNo == false) {
						ToastUtil.showToast(AddRecievePeoPleActivity.this,
								"手机号,邮箱格式不正确");
					}
				}
			} else {
				ToastUtil.showToast(AddRecievePeoPleActivity.this, "姓名必填");
			}

			break;

		default:
			break;
		}
	}
}
