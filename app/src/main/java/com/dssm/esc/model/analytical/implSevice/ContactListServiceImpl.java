package com.dssm.esc.model.analytical.implSevice;

import android.util.Log;

import com.dssm.esc.model.analytical.ContactListService;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.model.jsonparser.contact.GetEmergencyContactListParser;
import com.dssm.esc.model.jsonparser.contact.GetToastListParser;
import com.dssm.esc.model.jsonparser.contact.SendMessageParser;
import com.dssm.esc.model.jsonparser.contact.SendNoticeParser;
import com.dssm.esc.util.Const;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class ContactListServiceImpl implements ContactListService {
	private static ContactListServiceImpl contactListServiceImpl = null;

	/**
	 * 获取EmergencyServiceImpl对象
	 * 
	 * @return
	 */
	public synchronized static ContactListServiceImpl getContacteviceImpl() {
		// TODO Auto-generated method stub
		if (contactListServiceImpl == null) {
			contactListServiceImpl = new ContactListServiceImpl();
		}
		return contactListServiceImpl;
	}

	/**
	 * 不允许直接访问构造方法
	 */
	private ContactListServiceImpl() {

	}

	/**
	 * 设置Boolean值返回数据
	 * 
	 * @param listenser
	 * @param object
	 * @param error
	 */
	private void setContactBooleanListenser(
			ContactSeviceImplBackBooleanListenser listenser,
			Object object, String error) {
		boolean flag = false;
		String stRerror = null;
		String Exceptionerror = null;
		if (object != null) {
			Map<String, String> map = (Map<String, String>) object;
			if (map.containsKey("success") && map.containsKey("message")) {
				if (map.get("success").equals("true")) {
					flag = true;
				} else {
					flag = false;
				}
				stRerror = map.get("message");
			} else {
				stRerror = "未访问到数据";
			}
		} else {
			Exceptionerror = error;
		}
		listenser.setContactSeviceImplListenser(flag, stRerror, Exceptionerror);
	}

	/**
	 * 设置集合返回数据
	 * 
	 * @param listenser
	 * @param object
	 * @param error
	 */
	private void setContactListListenser(
			ContactSeviceImplListListenser listenser, Object object,
			String error) {
		List<Object> list = null;
		String Exceptionerror = null;
		if (object != null) {
			list = (List<Object>) object;

		} else {
			Exceptionerror = error;
		}
		Log.i("setContactListListenser", "setContactListListenser" + list);
		listenser.setContactSeviceImplListListenser(list, null, Exceptionerror);
	}

	public interface ContactSeviceImplBackValueListenser {
		/**
		 * 当backValue！=null时stRerror，Exceptionerror都为null；
		 * 当stRerror！=null时backValue，Exceptionerror都为null；
		 * 当Exceptionerror！=null时backValue，stRerror都为null； backValue回调得到值
		 * stRerror回调得到接口返回错误信息 Exceptionerror回调得到接口返回异常信息
		 * 
		 * @param stRerror
		 * @param Exceptionerror
		 */
		void setContactSeviceImplListenser(String backValue, String stRerror,
										   String Exceptionerror);
	}

	public interface ContactSeviceImplBackBooleanListenser {
		/**
		 * 当backflag=true,false时stRerror，Exceptionerror都为null；
		 * 当stRerror！=null时backflag=false，Exceptionerror都为null；
		 * 当Exceptionerror！=null时backflag=false，stRerror都为null；
		 * backflag回调得到状态true，false， stRerror回调得到接口返回错误信息
		 * Exceptionerror回调得到接口返回异常信息
		 * 
		 * @param stRerror
		 * @param Exceptionerror
		 */
		void setContactSeviceImplListenser(Boolean backflag, String stRerror,
										   String Exceptionerror);
	}

	public interface ContactSeviceImplListListenser {
		/**
		 * 当object=!null时stRerror，Exceptionerror都为null；
		 * 当stRerror！=null时object，Exceptionerror都为null；
		 * 当Exceptionerror！=null时object，stRerror都为null；
		 * 
		 * stRerror回调得到接口返回错误信息 Exceptionerror回调得到接口返回异常信息
		 * 
		 * @param object
		 * @param stRerror
		 * @param Exceptionerror
		 */
		void setContactSeviceImplListListenser(Object object, String stRerror,
											   String Exceptionerror);
	}

	/**
	 * 发送消息
	 */
	@Override
	public void sendMessage(String postId, String sendType, String content,
			ContactSeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		final WeakReference<ContactSeviceImplBackBooleanListenser> wr = new WeakReference<>(listenser);
		if (postId == null) {
			if(wr.get() != null)
				wr.get().setContactSeviceImplListenser(null, Const.PARAMETER_NULL,
					null);
		}
		new SendMessageParser(postId, sendType, content,
			new OnDataCompleterListener() {
				@Override
				public void onEmergencyParserComplete(Object object,
						String error) {
					// TODO Auto-generated method stub
					if(wr.get() != null)
						setContactBooleanListenser(wr.get(), object, error);
				}
			});
	}

	/**
	 * 获取紧急通讯录
	 */
	@Override
	public void getEmergencyContactList(
			ContactSeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		final WeakReference<ContactSeviceImplListListenser> wr = new WeakReference<>(listenser);
		new GetEmergencyContactListParser(new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				if(wr.get() != null)
					setContactListListenser(wr.get(), object, error);
			}
		});
	}

	/**
	 * 获取通知通讯录
	 */
	@Override
	public void getToastContactList(
			ContactSeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		final WeakReference<ContactSeviceImplListListenser> wr = new WeakReference<>(listenser);
		new GetToastListParser(new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				if(wr.get() != null)
					setContactListListenser(wr.get(), object, error);
			}
		});
	}

	/**
	 * 发送紧急通知消息
	 */
	@Override
	public void sendNotice(String postId, String sendType, String content,
			ContactSeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		final WeakReference<ContactSeviceImplBackBooleanListenser> wr = new WeakReference<>(listenser);
		if (postId == null) {
			if(wr.get() != null)
				wr.get().setContactSeviceImplListenser(null, Const.PARAMETER_NULL,
					null);
		}
		new SendNoticeParser(postId, sendType, content,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						if(wr.get() != null)
							setContactBooleanListenser(wr.get(), object, error);
					}
				});
	}

}
