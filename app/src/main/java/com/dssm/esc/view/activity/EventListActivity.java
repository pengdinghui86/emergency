package com.dssm.esc.view.activity;

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

import de.greenrobot.event.EventBus;

/**
 * 预案启动界面
 */
@ContentView(R.layout.activity_dismissvaluation)
public class EventListActivity extends BaseActivity implements
		MainActivity.onInitNetListener, LeftSlideEventAdapter.IonSlidingViewClickListener {
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
	/** 数据源 */
	private List<PlanStarListEntity> list = new ArrayList<PlanStarListEntity>();
	/** 当前页面 */
	private int i = 1;
	/** 1,待启动事件列表;2，执行中事件列表；3，执行完成事件列表；4,事件流程列表；*/
	private String tags;
	/** 下拉刷新控件 */
	@ViewInject(R.id.dismissv_refreshLinearLayout)
	private RefreshLinearLayout refreshLinearLayout;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<PlanStarListEntity> result = (List<PlanStarListEntity>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				refreshLinearLayout.onCompleteRefresh();
				list.clear();
				list.addAll(result);
				break;

			case AutoListView.LOAD:
				refreshLinearLayout.onLoadComplete();
				list.addAll(result);
				break;

			default:
				break;
			}
			// 用来显示"加载全部"
			refreshLinearLayout.setResultSize(result.size(), i);
			adapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.dismissvaluation);
		findViewById.setFitsSystemWindows(true);
		tags = getIntent().getStringExtra("tags");
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
		if (data.getData().equals("refres")) {
			initData();
		}
	}
	
	private void initview() {
		// TODO Auto-generated method stub
		back.setVisibility(View.VISIBLE);
        if (tags.equals("1")) {
            title.setText("待启动事件");
        } else if (tags.equals("2")) {
            title.setText("执行中事件");
        } else if (tags.equals("4")) {
            title.setText("执行完成事件");
        } else if (tags.equals("6")) {
            title.setText("事件流程");
        }
		adapter = new LeftSlideEventAdapter(EventListActivity.this, list, tags);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
        mRecyclerView.setAdapter(adapter);
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
		initData();
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

    /**
	 * 事件驳回
	 */
	private void rejectEvent(PlanStarListEntity entity) {
		Utils.getInstance().showProgressDialog(EventListActivity.this, "",
				Const.SUBMIT_MESSAGE);
		Control.getinstance().getEmergencyService().planStarBohui(entity.getPlanId(), entity.getEveName(), entity.getSubmitterId(), entity.getEveType(), rejectEventListener);
	}

	private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser rejectEventListener = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			String str = null;
			if (backflag) {
				str = stRerror;
				ToastUtil.showToast(EventListActivity.this,
						str);
				onEvent(new mainEvent("refres"));// 刷新预案启动列表
				finish();
			} else if (backflag == false) {
				ToastUtil.showToast(EventListActivity.this,
						stRerror);
			} else if (stRerror != null) {

				ToastUtil.showToast(EventListActivity.this,
						stRerror);
			} else if (Exceptionerror != null) {

				ToastUtil.showToast(EventListActivity.this,
						Exceptionerror);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void initData() {
		loadData(AutoListView.REFRESH);
	}

	// 从网络获取的总的list
	private List<PlanStarListEntity> allList = new ArrayList<PlanStarListEntity>();
	private int num = 20;// 每次显示20条

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListenser = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			List<PlanStarListEntity> datalist=null;
			if (object != null) {// 没有做分页，一次性加载所有的数据，本地做分页显示
				datalist = (List<PlanStarListEntity>) object;
				Log.i("待启动预案列表的长度", datalist.size() + "");

			}else if (stRerror!=null) {
				datalist=new ArrayList<PlanStarListEntity>();

			}else if (Exceptionerror!=null) {
				datalist=new ArrayList<PlanStarListEntity>();
				ToastUtil.showToast(EventListActivity.this, Const.NETWORKERROR);
			}
			Message message = handler.obtainMessage();
			message.what = 0;
			if (datalist.size() > 20) {// 如果超过20条，则分页
				List<PlanStarListEntity> subList = datalist
						.subList(0, 20);
				message.obj = subList;
			} else {
				message.obj = datalist;
			}
			handler.sendMessage(message);
			allList = datalist;
		}
	};

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			List<PlanStarListEntity> datalist=null;
			if (object != null) {
				datalist = (List<PlanStarListEntity>) object;
				Log.i("已启动预案列表的长度", datalist.size() + "");

			}else if (stRerror!=null) {
				datalist=new ArrayList<PlanStarListEntity>();

			}else if (Exceptionerror!=null) {
				datalist=new ArrayList<PlanStarListEntity>();
				ToastUtil.showToast(EventListActivity.this, Const.NETWORKERROR);
			}
			Message message = handler.obtainMessage();
			message.what = 0;
			if (datalist.size() > 20) {// 如果超过20条，则分页
				List<PlanStarListEntity> subList = datalist
						.subList(0, 20);
				message.obj = subList;
			} else {
				message.obj = datalist;
			}
			handler.sendMessage(message);
			allList = datalist;
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void loadData(final int what) {
		String status = "0";
		if (tags.equals("6"))
			status = "";
		else if (tags.equals("2"))
			status = "2";
		else if(tags.equals("4"))
			status = "3";
		if (what == 0) {// 刷新和第一次加载
			Control.getinstance().getEmergencyService().getPlanStarList(listListenser, status);
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
		allList.clear();
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}

	@Override
	public void onItemClick(View view, int position) {
        if (position >= 0 && position < list.size()) {
            //启动中
            if (list.get(position).getState().equals("5")) {
                ToastUtil.showLongToast(EventListActivity.this, "预案正在启动中，请稍后刷新列表重试");
                return;
            }
            if (tags.equals("1")) {

                Intent intent = new Intent(EventListActivity.this,
                        PlanStarDetailActivity.class);
                //intent.putExtra("tag", tag);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("name", list.get(position)
                        .getEveName());
                intent.putExtra("tag", list.get(position)
                        .getEveType());
                intent.putExtra("isStarter", list.get(position).getIsStarter());
                startActivity(intent);
                // EventListActivity.this.finish();
            } else {
                Intent intent = new Intent(EventListActivity.this,
                        EventProcessDetailActivity.class);
				intent.putExtra("id", list.get(position).getId());
				intent.putExtra("name", list.get(position).getEveName());
                startActivity(intent);
            }

        }
	}

	@Override
	public void onFunction1BtnClick(View view, int position) {

	}

	@Override
	public void onFunction2BtnClick(View view, final int position) {
		if(tags.equals("1")) {
			new android.app.AlertDialog.Builder(EventListActivity.this)
					.setMessage("确定驳回该事件？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									rejectEvent(list.get(position));
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {

								}
							}).show();
		}
		else if(tags.equals("3"))
		{
			new android.app.AlertDialog.Builder(EventListActivity.this)
					.setMessage("确定关闭该事件？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {

								}
							}).show();
		}
	}
}
