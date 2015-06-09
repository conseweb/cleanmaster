package com.bitants.cleanmaster.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Build;
import android.os.Debug.MemoryInfo;
import android.util.Log;

public class MemoryUtil {

	/**
	 * getTotalPss
	 * 
	 * @param context
	 * @param processName
	 * @return
	 */
	public static long getTotalPss(Context context, String processName) {

		ActivityManager activityMgr = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = activityMgr.getRunningAppProcesses();

		if (list != null) {
			for (RunningAppProcessInfo processInfo : list) {
				if (processInfo.processName.equals(processName)) {
					int pid = processInfo.pid;
					MemoryInfo[] memoryInfos = activityMgr
							.getProcessMemoryInfo(new int[] { pid });
					
					MemoryInfo memoryInfo = memoryInfos[0];
					int totalPss = memoryInfo.getTotalPss();
					
					return totalPss;
				}
			}
		}

		return -1;
	}
	
	/**
	 * 计算已使用内存的百分比
	 * 
	 */
	public static String getUsedPercentValue(Context context) {
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr, 2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine
					.indexOf("MemTotal:"));
			br.close();
			long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll(
					"\\D+", ""));
			long availableSize = getAvailableMemory(context) / 1024;
			int percent = (int) ((totalMemorySize - availableSize)
					/ (float) totalMemorySize * 100);
			return percent + "%";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取可用内存
	 * 
	 */
	public static long getAvailableMemory(Context context) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(mi);

		return mi.availMem;
	}
	
	@SuppressWarnings("deprecation")
	public static void clearMemory(Context context) {
		
		MSaveList mSaveList = new MSaveList(context.getSharedPreferences("demo360", Activity.MODE_PRIVATE));
		List<String> list2 = mSaveList.load();
		ActivityManager activityManger = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = activityManger
				.getRunningAppProcesses();
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				ActivityManager.RunningAppProcessInfo apinfo = list.get(i);

				String[] pkgList = apinfo.pkgList;
				
				if (apinfo.importance >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

					for (int j = 0; j < pkgList.length; j++) {

						if (pkgList[j].equals(context.getPackageName())) {
							continue;
						}  else {

							if(isInWhiteList(pkgList[j],list2))
							{
								Log.d("AAA", "跳过不杀的进程：" + apinfo.processName);
							}
							else
							{
								if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
								activityManger.restartPackage(pkgList[j]);}else {
								activityManger.killBackgroundProcesses(pkgList[j]);}
					
								Log.d("AAA", "杀掉的进程："+pkgList[j]);
							}
						
						         }
					}
				}
			}
	}
	
	
	
	//关闭当前运行的进程
		public Object [] killRunningAppInfo(Context context)
		{
			MSaveList mSaveList = new MSaveList(context.getSharedPreferences("demo360", Activity.MODE_PRIVATE));
			List<String> list = mSaveList.load();
			ActivityManager mActivityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> mRunningProcess = mActivityManager.getRunningAppProcesses();
			int appSize = getRunningTasksSize(context);
			long memory = getUesdMemory(context);
			for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess)
			{
				if(amProcess.processName.equals("com.mojing.demo360")||amProcess.processName.startsWith("system"))
				{
					Log.d("AAA", "跳过不杀的进程：" + amProcess.processName);
					continue;
				}
				else 
				{
					if(isInWhiteList(amProcess.processName,list))
					{
						Log.d("AAA", "跳过不杀的进程：" + amProcess.processName);
					}
					else
					{
						if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
						mActivityManager.restartPackage(amProcess.processName);}else {
						mActivityManager.killBackgroundProcesses(amProcess.processName);}
			
						Log.d("AAA", "杀掉的进程："+amProcess.processName);
					}
					
				}	
			}
			appSize = Math.abs(appSize -getRunningTasksSize(context));
			memory = Math.abs(memory -getUesdMemory(context));
			return getRecycleMemoryInfo(context,appSize,memory);
			}

			//强制关闭进程
			private  void forceKillApp(ActivityManager am, String packageName) 
			{
				Method forceStopPackage = null;
				try 
				{
					forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
					forceStopPackage.setAccessible(true);  
					forceStopPackage.invoke(am, packageName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
			
			/**
			 * 将要传出去的数据
			 * 杀了多少进程
			 * 释放多少M内存
			 * 当前内存百分比
			 * @param context
			 * @param appSize
			 * @param memory
			 * @return
			 */
			private Object [] getRecycleMemoryInfo(Context context,int appSize,long memory) { 
		        Object[] pram=new Object[]{0,0,0};;
		        if(memory>=0)
		        {  
		        	pram[0] = appSize;
		        	pram[1] = (memory/1024.0);
		        	pram[2] = getUesdMemoryRate(context);
		        }
		        return pram;
		    }
			
			 private int getRunningTasksSize(Context context)
			 {
				 ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
			     return am.getRunningAppProcesses().size();
			 }
			
			 	/**
				 * 得到设备的所有RAM
				 * @return 返回所有内存大小，单位：kb
				 */
				private int getAllMemory() {
					String filePath = "/proc/meminfo";
					int ram = 0;
					FileReader fr = null;
					BufferedReader localBufferedReader = null;
					try {
						fr = new FileReader(filePath);
						localBufferedReader = new BufferedReader(fr, 8192);
						String line = localBufferedReader.readLine();
						int a = line.length() - 3;
						int b = line.indexOf(' ');
						String str = line.substring(b, a);
						while (str.substring(0, 1).equals(" ")) {
							str = str.substring(1, str.length());
						}
						ram = Integer.parseInt(str);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							fr.close();
							localBufferedReader.close();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					return ram;
				}

				/**
				 * 得到设备的可用RAM
				 * @return 返回所有内存大小，单位：kb
				 */
				private long getAvailMemory(Context context) 
				{
					ActivityManager am = (ActivityManager) context
							.getSystemService(Context.ACTIVITY_SERVICE);
					ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
					am.getMemoryInfo(mi);
					return mi.availMem / 1024;
				}

				/**
				 * 得到设备的已用RAM
				 * @return 返回所有内存大小，单位：kb
				 */
				private long getUesdMemory(Context context) 
				{
					return getAllMemory() - getAvailMemory(context);
				}
				
				public int getUesdMemoryRate(Context context)
				{
					return (int) (getUesdMemory(context)*100/getAvailMemory(context));
				}
			
				/**
				 * 判断是否在白名单之内
				 * @param pkg
				 * @param list
				 * @return
				 */
				private static boolean isInWhiteList(String pkg,List<String> list)
				{
					boolean inOrNot = false;
					if(list!=null)
					{
						for(int i=0;i<list.size();i++)
						{
							if(pkg.equals(list.get(i)))
							{
								inOrNot = true;
								break;
							}
						}
					}
					
					return inOrNot;
				}
	
	
	
}