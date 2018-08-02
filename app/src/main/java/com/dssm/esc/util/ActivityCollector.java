package com.dssm.esc.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import com.easemob.chatuidemo.activity.SplashActivity;

public class ActivityCollector {
	
	public static  List<Activity> activities = new ArrayList<>();
	
	public static void addActivity(Activity activity){
		activities.add(activity);
	}
	
	public static void removeActivity(Activity activity){
		activities.remove(activity);
	}
	
	public static void finishAll(){
		for (Activity activity : activities) {
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
	}

	public static void finishSplashActivity(){
		for (Activity activity : activities) {
			if(!activity.isFinishing() && activity instanceof SplashActivity){
				activity.finish();
			}
		}
	}
}
