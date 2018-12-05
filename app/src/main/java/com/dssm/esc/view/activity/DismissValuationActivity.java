package com.dssm.esc.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.adapter.LeftSlideEventAdapter;
import com.dssm.esc.view.widget.AutoListView;
import com.dssm.esc.view.widget.RefreshLinearLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 驳回事件页面
 */
@ContentView(R.layout.activity_dismissvaluation)
public class DismissValuationActivity extends BaseActivity implements
		MainActivity.onInitNetListener, LeftSlideEventAdapter.IonSlidingViewClickListener{
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView title;
	/** 返回按钮 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	@ViewInject(R.id.dismissv_recyclerView)
	private RecyclerView mRecyclerView;
	/** 适配器 */
	private LeftSlideEventAdapter adapter;
	/** 下拉刷新控件 */
	@ViewInject(R.id.dismissv_refreshLinearLayout)
	private RefreshLinearLayout refreshLinearLayout;
	/** 数据源 */
	private List<PlanStarListEntity> list = new ArrayList<PlanStarListEntity>();
	private GetProjectEveInfoEntity entity;
	/** 当前页面 */
	private int i = 1;
	/** 1，应急；2，演练 */
	//private String tag;
	/** 事件编号 */
	private String id;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			/**
			 * 接收子集合
			 */
			List<PlanStarListEntity> result = (List<PlanStarListEntity>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				refreshLinearLayout.onCompleteRefresh();
				/**
				 * 总集合清理
				 */
				list.clear();
				/**
				 * 总集合添加
				 */
				list.addAll(result);
				break;

			case AutoListView.LOAD:
				refreshLinearLayout.onLoadComplete();
				list.addAll(result);
				break;

			default:
				break;
			}
            adapter.notifyDataSetChanged();
			refreshLinearLayout.setResultSize(result.size(), i);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.dismissvaluation);
		findViewById.setFitsSystemWindows(true);
		initview();
	}
	/***
	 * 打开EventBus开关
	 */
	protected boolean useEventBus() {
		return true;
	}

	/**
	 * 刷新界面
	 * @param data
	 */
	public void onEvent(mainEvent data) {
		if (data.getData().equals("re")) {
			initData();
		}
	}
	

	private void initview() {
		back.setVisibility(View.VISIBLE);
		title.setText(R.string.dismissvaluation);
		adapter = new LeftSlideEventAdapter(
				DismissValuationActivity.this, list,"3");
		//设置布局管理器
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
		mRecyclerView.setAdapter(adapter);
		initData();
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置控制Item增删的动画
		//添加自定义分割线
		DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
		divider.setDrawable(getResources().getDrawable(R.drawable.divider_line));
		mRecyclerView.addItemDecoration(divider);
		refreshLinearLayout.setOnRefreshListener(new RefreshLinearLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				i = 1;
				initData();
			}
		});
		refreshLinearLayout.setOnLoadListener(new RefreshLinearLayout.OnLoadListener() {
			@Override
			public void onLoad() {
				loadData(AutoListView.LOAD);
			}
		});
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if(isSlideToBottom(recyclerView))
					refreshLinearLayout.checkFooter();
			}
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy)
			{
				super.onScrolled(recyclerView, dx, dy);
				if(isSlideToBottom(recyclerView))
					refreshLinearLayout.checkFooter();
			}
		});
	}

	@Override
	public void onItemClick(View view, final int position) {
		if (position >= 0 && position < list.size()) {
			Dialog dialog = new AlertDialog.Builder(
					DismissValuationActivity.this)
					.setMessage(
							list.get(position).getEveName())

					// 设置LOGO
					.setPositiveButton("删除",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int which) {

									deleteData(position);
								}
							})
					.setNeutralButton("重新评估",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int which) {
									getValuation(position);

								}
							})

					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int which) {
								}
							}).create(); // 创建对话框
			dialog.show(); // 显示对话框

		}
	}

	@Override
	public void onFunction1BtnClick(View view, final int position) {
		new android.app.AlertDialog.Builder(DismissValuationActivity.this)
				.setMessage("确定重新评估？")
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								getValuation(position);
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).show();

	}

	@Override
	public void onFunction2BtnClick(View view, final int position) {
		new android.app.AlertDialog.Builder(DismissValuationActivity.this)
				.setMessage("确定删除该事件？")
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								deleteData(position);
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).show();

	}

	public static boolean isSlideToBottom(RecyclerView recyclerView) {
		if (recyclerView == null) return false;
		//computeVerticalScrollExtent()是当前屏幕显示的区域高度，
		// computeVerticalScrollOffset() 是当前屏幕之前滑过的距离，
		// 而computeVerticalScrollRange()是整个View控件的高度。
		if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
				>= recyclerView.computeVerticalScrollRange())
			return true;
		return false;
	}

	private void initData() {
		loadData(AutoListView.REFRESH);
	}

	// 从网络获取的总的list
	private List<PlanStarListEntity> allList = new ArrayList<PlanStarListEntity>();
	private int num = 20;// 每次显示20条

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListenser = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(Object object,
				String stRerror, String Exceptionerror) {
			// TODO Auto-generated method stub
			List<PlanStarListEntity> dataList=null;
			Message message = handler.obtainMessage();
			if (object != null) {
				dataList = (List<PlanStarListEntity>) object;
				Log.i("驳回事件的长度", dataList.size() + "");

			}else if (stRerror!=null) {
				dataList=new ArrayList<PlanStarListEntity>();

			}else if (Exceptionerror!=null) {
				dataList=new ArrayList<PlanStarListEntity>();
				ToastUtil.showToast(DismissValuationActivity.this, Const.NETWORKERROR);
			}
			message.what = 0;
			if (dataList.size() > 20) {// 如果超过20条，则分页
				List<PlanStarListEntity> subList = dataList.subList(0,
						20);
				message.obj = subList;
			} else {
				message.obj = dataList;
			}
			handler.sendMessage(message);
			allList = dataList;
		}
	};

	private void loadData(final int what) {

		if (what == 0) {// 刷新和第一次加载
			Control.getinstance().getEmergencyService().getPlanStarList(listListenser, "3");
		} else if (what == 1) {// 加载更多
			// 本地做分页，加载20条以后的数据，默认每20条分一页
			Log.i("list测试长度", allList.size() + "");
			Log.i("num", num + "");
			List<PlanStarListEntity> datalist2;
			if ((num + 20) <= allList.size()) {
				datalist2 = allList.subList(num, num + 20);
				num += 20;
			} else {
				datalist2 = allList.subList(num, allList.size());
			}

			Message message = handler.obtainMessage();
			message.what = 1;
			message.obj = datalist2;
			handler.sendMessage(message);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		list.clear();
		adapter=null;
		allList.clear();
	}

	private int curPosition = 1;
	private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser deleteEventListener = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {

			if (backflag) {
				list.remove(curPosition - 1);
				adapter.notifyDataSetChanged();
				ToastUtil.showToast(DismissValuationActivity.this,
						stRerror);
			} else if (backflag == false) {
				ToastUtil.showToast(DismissValuationActivity.this,
						stRerror);
			} else if (stRerror != null) {

				ToastUtil.showLongToast(DismissValuationActivity.this,
						stRerror);
			} else if (Exceptionerror != null) {

				ToastUtil.showLongToast(DismissValuationActivity.this,
						Const.NETWORKERROR);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	/**
	 * 删除驳回事件
	 */
	private void deleteData(final int position) {
		curPosition = position;
		Utils.getInstance().showProgressDialog(
				DismissValuationActivity.this, "",
				Const.SUBMIT_MESSAGE);
		Control.getinstance().getEmergencyService().deleteEvent(list.get(position - 1).getId(), deleteEventListener);

	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {

			if (object != null) {
				entity = (GetProjectEveInfoEntity) object;
				Intent intent = new Intent(
						DismissValuationActivity.this,
						AddeValuationActivity.class);
				//	intent.putExtra("tag", tag);
				intent.putExtra("type", "1");// 重新评估
				intent.putExtra("entity", entity);
				startActivity(intent);
			}else if (stRerror!=null) {
				entity = new GetProjectEveInfoEntity();

			}else if (Exceptionerror!=null) {
				entity = new GetProjectEveInfoEntity();
				ToastUtil.showToast(DismissValuationActivity.this, Const.NETWORKERROR);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	/**
	 * 获取评估信息
	 */
	private void getValuation(int position) {
		Utils.getInstance().showProgressDialog(
				DismissValuationActivity.this, "",
				Const.SUBMIT_MESSAGE);
		// 获取评估信息
		Control.getinstance().getEmergencyService().getEventInfo(list.get(position).getId(), listener);

	}
	@Override
	public void initNetData() {
		initData();
	}
	
}
