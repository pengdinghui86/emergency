package com.dssm.esc.model.database;

import android.database.Cursor;

import com.dssm.esc.model.entity.message.MessageInfoEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 数据库操作.
 * 
 * @Description
 * @author 綦巍
 * @date 2015-4-28
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc.
 *             All rights reserved.
 */
public class DataBaseUtil {

	/**
	 * 根据类型插入json
	 * 
	 * @version 1.0
	 * @createTime 2015-4-28,下午6:38:11
	 * @updateTime 2015-4-28,下午6:38:11
	 * @createAuthor 綦巍
	 * @updateAuthor 綦巍
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param json
	 *            json
	 * @param type
	 *            类型
	 */
	public static void updateJSON(String json, String type) {
		OperationDataBaseUtil helper = DataBaseManage
				.getOperationDataBaseUtil(DataBaseManage.DATA_BASE_NAME);
		helper.delete(false, DataBaseFields.TB_JSON, DataBaseFields.FIELD_TYPE
				+ " = ? ", new String[] { type });
		helper.insert(true, DataBaseFields.TB_JSON, new String[] {
				DataBaseFields.FIELD_TYPE, DataBaseFields.FIELD_JSON },
				new String[] { type, json });
	}

	/**
	 * 根据类型获取json
	 * 
	 * @version 1.0
	 * @createTime 2015-4-28,下午6:38:38
	 * @updateTime 2015-4-28,下午6:38:38
	 * @createAuthor 綦巍
	 * @updateAuthor 綦巍
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param type
	 *            类型
	 * @return
	 */
	public static String getJSON(String type) {
		OperationDataBaseUtil helper = DataBaseManage
				.getOperationDataBaseUtil(DataBaseManage.DATA_BASE_NAME);
		Cursor cursor = helper.select(DataBaseFields.TB_JSON,
				new String[] { DataBaseFields.FIELD_JSON },
				DataBaseFields.FIELD_TYPE + " = ? ", new String[] { type },
				null, null, null, null);
		String json = null;
		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			json = cursor.getString(cursor
					.getColumnIndex(DataBaseFields.FIELD_JSON));
		}
		cursor.close();
		helper.close();
		return json;
	}

	public static List<MessageInfoEntity> getList(String table, String limit) {
		// TODO Auto-generated method stub
		OperationDataBaseUtil helper = DataBaseManage
				.getOperationDataBaseUtil(DataBaseManage.DATA_BASE_NAME);
		ArrayList<HashMap<String, String>> list = helper.selectByEncrypt(false,
				DataBaseFields.TB_MESSAGE+table, new String[] { "*" }, null, null, null, null, "time desc",
				limit, null, false);
		
		List<MessageInfoEntity> infolist = new ArrayList<MessageInfoEntity>();
		for (int i = 0; i < list.size(); i++) {
			MessageInfoEntity entity = new MessageInfoEntity();
			HashMap<String, String> map = list.get(i);
			entity.setMessageId(map.get(DataBaseFields.MESSAGEID));
			entity.setTime(map.get(DataBaseFields.TIME));
			entity.setMessage(map.get(DataBaseFields.MESSAGE));
			infolist.add(entity);
			
		}
		return infolist;

	}
	/**
	 * 获取指定登录用户信息
	 * 
	 * @version 1.0
	 * @createTime 2013-10-22,上午11:04:50
	 * @updateTime 2013-10-22,上午11:04:50
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @return 指定的用户数据信息(-1 游客)
	 */
	public MessageInfoEntity getMessageBean(String table, String messageId) {
		OperationDataBaseUtil helper = DataBaseManage
				.getOperationDataBaseUtil(DataBaseManage.DATA_BASE_NAME);// 数据库操作对象
		Cursor cursor = helper.select(table, new String[] { "*" },
				DataBaseFields.MESSAGEID + " =? ", new String[] { messageId },
				null, null, "id asc", null);
		MessageInfoEntity bean = new MessageInfoEntity();

		if (cursor.moveToNext()) {
			bean.setId(cursor.getString(cursor
					.getColumnIndex(DataBaseFields.ID)));
			bean.setMessageId(cursor.getString(cursor
					.getColumnIndex(DataBaseFields.MESSAGEID)));
			// userNO 必须优先保证解析，用于后边的数据加解密
			bean.setTime(cursor.getString(cursor
					.getColumnIndex(DataBaseFields.TIME)));
			bean.setMessage(cursor.getString(cursor
					.getColumnIndex(DataBaseFields.MESSAGE)));
		}

		cursor.close();

		return bean;
	}

	// **************************用户数据表******************************//
	/**
	 * 插入一条登录用户记录
	 * 
	 * @version 1.0
	 * @createTime 2013-10-22,上午10:29:18
	 * @updateTime 2013-10-22,上午10:29:18
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param bean
	 *            登录用户属性
	 */
	public void insterUserInfo(String table, MessageInfoEntity bean) {
		OperationDataBaseUtil helper = DataBaseManage
				.getOperationDataBaseUtil(DataBaseManage.DATA_BASE_NAME);
		String[] titles = bean.getDataBaseTitles();
		String[] values = bean.getDataBaseValues();
		// 从本地数据库查找是否存在该用户记录
		Cursor cursor = helper.select(DataBaseFields.TB_MESSAGE+table, new String[] { "*" },
				DataBaseFields.MESSAGEID + " =? ",
				new String[] { bean.getMessageId() }, null, null, null, null);

		// 如果本地数据库存在该用户，则更新该记录，否则插入一条新的记录
		if (cursor.getCount() <= 0) {
			helper.insert(true, DataBaseFields.TB_MESSAGE+table, titles, values);
		} else {
			helper.update(true, DataBaseFields.TB_MESSAGE+table, titles, values, DataBaseFields.MESSAGEID
					+ " =? ", new String[] { bean.getMessageId() });
		}

	}

}
