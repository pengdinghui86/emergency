package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;
import com.dssm.esc.model.entity.emergency.PlanNameRowEntity;
import com.dssm.esc.model.entity.emergency.PlanNameSelectEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.ListvCheckboxMulselectAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * 预案名称（多选）
 */
@ContentView(R.layout.activity_plan_name)
public class PlanNameActivity extends BaseActivity implements OnClickListener,
		MainActivity.onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 确定 */
	@ViewInject(R.id.tv_actionbar_editData)
	private TextView mSelectConfirm;
	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;
	/** ListView */
	@ViewInject(R.id.planname_type_listview)
	private ListView mListView;
	private PlanNameSelectEntity entity;
	/** 类型数据的数据源 */
	private List<PlanNameRowEntity> list = new ArrayList<PlanNameRowEntity>();
	/** 适配器 */
	private ListvCheckboxMulselectAdapter mSelectAdapter = null;
	/** 被选中的list */
	private ArrayList<PlanNameRowEntity> type = new ArrayList<PlanNameRowEntity>();
	/** 1,应急 */
	/** 1,预案执行;2,添加评估 */
	private String tags;
	/** 预案执行的名称类型1=默认2=其他3=分类预案4=可选预案 */
	private int plantags;
	@ViewInject(R.id.emptytv)
	private TextView emptytv;
	/** 预案名称 */
	private String name = "";
	private List<PlanNameRowEntity> selectedIds;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<PlanNameRowEntity> result = (List<PlanNameRowEntity>) msg.obj;
			switch (msg.what) {
			case 0:
				if (result == null || result.size() == 0)
					return;
				list.clear();
				list.addAll(result);
				if (selectedIds != null && selectedIds.size() > 0) {
					list.clear();
					list.addAll(selectedIds);

				}
				mSelectAdapter = new ListvCheckboxMulselectAdapter(
						PlanNameActivity.this, list);
				// if (tags.equals("2")) {
				// } else if (tags.equals("1")) {
				// mSelectAdapter = new ListvCheckboxMulselectAdapter(
				// PlanNameActivity.this, list);
				// }
				mListView.setAdapter(mSelectAdapter);
				mSelectAdapter.notifyDataSetChanged();

				break;

			default:
				break;
			}
		};
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

