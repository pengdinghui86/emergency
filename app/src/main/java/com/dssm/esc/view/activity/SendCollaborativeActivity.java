package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.entity.emergency.BusinessTypeEntity;
import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.model.entity.emergency.RecieveListEntity;
import com.dssm.esc.model.entity.emergency.SendNoticyEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.JsonUtil;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.adapter.RecieveListAdapter;
import com.dssm.esc.view.widget.MyScrollView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * 协同通告发送数据界面
 * 
 * @author zsj
 * 
 */
@ContentView(R.layout.activity_sendcollaborate)
public class SendCollaborativeActivity extends BaseActivity implements
		OnClickListener, MainActivity.onInitNetListener {
	/** 1，协同；2，通告(SegmentControl点击) */
	private int sem_tags2;
	@ViewInject(R.id.rb_failsafe)
	private RadioButton rb_failsafe;
	@ViewInject(R.id.rb_show)
	private RadioButton rb_show;
	/** 协同布局 */
	@ViewInject(R.id.cooridiration_ll)
	private LinearLayout cooridiration_ll;
	/** 联系人布局 */
	@ViewInject(R.id.contact_people_ll)
	private LinearLayout contact_people_ll;
	/** 联系人布局上方的视图 */
	@ViewInject(R.id.contact_people_v)
	private View contact_people_v;
	/** 要发送的联系人 */
	@ViewInject(R.id.contact_people)
	private TextView contact_people;
	/** 发送对象布局 */
	@ViewInject(R.id.send_object_ll)
	private LinearLayout send_object_ll;
	/** 发送对象 */
	@ViewInject(R.id.send_object)
	private TextView send_object;

	/** 添加联系人的布局 */
	@ViewInject(R.id.pell)
	private LinearLayout pell;

	/** 阶段布局 */
	@ViewInject(R.id.stage_ll)
	private LinearLayout stage_ll;
	/** 阶段 */
	@ViewInject(R.id.stage)
	private TextView stagetv;
	/** 系统的布局 */
	@ViewInject(R.id.xitong_ll1)
	private LinearLayout xitong_ll;
	/** 短信的布局 */
	@ViewInject(R.id.message_ll1)
	private LinearLayout message_ll;
	/** 邮件的布局 */
	@ViewInject(R.id.email_ll1)
	private LinearLayout email_ll;
	/** APP的布局 */
	@ViewInject(R.id.APP_ll1)
	private LinearLayout APP_ll;
	/** 系统 */
	@ViewInject(R.id.xitong_tg)
	private ToggleButton xitong;
	/** 短信 */
	@ViewInject(R.id.shortmessage_tg)
	private ToggleButton message;
	/** 邮件 */
	@ViewInject(R.id.email_tg)
	private ToggleButton email;
	/** APP */
	@ViewInject(R.id.app_tg)
	private ToggleButton APP;
	/** 被选中的人员的id */
	// public ArrayList<String> selectId = new ArrayList<String>();
	// private String tag = "";
	private String stage = "";
	private String type = "";
	/** 通告：发送对象（对内对外对监管） */
	private String sendString = "";
	private String precautionId = "";
	private String stageString = "";
	@ViewInject(R.id.edit_message)
	private EditText edit_message;

	/** 发送方式 */
	// private String sendType = "";
	/** 发送内容 */
	private String content = "";
	/** 通知类型 */
	private String busType = "";
	/** 预案执行编号 */
	private String planInfoId = "";
	/** 通告--阶段 */
	private String coorStage = "";
	/** 通告—对象 */
	private String sendObj = "";
	/** 联系人列表 */
	private ArrayList<RecieveListEntity> receviewdataList = new ArrayList<RecieveListEntity>();
	/** 被选中联系人的list */
	private ArrayList<RecieveListEntity> checkList = new ArrayList<RecieveListEntity>();
	String[] sendTypearray = new String[]{"a","a","a","a"};
	private SendNoticyEntity entity = new SendNoticyEntity();
	/** 预案名 */
	private String name = "";
	@ViewInject(R.id.send_tv)
	private TextView send_tv;
	/** 选择的发送对象 */
	private String selectObj = "";
	/** 选择的阶段 */
	private String selectStage = "";
	/** 添加联系人按钮 */
	@ViewInject(R.id.add)
	private TextView add;
	// /** 无联系人时 */
	@ViewInject(R.id.emptytv)
	private TextView empty;
	/** 联系人 */
	@ViewInject(R.id.recieve_listview_a)
	private ListView listview;
	/** 适配器 */
	private RecieveListAdapter mSelectAdapter;
	@ViewInject(R.id.back)
	private ImageView back;
	@ViewInject(R.id.scrollview)
	private MyScrollView scrollview;
	public static final int ADD_OTHER_PEOPLE = 0;
	public static final int ADD_CONTACT_PEOPLE = 1;
	public static final int SEND_OBJECT = 5;
	public static final int SELECT_STAGE = 6;
	//所选联系人ID
	private String contactIds = "";
	//所选联系人名字
	private String contactNames = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_sendcollaborate);
		View findViewById = findViewById(R.id.sendcollaborate);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		precautionId = intent.getStringExtra("precautionId");
		planInfoId = intent.getStringExtra("id");

		initView();
	}
	private void initView() {
		listview.setEmptyView(empty);
		pell.setVisibility(View.GONE);
		sem_tags2 = 1;// 协同
		busType = "collaborNotice";
		send_object_ll.setOnClickListener(this);
		contact_people_ll.setOnClickListener(this);
		// phone_email_ll.setOnClickListener(this);
		stage_ll.setOnClickListener(this);
		xitong.setOnClickListener(this);
		email.setOnClickListener(this);
		message.setOnClickListener(this);
		APP.setOnClickListener(this);
		send_tv.setOnClickListener(this);
		add.setOnClickListener(this);
		rb_failsafe.setOnClickListener(this);
		rb_show.setOnClickListener(this);
		back.setOnClickListener(this);
		//根据listView的item数量动态设置高度，因此不需要listView的滚动事件
//		listview.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_UP){
//                	scrollview.requestDisallowInterceptTouchEvent(false);
//                }else{
//                	scrollview.requestDisallowInterceptTouchEvent(true);
//                }
//                return false;
//            }
//        });
	}

	private void initMesgData(int sem_tags2) {
		if (sem_tags2 == 1) {
			cooridiration_ll.setVisibility(View.GONE);
			contact_people_v.setVisibility(View.VISIBLE);
			contact_people_ll.setVisibility(View.VISIBLE);
		} else if (sem_tags2 == 2) {
			contact_people_v.setVisibility(View.GONE);
			contact_people_ll.setVisibility(View.GONE);
			cooridiration_ll.setVisibility(View.VISIBLE);
			if (sendString.equals("对内")) {
				type = "1";
				contact_people_v.setVisibility(View.VISIBLE);
				contact_people_ll.setVisibility(View.VISIBLE);
				pell.setVisibility(View.GONE);

			} else if (sendString.equals("对外")) {
				type = "2";

				pell.setVisibility(View.GONE);

			} else if (sendString.equals("对监管")) {
				type = "3";

				pell.setVisibility(View.VISIBLE);
			}
		}

	}

	/** 带回来的集合 */
	List<BusinessTypeEntity> resutList1 = new ArrayList<BusinessTypeEntity>();
	List<BusinessTypeEntity> resutList2 = new ArrayList<BusinessTypeEntity>();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.rb_failsafe:
			sem_tags2 = 1;
			contactIds = "";
			contactNames = "";
			// 协同:
			busType = "collaborNotice";
			initMesgData(sem_tags2);
			break;
		case R.id.rb_show:
			sem_tags2 = 2;
			contactIds = "";
			contactNames = "";
			// 通告:
			busType = "displayNotice";
			initMesgData(sem_tags2);
			break;
		case R.id.send_tv://发送
			sendNotice();
			break;
		case R.id.contact_people_ll:
			addContactPeople();
			break;
		case R.id.send_object_ll:// 发送对象
			Intent intent1 = new Intent(SendCollaborativeActivity.this,
					EmergencyTypeActivity.class);
			// intent1.putExtra("tag", tag);
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("arrlist", (Serializable) resutList1);
			bundle3.putString("tags", "5");
			intent1.putExtras(bundle3);
			startActivityForResult(intent1, SEND_OBJECT);
			break;
		case R.id.add:
			Intent intent3 = new Intent(SendCollaborativeActivity.this,
					RecieveListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("list", receviewdataList);
			intent3.putExtras(bundle);
			startActivityForResult(intent3, ADD_OTHER_PEOPLE);
			break;
		case R.id.stage_ll:// 阶段
			Intent intent2 = new Intent(SendCollaborativeActivity.this,
					EmergencyTypeActivity.class);
			// intent2.putExtra("tag", tag);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("arrlist", (Serializable) resutList2);
			bundle1.putString("tags", "6");
			intent2.putExtras(bundle1);
			startActivityForResult(intent2, SELECT_STAGE);
			break;
		case R.id.xitong_tg:// 0系统，1邮件，2短信，3 APP，逗号隔开
			if (xitong.isChecked()) {

				xitong_ll
						.setBackgroundResource(R.drawable.ump_select_back_checked_on);
				xitong.setTextColor(getResources().getColor(
						R.color.textColor_selected));
				// sendType = "0";
				// sendTypeList.add("0");
				sendTypearray[0] = "0";
			} else {

				xitong_ll.setBackgroundResource(R.drawable.tvbk_gray);
				 xitong.setTextColor(getResources().getColor(
				 R.color.textColor_unselected));
				sendTypearray[0] = "a";
			}

			break;

		case R.id.shortmessage_tg:
			if (message.isChecked()) {
				message_ll
						.setBackgroundResource(R.drawable.ump_select_back_checked_on);
				message.setTextColor(getResources().getColor(
						R.color.textColor_selected));
				sendTypearray[1] = "2";
			} else {

				message_ll.setBackgroundResource(R.drawable.tvbk_gray);
				 message.setTextColor(getResources().getColor(
				 R.color.textColor_unselected));
				sendTypearray[1] = "a";
			}

			break;
		case R.id.email_tg:
			if (email.isChecked()) {
				email_ll.setBackgroundResource(R.drawable.ump_select_back_checked_on);
				email.setTextColor(getResources().getColor(
						R.color.textColor_selected));
				sendTypearray[2] = "1";
			} else {

				email_ll.setBackgroundResource(R.drawable.tvbk_gray);
				email.setTextColor(getResources().getColor(
						R.color.textColor_unselected));
				sendTypearray[2] = "a";
			}

			Log.i("email_ll1", "email_ll1被点击");

			break;
		case R.id.app_tg:
			if (APP.isChecked()) {
				APP_ll.setBackgroundResource(R.drawable.ump_select_back_checked_on);
				APP.setTextColor(getResources().getColor(
						R.color.textColor_selected));
				sendTypearray[3] = "3";
			} else {

				APP_ll.setBackgroundResource(R.drawable.tvbk_blue);
				// APP.setTextColor(getResources().getColor(
				// R.color.textColor_unselected));
				sendTypearray[3] = "a";
			}

			Log.i("app", sendTypearray[3]);
			break;
		}
	}

	private EmergencyServiceImpl.EmergencySeviceImplListListenser listListener = new EmergencyServiceImpl.EmergencySeviceImplListListenser() {

		@Override
		public void setEmergencySeviceImplListListenser(
				Object object, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated method stub
			GetProjectEveInfoEntity entity = null;
			if (object != null) {
				entity = (GetProjectEveInfoEntity) object;
				edit_message.setText(entity
						.getTradeTypeName());
			} else if (stRerror != null) {
				entity = new GetProjectEveInfoEntity();

			} else if (Exceptionerror != null) {
				entity = new GetProjectEveInfoEntity();
				ToastUtil
						.showToast(
								SendCollaborativeActivity.this,
								Const.NETWORKERROR
										+ ":"
										+ Exceptionerror);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data != null && resultCode == RESULT_OK) {

			switch (requestCode) {
			case ADD_OTHER_PEOPLE:
				@SuppressWarnings("unchecked")
				ArrayList<RecieveListEntity> dataArrayList = (ArrayList<RecieveListEntity>) data
						.getExtras().getSerializable("arrlist");
				receviewdataList.clear();
				receviewdataList.addAll(dataArrayList);

				mSelectAdapter = new RecieveListAdapter(
						SendCollaborativeActivity.this, receviewdataList);
				listview.setAdapter(mSelectAdapter);
				mSelectAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(listview);
				break;
			case ADD_CONTACT_PEOPLE:
				contactIds = data.getExtras().getString("people");
				contactNames = data.getExtras().getString("peopleName");
				contact_people.setText(contactNames);
				break;
			case SEND_OBJECT:
				sendString = "";
				resutList1 = (ArrayList<BusinessTypeEntity>) data
						.getSerializableExtra("arrlist");
				ArrayList<BusinessTypeEntity> typelist = new ArrayList<BusinessTypeEntity>();
				if (resutList1 != null && resutList1.size() > 0) {
					typelist.clear();
					typelist.addAll(resutList1);
					if (typelist.size() > 0) {
						for (int i = 0; i < typelist.size(); i++) {
							BusinessTypeEntity businessTypeEntity = typelist
									.get(i);
							boolean select = businessTypeEntity.isSelect();
							if (select) {
								sendString = businessTypeEntity.getName();
							}
						}
						if (sem_tags2 == 2) {

							if (sendString.equals("对内")) {
								type = "1";
								contact_people_v.setVisibility(View.VISIBLE);
								contact_people_ll.setVisibility(View.VISIBLE);
								pell.setVisibility(View.GONE);
							} else if (sendString.equals("对外")) {
								type = "2";
								contact_people_v.setVisibility(View.GONE);
								contact_people_ll.setVisibility(View.GONE);
								pell.setVisibility(View.GONE);

							} else if (sendString.equals("对监管")) {
								type = "3";
								contact_people_v.setVisibility(View.GONE);
								contact_people_ll.setVisibility(View.GONE);
								pell.setVisibility(View.VISIBLE);
							}
						}
						selectObj = sendString;
						send_object.setText(sendString);

						if (!sendString.equals("") && !stageString.equals("")) {
							Utils.getInstance().showProgressDialog(
									SendCollaborativeActivity.this, "",
									Const.SUBMIT_MESSAGE);
							Control.getinstance().getEmergencyService().getNotiConfigContent(precautionId, type,
									stage, listListener);
						}
					}
				}
				break;
			case SELECT_STAGE:
				stageString = "";
				resutList2 = (ArrayList<BusinessTypeEntity>) data
						.getSerializableExtra("arrlist");
				ArrayList<BusinessTypeEntity> typelist1 = new ArrayList<BusinessTypeEntity>();
				if (resutList2 != null && resutList2.size() > 0) {
					typelist1.clear();
					typelist1.addAll(resutList2);
					if (typelist1.size() > 0) {
						for (int i = 0; i < typelist1.size(); i++) {
							BusinessTypeEntity businessTypeEntity = typelist1
									.get(i);
							boolean select = businessTypeEntity.isSelect();
							if (select) {
								stageString = businessTypeEntity.getName();
							}
						}
						// stageString = typelist1.get(0).getName();
						if (stageString.equals("预案启动")) {
							stage = "1";
						} else if (stageString.equals("决策授权")) {
							stage = "2";
						} else if (stageString.equals("业务验证")) {
							stage = "3";
						} else if (stageString.equals("业务恢复")) {
							stage = "4";
						}
						selectStage = stageString;
						coorStage = stage;
						stagetv.setText(stageString);
						if (!sendString.equals("") && !stageString.equals("")) {
							Log.i("precautionId", precautionId);
							Log.i("type", type);
							Log.i("stage", stage);
							Control.getinstance().getEmergencyService().getNotiConfigContent(precautionId, type,
									stage, listListener);
						}
					}
				}
				break;

			}
		} else {
			Log.i("", "data为空");
			// ToastUtil.showToast(SendCollaborativeActivity.this, "data为空");
		}
	}
	/**
	 * 去掉字符串首尾的逗号
	 */
	public  void split(String s) {
	    String[] strArray = s.split(",");
	    StringBuilder sb = new StringBuilder();
	    for(String tmpStr : strArray) {
	        if(tmpStr.length()>0)
	            sb.append(tmpStr).append(",");
	    }
	    if(sb.toString().endsWith(",")) sb.deleteCharAt(sb.length()-1);
	    System.out.println(sb.toString());
	}
	/***
	 * 发送通告
	 */
	private void sendNotice() {
		entity.setBusType(busType);
		entity.setPlanInfoId(planInfoId);
		content = edit_message.getText().toString().trim();
		String sendType = "";
		for (int i = 0; i < sendTypearray.length; i++) {
			String string = sendTypearray[i];
			if (!string.equals("a")) {
				sendType = sendType + "," + sendTypearray[i];
			}
		}
		if (!content.equals("")) {
			entity.setContent(content);
			if (!sendType.equals("") && sendType.length() > 0) {
				split(sendType);
				if (sendType.subSequence(0, 1).equals(",")) {
					sendType = (String) sendType.subSequence(1,
							sendType.length());
				}
				Log.i("sendType", sendType);
				entity.setSendType(sendType);
				if (sem_tags2 == 1) {// 协同
					entity.setCoorStage(coorStage);
					entity.setSendObj("[]");
					if (!contactIds.equals("")) {
						entity.setId(contactIds);
						initContent();
					} else {
						ToastUtil.showToast(SendCollaborativeActivity.this,
								"发送人不能为空");
					}
				} else if (sem_tags2 == 2) {// 通告
					if (!sendString.equals("")) {
						if (!coorStage.equals("")) {
							entity.setCoorStage(coorStage);
							// int visibility = phone_email_ll.getVisibility();
							int visibility = pell.getVisibility();

							if (visibility == 0) {// 对监管
								entity.setId("");
								for (int i = 0; i < receviewdataList.size(); i++) {
									RecieveListEntity entity = receviewdataList
											.get(i);
									boolean ischeck = entity.getIschecked();
									if (ischeck) {
										checkList.add(entity);
									}

								}
								if (checkList != null && checkList.size() > 0) {
									sendObj = JsonUtil
											.changeArrayDateToJson(checkList);
									Log.i("sendObj", sendObj + "");
									entity.setSendObj(sendObj);
									initContent();
								} else {
									ToastUtil.showToast(
											SendCollaborativeActivity.this,
											"对监管时发送人的手机/邮箱必填");
								}

							} else if(sendString.equals("对外")) {// 对外
								entity.setId("");
								entity.setSendObj("[]");
								initContent();
							} else {// 对内
								entity.setSendObj("[]");
								if (!contactIds.equals("")) {
									entity.setId(contactIds);
									initContent();
								} else {
									ToastUtil.showToast(SendCollaborativeActivity.this,
											"发送人不能为空");
								}
							}
						} else {
							ToastUtil.showToast(SendCollaborativeActivity.this,
									"阶段必选");
						}
					} else {
						ToastUtil.showToast(SendCollaborativeActivity.this,
								"发送对象必选");
					}
				}
			} else {
				ToastUtil.showToast(SendCollaborativeActivity.this, "发送方式必选");
			}
		} else {
			ToastUtil.showToast(SendCollaborativeActivity.this, "发送内容不能为空");
		}
	}

	/***
	 * 添加联系人
	 */
	private void addContactPeople() {
		Intent intent = new Intent(SendCollaborativeActivity.this,
				CollaborativeCircularActivity.class);
		intent.putExtra("planInfoId", planInfoId);
		intent.putExtra("precautionId", precautionId);
		startActivityForResult(intent, ADD_CONTACT_PEOPLE);
	}

	/***
	 * 根据item数量设置listView的高度
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if(listItem == null)
				continue;
			if (listItem instanceof LinearLayout){
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}else {
				try {
					listItem.measure(0, 0);
					totalHeight += listItem.getMeasuredHeight();
				}catch (NullPointerException e){
					totalHeight += 50; //这里随便写个大小做容错处理
				}
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser listener = new EmergencyServiceImpl.EmergencySeviceImplBackBooleanListenser() {

		@Override
		public void setEmergencySeviceImplListenser(
				Boolean backflag, String stRerror,
				String Exceptionerror) {
			// TODO Auto-generated
			// method
			// stub
			if (backflag) {
				ToastUtil.showToast(SendCollaborativeActivity.this,
						stRerror);
				SendCollaborativeActivity.this.finish();

			} else if (backflag == false) {
				ToastUtil.showToast(SendCollaborativeActivity.this,
						stRerror);
				resetting();
			} else if (stRerror != null) {

				ToastUtil.showToast(SendCollaborativeActivity.this,
						stRerror);
				resetting();
			} else if (Exceptionerror != null) {

				ToastUtil.showToast(SendCollaborativeActivity.this,
						Exceptionerror);
				resetting();
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void initContent() {
		Utils.getInstance().showProgressDialog(
				SendCollaborativeActivity.this, "",
				Const.SUBMIT_MESSAGE);
		Control.getinstance().getEmergencyService().sendNotice(entity, listener);

	}

	private void resetting() {
		for (int i = 0; i < sendTypearray.length; i++) {
			sendTypearray[i] = "a";
		}
		APP_ll.setBackgroundResource(R.drawable.tvbk_blue);
		APP.setTextColor(getResources().getColor(R.color.textColor_unselected));
		email_ll.setBackgroundResource(R.drawable.tvbk_blue);
		email.setTextColor(getResources()
				.getColor(R.color.textColor_unselected));
		xitong_ll.setBackgroundResource(R.drawable.tvbk_blue);
		xitong.setTextColor(getResources().getColor(
				R.color.textColor_unselected));
		message_ll.setBackgroundResource(R.drawable.tvbk_blue);
		message.setTextColor(getResources().getColor(
				R.color.textColor_unselected));
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initContent();
	}

}
