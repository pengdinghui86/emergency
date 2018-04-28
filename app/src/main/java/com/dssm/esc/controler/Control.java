package com.dssm.esc.controler;


import com.dssm.esc.model.analytical.ContactListService;
import com.dssm.esc.model.analytical.ControlSevice;
import com.dssm.esc.model.analytical.EmergencyService;
import com.dssm.esc.model.analytical.UserSevice;
import com.dssm.esc.model.analytical.implSevice.ContactListServiceImpl;
import com.dssm.esc.model.analytical.implSevice.ControlServiceImpl;
import com.dssm.esc.model.analytical.implSevice.EmergencyServiceImpl;
import com.dssm.esc.model.analytical.implSevice.UserSeviceImpl;

public class Control {
	/**
	 * private的构造函数用于避免外界直接使用new来实例化对象
	 */
	private Control() {

	}

	public synchronized static Control getinstance() {

		return SingletonHolder.INSTANCE;
	}

	/**
	 * 单例支架
	 * 
	 * @author Administrator
	 * 
	 */
	private static class SingletonHolder {
		/**
		 * 单例对象实例化
		 */
		static final Control INSTANCE = new Control();
	}

	/**
	 * 获取用户模块接口对象
	 * 
	 * @return
	 */
	public UserSevice getUserSevice() {

		return UserSeviceImpl.getUserSeviceImpl();
	}

	/**
	 * 获取应急管理模块接口对象
	 * 
	 * @return
	 */
	public EmergencyService getEmergencyService() {
		return EmergencyServiceImpl.getEmergencySeviceImpl();
	}

	/**
	 * 获取控制中心模块接口对象
	 * 
	 * @return
	 */
	public ControlSevice getControlSevice() {
		return ControlServiceImpl.getControlSeviceImpl();
	}

	/**
	 * 通讯录模块接口对象
	 * 
	 * @return
	 */
	public ContactListService getContactSevice() {
		return ContactListServiceImpl.getContacteviceImpl();
	}
}
