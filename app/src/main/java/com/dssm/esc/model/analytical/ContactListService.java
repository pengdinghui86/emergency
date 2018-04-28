package com.dssm.esc.model.analytical;


import com.dssm.esc.model.analytical.implSevice.ContactListServiceImpl;

public interface ContactListService {
	/**
	 * 发送应急消息
	 * 
	 * @param postId
	 * @param sendType
	 * @param content
	 * @param listenser
	 */
	void sendMessage(String postId, String sendType, String content,
					 ContactListServiceImpl.ContactSeviceImplBackBooleanListenser listenser);
	/**
	 * 发送紧急通知消息
	 * 
	 * @param postId
	 * @param sendType
	 * @param content
	 * @param listenser
	 */
	void sendNotice(String postId, String sendType, String content,
					ContactListServiceImpl.ContactSeviceImplBackBooleanListenser listenser);

	/**
	 * 获取紧急通讯录
	 * 
	 * @param listenser
	 */
	void getEmergencyContactList(ContactListServiceImpl.ContactSeviceImplListListenser listenser);
	/**
	 * 获取通知通讯录
	 * 
	 * @param listenser
	 */
	void getToastContactList(ContactListServiceImpl.ContactSeviceImplListListenser listenser);
}
