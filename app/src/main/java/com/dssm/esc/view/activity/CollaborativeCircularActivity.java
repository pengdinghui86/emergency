package com.dssm.esc.view.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
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
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl.EmergencySeviceImplListListenser;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.model.entity.emergency.SendNoticyEntity;
import com.dssm.esc.util.CharacterParser;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.activity.BaseActivity.onInitNetListener;
import com.dssm.esc.view.adapter.ExpanListChexboxMulselectAdapter;
import com.dssm.esc.view.widget.ClearEditText;

/**
 * 协同通告界面
 * 
 * @Description TODO
 * @author Zsj
 * @date 2015-9-14
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 *             Ltd. Inc. All rights reserved.
 */
public class CollaborativeCircularActivity extends BaseActivity implements
		OnClickListener, onInitNetListener {
	/** 标题 */
	@ViewInject(id = R.id.tv_actionbar_title)
	private TextView mSelectTypeTitle;
	/** 确定 */
	@ViewInject(id = R.id.tv_actionbar_editData)
	private TextView mSelectConfirm;
	/** 返回 */
	@ViewInject(id = R.id.iv_actionbar_back)
	private ImageView mBack;
	/** 可扩展ListView */
	@ViewInject(id = R.id.collab_expandlistview)
	private ExpandableListView expandableList;
	/** 可扩展ListView适配器 */
	private ExpanListChexboxMulselectAdapter adapter;
	/** 父list显示组 */
	private List<GroupEntity> groupList =new ArrayList<GroupEntity>();
	/** 子list显示人 */
	private List<ChildEntity> childList = new ArrayList<ChildEntity>();
	/** 被选中的人员的id */
	public List<String> selectId = new ArrayList<String>();
	/** 可清除的EditText */
	@ViewInject(id = R.id.filter_edit_c)
	private ClearEditText myClearEt;
	/** 显示未搜索到 */
	@ViewInject(id = R.id.no_search_result_tv_c)
	private TextView noSearchResultTv;
	private String planInfoId = "";
	private String precautionId = "";
	private SendNoticyEntity entity;
	private String ids = "";
	/** 汉字转换成拼音的类 */
	private CharacterParser characterParser = CharacterParser.getInstance();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				groupList = (List<GroupEntity>) msg.obj;
				Log.i("result", groupList.size() + "");
				adapter = new ExpanListChexboxMulselectAdapter(groupList,
						CollaborativeCircularActivity.this);
				expandableList.setAdapter(adapter);
				for (int i = 0; i < groupList.size(); i++) {
					GroupEntity groupEntity = groupList.get(i);
					List<ChildEntity> getcList = groupEntity.getcList();
					for (int j = 0; j < getcList.size(); j++) {
						ChildEntity childEntity = getcList.get(j);
						searchChildItemData(childEntity);
					}
					//Collections.sort(getcList, pinyinComparator2);
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
		setContentView(R.layout.activity_collaborativecircular);
		View findViewById = findViewById(R.id.collaborativecircular);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		entity = (SendNoticyEntity) intent.getSerializableExtra("entity");
		planInfoId = intent.getStringExtra("planInfoId");
		precautionId = intent.getStringExtra("precautionId");
		expandableList.setGroupIndicator(null);
		initView();
	}

	private void initView() {
		mSelectTypeTitle.setText("协同通告");
		mSelectConfirm.setVisibility(View.VISIBLE);
		mSelectConfirm.setText("发送");
		mBack.setVisibility(View.VISIBLE);
		mSelectConfirm.setOnClickListener(this);
		if (groupList.size() == 0) {
			initData();
		}
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
		//segmentControlListDate();
//		setNetListener(this);
	}
	/**
	 * 
	 * 子list正则过滤，用于搜索
	 * 
	 * @version 1.0
	 * @createTime 2015-9-9,下午5:49:34
	 * @updateTime 2015-9-9,下午5:49:34
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param childrenItem
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
	 * 
	 * 父list正则过滤，用于搜索
	 * 
	 * @version 1.0
	 * @createTime 2015-9-9,下午5:50:13
	 * @updateTime 2015-9-9,下午5:50:13
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param groupItem
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
					//Collections.sort(childFilterList, pinyinComparator2);
				}

			}

			// 根据a-z进行排序
		//	Collections.sort(groupList, pinyinComparator);

		}

		if (adapter != null) {
			adapter.updateListView(groupFilterList);

			if (TextUtils.isEmpty(filterStr)) {
				for (int i = 0; i < groupFilterList.size(); i++) {
					if (i == 0) {
						expandableList.expandGroup(i);
						continue;
					}
					expandableList.collapseGroup(i);
				}
			} else {
				// 搜索的结果全部展开
				for (int i = 0; i < groupFilterList.size(); i++) {
					expandableList.expandGroup(i);
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
				CollaborativeCircularActivity.this, "", Const.LOAD_MESSAGE);
		esevice.getEmergencyGropData(planInfoId, precautionId,
				new EmergencySeviceImplListListenser() {

					@Override
					public void setEmergencySeviceImplListListenser(
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
							ToastUtil.showToast(
									CollaborativeCircularActivity.this,
									Const.NETWORKERROR + ":" + Exceptionerror);
						}
						Message message = new Message();
						message.what = 0;
						message.obj = dataList;
						handler.sendMessage(message);
//						if (Utils.getInstance().progressDialog.isShowing()) {
							Utils.getInstance().hideProgressDialog();
//						}
					}
				});
	}

	/**
	 * 
	 * 所选列表值的id
	 * 
	 * @version 1.0
	 * @createTime 2015-9-8,下午8:42:01
	 * @updateTime 2015-9-8,下午8:42:01
	 * @createAuthor Zsj
	 * @updateAuthor
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	private List<String> showCheckedItems() {
		String checkedItems = "";
		List<String> checkedChildren = adapter.getCheckedChildren();
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
			if (!ids.equals("")) {

				entity.setId(ids);
				Utils.getInstance().showProgressDialog(
						CollaborativeCircularActivity.this, "",
						Const.SUBMIT_MESSAGE);
				esevice.sendNotice(entity,
						new EmergencySeviceImplBackBooleanListenser() {

							@Override
							public void setEmergencySeviceImplListenser(
									Boolean backflag, String stRerror,
									String Exceptionerror) {
								// TODO Auto-generated method stub
								if (backflag) {
									ToastUtil.showToast(
											CollaborativeCircularActivity.this,
											stRerror);
									finish();
								} else if (backflag == false) {
									ToastUtil.showToast(CollaborativeCircularActivity.this,
											stRerror);
								} else if (stRerror != null) {

									ToastUtil.showLongToast(CollaborativeCircularActivity.this,
											stRerror);
								} else if (Exceptionerror != null) {

									ToastUtil.showLongToast(CollaborativeCircularActivity.this,
											Const.NETWORKERROR + Exceptionerror);
								}
//								if (Utils.getInstance().progressDialog
//										.isShowing()) {
									Utils.getInstance().hideProgressDialog();
//								}
							}
						});
			} else {
				ToastUtil.showToast(CollaborativeCircularActivity.this,
						"发送人不能为空");
			}
			break;

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		groupList.clear();
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initData();
	}

}
