package com.moto.myactivity;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class AppManager extends Application{
	private List<Activity> activityList=new LinkedList<Activity>();
    private static AppManager instance;
    
    private AppManager(){
        
    }
    public static AppManager getInstance(){
        if (instance==null) {
			instance=new AppManager();
		}
        return instance;
    }
    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    public void exit(){
        for(Activity activity:activityList){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        int id=android.os.Process.myPid();
        if(id!=0){
            android.os.Process
            .killProcess(id);
        }
    }
}
