package com.dssm.esc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;



/**
 * 用来保存用户信息 (Android平台给我们提供了一个SharedPreferences类，它是一个轻量级的存储类，特别适合用于保存软件配置参数。
 * 使用SharedPreferences保存数据，其背后是用xml文件存放数据，文件存放在/data/data/<package
 * name>/shared_prefs目录下)
 * 
 * @author zsj
 * 
 */
public class MySharePreferencesService {
	private SharedPreferences preferences;
	private static MySharePreferencesService instance;

	public MySharePreferencesService() {
	}

	public MySharePreferencesService(Context context) {
		preferences = context.getSharedPreferences("esc_preferences",
				Context.MODE_PRIVATE);
	}

	public static MySharePreferencesService getInstance(Context context) {
		if (instance == null) {
			instance = new MySharePreferencesService(context);
		}
		return instance;
	}

	/**
	 * 把用户名和密码,角色保存到SharedPreferences中
	 * 
	 * @param loginName
	 *            用户名
	 * 
	 * @param password
	 *            密码
	 * 
	 * @param roleIds
	 *            角色id
	 * @param roleNames
	 *            角色名称
	 * @param roleCodes
	 *            角色编号
	 * @param postFlag
	 *            岗位标识
	 * @param userId
	 *            用户id
	 * @param selectedRolem
	 *            被选中的角色id
	 * @param selectedRolemName
	 *            被选中的角色名称
	 * @param roleCode
	 *            被选中的角色编号
	 * @param name
	 *            用户姓名
	 */
	public void save(String loginName, String password, String roleIds,
			String roleNames, String roleCodes, String selectedRolem,
			String postFlag, String userId, String selectedRolemName,
			String roleCode, String name) {
		Editor edit = preferences.edit();
		// 数据是放在内存中的
		edit.putString("loginName", loginName);
		edit.putString("password", password);
		edit.putString("roleIds", roleIds);
		edit.putString("roleNames", roleNames);
		edit.putString("roleCodes", roleCodes);
		edit.putString("selectedRolem", selectedRolem);
		edit.putString("postFlag", postFlag);
		edit.putString("userId", userId);
		edit.putString("selectedRolemName", selectedRolemName);
		edit.putString("roleCode", roleCode);
		edit.putString("name", name);
		// 提交方法，把内存中的数据提交到文件中
		edit.commit();
	}

	/**
	 * 从SharedPreferences中取用户名和密码
	 * 
	 * @return
	 */
	// 获取保存的文件内容
	public Map<String, String> getPreferences() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("loginName", preferences.getString("loginName", ""));
		param.put("password", preferences.getString("password", ""));
		param.put("roleIds", preferences.getString("roleIds", ""));
		param.put("roleNames", preferences.getString("roleNames", ""));
		param.put("roleCodes", preferences.getString("roleCodes", ""));

		param.put("selectedRolem", preferences.getString("selectedRolem", ""));
		param.put("postFlag", preferences.getString("postFlag", ""));
		param.put("userId", preferences.getString("userId", ""));
		param.put("selectedRolemName",
				preferences.getString("selectedRolemName", ""));
		param.put("roleCode", preferences.getString("roleCode", ""));
		param.put("name", preferences.getString("name", ""));
		return param;
	}

	/**
	 * 保存联系人的姓名
	 */
//	public void saveContactName(List<GroupEntity> list) {
//		// TODO Auto-generated method stub
//		Editor edit = preferences.edit();
//		for (int i = 0; i < list.size(); i++) {
//			GroupEntity groupEntity = list.get(i);
//			List<ChildEntity> getcList = groupEntity.getcList();
//			for (int j = 0; j < getcList.size(); j++) {
//				ChildEntity childEntity = getcList.get(j);
//				// 数据是放在内存中的
//				edit.putString(childEntity.getUserId(), childEntity.getName());
//			}
//		}
//
//		// 提交方法，把内存中的数据提交到文件中
//		edit.commit();
//	}

	public void saveContactName(String userId,String name) {
		// TODO Auto-generated method stub
		Editor edit = preferences.edit();
		// 数据是放在内存中的
		edit.putString(userId, name);

		// 提交方法，把内存中的数据提交到文件中
		edit.commit();
	}

	/**
	 * 获取联系人的姓名
	 */
	public String getcontectName(String userId) {
		// TODO Auto-generated method stub
		return preferences.getString(userId, "");
	}
}