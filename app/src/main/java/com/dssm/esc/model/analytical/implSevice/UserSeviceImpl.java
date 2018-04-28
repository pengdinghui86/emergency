package com.dssm.esc.model.analytical.implSevice;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.entity.emergency.PlanNameSelectEntity;
import com.dssm.esc.model.entity.user.UserEntity;
import com.dssm.esc.model.entity.user.UserPersonIdEntity;
import com.dssm.esc.model.entity.user.UserPowerEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.model.jsonparser.message.ConfirMessageParser;
import com.dssm.esc.model.jsonparser.message.GetFirstAllMessageListParser;
import com.dssm.esc.model.jsonparser.message.GetMessageListParser;
import com.dssm.esc.model.jsonparser.user.GetPhoneByExecuteParser;
import com.dssm.esc.model.jsonparser.user.GetUserMenuPower;
import com.dssm.esc.model.jsonparser.user.GetUserNameByIdParser;
import com.dssm.esc.model.jsonparser.user.SearchOtherscenPlanParser;
import com.dssm.esc.model.jsonparser.user.UserLoginParse;
import com.dssm.esc.model.jsonparser.user.UserLoginRoleParser;
import com.dssm.esc.model.jsonparser.user.UserReLoginParser;
import com.dssm.esc.util.Const;

public class UserSeviceImpl implements UserSevice {

	private static UserSeviceImpl userSeviceImpl = null;

	/**
	 * 获取UserSeviceImpl对象
	 * 
	 * @return
	 */
	public synchronized static UserSeviceImpl getUserSeviceImpl() {
		// TODO Auto-generated method stub
		if (userSeviceImpl == null) {
			userSeviceImpl = new UserSeviceImpl();
		}
		return userSeviceImpl;
	}

	/**
	 * 不允许直接访问构造方法
	 */
	private UserSeviceImpl() {

	}

	/**
	 * 设置Boolean值返回数据
	 * 
	 * @param listenser
	 * @param object
	 * @param error
	 */
	private void setUserBooleanListenser(
			final UserSeviceImplBackBooleanListenser listenser, Object object,
			String error) {
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
		Log.i("setUserBooleanListenser", "setUserBooleanListenser" + flag);
		listenser.setUserSeviceImplListenser(flag, stRerror, Exceptionerror);
	}

	/**
	 * 设置集合返回数据
	 * 
	 * @param listenser
	 * @param object
	 * @param error
	 */
	private void setUserListListenser(
			final UserSeviceImplListListenser listenser, Object object,
			String error) {
		List<Object> list = null;
		String Exceptionerror = null;
		if (object != null) {
			list = (List<Object>) object;

		} else {
			Exceptionerror = error;
		}
		Log.i("setUserListListenser", "setUserListListenser" + list);
		listenser.setUserSeviceImplListListenser(list, null, Exceptionerror);
	}

	public interface UserSeviceImplBackValueListenser {
		/**
		 * 当backValue！=null时stRerror，Exceptionerror都为null；
		 * 当stRerror！=null时backValue，Exceptionerror都为null；
		 * 当Exceptionerror！=null时backValue，stRerror都为null； backValue回调得到值
		 * stRerror回调得到接口返回错误信息 Exceptionerror回调得到接口返回异常信息
		 * 
		 * @param stRerror
		 * @param Exceptionerror
		 */
		void setUserSeviceImplListenser(String backValue, String stRerror,
										String Exceptionerror);
	}

	public interface UserSeviceImplBackBooleanListenser {
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
		void setUserSeviceImplListenser(Boolean backflag, String stRerror,
										String Exceptionerror);
	}

	public interface UserSeviceImplListListenser {
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
		void setUserSeviceImplListListenser(Object object, String stRerror,
											String Exceptionerror);
	}

