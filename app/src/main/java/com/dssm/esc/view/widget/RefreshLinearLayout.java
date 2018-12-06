package com.dssm.esc.view.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.util.Utils;

/**
 * 自定义LinearLayout下拉刷新
 */
public class RefreshLinearLayout extends LinearLayout {

	private LayoutInflater inflater;
	private Context context;
	private View header;
	private View footer;
	private LinearLayout noData;
	private TextView loadFull;
	private TextView more;
	private ProgressBar loading;
	private boolean isRecorded;
	private boolean isLoading;// 判断是否正在加载
	private boolean loadEnable = true;// 开启或者关闭加载更多功能
	private boolean isLoadFull;
	private int pageSize = 20;
	private OnLoadListener onLoadListener;

	private TextView tip;
	private TextView lastUpdate;
	private ImageView arrow;
	private ProgressBar refreshing;
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	//view的真实高度
	private int originRefreshHeight;
	//有效下拉刷新需要达到的高度
	private int refreshArrivedStateHeight;
	//刷新时显示的高度
	private int refreshingHeight;
	//正常为刷新的高度
	private int refreshNormalHeight;
	//若设置它的值为false，表示不把移动的事件传递给子控件
	private boolean interceptAllMoveEvents = true;
	//默认不允许拦截
	private boolean disallowIntercept = true;
	private float downY = Float.MAX_VALUE;

	// 定义header的四种状态和当前状态
	//刷新状态
	private int refreshState = STATE_REFRESH_NORMAL;
	//静止状态
	public static final int STATE_REFRESH_NORMAL = 0x000001;
	//下拉刷新
	public static final int STATE_REFRESH_NOT_ARRIVED = 0x000002;
	//放下刷新
	public static final int STATE_REFRESH_ARRIVED = 0x000003;
	//正在刷新
	public static final int STATE_REFRESHING = 0x000004;

	private OnRefreshListener onRefreshListener;
    private Utils utils = Utils.getInstance();
	public RefreshLinearLayout(Context context) {
		super(context);
		init(context);
	}

	public RefreshLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RefreshLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	// 下拉刷新监听
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	/**
	 * 设置布局样式
	 * @param context
	 */
	private void init(Context context) {
		this.context = context;
		this.setOrientation(VERTICAL);
	}

	// 初始化组件
	private void initView() {
		// 设置箭头特效
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(100);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(100);
		reverseAnimation.setFillAfter(true);

		inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.pull_to_refresh_header, null);
		arrow = (ImageView) header.findViewById(R.id.arrow);
		tip = (TextView) header.findViewById(R.id.tip);
		lastUpdate = (TextView) header.findViewById(R.id.lastUpdate);
		refreshing = (ProgressBar) header.findViewById(R.id.refreshing);

