package com.easemob.chatuidemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.easemob.chatuidemo.DemoApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;


/**
 * sp数据操作工具类
 * 

 */
public class SpUtil {
	public static final String CONFIG_PREFERENCES = "config_preferences";
	private static SpUtil spUtil;
	private static SharedPreferences sp;
	/** 应用第一次开启 */
	public static final String FIRST_INTO = "first_into";

	public static final String STOCK_FOLLOE_LIST = "a";

	/**
	 * 获取一个操作sp数据的实例
	 * @param fileKey
	 * @param mode
	 * @return
	 */
	public static SpUtil getSpUtil(String fileKey, int mode) {
		if (spUtil == null) {
			spUtil = new SpUtil(fileKey, mode);
		} else {
			sp = DemoApplication.applicationContext.getSharedPreferences(fileKey, mode);
		}
		return spUtil;
	}

	public static SpUtil getSpUtil() {
		if (spUtil == null) {
			spUtil = new SpUtil(CONFIG_PREFERENCES, Context.MODE_PRIVATE);
		} else {
			sp = DemoApplication.applicationContext.getSharedPreferences(CONFIG_PREFERENCES, Context.MODE_PRIVATE);
		}
		return spUtil;
	}

	public SpUtil(String fileKey, int mode) {
		sp = DemoApplication.applicationContext.getSharedPreferences(fileKey, mode);
	}
	/**
	 * sp保存对象
	 * 
	 *
	 * @param context
	 * @param key
	 * @param obj
	 *                要保存的对象，只能保存实现了serializable的对象
	 */
	public static void setObject( Context context, String key, Object obj) {
		try {
			// 先将序列化结果写到byte缓存中，其实就分配一个内存空间
			if (obj!=null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream os = new ObjectOutputStream(bos);
				// 将对象序列化写入byte缓存
				os.writeObject(obj);
				// 将序列化的数据转为16进制保存
				String bytesToHexString = bytesToHexString(bos.toByteArray());
				// 保存该16进制数组
				sp.edit().putString(key, bytesToHexString).commit();
			}else{
				sp.edit().putString(key, "").commit();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取sp里面的obj
	 *

	 *  @param context
	 *  @param key
	 *  @return
	 */
	public static Object getObject(Context context, String key) {
		try {
			if (sp.contains(key)) {
				String string = sp.getString(key, "");
				if (TextUtils.isEmpty(string)) {
					return null;
				} else {
					// 将16进制的数据转为数组，准备反序列化
					byte[] stringToBytes = StringToBytes(string);
					ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
					ObjectInputStream is = new ObjectInputStream(bis);
					// 返回反序列化得到的对象
					Object readObject = is.readObject();
					return readObject;
				}
			}
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 所有异常返回null
		return null;

	}

	/**
	 * sp保存对象
	 * 
	 *fo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param sharedata
	 * @param context
	 * @param key
	 * @param obj 要保存的对象，只能保存实现了serializable的对象
	 */
	public static void setObject(SharedPreferences sharedata, Context context, String key, Object obj) {
		try {
			// 先将序列化结果写到byte缓存中，其实就分配一个内存空间
			if (obj != null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream os = new ObjectOutputStream(bos);
				// 将对象序列化写入byte缓存
				os.writeObject(obj);
				// 将序列化的数据转为16进制保存
				String bytesToHexString = bytesToHexString(bos.toByteArray());
				// 保存该16进制数组
				sharedata.edit().putString(key, bytesToHexString).commit();
			} else {
				sharedata.edit().putString(key, "").commit();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * desc:将数组转为16进制
	 * 
	 * @param bArray
	 * @return modified:
	 */
	public static String bytesToHexString(byte[] bArray) {
		if (bArray == null) {
			return null;
		}
		if (bArray.length == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 获取sp里面的obj
	 * 
	 *
	 * @param sharedata
	 * @param context
	 * @param key
	 * @return
	 */
	public static Object getObject(SharedPreferences sharedata, Context context, String key) {
		try {
			if (sharedata.contains(key)) {
				String string = sharedata.getString(key, "");
				if (TextUtils.isEmpty(string)) {
					return null;
				} else {
					// 将16进制的数据转为数组，准备反序列化
					byte[] stringToBytes = StringToBytes(string);
					ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
					ObjectInputStream is = new ObjectInputStream(bis);
					// 返回反序列化得到的对象
					Object readObject = is.readObject();
					return readObject;
				}
			}
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 所有异常返回null
		return null;

	}

	/**
	 * desc:将16进制的数据转为数组

	 * @param data
	 * @return modified:
	 */
	public static byte[] StringToBytes(String data) {
		String hexString = data.toUpperCase().trim();
		if (hexString.length() % 2 != 0) {
			return null;
		}
		byte[] retData = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i++) {
			int int_ch; // 两位16进制数转化后的10进制数
			char hex_char1 = hexString.charAt(i); // //两位16进制数中的第一位(高位*16)
			int int_ch1;
			if (hex_char1 >= '0' && hex_char1 <= '9')
				int_ch1 = (hex_char1 - 48) * 16; // // 0 的Ascll
			// - 48
			else if (hex_char1 >= 'A' && hex_char1 <= 'F')
				int_ch1 = (hex_char1 - 55) * 16; // // A 的Ascll
			// - 65
			else
				return null;
			i++;
			char hex_char2 = hexString.charAt(i); // /两位16进制数中的第二位(低位)
			int int_ch2;
			if (hex_char2 >= '0' && hex_char2 <= '9')
				int_ch2 = (hex_char2 - 48); // // 0 的Ascll - 48
			else if (hex_char2 >= 'A' && hex_char2 <= 'F')
				int_ch2 = hex_char2 - 55; // // A 的Ascll - 65
			else
				return null;
			int_ch = int_ch1 + int_ch2;
			retData[i / 2] = (byte) int_ch;// 将转化后的数放入Byte里
		}
		return retData;
	}

	/**
	 * sp保存一个int数据
	 * 

	 * @param valueKey
	 * @param value
	 */
	public void putSPValue(String valueKey, int value) {
		sp.edit().putInt(valueKey, value).commit();
	}

	/**
	 * sp保存一个float数据
	 * 

	 * @param valueKey
	 * @param value
	 */
	public void putSPValue(String valueKey, float value) {
		sp.edit().putFloat(valueKey, value).commit();
	}

	/**
	 * sp保存一个String数据
	 * 
	 *
	 * @param valueKey
	 * @param value
	 */
	public  static void putSPValue(String valueKey, String value) {
		sp.edit().putString(valueKey, value).commit();
	}

	/**
	 * sp保存一个boolean数据
	 * 

	 * @param valueKey
	 * @param value
	 */
	public void putSPValue(String valueKey, boolean value) {
		sp.edit().putBoolean(valueKey, value).commit();
	}

	/**
	 * sp保存一个long数据
	 * 

	 * @param valueKey
	 * @param value
	 */
	public void putSPValue(String valueKey, long value) {
		sp.edit().putLong(valueKey, value).commit();
	}

	/**
	 * sp获取一个int数据
	 * 

	 * @param valueKey
	 * @param value
	 */
	public int getSPValue(String valueKey, int value) {
		return sp.getInt(valueKey, value);
	}

	/**
	 * sp获取一个float数据

	 * @param valueKey
	 * @param value
	 */
	public float getSPValue(String valueKey, float value) {
		return sp.getFloat(valueKey, value);
	}

	/**
	 * sp获取一个String数据
	 * 

	 * @param valueKey
	 * @param value
	 */
	public static String getSPValue(String valueKey, String value) {
		return sp.getString(valueKey, value);
	}

	/**
	 * sp获取一个boolean数据
	 * 

	 * @param valueKey
	 * @param value
	 */
	public boolean getSPValue(String valueKey, boolean value) {
		return sp.getBoolean(valueKey, value);
	}

	/**
	 * sp获取一个long数据
	 * 

	 * @param valueKey
	 * @param value
	 */
	public long getSPValue(String valueKey, long value) {
		return sp.getLong(valueKey, value);
	}

	/**
	 * 清理sp数据

	 */
	public void clear() {
		sp.edit().clear().commit();
	}

	/** SP保存一个int数据类型 */
	public static void putSPValue(Context context, String FileKey, String valueKey, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(FileKey, context.MODE_PRIVATE);
		sharedPreferences.edit().putInt(valueKey, value).commit();
	}

	/** SP保存一个String数据类型 */
	public static void putSPValue(Context context, String FileKey, String valueKey, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(FileKey, context.MODE_PRIVATE);
		sharedPreferences.edit().putString(valueKey, value).commit();
	}

	/** SP保存一个float数据类型 */
	public static void putSPValue(Context context, String FileKey, String valueKey, float value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(FileKey, context.MODE_PRIVATE);
		sharedPreferences.edit().putFloat(valueKey, value).commit();
	}

	/** SP保存一个boolean数据类型 */
	public static void putSPValue(Context context, String FileKey, String valueKey, boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(FileKey, context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(valueKey, value).commit();
	}

	/** SP 获取一个int数据类型 */
	public static int getSPValue(Context context, String FileKey, String valueKey, int defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(FileKey, context.MODE_PRIVATE);
		return sharedPreferences.getInt(valueKey, defaultValue);
	}

	/** SP 获取一个boolean数据类型 */
	public static boolean getSPValue(Context context, String FileKey, String valueKey, boolean defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(FileKey, context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(valueKey, defaultValue);
	}

	/** SP 获取一个string数据类型 */
	public static String getSPValue(Context context, String FileKey, String valueKey, String defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(FileKey, context.MODE_PRIVATE);
		return sharedPreferences.getString(valueKey, defaultValue);
	}

	/** SP 获取一个float数据类型 */
	public static float getSPValue(Context context, String FileKey, String valueKey, float defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(FileKey, context.MODE_PRIVATE);
		return sharedPreferences.getFloat(valueKey, defaultValue);
	}

}
