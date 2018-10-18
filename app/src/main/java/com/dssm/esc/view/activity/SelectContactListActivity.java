package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.ContactListService;
import com.dssm.esc.model.analytical.implSevice.ContactListServiceImpl;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.util.CharacterParser;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.activity.BaseActivity.onInitNetListener;
import com.dssm.esc.view.adapter.ExpandListviewContactCheckboxAdapter;
import com.dssm.esc.view.widget.ClearEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人选择界面
 */
@ContentView(R.layout.activity_select_contact_list)
public class SelectContactListActivity extends BaseActivity implements
		OnClickListener, onInitNetListener {
	/** 标题 */
	@ViewInject(R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 确定 */
	@ViewInject(R.id.tv_actionbar_editData)
	private TextView mSelectConfirm;
	/** 返回 */
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView mBack;
	/** 可扩展ListView */
	@ViewInject(R.id.select_contact_list_expandable_lv)
	private ExpandableListView expandableListView;
	/** 适配器 */
	private ExpandListviewContactCheckboxAdapter adapter;
	/** 父list显示组 */
	private List<GroupEntity> groupList = null;
	/** 子list显示人 */
	private List<ChildEntity> childList = new ArrayList<ChildEntity>();
	/** 被选中的人员的id */
	public List<String> selectId = new ArrayList<String>();
	/** 汉字转换成拼音的类 */
	private CharacterParser characterParser = CharacterParser.getInstance();
	/** 显示未搜索到 */
	@ViewInject(R.id.no_search_result_tv_c)
	private TextView noSearchResultTv;
	/** 可清除的edittext */
	@ViewInject(R.id.filter_edit_c)
	private ClearEditText myClearEt;
	private String ids = "";
	private ContactListService contactListService;
	private String checkedNames = "";

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					groupList = (List<GroupEntity>) msg.obj;
					adapter = new ExpandListviewContactCheckboxAdapter(groupList,
							SelectContactListActivity.this);
					expandableListView.setAdapter(adapter);
					if (groupList.size() == 0) {
						expandableListView.setVisibility(View.GONE);
						noSearchResultTv.setVisibility(View.VISIBLE);
					} else {
						expandableListView.setVisibility(View.VISIBLE);
						noSearchResultTv.setVisibility(View.GONE);
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
					break;
				default:
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.select_contact_list);
		findViewById.setFitsSystemWindows(true);
		initView();
	}

	private void initView() {
		mSelectTypeTitle.setText("请选择联系人");
		mSelectConfirm.setVisibility(View.VISIBLE);
		mSelectConfirm.setText("确定");
		mBack.setVisibility(View.VISIBLE);
		expandableListView.setGroupIndicator(null);
		mSelectConfirm.setOnClickListener(this);
		contactListService = Control.getinstance().getContactSevice();
		initData();
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

		Log.i("xx", "dd" + groupList.get(0).getcList().size());
		if (TextUtils.isEmpty(filterStr)) {
			groupFilterList = groupList;
			childFilterList = childList;
			Log.i("xx", "ddxx" + groupList.get(0).getcList().size());
			noSearchResultTv.setVisibility(View.GONE);
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
			adapter.updateListView(groupFilterList);

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
			noSearchResultTv.setVisibility(View.VISIBLE);
		} else {
			noSearchResultTv.setVisibility(View.GONE);
		}
	}

	private void initData() {
		Utils.getInstance().showProgressDialog(
				SelectContactListActivity.this, "", Const.LOAD_MESSAGE);
		contactListService.getEmergencyContactList(contactSeviceImplListListenser);

	}

	private ContactListServiceImpl.ContactSeviceImplListListenser contactSeviceImplListListenser = new ContactListServiceImpl.ContactSeviceImplListListenser() {

		@Override
		public void setContactSeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			List<GroupEntity> dataList = null;
			if (object != null) {
				dataList = (List<GroupEntity>) object;

			} else if (stRerror != null) {
				dataList = new ArrayList<GroupEntity>();

			} else if (Exceptionerror != null) {
				dataList = new ArrayList<GroupEntity>();
				ToastUtil.showToast(SelectContactListActivity.this,
						Const.NETWORKERROR);
			}
			Message message = new Message();
			message.what = 1;
			message.obj = dataList;
			handler.sendMessage(message);
			Utils.getInstance().hideProgressDialog();
		}
	};

	private List<String> showCheckedItems() {
		checkedNames = "";
		List<String> checkedChildren = adapter.getCheckedChildren();
		List<String> checkedChildrenName = adapter.getCheckedChildrenName();
		if (checkedChildrenName != null && !checkedChildrenName.isEmpty()) {
			for (String child : checkedChildrenName) {
				if (checkedNames.length() > 0) {
					checkedNames += "，";
				}
				checkedNames += child;
			}
		}

		return checkedChildren;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_actionbar_editData:// 确定
			selectId = showCheckedItems();
			if (selectId.size() > 0) {
				for (int i = 0; i < selectId.size(); i++) {
					ids = ids + "," + selectId.get(i);
				}
				if (ids.subSequence(0, 1).equals(",")) {
					ids = (String) ids.subSequence(1, ids.length());
				}
			}
			Intent intent = new Intent();
			intent.putExtra("people", ids);
			intent.putExtra("peopleName", checkedNames);
			SelectContactListActivity.this.setResult(RESULT_OK, intent);
			SelectContactListActivity.this.finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}

}
