package com.dssm.esc.model.analytical;

import android.content.Context;

import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;


public interface UserSevice {
	/**
	 * 登陆方法
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param listenser
	 */
	void login(String userName, String password,
			   UserSeviceImpl.UserSeviceImplListListenser listenser);
	/**
	 * 重新登录
	 * @param userName
	 * @param password
	 * @param roleId
	 * @param listenser
	 */
void relogin(String userName, String password, String roleId,
			 UserSeviceImpl.UserSeviceImplListListenser listenser);
	/**
	 * 选择角色
	 * 
	 *            角色id
	 * @param listenser
	 */
	void loginRole(String roleId, UserSeviceImpl.UserSeviceImplBackBooleanListenser listenser);
	/**
	 * 根据用户id获取用户姓名
	 * @param userId
	 * @param listenser
	 */
	void getUserNameByID(String userId, UserSeviceImpl.UserSeviceImplBackValueListenser listenser);

	/**
	 * 1.1.1查询所有消息（第一次查询）
	 * 
	 *            1,任务通知2,系统通知3,紧急通知4,我的消息
	 * @param isconfirm
	 *            是否收到通知,true：查询已经接收确认过的消息，false：查询没有接收确认过的消息，不传递此参数查询全部
	 * @param listenser
	 */
	void getMessageList(Context context, String msgType, String isconfirm,
						UserSeviceImpl.UserSeviceImplListListenser listenser);

	/**
	 * 1.1.2根据消息类型查询消息
	 * 
	 * @param msgType
	 *            app消息类型（1：任务通知，2：系统通知，3：我的消息，4：紧急通知）
	  * @param isconfirm
	 *            是否收到通知,true：查询已经接收确认过的消息，false：查询没有接收确认过的消息，不传递此参数查询全部
	 * @param listenser
	 */
	void getFrashMessageList(Context context, String msgType, String isconfirm, String tag,
							 UserSeviceImpl.UserSeviceImplListListenser listenser);

	/**
	 * 1.1.3根据消息类型批量确认未读消息
	 * 
	 * @param msgType
	 *            app消息类型（1：任务通知，2：系统通知，3：我的消息，4：紧急通知）
	 * @param listenser
	 */
	void confirMsg(String msgType, UserSeviceImpl.UserSeviceImplBackBooleanListenser listenser);

	
	/**
	 * 6.2.1预案查询
	 * @param name
	 * @param listenser
	 */
	void getSearchPlanList(String name, String id, UserSeviceImpl.UserSeviceImplListListenser listenser);
	/**
	 * 1.1.6用户权限控制
	 * @param listenser
	 */
	void getUserPower(UserSeviceImpl.UserSeviceImplListListenser listenser);
	/**
	 * 获取执行人信息
	 * @param executePeopleId
	 */
	void getUserMessageByPersonalId(String executePeopleId, UserSeviceImpl.UserSeviceImplListListenser listListenser);
}