		footer = inflater.inflate(R.layout.listview_footer, null);
		loadFull = (TextView) footer.findViewById(R.id.loadFull);
		noData = (LinearLayout) footer.findViewById(R.id.ll_no_data);
		more = (TextView) footer.findViewById(R.id.more);
		loading = (ProgressBar) footer.findViewById(R.id.loading);
		refreshHeaderViewByState();
	}

	public void onRefresh() {
		if (onRefreshListener != null) {
			onRefreshListener.onRefresh();
		}
	}

	/**
	 * 当view的大小发生变化时触发
	 *
	 * @param w
	 * @param h
	 * @param oldw
	 * @param oldh
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (null != onRefreshListener && null == header) {
			initView();
		}
		this.removeView(header);
		this.addView(header, 0);
		//计算header尺寸   根据大小和模式创建
		int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
		header.measure(widthMeasureSpec, heightMeasureSpec);
		originRefreshHeight = header.getMeasuredHeight();
		// 初始化各个高度
		refreshArrivedStateHeight = originRefreshHeight;
		refreshingHeight = originRefreshHeight;
		refreshNormalHeight = 0;
		//改变控件的高度
		changeViewHeight(header, refreshNormalHeight);
        this.removeView(footer);
        this.addView(footer);
		//初始化为正常状态
		setRefreshState(STATE_REFRESH_NORMAL);
	}

	 /** 改变控件的高度
      * @param view
      * @param height
      */
	private void changeViewHeight(View view, int height) {
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		lp.height = height;
		view.setLayoutParams(lp);
	}

	public void checkFooter()
	{
		ifNeedLoad();
	}

	/**
	 * 修改当前的刷新状态
	 *
	 * @param expectRefreshState
	 */
	private void setRefreshState(int expectRefreshState) {
		if (expectRefreshState != refreshState) {
			refreshState = expectRefreshState;
		}
	}

	// 这里的开启或者关闭加载更多，并不支持动态调整
	public void setLoadEnable(boolean loadEnable) {
		this.loadEnable = loadEnable;
		this.removeView(footer);
	}

	// 加载更多监听
	public void setOnLoadListener(OnLoadListener onLoadListener) {
		this.loadEnable = true;
		this.onLoadListener = onLoadListener;
	}

	//判断是否需要加载更多
	private void ifNeedLoad() {
		if (!loadEnable) {
			return;
		}
		try {
			if (!isLoading && !isLoadFull && refreshState == STATE_REFRESH_NORMAL) {
				onLoad();
				isLoading = true;
			}
		} catch (Exception e) {
		}
	}

	public void onLoad() {
		if (onLoadListener != null) {
			onLoadListener.onLoad();
		}
	}

	/**
	 * 这个方法是根据结果的大小来决定footer显示的。
	 * <p>
	 * 这里假定每次请求的条数为10。如果请求到了10条。则认为还有数据。如过结果不足10条，则认为数据已经全部加载，这时footer显示已经全部加载
	 * </p>
	 *
	 * @param resultSize
	 */
	public void setResultSize(int resultSize, int i) {
		if (resultSize == 0 && i == 1) {
			isLoadFull = true;
			loadFull.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.VISIBLE);
		} else if (resultSize == 0 && i == 3) {
			isLoadFull = true;
			loadFull.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.GONE);
		} else if (resultSize > 0 && resultSize < pageSize) {
			isLoadFull = true;
			loadFull.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.GONE);
		} else if (resultSize == pageSize) {
			isLoadFull = false;
			loadFull.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			more.setVisibility(View.VISIBLE);
			noData.setVisibility(View.GONE);
		}
		if (resultSize == 0 && i != 1 && i != 3) {
			isLoadFull = true;
			loadFull.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.GONE);
		}
		//loadFull.setVisibility(View.GONE);
	}

	/*
	 * 定义加载更多接口
	 */
	public interface OnLoadListener {
		void onLoad();
	}

	/**
	 * 刷新完毕后调用此方法
	 */
	public void onCompleteRefresh() {
		if (STATE_REFRESHING == refreshState) {
			setRefreshState(STATE_REFRESH_NORMAL);
			startHeightAnimation(header, header.getMeasuredHeight(), refreshNormalHeight);
			refreshHeaderViewByState();
		}
		lastUpdate.setText(this.getContext().getString(R.string.lastUpdateTime,
				utils.getCurrentTime()));
	}

	// 用于加载更多结束后的回调
	public void onLoadComplete() {
		isLoading = false;
	}

	/**
	 * 改变控件的高度动画
	 *
	 * @param view
	 * @param fromHeight
	 * @param toHeight
	 */
	private void startHeightAnimation(final View view, int fromHeight, int toHeight) {
		startHeightAnimation(view, fromHeight, toHeight, null);
	}

	private void startHeightAnimation(final View view, int fromHeight, int toHeight, Animator.AnimatorListener animatorListener) {
		if (toHeight == view.getMeasuredHeight()) {
			return;
		}
		ValueAnimator heightAnimator = ValueAnimator.ofInt(fromHeight, toHeight);
		heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				Integer value = (Integer) valueAnimator.getAnimatedValue();
				if (null == value) return;
				changeViewHeight(view, value);
			}
		});
		if (null != animatorListener) {
			heightAnimator.addListener(animatorListener);
		}
		heightAnimator.setInterpolator(new LinearInterpolator());
		heightAnimator.setDuration(300/*ms*/);
		heightAnimator.start();
	}

	AnimatorListenerAdapter normalAnimatorListener = new AnimatorListenerAdapter() {
		@Override
		public void onAnimationEnd(Animator animation) {
			super.onAnimationEnd(animation);
			// 回归正常状态
			setRefreshState(STATE_REFRESH_NORMAL);
		}
	};

	public void setRefreshArrivedStateHeight(int refreshArrivedStateHeight) {
		this.refreshArrivedStateHeight = refreshArrivedStateHeight;
	}

	public void setRefreshingHeight(int refreshingHeight) {
		this.refreshingHeight = refreshingHeight;
	}

	public void setRefreshNormalHeight(int refreshNormalHeight) {
		this.refreshNormalHeight = refreshNormalHeight;
	}

	public int getOriginRefreshHeight() {
		return originRefreshHeight;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 事件传递(拦截)
	 *
	 * @param ev
	 * @return
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!interceptAllMoveEvents) {
			return !disallowIntercept;
		}
		if(MotionEvent.ACTION_DOWN == ev.getAction())
			downY = ev.getY();
		// 如果设置了拦截所有move事件，即interceptAllMoveEvents为true
		else if (MotionEvent.ACTION_MOVE == ev.getAction()) {
			float curY = ev.getY();
			float deltaY = curY - downY;
			//是否是有效的往下拖动事件
			boolean isDropDownValidate = (Float.MAX_VALUE != downY && deltaY > 50);
			//recycleView没有滑到顶部时屏蔽下拉刷新
			if(getChildAt(1) != null && getChildAt(1) instanceof RecyclerView) {
				RecyclerView recycle_view = (RecyclerView) getChildAt(1);
				if (recycle_view != null && canScrollVertically(recycle_view, -1)) {
					return false;
				}
			}
			if(isDropDownValidate)
			{
				onTouchEvent(ev);
				return true;
			}
		}
		return false;
	}

	/**
	 * 阻止父层的View截获touch事件
	 * 底层View收到touch的action后调用这个方法那么父层View就不会再调用onInterceptTouchEvent了
	 *
	 * @param disallowIntercept
	 */
	@Override
	public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
		if (this.disallowIntercept == disallowIntercept) {
			return;
		}
		this.disallowIntercept = disallowIntercept;
		super.requestDisallowInterceptTouchEvent(disallowIntercept);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downY = event.getY();
				//保证事件可往下传递