	/**
	 * 登录方法
	 */
	@Override
	public void login(String userName, String password,
			final UserSeviceImplListListenser listenser) {
		if (userName == null) {
			listenser.setUserSeviceImplListListenser(null, Const.PARAMETER_NULL, null);
			return;
		}
		new UserLoginParse(userName, password, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				UserEntity userEntity = null;
				String stRerror = null;
				String Exceptionerror = null;
				if (object != null) {
					userEntity = (UserEntity) object;
				} else if (error != null) {
					Exceptionerror = error;
				} else {
					stRerror = "登录失败";
				}

				listenser.setUserSeviceImplListListenser(userEntity, stRerror,
						Exceptionerror);
			}
		});
	}

	/**
	 * 选择角色
	 */
	@Override
	public void loginRole(String roleId,
			final UserSeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (roleId == null) {
			listenser.setUserSeviceImplListenser(null, Const.PARAMETER_NULL,
					null);
			return;
		}
		new UserLoginRoleParser(roleId, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setUserBooleanListenser(listenser, object, error);
			}

		});
	}

	/**
	 * 获取消息列表
	 */
	@Override
	public void getMessageList(Context context, String msgType,
			String isconfirm, final UserSeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		new GetFirstAllMessageListParser(context, msgType, isconfirm,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setUserListListenser(listenser, object, error);
					}
				});
	}

	/**
	 * 确认消息
	 */
	@Override
	public void confirMsg(String msgType,
			final UserSeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (msgType == null) {
			listenser.setUserSeviceImplListenser(null, Const.PARAMETER_NULL,
					null);
			return;
		}
		new ConfirMessageParser(msgType, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setUserBooleanListenser(listenser, object, error);
			}
		});

	}

	/**
	 * 获取刷新的消息列表
	 */
	@Override
	public void getFrashMessageList(Context context, String msgType,
			String isconfirm, String tag,
			final UserSeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		new GetMessageListParser(context, msgType, isconfirm, tag,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setUserListListenser(listenser, object, error);
					}
				});
	}

	/**
	 * 根据用户id获取用户姓名
	 * 
	 * @param userId
	 * @param listenser
	 */
	@Override
	public void getUserNameByID(String userId,
			final UserSeviceImplBackValueListenser listenser) {
		// TODO Auto-generated method stub
		new GetUserNameByIdParser(userId, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				String name = null;
				String stRerror = null;
				String str = null;
				String Exceptionerror = null;
				if (object != null) {
					Map<String, String> map = (Map<String, String>) object;
					if (map.containsKey("name")) {
						name = map.get("name");
						str = name;
					}else {
						
					}
					Log.i("GetCodeListense", "GetCodeListense" + name);
				} else {
					Exceptionerror = error;
				}
				listenser.setUserSeviceImplListenser(name, stRerror,
						Exceptionerror);

			}
		});
	}

	/**
	 * 6.2.1预案查询
	 */
	@Override
	public void getSearchPlanList(String name, String id,
			final UserSeviceImplListListenser listenser) {
		new SearchOtherscenPlanParser(name, id, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				if (object != null) {
					PlanNameSelectEntity planNameSelectEntity = null;
					String stRerror = null;
					String Exceptionerror = null;
					if (object != null) {
						planNameSelectEntity = (PlanNameSelectEntity) object;
					} else if (error != null) {
						Exceptionerror = error;
					} else {
						stRerror = Const.NO_MSG;
					}

					listenser.setUserSeviceImplListListenser(
							planNameSelectEntity, stRerror, Exceptionerror);
				}
			}
		});
	}

	/**
	 * 1.1.6用户权限控制
	 */
	@Override
	public void getUserPower(final UserSeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		new GetUserMenuPower(new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				if (object != null) {
					UserPowerEntity powerEntity = null;
					String stRerror = null;
					String Exceptionerror = null;
					if (object != null) {
						powerEntity = (UserPowerEntity) object;
					} else if (error != null) {
						Exceptionerror = error;
					} else {
						stRerror = Const.NO_MSG;
					}

					listenser.setUserSeviceImplListListenser(powerEntity,
							stRerror, Exceptionerror);
				}
			}
		});
	}

	/**
	 * 重新登录
	 */
	@Override
	public void relogin(String userName, String password, String roleId,
			final UserSeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		if (userName == null) {
			listenser.setUserSeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new UserReLoginParser(userName, password, roleId,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						UserEntity userEntity = null;
						String stRerror = null;
						String Exceptionerror = null;
						if (object != null && (object instanceof UserEntity)) {
							userEntity = (UserEntity) object;
						} else if (error != null) {
							Exceptionerror = error;
						} else {
							stRerror = "重新登录失败";
						}

						listenser.setUserSeviceImplListListenser(userEntity,
								stRerror, Exceptionerror);
					}
				});
	}

	/**
	 * 获取执行人信息
	 */
	@Override
	public void getUserMessageByPersonalId(String executePeopleId,final UserSeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		if (executePeopleId == null) {
			listenser.setUserSeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new GetPhoneByExecuteParser(executePeopleId, new OnDataCompleterListener() {
			
			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				UserPersonIdEntity entity = null;
				String stRerror = null;
				String Exceptionerror = null;
				if (object != null) {
					entity = (UserPersonIdEntity) object;
				} else if (error != null) {
					Exceptionerror = error;
				} else {
					stRerror = "未查找到数据！";
				}

				listenser.setUserSeviceImplListListenser(entity,
						stRerror, Exceptionerror);
			}
		});
		
	}

}