//		setContentView(R.layout.activity_plan_name);
		View findViewById = findViewById(R.id.plan_name);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		Bundle bundleExtra = intent.getExtras();
		tags = bundleExtra.getString("tags");
		plantags = bundleExtra.getInt("plantags", 0);
		selectedIds = (ArrayList<PlanNameRowEntity>) bundleExtra
				.getSerializable("arrlist");
		//可选预案
		if(plantags == 4)
		{
			list = (ArrayList<PlanNameRowEntity>) bundleExtra
					.getSerializable("list");
		}
		initView();
		lvListener();
		mSelectConfirm.setOnClickListener(this);

	}

	private void initView() {
		// TODO Auto-generated method stub
		mSelectConfirm.setVisibility(View.VISIBLE);
		mSelectConfirm.setText(R.string.sure);
		mBack.setVisibility(View.VISIBLE);
		mListView.setEmptyView(emptytv);
		initData(plantags);

		if (tags.equals("1")) {
			if (plantags == 1) {
				mSelectTypeTitle.setText("选择参考预案");
			} else if (plantags == 2) {
				mSelectTypeTitle.setText("选择其他预案");
			} else if (plantags == 3) {
				mSelectTypeTitle.setText("选择分类预案");
			} else if (plantags == 4) {
				mSelectTypeTitle.setText("选择可选预案");
			}

			/** 为Adapter准备数据 */
		} else if (tags.equals("2")) {
			if (plantags == 1) {
				mSelectTypeTitle.setText("选择参考预案");
			} else if (plantags == 2) {
				mSelectTypeTitle.setText("选择其他预案");
			} else if (plantags == 3) {
				mSelectTypeTitle.setText("选择分类预案");
			}

		}
	}

	private UserSeviceImpl.UserSeviceImplListListenser listListener = new UserSeviceImpl.UserSeviceImplListListenser() {

		@Override
		public void setUserSeviceImplListListenser(Object object,
				String stRerror, String Exceptionerror) {
			// TODO Auto-generated method stub
			List<PlanNameRowEntity> list = null;
			if (object != null) {
				PlanNameSelectEntity entity = (PlanNameSelectEntity) object;
				list = (ArrayList<PlanNameRowEntity>) entity
						.getRows();

			} else if (stRerror != null) {
				list = new ArrayList<PlanNameRowEntity>();

			} else if (Exceptionerror != null) {
				list = new ArrayList<PlanNameRowEntity>();
				ToastUtil.showToast(PlanNameActivity.this,
						Const.NETWORKERROR);
			}

			Message message = new Message();
			message.obj = list;
			message.what = 0;
			handler.sendMessage(message);
//						if (Utils.getInstance().progressDialog.isShowing()) {
			Utils.getInstance().hideProgressDialog();
//						}
		}
	};

	/**
	 * 获取预案列表
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private void getPlanlist() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		Utils.getInstance().showProgressDialog(PlanNameActivity.this, "",
				Const.LOAD_MESSAGE);
		// 解决传到服务器的中文字符串是乱码
		Control.getinstance().getUserSevice().getSearchPlanList(
				URLEncoder.encode(URLEncoder.encode(name), "UTF-8"), "", listListener);

	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser emergencySeviceImplListListenser = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			List<PlanNameRowEntity> list = null;
			if (object != null) {
				if (plantags == 2) {

					PlanNameSelectEntity entity = (PlanNameSelectEntity) object;
					list = (ArrayList<PlanNameRowEntity>) entity
							.getRows();

				} else if (plantags == 1 || plantags == 3) {
					list = (ArrayList<PlanNameRowEntity>) object;
					// Message message = new Message();
					// message.obj = list;
					// message.what = 0;
					// handler.sendMessage(message);
				}
			} else if (stRerror != null) {
				list = new ArrayList<PlanNameRowEntity>();

			} else if (Exceptionerror != null) {
				list = new ArrayList<PlanNameRowEntity>();
				ToastUtil.showToast(PlanNameActivity.this,
						Const.NETWORKERROR);
			}

			Message message = new Message();
			message.obj = list;
			message.what = 0;
			handler.sendMessage(message);
//							if (Utils.getInstance().progressDialog.isShowing()) {
			Utils.getInstance().hideProgressDialog();
//							}
		}
	};

	private void initData(final int plantags) {
		// TODO Auto-generated method stub

		if (list != null && list.size() == 0) {// 只访问一次网络
			Utils.getInstance().showProgressDialog(PlanNameActivity.this, "",
					Const.LOAD_MESSAGE);
			Control.getinstance().getEmergencyService().getPlanName(plantags, "", emergencySeviceImplListListenser);
		} else if (list != null && list.size() > 0) {
			List<PlanNameRowEntity> result = new ArrayList<>(list);
			Message message = new Message();
			message.obj = result;
			message.what = 0;
			handler.sendMessage(message);
		}
	}

	/**
	 * 监听ListView
	 */
	private void lvListener() {
		for (int i = 0; i < list.size(); i++) {
			list.clear();
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ListvCheckboxMulselectAdapter.ViewHolderPlan holder = (ListvCheckboxMulselectAdapter.ViewHolderPlan) view.getTag();
				PlanNameRowEntity planNameRowEntity = list.get(position);
				if (tags.equals("2")) {// 添加评估

					holder.checkBox.toggle();
					planNameRowEntity.setSelect(holder.checkBox.isChecked());
					mSelectAdapter.notifyDataSetChanged();
				} else if (tags.equals("1")) {// 预案启动

					if (planNameRowEntity.getHasStartAuth().equals("true")) {
						holder.checkBox.toggle();
						planNameRowEntity.setSelect(holder.checkBox.isChecked());
						mSelectAdapter.notifyDataSetChanged();
					} else if (planNameRowEntity.getHasStartAuth().equals(
							"false")) {
						ToastUtil.showToast(context, "您对该预案没有启动权限！");
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_actionbar_editData:
			Intent intent = new Intent();
			int count = 0;
			if (mSelectAdapter != null && mSelectAdapter.arraylist != null
					&& mSelectAdapter.arraylist.size() > 0) {
				for (int i = 0; i < mSelectAdapter.arraylist.size(); i++) {
					if (!mSelectAdapter.arraylist.get(i).isSelect()) {
						count++;
					}
				}
				if (count != mSelectAdapter.arraylist.size()) {
					intent.putExtra("arrlist",
							(Serializable) mSelectAdapter.arraylist);
				}
				// Bundle bundle = new Bundle();
				// if (mSelectAdapter != null && mSelectAdapter.arraylist !=
				// null
				// && mSelectAdapter.arraylist.size() > 0) {
				// bundle.putSerializable("arrlist",
				// (Serializable) mSelectAdapter.arraylist);
				// }
				// intent.putExtras(bundle);
			}
			PlanNameActivity.this.setResult(RESULT_OK, intent);
			PlanNameActivity.this.finish();
			break;

		case R.id.iv_actionbar_back:
			PlanNameActivity.this.finish();

			break;

		}
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData(plantags);
	}

}
