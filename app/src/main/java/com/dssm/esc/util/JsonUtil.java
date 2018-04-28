package com.dssm.esc.util;

import com.dssm.esc.model.entity.emergency.RecieveListEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class JsonUtil {
	/**
	 * 将数组转换为JSON格式的数据。
	 * 
	 * @param stoneList
	 *            数据源
	 * @return JSON格式的数据
	 */
	public static String changeArrayDateToJson(ArrayList<RecieveListEntity> stoneList) {
		try {
			JSONArray array = new JSONArray();
			int length = stoneList.size();
			for (int i = 0; i < length; i++) {
				RecieveListEntity stone = stoneList.get(i);
				String receiverPhone = stone.getReceiverPhone();
				String receiverEmail = stone.getReceiverEmail();
				String receiver = stone.getReceiver();
				JSONObject stoneObject = new JSONObject();
				stoneObject.put("receiverPhone", receiverPhone);
				stoneObject.put("receiverEmail", receiverEmail);
				stoneObject.put("receiver", receiver);
				array.put(stoneObject);
			}
			
			return array.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将JSON转化为数组并返回。
	 * 
	 * @param Json
	 * @return ArrayList<Stone>
	 */
	public static ArrayList<RecieveListEntity> changeJsonToArray(String Json) {
		ArrayList<RecieveListEntity> gameList = new ArrayList<RecieveListEntity>();
		try {
			JSONObject jsonObject = new JSONObject(Json);
			if (!jsonObject.isNull("sendObj")) {
				String aString = jsonObject.getString("sendObj");
				JSONArray aJsonArray = new JSONArray(aString);
				int length = aJsonArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject stoneJson = aJsonArray.getJSONObject(i);
					String receiverPhone = stoneJson.getString("receiverPhone");
					String receiverEmail = stoneJson.getString("receiverEmail");
					String receiver = stoneJson.getString("receiver");
					RecieveListEntity stone = new RecieveListEntity();
					stone.setReceiverPhone(receiverPhone);
					stone.setReceiverEmail(receiverEmail);
					stone.setReceiver(receiver);
					gameList.add(stone);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gameList;
	}
}
