package com.dssm.esc.util;

public class MyDate {
	//根据秒得到相对应的时分秒时间
	public String formatSeconds(long value) {

		    long theTime = value;// 秒

		    long theTime1 = 0;// 分

		    long theTime2 = 0;// 小时

		    if(theTime > 60) {

		        theTime1 = (theTime/60);

		        theTime = (theTime%60);

		            if(theTime1 > 60) {

		            theTime2 = (theTime1/60);

		            theTime1 = (theTime1%60);

		            }

		    }

		        String result = ""+theTime+"秒";

		        if(theTime1 > 0) {

		        result = ""+theTime1+"分"+result;

		        }

		        if(theTime2 > 0) {

		        result = ""+theTime2+"小时"+result;

		        }

		    return result;

		}

	
}
