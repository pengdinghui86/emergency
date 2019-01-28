package com.dssm.esc.view.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.controler.Control;
import com.dssm.esc.model.analytical.ContactListService;
import com.dssm.esc.model.analytical.implSevice.ContactListServiceImpl;
import com.dssm.esc.model.entity.emergency.ChildEntity;
import com.dssm.esc.model.entity.emergency.GroupEntity;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.MySharePreferencesService;
import com.dssm.esc.util.ToastUtil;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.activity.MainActivity;
import com.dssm.esc.DemoApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 个人中心
 *
 */
public class PersonalCenterFragment extends BaseFragment implements
		OnClickListener {
	private TextView title;
	private TextView name;
	private TextView personal_center_tv_role;
	private TextView personal_center_tv_post;
	private TextView personal_center_tv_department;
	private TextView personal_center_tv_email;
	private ImageView personal_center_iv_header;
	private Button personal_center_bt_change_role;
	private Button personal_center_bt_logout;
	private Context context;
	private MySharePreferencesService preferencesService;
	private Map<String, String> map;
	private ContactListService contactListService;

	public PersonalCenterFragment() {
	}
	@SuppressLint("ValidFragment")
	public PersonalCenterFragment(Context context) {
		this.context = context;
		preferencesService = new MySharePreferencesService(DemoApplication.applicationContext);
		// 回显
		map = preferencesService.getPreferences();
	}

	@Override
	protected View getViews() {
		// TODO Auto-generated method stub
		return view_Parent = LayoutInflater.from(context).inflate(
				R.layout.fragment_personal_center, null);
	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		title = (TextView) view_Parent.findViewById(R.id.tv_actionbar_title);
		name = (TextView) view_Parent.findViewById(R.id.personal_center_tv_name);
		personal_center_tv_role = (TextView) view_Parent.findViewById(R.id.personal_center_tv_role);
		personal_center_tv_post = (TextView) view_Parent.findViewById(R.id.personal_center_tv_post);
		personal_center_tv_department = (TextView) view_Parent.findViewById(R.id.personal_center_tv_department);
		personal_center_tv_email = (TextView) view_Parent.findViewById(R.id.personal_center_tv_email);
		personal_center_iv_header = (ImageView) view_Parent.findViewById(R.id.personal_center_iv_header);
		personal_center_bt_change_role = (Button) view_Parent.findViewById(R.id.personal_center_bt_change_role);
        personal_center_bt_change_role.setOnClickListener(this);
		personal_center_bt_logout = (Button) view_Parent.findViewById(R.id.personal_center_bt_logout);
        personal_center_bt_logout.setOnClickListener(this);
		title.setText("我的");

	}

	@Override
	protected void widgetListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		name.setText(map.get("name").toString());
		personal_center_tv_role.setText(map.get("selectedRolemName").toString());
		personal_center_tv_post.setText("");
		personal_center_tv_department.setText("");
		personal_center_tv_email.setText("");

	}

	@Override
	public void initGetData() {
		// TODO Auto-generated method stub
		contactListService = Control.getinstance().getContactSevice();
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Utils.getInstance().showProgressDialog(getActivity(), "",
				Const.LOAD_MESSAGE);
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
				showInfo(dataList);
			} else if (stRerror != null) {
				dataList = new ArrayList<GroupEntity>();

			} else if (Exceptionerror != null) {
				dataList = new ArrayList<GroupEntity>();
				ToastUtil.showToast(getActivity(),
						Const.REQUESTERROR);
			}
			Utils.getInstance().hideProgressDialog();
		}
	};

	private void showInfo(final List<GroupEntity> dataList)
	{
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				for(GroupEntity groupEntity : dataList) {
					boolean flag = false;
					for(ChildEntity childEntity : groupEntity.getcList()) {
						if(childEntity.getUserId().toString().equals(map.get("userId").toLowerCase())) {
							personal_center_tv_post.setText(childEntity.getZhiwei().toString());
							personal_center_tv_department.setText(childEntity.getEmergTeam().toString());
							personal_center_tv_email.setText(childEntity.getEmail().toString());
							if("女".equals(childEntity.getEmail().toString()))
								personal_center_iv_header.setImageResource(R.drawable.m_p_w);
							else
								personal_center_iv_header.setImageResource(R.drawable.m_p_m);
							flag = true;
							break;
						}
					}
					if(flag)
						break;
				}
			}
		});
	}

	public void updateRoleName()
	{
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				map = preferencesService.getPreferences();
				personal_center_tv_role.setText(map.get("selectedRolemName").toString());

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.personal_center_bt_change_role:
				if(getActivity() instanceof MainActivity){
					((MainActivity)getActivity()).changeRoles();
				}
				break;
			case R.id.personal_center_bt_logout:
				AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity());
				adBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
						if(getActivity() instanceof MainActivity){
							((MainActivity)getActivity()).logout();
						}
					}
				});
				adBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});
				adBuilder.setTitle("提示");
				adBuilder.setMessage("确定退出当前账号");
				adBuilder.setCancelable(true);
				adBuilder.show();
				break;
		}
	}
}