package com.dssm.esc.model.database;

import android.database.sqlite.SQLiteDatabase;

import com.dssm.esc.util.LogUtil;

import com.easemob.chatuidemo.DemoApplication;


/**
 * 数据库管理工具
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class DataBaseManage {
	/** 数据库名称 */
	public static final String DATA_BASE_NAME = "esc_database.db";
	/** 数据库版本 */
	public static final int DATA_BASE_VERSION = 1;

	/** 数据库操作对象 */
	private static OperationDataBaseUtil dataBaseHelper;
	/** 公用数据库操作对象 */
	private static OperationDataBaseUtil publicDataBaseHelper;

	/**
	 * 创建私有数据库
	 * 
	 * @updateTime 2015-6-22,下午3:45:02
	 * @updateAuthor qw
	 */
	public static void createDataBase(final String table1,final String table2) {
		dataBaseHelper = new OperationDataBaseUtil(DemoApplication.applicationContext, DATA_BASE_NAME, null, DATA_BASE_VERSION, new OnOperationDataBase() {
			@Override
			public void updateDataBase(SQLiteDatabase db, int oldVersion, int newVersion) {
				updateTables(db, oldVersion, newVersion);
			}

			@Override
			public void createTable(SQLiteDatabase db) {
				createTables(table1,db);
				createTables(table2,db);
			}
		});
		dataBaseHelper.onCreate(dataBaseHelper.getWritableDatabase());
		dataBaseHelper.close();
		dataBaseHelper = null;
	}

	/**
	 * 获取私有数据库操作对象
	 * 
	 * @updateTime 2015-6-22,下午3:45:28
	 * @updateAuthor qw
	 * @param dataBaseName
	 * @return
	 */
	public static OperationDataBaseUtil getOperationDataBaseUtil(String dataBaseName) {
		if (null != dataBaseHelper) {
			dataBaseHelper.close();
			dataBaseHelper = null;
		}
		dataBaseHelper = new OperationDataBaseUtil(DemoApplication.applicationContext, dataBaseName, null, DATA_BASE_VERSION);
		return dataBaseHelper;

	}

	/**
	 * 获取单独不受他人影响的数据库操作对象
	 * 
	 * @updateTime 2015-6-22,下午3:45:42
	 * @updateAuthor qw
	 * @param dataBaseName
	 * @return
	 */
	public static OperationDataBaseUtil getOperation(String dataBaseName) {
		return new OperationDataBaseUtil(DemoApplication.applicationContext, dataBaseName, null, DATA_BASE_VERSION);
	}

	/**
	 * 创建共有数据库
	 * 
	 * @updateTime 2015-6-22,下午3:46:13
	 * @updateAuthor qw
	 */
//	public static void createPulibicDataBase() {
//		publicDataBaseHelper = new OperationDataBaseUtil(MainActivity.context, DATA_BASE_NAME, null, DATA_BASE_VERSION, new OnOperationDataBase() {
//			@Override
//			public void updateDataBase(SQLiteDatabase db, int oldVersion, int newVersion) {
//				updatePublicTables(db, oldVersion, newVersion);
//			}
//
//			@Override
//			public void createTable(SQLiteDatabase db) {
//				createPublicTables(db);
//			}
//		});
//		publicDataBaseHelper.onCreate(publicDataBaseHelper.getWritableDatabase());
//		publicDataBaseHelper.close();
//		publicDataBaseHelper = null;
//	}

	/**
	 * 获取公用数据库操作对象
	 * 
	 * @updateTime 2015-6-22,下午3:46:34
	 * @updateAuthor qw
	 * @return
	 */
//	public static OperationDataBaseUtil getPublicOperationDataBaseUtil() {
//		if (null != publicDataBaseHelper) {
//			publicDataBaseHelper.close();
//			publicDataBaseHelper = null;
//		}
//		publicDataBaseHelper = new OperationDataBaseUtil(MainActivity.context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
//		return publicDataBaseHelper;
//	}

	protected static void updatePublicTables(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion < oldVersion) {// 如果不是数据库升级 公用数据库升级
			return;
		}
		try {
			switch (db.getVersion()) {
			case 1:// 数据库版本1-2
			case 2:// 数据库版本2-3
				break;
			}
		} catch (RuntimeException e) {
			// 数据库更新失败
			LogUtil.out("公用数据库更新失败 ============>\n" + e.getMessage());
		}
	}

	/**
	 * 公用数据库表
	 * 
	 * @updateTime 2015-6-22,下午3:43:11
	 * @updateAuthor qw
	 * @param db
	 */
//	protected static void createPublicTables(SQLiteDatabase db) {
//		/**
//		 * json的缓存表
//		 * 
//		 */
//		String sql = "create table if not exists " + DataBaseFields.TB_JSON + "(" // JSON表
//				+ " id " + " integer PRIMARY KEY autoincrement, " // 自增长id.
//				+ DataBaseFields.FIELD_TYPE + " varchar, " // 类型.
//				+ DataBaseFields.FIELD_JSON + " varchar" // json
//				+ ");";
//		db.execSQL(sql);
//	}

	/**
	 * 更新私有数据库表
	 * 
	 * @updateTime 2015-6-22,下午3:43:35
	 * @updateAuthor qw
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public static void updateTables(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion < oldVersion) {// 如果不是数据库升级 私用数据库升级
			return;
		}
		try {
			switch (db.getVersion()) {
			case 1:// 数据库版本1-2
				break;
			}
		} catch (RuntimeException e) {
			LogUtil.out("私用数据库更新失败 ============>\n" + e.getMessage());
		}
	}

	/**
	 * 创建私有数据表
	 * 
	 * @updateTime 2015-6-22,下午3:47:00
	 * @updateAuthor qw
	 * @param db
	 */
	private static void createTables(String table,SQLiteDatabase db) {
		/**
		 * 创建用户表
		 * 
		 * @version 1.0
		 * @createTime 2013-10-21,下午4:20:03
		 * @updateTime 2013-10-21,下午4:20:03
		 * @createAuthor CodeApe
		 * @updateAuthor CodeApe
		 */
		String sql = "create table if not exists " +DataBaseFields.TB_MESSAGE+ table+ "(" // 创建信息表
				+ DataBaseFields.ID + " integer PRIMARY KEY autoincrement, " // 自增长id.
				+ DataBaseFields.MESSAGEID + " varchar, " //消息id
				+ DataBaseFields.MESSAGE + " varchar, " // 消息
				+ DataBaseFields.TIME + " varchar, " // 时间
				+ DataBaseFields.EVENTNAME + " varchar, " // 事件名称
				+ DataBaseFields.EVENTTYPE + " varchar, " // 预案类型
				+ DataBaseFields.SENDER + " varchar, " // 发送人
				+ DataBaseFields.MODELFLAG+ " varchar, " // 状态标识
				+ DataBaseFields.PLANNAME + " varchar" // 预案名称
				+ ");";
		db.execSQL(sql);
	}
}
