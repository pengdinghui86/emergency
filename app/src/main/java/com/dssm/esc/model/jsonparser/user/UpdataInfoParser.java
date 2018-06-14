package com.dssm.esc.model.jsonparser.user;

import android.util.Xml;

import com.dssm.esc.model.entity.user.UpdataInfo;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;


/**
 * 版本自动更新解析类
 * 
 * @author zsj
 * 
 */
public class UpdataInfoParser {
	
	public UpdataInfo info;
	OnDataCompleterListener OnUserParseLoadCompleteListener;

	public UpdataInfoParser(
			OnDataCompleterListener completeListener) {
		// TODO Auto-generated constructor stub
		this.OnUserParseLoadCompleteListener = completeListener;
	}

	

	/**
	 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static UpdataInfo getUpdataInfo(InputStream is) throws Exception {

		XmlPullParser parser = Xml.newPullParser();

		parser.setInput(is, "utf-8");

		int type = parser.getEventType();

		UpdataInfo info = new UpdataInfo();

		while (type != XmlPullParser.END_DOCUMENT) {

			switch (type) {

			case XmlPullParser.START_TAG:

				if ("version".equals(parser.getName())) {

					info.setVersion(parser.nextText());

				} else if ("url".equals(parser.getName())) {

					info.setUrl(parser.nextText());

				} else if ("description".equals(parser.getName())) {

					info.setDescription(parser.nextText());

				}else if ("url_server".equals(parser.getName())) {

					info.setUrl_server(parser.nextText());

				}

				break;

			}

			type = parser.next();

		}

		return info;

	}

}