//				requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_MOVE:
				float curY = event.getY();
				float deltaY = curY - downY;
				//是否是有效的往下拖动事件
				boolean isDropDownValidate = Float.MAX_VALUE != downY;
				/**
				 * 修改拦截设置
				 * 如果是有效往下拖动事件，则事件需要在本ViewGroup中处理，所以需要拦截不往子控件传递，
				 * 即不允许拦截设为false
				 * 如果不是有效往下拖动事件，则事件传递给子控件处理，所以不需要拦截，并往子控件传递，
				 * 即不允许拦截设为true
				 */
				requestDisallowInterceptTouchEvent(!isDropDownValidate);

				downY = curY;
				int curHeight = header.getMeasuredHeight();
				int exceptHeight = curHeight + (int) (deltaY / 2);
				//如果当前没有处在正在刷新状态，则更新刷新状态
				if (STATE_REFRESHING != refreshState) {
					//达到可刷新状态
					if (curHeight >= refreshArrivedStateHeight) {
						setRefreshState(STATE_REFRESH_ARRIVED);
					} else { // 未达到可刷新状态
						setRefreshState(STATE_REFRESH_NOT_ARRIVED);
					}
					refreshHeaderViewByState();
				}
				if (isDropDownValidate) {
					changeViewHeight(header, Math.max(refreshNormalHeight, exceptHeight));
				} else {
					// 防止从子控件修改拦截后引发的downY为Float.MAX_VALUE的问题
					changeViewHeight(header, Math.max(curHeight, exceptHeight));
				}
				break;
			case MotionEvent.ACTION_UP:
				downY = Float.MAX_VALUE;
				//保证事件可往下传递
				requestDisallowInterceptTouchEvent(true);
				// 达到了刷新的状态
				if (STATE_REFRESH_ARRIVED == refreshState) {
					startHeightAnimation(header, header.getMeasuredHeight(), refreshingHeight);
					setRefreshState(STATE_REFRESHING);
					onRefresh();
					//正在刷新的状态
				} else if (STATE_REFRESHING == refreshState) {
					startHeightAnimation(header, header.getMeasuredHeight(), refreshingHeight);
				} else {
					//执行动画后回归正常状态
					startHeightAnimation(header, header.getMeasuredHeight(), refreshNormalHeight, normalAnimatorListener);
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * Check if this view can be scrolled vertically in a certain direction.
	 *
	 * @param direction Negative to check scrolling up, positive to check scrolling down.
	 * @return true if this view can be scrolled in the specified direction, false otherwise.
	 */
	public boolean canScrollVertically(RecyclerView recycle_view, int direction) {
		final int offset = recycle_view.computeVerticalScrollOffset();
		final int range = recycle_view.computeVerticalScrollRange() - computeVerticalScrollExtent();
		if (range == 0) return false;
		if (direction < 0) {
			return offset > 0;
		} else {
			return offset < range - 1;
		}
	}

	// 根据当前状态，调整header
	private void refreshHeaderViewByState() {
		switch (refreshState) {
		case STATE_REFRESH_NORMAL:
			header.setVisibility(GONE);
			tip.setText(R.string.pull_to_refresh);
			refreshing.setVisibility(View.GONE);
			arrow.clearAnimation();
			arrow.setImageResource(R.drawable.pull_to_refresh_arrow);
			break;
		case STATE_REFRESH_NOT_ARRIVED:
			header.setVisibility(VISIBLE);
			arrow.setVisibility(View.VISIBLE);
			tip.setVisibility(View.VISIBLE);
			lastUpdate.setVisibility(View.VISIBLE);
			refreshing.setVisibility(View.GONE);
			tip.setText(R.string.pull_to_refresh);
			arrow.clearAnimation();
			arrow.setAnimation(reverseAnimation);
			break;
		case STATE_REFRESH_ARRIVED:
			header.setVisibility(VISIBLE);
			arrow.setVisibility(View.VISIBLE);
			tip.setVisibility(View.VISIBLE);
			lastUpdate.setVisibility(View.VISIBLE);
			refreshing.setVisibility(View.GONE);
			tip.setText(R.string.release_to_refresh);
			arrow.clearAnimation();
			arrow.setAnimation(animation);
			break;
		case STATE_REFRESHING:
			refreshing.setVisibility(View.VISIBLE);
			arrow.clearAnimation();
			arrow.setVisibility(View.GONE);
			tip.setVisibility(View.GONE);
			lastUpdate.setVisibility(View.GONE);
			break;
		}
	}

	/*
	 * 定义下拉刷新接口
	 */
	public interface OnRefreshListener {
		void onRefresh();
	}

}
