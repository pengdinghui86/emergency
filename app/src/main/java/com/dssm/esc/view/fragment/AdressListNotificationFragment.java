package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.ContactListService;
import com.dssm.esc.model.analytical.implSevice.ContactListServiceImpl;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.util.CharacterParser;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.ContactSelectIds;
import com.dssm.esc.util.event.mainEvent;
import com.dssm.esc.view.activity.IntroductionActivity;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.view.adapter.ExpandListviewContactAdapter;
import com.dssm.esc.view.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 应急通知组碎片
 *
 */
public class AdressListNotificationFragment extends BaseFragment
		implements OnRefreshListener, MainActivity.onInitNetListener {
	/** 可扩展ListView */
	private ExpandableListView expandableListView;
	/** 适配器 */
	private ExpandListviewContactAdapter adapter;
	/** 父list显示组 */
	private List<GroupEntity> groupList = null;
	/** 子list显示人 */
	private List<ChildEntity> childList = new ArrayList<ChildEntity>();
	/** 被选中的人员的id */
	public List<String> selectId = new ArrayList<String>();
	/** 汉字转换成拼音的类 */
	private CharacterParser characterParser = CharacterParser.getInstance();
	/** 没有数据 */
	private LinearLayout ll_no_data;
	/** 可清除的edittext */
	private ClearEditText myClearEt;
	/** 子entity */
	private ChildEntity child = null;
	/** 父entity */
	private GroupEntity group = null;
	private Context context;
	private ContactListService contactListService;
	/** 保存用户信息 */
	private MySharePreferencesService service = null;
	private SwipeRefreshLayout mSwipeLayout;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				List<GroupEntity> result = (List<GroupEntity>) msg.obj;
				groupList.clear();
				groupList.addAll(result);
				adapter.notifyDataSetChanged();
				mSwipeLayout.setRefreshing(false);
				if (groupList.size() == 0) {
					mSwipeLayout.setVisibility(View.GONE);
					expandableListView.setVisibility(View.GONE);
					ll_no_data.setVisibility(View.VISIBLE);
				} else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					expandableListView.setVisibility(View.VISIBLE);
					ll_no_data.setVisibility(View.GONE);
				}
				for (int i = 0; i < groupList.size(); i++) {
					GroupEntity groupEntity = groupList.get(i);
					List<ChildEntity> getcList = groupEntity.getcList();
					for (int j = 0; j < getcList.size(); j++) {
						ChildEntity childEntity = getcList.get(j);
						searchChildItemData(childEntity);
					}
					searchGroupData(groupEntity);
				}
				mSwipeLayout.setRefreshing(false);
				break;
			case 1:
				groupList = (List<GroupEntity>) msg.obj;
				adapter = new ExpandListviewContactAdapter(groupList,
						getActivity(), "");
				expandableListView.setAdapter(adapter);
				if (groupList.size() == 0) {
					mSwipeLayout.setVisibility(View.GONE);
					expandableListView.setVisibility(View.GONE);
					ll_no_data.setVisibility(View.VISIBLE);
				} else {
					mSwipeLayout.setVisibility(View.VISIBLE);
					expandableListView.setVisibility(View.VISIBLE);
					ll_no_data.setVisibility(View.GONE);
				}
				for (int i = 0; i < groupList.size(); i++) {
					GroupEntity groupEntity = groupList.get(i);
					List<ChildEntity> getcList = groupEntity.getcList();
					for (int j = 0; j < getcList.size(); j++) {
						ChildEntity childEntity = getcList.get(j);
						searchChildItemData(childEntity);
					}
					searchGroupData(groupEntity);
				}
				mSwipeLayout.setRefreshing(false);
				break;
			}
		};
	};

	public AdressListNotificationFragment() {
	}
	/**
	 * 页面隐藏后再显示回调此方法，刷新界面
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			initData();
		}
	}
	@SuppressLint("ValidFragment")
	public AdressListNotificationFragment(Context context) {
		this.context = context;
		contactListService = Control.getinstance().getContactSevice();
		service = MySharePreferencesService.getInstance(context);
	}

	@Override
	protected View getViews() {
		// TODO Auto-generated method stub
		return view_Parent = LayoutInflater.from(context).inflate(
				R.layout.test, null);
	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		expandableListView = (ExpandableListView) view_Parent
				.findViewById(R.id.expandable_listv_failsafe);
		expandableListView.setGroupIndicator(null);
		myClearEt = (ClearEditText) view_Parent.findViewById(R.id.filter_edit);
		ll_no_data = (LinearLayout) view_Parent
				.findViewById(R.id.ll_no_data);
		mSwipeLayout = (SwipeRefreshLayout) view_Parent
				.findViewById(R.id.id_swipe_ly);
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

	}

	@Override
	protected void widgetListener() {
		// TODO Auto-generated method stub
		mSwipeLayout.setOnRefreshListener(this);
		// 初始化，默认加载任务通知界面
		myClearEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				filterData(s.toString());
			}
		});
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						IntroductionActivity.class);
				child = adapter.getChild(groupPosition, childPosition);
				group = adapter.getGroup(groupPosition);

				intent.putExtra("id", child.getUserId());

				intent.putExtra("name", child.getName());
				intent.putExtra("mobileNumber", child.getPhoneNumber());
				intent.putExtra("telephoneNumber", child.getPhoneNumTwo());
				intent.putExtra("post", child.getZhiwei());
				intent.putExtra("department", child.getEmergTeam());
				intent.putExtra("sex", child.getSex());
				intent.putExtra("email", child.getEmail());
				startActivity(intent);
				return true;
			}
		});

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initGetData() {
		// TODO Auto-generated method stub
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Utils.getInstance().showProgressDialog(getActivity(), "",
				Const.LOAD_MESSAGE);
		contactListService.getToastContactList(contactSeviceImplListListenser);

	}

	private ContactListServiceImpl.ContactSeviceImplListListenser contactSeviceImplListListenser = new ContactListServiceImpl.ContactSeviceImplListListenser() {

		@Override
		public void setContactSeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			List<GroupEntity> dataList = null;
			if (i != 1) {
				if (object != null) {
					dataList = (List<GroupEntity>) object;

				} else if (stRerror != null) {
					dataList = new ArrayList<GroupEntity>();

				} else if (Exceptionerror != null) {
					dataList = new ArrayList<GroupEntity>();
					ToastUtil.showToast(getActivity(),
							Const.REQUESTERROR);
				}
				Message message = new Message();
				message.what = 1;
				message.obj = dataList;
				handler.sendMessage(message);
			} else if (i == 1) {
				if (object != null) {
					dataList = (List<GroupEntity>) object;

				} else if (stRerror != null) {
					dataList = new ArrayList<GroupEntity>();

				} else if (Exceptionerror != null) {
					dataList = new ArrayList<GroupEntity>();
					ToastUtil.showToast(getActivity(),
							Const.REQUESTERROR);
				}
				Message message = new Message();
				message.what = 0;
				message.obj = dataList;
				handler.sendMessage(message);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	/**
	 * 所选列表值的岗位标识postFlag
	 */
	private List<String> showCheckedItems() {
		String checkedItems = "";
		List<String> checkedChildren = new ArrayList<>();
		if (checkedChildren != null && !checkedChildren.isEmpty()) {
			for (String child : checkedChildren) {
				if (checkedItems.length() > 0) {
					checkedItems += "\n";
				}

				checkedItems += child;
			}
		}

		return checkedChildren;
	}

	/**
	 * 子list正则过滤，用于搜索
	 */
	private void searchChildItemData(ChildEntity childrenItem) {
		// TODO Auto-generated method stub
		String name = characterParser.getSelling(childrenItem.getName());
		String flag = characterParser.getSelling(childrenItem.getZhiwei());
		String sortString = name.substring(0, 1).toUpperCase();
		String sortString2 = flag.substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortString.matches("[A-Z]")) {
			childrenItem.setSortLetters(sortString.toUpperCase());
		} else {
			childrenItem.setSortLetters("#");
		}
		// 正则表达式，判断首字母是否是英文字母
		if (sortString2.matches("[A-Z]")) {
			childrenItem.setSortLetters(sortString2.toUpperCase());
		} else {
			childrenItem.setSortLetters("#");
		}
	}

	/**
	 * 父list正则过滤，用于搜索
	 */
	private void searchGroupData(GroupEntity groupItem) {
		// TODO Auto-generated method stub
		// 汉字转换成拼音
		String pinyin = characterParser.getSelling(groupItem.getGroupname());
		String sortString = pinyin.substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortString.matches("[A-Z]")) {
			groupItem.setSortLetters(sortString.toUpperCase());
		} else {
			groupItem.setSortLetters("#");
		}
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<GroupEntity> groupFilterList = new ArrayList<GroupEntity>();
		List<ChildEntity> childFilterList = null;

		if (TextUtils.isEmpty(filterStr)) {
			groupFilterList = groupList;
			childFilterList = childList;
			ll_no_data.setVisibility(View.GONE);
		} else {
			groupFilterList.clear();
			// ildFilterList.clear();
			for (int i = 0; i < groupList.size(); i++) {
				// 标记departGroup是否加入元素
				// boolean isAddGroup = false;
				GroupEntity sortModel = groupList.get(i);
				String name = sortModel.getGroupname();
				// depart有字符直接加入
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					if (!groupFilterList.contains(sortModel)) {
						groupFilterList.add(sortModel);
						// isAddGroup = true;
					}
				} else {
					childFilterList = new ArrayList<ChildEntity>();
					for (int j = 0; j < groupList.get(i).getcList().size(); j++) {
						ChildEntity sortChildModel = groupList.get(i)
								.getcList().get(j);
						String childName = sortChildModel.getName();
						String flag = sortChildModel.getZhiwei();
						// child有字符直接加入，其父也加入
						if (childName.indexOf(filterStr.toString()) != -1
								|| characterParser.getSelling(childName)
										.startsWith(filterStr.toString())
								|| flag.indexOf(filterStr.toString()) != -1
								|| characterParser.getSelling(flag)
										.startsWith(filterStr.toString())) {
							childFilterList.add(sortChildModel);

						}
					}
					if (childFilterList.size() > 0) {
						GroupEntity entity = new GroupEntity();
						entity.setGroup_id(groupList.get(i).getGroup_id());
						entity.setGroupname(groupList.get(i).getGroupname());
						entity.setSortLetters(groupList.get(i).getSortLetters());
						entity.setcList(childFilterList);
						groupFilterList.add(entity);
					}
				}
			}
		}

		if (adapter != null) {
			adapter.updateListView(groupFilterList, "");

			if (TextUtils.isEmpty(filterStr)) {
				for (int i = 0; i < groupFilterList.size(); i++) {
					if (i == 0) {
						expandableListView.expandGroup(i);
						continue;
					}
					expandableListView.collapseGroup(i);
				}
			} else {
				// 搜索的结果全部展开
				for (int i = 0; i < groupFilterList.size(); i++) {
					expandableListView.expandGroup(i);
				}
			}
		}

		// 如果查询的结果为0时，显示为搜索到结果的提示
		if (groupFilterList.size() == 0) {
			ll_no_data.setVisibility(View.VISIBLE);
		} else {
			ll_no_data.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	int i;

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		i = 0;
		initData();
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}

	/***
	 * 打开EventBus开关
	 */
	protected boolean useEventBus() {
		return true;
	}

	/**
	 * 接收消息把已选人的id发过去
	 * 
	 * @param data
	 */
	public void onEvent(mainEvent data) {
		if (data.getData().equals("idss")) {
			selectId = showCheckedItems();
			EventBus.getDefault().post(new ContactSelectIds(selectId));
		}

	}
}
