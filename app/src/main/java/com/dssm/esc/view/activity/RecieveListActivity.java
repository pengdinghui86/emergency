package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.RecieveListEntity;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.ReceveListAdapter;

import net.tsz.afinal.annotation.view.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 接受列表界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-15
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class RecieveListActivity extends BaseActivity implements
		OnClickListener,MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 返回 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView mBack;
	/** ListView */
	@ViewInject(id = R.id.recieve_listview)
	private ListView mListView;
	/** 类型数据 */
	private ArrayList<RecieveListEntity> list = new ArrayList<RecieveListEntity>();
	/** 适配器 */
	//private RecieveListAdapter mSelectAdapter;
	private ReceveListAdapter mSelectAdapter;
	
	/** 添加*/
	@ViewInject(id = R.id.submit)
	private TextView submit;
	/** 提交 */
	@ViewInject(id = R.id.ok)
	private TextView ok;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ArrayList<RecieveListEntity> result = (ArrayList<RecieveListEntity>) msg.obj;

				list.clear();
				list.addAll(result);
				
				mSelectAdapter.notifyDataSetChanged();

				break;
		
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recieve);
		View findViewById = findViewById(R.id.recieve);
		findViewById.setFitsSystemWindows(true);
		list= (ArrayList<RecieveListEntity>) getIntent().getExtras().getSerializable("list");
		initView();
		lvListener();
	}

	private void initView() {
			mSelectTypeTitle.setText( "接收列表");
		mBack.setVisibility(View.VISIBLE);
		/**
		 * 为Adapter准备数据
		 */
		initData();
		submit.setOnClickListener(this);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("arrlist", (Serializable) mSelectAdapter.arraylist);
				intent.putExtras(bundle);
				RecieveListActivity.this.setResult(RESULT_OK, intent);
				RecieveListActivity.this.finish();
			}
		});
		
//		setNetListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub

		
		mSelectAdapter = new ReceveListAdapter(
				RecieveListActivity.this, list);
		mListView.setAdapter(mSelectAdapter);

	}

	/**
	 * 监听ListView
	 */
	private void lvListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

//				ViewHolderRecieve holder = (ViewHolderRecieve) view.getTag();
//				holder.checkBox.toggle();
//				RecieveListAdapter.getIsSelected().put(position,
//						holder.checkBox.isChecked());
//
//				if (holder.checkBox.isChecked()) {
//					type.add(list.get(position));
//				} else {
//					type.remove(list.get(position));
//				}
				Intent intent = new Intent(RecieveListActivity.this,AddRecievePeoPleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("tag", "2");
				bundle.putString("name", list.get(position).getReceiver());
				bundle.putString("phone", list.get(position).getReceiverPhone());
				bundle.putString("email", list.get(position).getReceiverEmail());
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);
				list.remove(position);
				mSelectAdapter.notifyDataSetChanged();
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ok:// 确定
			//if (list.size() > 0) {
				//Intent intent = new Intent();
				//Bundle bundle = new Bundle();
				//bundle.putSerializable("arrlist", (Serializable)list);
				//intent.putExtras(bundle);
				//RecieveListActivity.this.setResult(RESULT_OK, intent);
				//RecieveListActivity.this.finish();
//			}else {
//				ToastUtil.showToast(RecieveListActivity.this, "请添加联系人");
//			}
				
			break;

		case R.id.submit:// 添加
			Intent intent2 = new Intent(RecieveListActivity.this,
					AddRecievePeoPleActivity.class);
			//intent2.putExtra("tag", tag);
			Bundle bundle2 = new Bundle();
			bundle2.putString("tag", "1");
			intent2.putExtras(bundle2);
			startActivityForResult(intent2, 0);
			
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (data != null && resultCode == RESULT_OK) {

				Log.i("data", data.getStringExtra("data"));
				
				String[] split = data.getStringExtra("data").split(",");
				ArrayList<RecieveListEntity> result2 = new ArrayList<RecieveListEntity>();
				RecieveListEntity entity = new RecieveListEntity();
				if (split.length>=2) {
					entity.setReceiver(split[0]);
					if (split.length==3) {
						
						entity.setReceiverPhone(split[1]);
						entity.setReceiverEmail(split[2]);
					}else if (split.length==2) {
						
						boolean b = Utils.getInstance().isMobileNo(split[1]);
						if (b) {
							entity.setReceiverPhone(split[1]);
							entity.setReceiverEmail("");
						}else {
							entity.setReceiverPhone("");
							entity.setReceiverEmail(split[1]);
						}
					}
					entity.setIschecked(false);
					result2.add(entity);
					list.addAll(result2);
					mSelectAdapter = new ReceveListAdapter(
							RecieveListActivity.this, list);
					mListView.setAdapter(mSelectAdapter);
					mSelectAdapter.notifyDataSetChanged();
				}else {
				ToastUtil.showToast(RecieveListActivity.this, "数据填写不完整");
				}
				//mSelectAdapter.setList(list);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}
}
