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
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * 协同通告发送界面
 */
@ContentView(R.layout.activity_send_collaboration)
public class SendCollaborationActivity extends BaseActivity implements
		OnClickListener, MainActivity.onInitNetListener {
	/** 1，协同；2，通告 */
	private int sem_tags2;
	@ViewInject(R.id.iv_actionbar_back)
	private ImageView back;
	@ViewInject(R.id.iv_actionbar_send_history)
	private ImageView iv_send_history;
	@ViewInject(R.id.rb_send_collaboration)
	private RadioButton rb_send_collaboration;
	@ViewInject(R.id.rb_send_notice)
	private RadioButton rb_send_notice;
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
	@ViewInject(R.id.send_message_rl_sys)
	private RelativeLayout send_message_rl_sys;
	/** 短信的布局 */
	@ViewInject(R.id.send_message_rl_msg)
	private RelativeLayout send_message_rl_msg;
	/** 邮件的布局 */
	@ViewInject(R.id.send_message_rl_email)
	private RelativeLayout send_message_rl_email;
	/** APP的布局 */
	@ViewInject(R.id.send_message_rl_app)
	private RelativeLayout send_message_rl_app;

	@ViewInject(R.id.send_message_iv_sys)
	private ImageView send_message_iv_sys;
	@ViewInject(R.id.send_message_iv_msg)
	private ImageView send_message_iv_msg;
	@ViewInject(R.id.send_message_iv_email)
	private ImageView send_message_iv_email;
	@ViewInject(R.id.send_message_iv_app)
	private ImageView send_message_iv_app;

	@ViewInject(R.id.send_message_iv_sys_check)
	private ImageView send_message_iv_sys_check;
	@ViewInject(R.id.send_message_iv_msg_check)
	private ImageView send_message_iv_msg_check;
	@ViewInject(R.id.send_message_iv_email_check)
	private ImageView send_message_iv_email_check;
	@ViewInject(R.id.send_message_iv_app_check)
	private ImageView send_message_iv_app_check;

	@ViewInject(R.id.send_message_tv_sys)
	private TextView send_message_tv_sys;
	@ViewInject(R.id.send_message_tv_msg)
	private TextView send_message_tv_msg;
	@ViewInject(R.id.send_message_tv_email)
	private TextView send_message_tv_email;
	@ViewInject(R.id.send_message_tv_app)
	private TextView send_message_tv_app;

	private String stage = "";
	private String type = "";
	/** 通告：发送对象（对内对外对监管） */
	private String sendString = "";
	private String precautionId = "";
	private String stageString = "";
	@ViewInject(R.id.edit_message)
	private EditText edit_message;

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
	/** 联系人 */
	@ViewInject(R.id.recieve_listview_a)
	private ListView listview;
	/** 适配器 */
	private RecieveListAdapter mSelectAdapter;
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
		super.onCreate(savedInstanceState);
		View findViewById = findViewById(R.id.sendcollaborate);
		findViewById.setFitsSystemWindows(true);
		Intent intent = getIntent();
		precautionId = intent.getStringExtra("precautionId");
		planInfoId = intent.getStringExtra("id");

		initView();
	}
	private void initView() {
		pell.setVisibility(View.GONE);
		sem_tags2 = 1;// 协同
		busType = "collaborNotice";
		back.setOnClickListener(this);
		iv_send_history.setOnClickListener(this);
		send_object_ll.setOnClickListener(this);
		contact_people_ll.setOnClickListener(this);
		// phone_email_ll.setOnClickListener(this);
		stage_ll.setOnClickListener(this);
		send_message_rl_sys.setOnClickListener(this);
		send_message_rl_msg.setOnClickListener(this);
		send_message_rl_email.setOnClickListener(this);
		send_message_rl_app.setOnClickListener(this);
		send_tv.setOnClickListener(this);
		add.setOnClickListener(this);
		rb_send_collaboration.setOnClickListener(this);
		rb_send_notice.setOnClickListener(this);
		back.setOnClickListener(this);
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
				send_tv.setVisibility(View.VISIBLE);
			} else if (sendString.equals("对外")) {
				type = "2";
				pell.setVisibility(View.GONE);
				send_tv.setVisibility(View.GONE);
			} else if (sendString.equals("对监管")) {
				type = "3";
				pell.setVisibility(View.VISIBLE);
				send_tv.setVisibility(View.VISIBLE);
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
			case R.id.iv_actionbar_back:
				finish();
				break;
			case R.id.iv_actionbar_send_history:
				//协同与通告通知历史信息
				Intent intent = new Intent(this, HistoryNoticeActivity.class);
				intent.putExtra("type", "2");
				intent.putExtra("planInfoId", planInfoId);
				startActivity(intent);
				break;
			case R.id.rb_send_collaboration:
				sem_tags2 = 1;
				contactIds = "";
				contactNames = "";
				// 协同:
				busType = "collaborNotice";
				rb_send_collaboration.setChecked(true);
				rb_send_notice.setChecked(false);
				send_tv.setVisibility(View.VISIBLE);
				initMesgData(sem_tags2);
				break;
			case R.id.rb_send_notice:
				sem_tags2 = 2;
				contactIds = "";
				contactNames = "";
				// 通告:
				busType = "displayNotice";
				rb_send_collaboration.setChecked(false);
				rb_send_notice.setChecked(true);
				initMesgData(sem_tags2);
				break;
			case R.id.send_tv://发送
				sendNotice();
				break;
			case R.id.contact_people_ll:
				addContactPeople();
				break;
			case R.id.send_object_ll:// 发送对象
				Intent intent1 = new Intent(SendCollaborationActivity.this,
						EmergencyTypeActivity.class);
				// intent1.putExtra("tag", tag);
				Bundle bundle3 = new Bundle();
				bundle3.putSerializable("arrlist", (Serializable) resutList1);
				bundle3.putString("tags", "5");
				intent1.putExtras(bundle3);
				startActivityForResult(intent1, SEND_OBJECT);
				break;
			case R.id.add:
				Intent intent3 = new Intent(SendCollaborationActivity.this,
						RecieveListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("list", receviewdataList);
				intent3.putExtras(bundle);
				startActivityForResult(intent3, ADD_OTHER_PEOPLE);
				break;
			case R.id.stage_ll:// 阶段
				Intent intent2 = new Intent(SendCollaborationActivity.this,
						EmergencyTypeActivity.class);
				// intent2.putExtra("tag", tag);
				Bundle bundle1 = new Bundle();
				bundle1.putSerializable("arrlist", (Serializable) resutList2);
				bundle1.putString("tags", "6");
				intent2.putExtras(bundle1);
				startActivityForResult(intent2, SELECT_STAGE);
				break;
			case R.id.send_message_rl_sys:// 0系统，1邮件，2短信，3 APP，逗号隔开
				if (send_message_iv_sys_check.getVisibility() == View.INVISIBLE) {
					send_message_rl_sys.setBackgroundResource(R.drawable.btbg_blue);
					send_message_tv_sys.setTextColor(getResources().getColor(
							R.color.white));
					send_message_iv_sys.setImageResource(R.drawable.system_select);
					send_message_iv_sys_check.setVisibility(View.VISIBLE);
					sendTypearray[0] = "0";
				} else {
					send_message_rl_sys.setBackgroundResource(R.drawable.tvbk_gray);
					send_message_tv_sys.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					send_message_iv_sys.setImageResource(R.drawable.system);
					send_message_iv_sys_check.setVisibility(View.INVISIBLE);
					sendTypearray[0] = "a";
				}
				break;
			case R.id.send_message_rl_msg:
				if (send_message_iv_msg_check.getVisibility() == View.INVISIBLE) {
					send_message_rl_msg
							.setBackgroundResource(R.drawable.btbg_blue);
					send_message_tv_msg.setTextColor(getResources().getColor(
							R.color.white));
					send_message_iv_msg.setImageResource(R.drawable.short_message_select);
					send_message_iv_msg_check.setVisibility(View.VISIBLE);
					sendTypearray[1] = "2";
				} else {
					send_message_rl_msg.setBackgroundResource(R.drawable.tvbk_gray);
					send_message_tv_msg.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					send_message_iv_msg.setImageResource(R.drawable.short_message);
					send_message_iv_msg_check.setVisibility(View.INVISIBLE);
					sendTypearray[1] = "a";
				}
				break;
			case R.id.send_message_rl_email:
				if (send_message_iv_email_check.getVisibility() == View.INVISIBLE) {
					send_message_rl_email.setBackgroundResource(R.drawable.btbg_blue);
					send_message_tv_email.setTextColor(getResources().getColor(
							R.color.white));
					send_message_iv_email.setImageResource(R.drawable.email_select);
					send_message_iv_email_check.setVisibility(View.VISIBLE);
					sendTypearray[2] = "1";
				} else {
					send_message_rl_email.setBackgroundResource(R.drawable.tvbk_gray);
					send_message_tv_email.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					send_message_iv_email.setImageResource(R.drawable.email);
					send_message_iv_email_check.setVisibility(View.INVISIBLE);
					sendTypearray[2] = "a";
				}
				break;
			case R.id.send_message_rl_app:
				if (send_message_iv_app_check.getVisibility() == View.INVISIBLE) {
					send_message_rl_app.setBackgroundResource(R.drawable.btbg_blue);
					send_message_tv_app.setTextColor(getResources().getColor(
							R.color.white));
					send_message_iv_app.setImageResource(R.drawable.app_select);
					send_message_iv_app_check.setVisibility(View.VISIBLE);
					sendTypearray[3] = "3";
				} else {
					send_message_rl_app.setBackgroundResource(R.drawable.tvbk_blue);
					send_message_tv_app.setTextColor(getResources().getColor(
							R.color.textColor_unselected));
					send_message_iv_app.setImageResource(R.drawable.app);
					send_message_iv_app_check.setVisibility(View.INVISIBLE);
					sendTypearray[3] = "a";
				}
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
								SendCollaborationActivity.this,
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
						SendCollaborationActivity.this, receviewdataList);
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
								send_tv.setVisibility(View.VISIBLE);
							} else if (sendString.equals("对外")) {
								type = "2";
								contact_people_v.setVisibility(View.GONE);
								contact_people_ll.setVisibility(View.GONE);
								pell.setVisibility(View.GONE);
								send_tv.setVisibility(View.GONE);
							} else if (sendString.equals("对监管")) {
								type = "3";
								contact_people_v.setVisibility(View.GONE);
								contact_people_ll.setVisibility(View.GONE);
								pell.setVisibility(View.VISIBLE);
								send_tv.setVisibility(View.VISIBLE);
							}
						}
						selectObj = sendString;
						send_object.setText(sendString);

						if (!sendString.equals("") && !stageString.equals("")) {
							Utils.getInstance().showProgressDialog(
									SendCollaborationActivity.this, "",
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
							Control.getinstance().getEmergencyService().getNotiConfigContent(precautionId, type,
									stage, listListener);
						}
					}
				}
				break;

			}
		} else {
			Log.i("", "data为空");
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
						ToastUtil.showToast(SendCollaborationActivity.this,
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
											SendCollaborationActivity.this,
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
									ToastUtil.showToast(SendCollaborationActivity.this,
											"发送人不能为空");
								}
							}
						} else {
							ToastUtil.showToast(SendCollaborationActivity.this,
									"阶段必选");
						}
					} else {
						ToastUtil.showToast(SendCollaborationActivity.this,
								"发送对象必选");
					}
				}
			} else {
				ToastUtil.showToast(SendCollaborationActivity.this, "发送方式必选");
			}
		} else {
			ToastUtil.showToast(SendCollaborationActivity.this, "发送内容不能为空");
		}
	}

	/***
	 * 添加联系人
	 */
	private void addContactPeople() {
		Intent intent = new Intent(SendCollaborationActivity.this,
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
				ToastUtil.showToast(SendCollaborationActivity.this,
						stRerror);
				SendCollaborationActivity.this.finish();

			} else if (backflag == false) {
				ToastUtil.showToast(SendCollaborationActivity.this,
						stRerror);
				resetting();
			} else if (stRerror != null) {

				ToastUtil.showToast(SendCollaborationActivity.this,
						stRerror);
				resetting();
			} else if (Exceptionerror != null) {

				ToastUtil.showToast(SendCollaborationActivity.this,
						Exceptionerror);
				resetting();
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void initContent() {
		Utils.getInstance().showProgressDialog(
				SendCollaborationActivity.this, "",
				Const.SUBMIT_MESSAGE);
		Control.getinstance().getEmergencyService().sendNotice(entity, listener);

	}

	private void resetting() {
		for (int i = 0; i < sendTypearray.length; i++) {
			sendTypearray[i] = "a";
		}
		send_message_rl_sys.setBackgroundResource(R.drawable.tvbk_blue);
		send_message_tv_sys.setTextColor(getResources().getColor(R.color.textColor_unselected));
		send_message_iv_sys.setImageResource(R.drawable.system);
		send_message_iv_sys_check.setVisibility(View.INVISIBLE);
		send_message_rl_msg.setBackgroundResource(R.drawable.tvbk_blue);
		send_message_tv_msg.setTextColor(getResources()
				.getColor(R.color.textColor_unselected));
		send_message_iv_msg.setImageResource(R.drawable.short_message);
		send_message_iv_msg_check.setVisibility(View.INVISIBLE);
		send_message_rl_email.setBackgroundResource(R.drawable.tvbk_blue);
		send_message_tv_email.setTextColor(getResources().getColor(
				R.color.textColor_unselected));
		send_message_iv_email.setImageResource(R.drawable.email);
		send_message_iv_email_check.setVisibility(View.INVISIBLE);
		send_message_rl_app.setBackgroundResource(R.drawable.tvbk_blue);
		send_message_tv_app.setTextColor(getResources().getColor(
				R.color.textColor_unselected));
		send_message_iv_app.setImageResource(R.drawable.app);
		send_message_iv_app_check.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initNetData() {
		// TODO Auto-generated method stub
		initContent();
	}

}
