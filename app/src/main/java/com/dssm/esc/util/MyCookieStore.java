package com.dssm.esc.util;

import net.tsz.afinal.FinalHttp;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class MyCookieStore {
	public static CookieStore cookieStore = null;
	private static final String TAG = "MyCookieStore";
	// 从服务器获取数据，若成功则本地设置CookieStore，保存到MyCookieStore
	public static void setcookieStore(FinalHttp finalHttp) {
		DefaultHttpClient client = (DefaultHttpClient) finalHttp
				.getHttpClient();
		MyCookieStore.cookieStore = client.getCookieStore();
		Log.i(TAG, "" + MyCookieStore.cookieStore);
	}

	public static void getcookieStore(FinalHttp finalHttp) {
		if (MyCookieStore.cookieStore != null) {
			finalHttp.configCookieStore(MyCookieStore.cookieStore);
			Log.i(TAG, "" + MyCookieStore.cookieStore);
		}
	}
}
