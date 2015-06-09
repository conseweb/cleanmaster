package com.bitants.cleanmaster.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bitants.cleanmaster.MainActivity;
import com.bitants.cleanmaster.floa.FloatService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MService extends Service {
	
	 SharedPreferences sp; 
     SharedPreferences.Editor editor; 
	Context context;
	Timer timer;
	static ActivityManager mActivityManager;
	
	 @Override
	    public void onDestroy() {
	        // TODO Auto-generated method stub
		 Intent intent = new Intent(MService.this, FloatService.class);
			stopService(intent);
			timer.cancel();
	        super.onDestroy();
	    }

	 
	 @Override
		public void onCreate() 
		{
			// TODO Auto-generated method stub
			super.onCreate();
		    context=getApplicationContext();
		    sp = getSharedPreferences("SP", MODE_PRIVATE);  
	         editor = sp.edit();
	       
	        
	        //Toast.makeText(FxService.this, "create FxService", Toast.LENGTH_LONG);		
		}
	 
	 
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO 自动生成的方法存根
		super.onStart(intent, startId);
		timer = new Timer();
		  timer.schedule(timertask, 1000, 100);
		
	}
	

	//Handler
			private Handler handler = new Handler() {
				public void handleMessage(Message msg) {
					// String ms=getUsedPercentValue(getBaseContext());
					// editor.putString("ms", ms);
			       //  editor.commit();
			         
					if (isHome()){
						
						Intent intent = new Intent(MService.this, FloatService.class);
						
						stopService(intent);startService(intent);
						
					}else {Intent intent = new Intent(MService.this, FloatService.class);
					stopService(intent);}
				}
			};

			private TimerTask timertask = new TimerTask() {
				public void run() {
					Message message = new Message();
					handler.sendMessage(message);
				}
			};

			
	/** 
     * 获得属于桌面的应用的应用包名称 
     * @return 返回包含所有包名的字符串列表 
     */  
    private List<String> getHomes() {  
        List<String> names = new ArrayList<String>();  
        PackageManager packageManager = this.getPackageManager();  
        //属性   
        Intent intent = new Intent(Intent.ACTION_MAIN);
     intent.addCategory(Intent.CATEGORY_HOME);  
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,  
              PackageManager.MATCH_DEFAULT_ONLY);  
        for(ResolveInfo ri : resolveInfo){  
           names.add(ri.activityInfo.packageName);  
           Log.i("zhangyinfu PinyinIME.java", "packageName =" + ri.activityInfo.packageName);
        }  
        return names;
    }
    
    
    /** 
     * 判断当前界面是否是桌面 
     */ 
    public boolean isHome(){ 
        ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);  
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        List<String> strs = getHomes();
        if(strs != null && strs.size() > 0){
            return strs.contains(rti.get(0).topActivity.getPackageName());
        }else{
            return false;
        }
    }
    
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	* 计算已使用内存的百分比，并返回。
	*
	* @param context
	* 可传入应用程序上下文。
	* @return 已使用内存的百分比，以字符串形式返回。
	*/
	public static String getUsedPercentValue(Context context) {
	String dir = "/proc/meminfo";
	try {
	FileReader fr = new FileReader(dir);
	BufferedReader br = new BufferedReader(fr, 2048);
	String memoryLine = br.readLine();
	String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
	br.close();
	long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
	long availableSize = getAvailableMemory(context) / 1024;
	int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
	return percent + "%";
	} catch (IOException e) {
	e.printStackTrace();
	}
	return "没有";
	}
	
	/**
	* 获取当前可用内存，返回数据以字节为单位。
	*
	* @param context
	* 可传入应用程序上下文。
	* @return 当前可用内存。
	*/
	private static long getAvailableMemory(Context context) {
	ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
	getActivityManager(context).getMemoryInfo(mi);
	return mi.availMem;
	}
	
	/**
	* 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
	*
	* @param context
	* 可传入应用程序上下文。
	* @return ActivityManager的实例，用于获取手机可用内存。
	*/
	private static ActivityManager getActivityManager(Context context) {
	if (mActivityManager == null) {
	mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}
	return mActivityManager;
	}

}
