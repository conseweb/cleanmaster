package com.bitants.cleanmaster.floa;




import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bitants.cleanmaster.FunActivity;
import com.bitants.cleanmaster.R;
import com.bitants.cleanmaster.service.MService;
import com.bitants.cleanmaster.util.MemoryUtil;
import com.bitants.cleanmaster.view.CircleProgressBar;
import com.bitants.cleanmaster.view.CircleProgressBar2;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatService extends Service 
{
	private Timer timer;
	static ActivityManager mActivityManager;
	//  
	private FrameLayout mFloatLayout=null,mFloatLayout2=null;
    WindowManager.LayoutParams wmParams,wmParams2;
    //  
	WindowManager mWindowManager,mWindowManager2;
	
	private Button mFloatView,mFloatView2;
	private TextView back2;
	
	 CircleProgressBar2 progressBar;
	 
	 SharedPreferences sp; 
     SharedPreferences.Editor editor; 
     private static String pac;
     private static int fff=0,ap,tu,sx,sy,dx,dy,x0,y0,xs;
     private static Float tou=0.8f;
     private static String f1,f2,f3,f4,f5,f6,f7,f8,f9,f0;
     private PackageManager pm;
     private static String fsz,fsy,fxz,fxy,fzs,fzx,fys,fyx;  
     
	private static final String TAG = "FloatService";
	private static final String LOCK_TAG = "LockScreen";
	private float DownX,DownY,moveX,moveY;
	private  long moveTime,currentMS;
	 private  int mx,my,ox,ox2,oy,height,width,ss=0,ss2=0;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "oncreat");
		pm= this.getPackageManager();
		 sp = getSharedPreferences("SP", MODE_PRIVATE);  
	        editor = sp.edit();
	        pac = sp.getString("pac", "");
	        f0 = sp.getString("f0", "");
	       
	        ap = sp.getInt("ap", 6);
	        tou=sp.getFloat("tou", 0.8f);
	        tu = sp.getInt("tu", 21);
	        dx = sp.getInt("dx", 60);
	        dy = sp.getInt("dy", 60);
	        xs = sp.getInt("xs", 0);
	        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();  
	         height = display.getHeight();  
	         width = display.getWidth();  
	         int zhi= width/8;
	         ox=width/2-zhi;ox2=width/2+zhi;oy=width/2+20;
        //Toast.makeText(FxService.this, "create FxService", Toast.LENGTH_LONG);		
	}

	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO 自动生成的方法存根
		super.onStart(intent, startId);
		timer = new Timer();
		  timer.schedule(timertask, 1000, 1000);
		
	}
	
	//Handler
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			 xs = sp.getInt("xs", 0);
			if (xs==1){
			if (isHome()){
				if (mFloatLayout==null){
				createFloatView();}else{
					int i=getUsedPercent(getBaseContext());
					//  back.setText(i+"%");
					 if (i>=20&i<40){ progressBar.setProgressNotInUiThread(i,1);}else
					        if (i>=40&i<60){ progressBar.setProgressNotInUiThread(i,2);}else
					        if (i>=60&i<80){ progressBar.setProgressNotInUiThread(i,3);}else
					        if (i>=80&i<101){ progressBar.setProgressNotInUiThread(i,4);}else
					        {progressBar.setProgressNotInUiThread(i,1);}
				}
			
				
			}else {
				if(mFloatLayout != null)
				{
					mWindowManager.removeView(mFloatLayout);
					mFloatLayout=null;
				}
                     }
		}else{
			if (mFloatLayout==null){
			createFloatView();}else{
				int i=getUsedPercent(getBaseContext());
				//  back.setText(i+"%");
				 if (i>=20&i<40){ progressBar.setProgressNotInUiThread(i,1);}else
				        if (i>=40&i<60){ progressBar.setProgressNotInUiThread(i,2);}else
				        if (i>=60&i<80){ progressBar.setProgressNotInUiThread(i,3);}else
				        if (i>=80&i<101){ progressBar.setProgressNotInUiThread(i,4);}else
				        {progressBar.setProgressNotInUiThread(i,1);}
			}
			
		             }
		            	   
		                                        }
	};

	private TimerTask timertask = new TimerTask() {
		public void run() {
			Message message = new Message();
			handler.sendMessage(message);
		}
	};

	
	
	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private void createFloatView2()
	{   
		
		//NotificationManager notificationManager = (NotificationManager) this
		 //       .getSystemService(NOTIFICATION_SERVICE);
		//notificationManager.cancel(0);
		wmParams2 = new WindowManager.LayoutParams();
		// WindowManagerImpl.CompatModeWrapper
		 mWindowManager2 = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
		// window type
		//wmParams2.type = LayoutParams.TYPE_SYSTEM_ALERT; 
		
			wmParams2.type = LayoutParams.TYPE_PHONE; 
			//图片透明
	        wmParams2.format = PixelFormat.RGBA_8888; 
	        //  
	        wmParams2.flags = 
//	          LayoutParams.FLAG_NOT_TOUCH_MODAL |
	          LayoutParams.FLAG_NOT_FOCUSABLE
//	          LayoutParams.FLAG_NOT_TOUCHABLE
	          ;


		// 初始位置  
        int sx = sp.getInt("sx", 0);
        //sy = sp.getInt("sy", 100);
       
       
        int quyu=height/13;
       
	
        // 
        wmParams2.gravity = Gravity.CENTER | Gravity.TOP; 
        
  
        	wmParams2.height = quyu;
        	wmParams2.width = WindowManager.LayoutParams.FILL_PARENT;
        
        //wmParams2.x = sx;wmParams2.y = sy;
        wmParams2.x = 0;wmParams2.y = height-quyu;
        
        
        
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //悬浮窗口
        mFloatLayout2 = (FrameLayout) inflater.inflate(R.layout.float_layout2, null);
        // mFloatLayout
        mWindowManager2.addView(mFloatLayout2, wmParams2);
        
        Log.i(TAG, "mFloatLayout-->left" + mFloatLayout2.getLeft());
        Log.i(TAG, "mFloatLayout-->right" + mFloatLayout2.getRight());
        Log.i(TAG, "mFloatLayout-->top" + mFloatLayout2.getTop());
        Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout2.getBottom());      
        //浮窗大小
        android.view.ViewGroup.LayoutParams lp = mFloatLayout2.getLayoutParams();
       
        	//lp.height = dy;lp.width = dx;
        	lp.height = quyu;//lp.width = width;
        	lp.width=WindowManager.LayoutParams.FILL_PARENT;;
        	
        mFloatLayout2.setLayoutParams(lp);mFloatLayout2.requestLayout(); 
        // 窗口大小 
         
       
        
        mFloatView2 = (Button)mFloatLayout2.findViewById(R.id.float_id2);
        back2 = (TextView)mFloatLayout2.findViewById(R.id.back_id2);
        mFloatLayout2.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mFloatView2.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + mFloatView2.getMeasuredHeight()/2);
        
	}

	private void createFloatView()
	{   
		
		//NotificationManager notificationManager = (NotificationManager) this
		 //       .getSystemService(NOTIFICATION_SERVICE);
		//notificationManager.cancel(0);
		wmParams = new WindowManager.LayoutParams();
		// WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
		// window type
		wmParams.type = LayoutParams.TYPE_SYSTEM_ERROR|
				LayoutParams.FLAG_FULLSCREEN|LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		//图片透明
        wmParams.format = PixelFormat.RGBA_8888; 
        //  
        wmParams.flags = 
//          LayoutParams.FLAG_NOT_TOUCH_MODAL |
          LayoutParams.FLAG_NOT_FOCUSABLE
//          LayoutParams.FLAG_NOT_TOUCHABLE
          ;
        wmParams.alpha=tou;
        
        // 
        wmParams.gravity = Gravity.LEFT | Gravity.TOP; 
        
  
        	wmParams.height = dy;wmParams.width = dx;
      //  wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
       // wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        //
        sx = sp.getInt("sx", 0);
        sy = sp.getInt("sy", 100);
      
        
        wmParams.x = sx;
        wmParams.y = sy;
        
        if (height>=width){
        dx=dy=width/11;}else{dx=dy=height/10;};
        
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //悬浮窗口
        mFloatLayout = (FrameLayout) inflater.inflate(R.layout.float_layout, null);
        // mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        
        Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
        Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
        Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
        Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom()); 
        
        //
        android.view.ViewGroup.LayoutParams lp = mFloatLayout.getLayoutParams();
       
        	lp.height = dy;lp.width = dx;
       
        mFloatLayout.setLayoutParams(lp);mFloatLayout.requestLayout(); 
        // 
         
       
        
        mFloatView = (Button)mFloatLayout.findViewById(R.id.float_id);
     //   back = (TextView)mFloatLayout.findViewById(R.id.back_id);
        progressBar=(CircleProgressBar2)mFloatLayout.findViewById(R.id.circleProgressbar2);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight()/2);
        
        
        
        // 
   
        
        
       // back.setText(getUsedPercentValue(getBaseContext()));
        
        
        mFloatView.setOnTouchListener(new OnTouchListener() 
        {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				// TODO Auto-generated method stub
					
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					
					mFloatView.setBackgroundResource(R.drawable.m);
					//int a =  (int)(Math.random() * 4)  ; 
					//if (a==0){ mFloatView.setBackgroundResource(R.drawable.ic_launcher0);}else
					//if (a==1){ mFloatView.setBackgroundResource(R.drawable.ic_launcher20);}else
					//if (a==2){ mFloatView.setBackgroundResource(R.drawable.ic_launcher30);}else
					//{mFloatView.setBackgroundResource(R.drawable.ic_launcher40);}
				//	back.setVisibility(0);
					
					 DownX = event.getX();//
					    DownY = event.getY();//float 
					    x0=(int) event.getRawX()- mFloatView.getMeasuredWidth()/2;
					    y0=(int) event.getRawY()- mFloatView.getMeasuredHeight()/2 - 30;
					    currentMS = System.currentTimeMillis();//     
				// Log.i("startP", "startX"+mTouchStartX+"====startY"+mTouchStartY);
				// isPressed=false;
				break;
				case MotionEvent.ACTION_MOVE:   
				//
                     Log.e("YYY", "Y"+event.getY());
					  moveX = event.getX() - DownX;//X
					     moveY = event.getY() - DownY;//y
					    moveTime = System.currentTimeMillis() - currentMS;  //
					  
					   
					    if (event.getRawY()<(200)& moveY>0) {
						     	if (fff==1){
						    	if(mFloatLayout2 != null)
							 	{
							 		mWindowManager2.removeView(mFloatLayout2);mFloatLayout2=null;
							 		fff=0;
							 	}
						                    }
						               }
					    
					   // if (event.getRawY()<=(height/1.5)&event.getRawY()>=(height/2.4)) {
					    //	if (fff==1){
					    //	if(mFloatLayout2 != null)
						//	{
						//		mWindowManager2.removeView(mFloatLayout2);
						//	}
					    //	            }else{
									    	if (fff==0){
										    	createFloatView2();fff=1;
										    	}else{
										    		//mFloatView2.setBackgroundResource(R.drawable.t);
										    	}
										    	
								//		    }
					 //   }
					    if (event.getRawY()>(height/1.2)&event.getRawX()>ox&event.getRawX()<ox2) {
						   	if (fff==1){
						    //	if(mFloatLayout2 != null)
							//	{
							//		mWindowManager2.removeView(mFloatLayout2);
							//	}
						    //	            }else{
										    
						   		mFloatView2.setBackgroundResource(R.drawable.u);			    	
											    }
						    }else{

					    
					//if (moveTime>=2200) { //
					  wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth()/2;
					//Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
					Log.i(TAG, "RawX" + event.getRawX());
					Log.i(TAG, "X" + event.getX());
					// 
		           wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight()/2 - 20;
		           // Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredHeight()/2);
		            Log.i(TAG, "RawY" + event.getRawY());
		            Log.i(TAG, "Y" + event.getY());
				//	}
		   
		             // 
		              mWindowManager.updateViewLayout(mFloatLayout, wmParams);}
					
			//	int mTouchStartX = (int) event.getRawX() - mFloatView.getMeasuredWidth()/2;
			//	int mTouchStartY = (int) event.getRawY() - mFloatView.getMeasuredHeight()/2 - 20;
				//isPressed=true;
				break;

				case MotionEvent.ACTION_UP:
				//event.getXPrecision()
				//	back.setVisibility(4);
				if(moveX>11||moveY>11||moveX<-11||moveY<-11)
				{
					
					if (event.getRawY()>=(height/1.4)){
						
						  Toast.makeText(getBaseContext(), "成功清理！清爽运行中", Toast.LENGTH_SHORT).show();
							MemoryUtil.clearMemory(getApplicationContext());
							//SystemClock.sleep(1000);
						
					}else{
					
					     }
					
					if (event.getRawY()<=(height/1.8)){
					                	//更新
					                	editor.putInt("sx", wmParams.x);
					                	editor.putInt("sy", wmParams.y);
					                	editor.commit();
				 	mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					                                  }else{
					                                	  wmParams.x=x0; wmParams.y=y0;
					                                	  mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					                                  }
					// moveX=moveY=0;
				}
				else {  //单击
					if  (moveTime<=300) {  
				
						int ff=0; //sp.getInt("ff", 0);
						if (ff==0)
						{Intent i = new Intent(FloatService.this,FunActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
						startActivity(i);}
					
			                              };
					                    }ss=ss2=0;x0=y0=0;moveX=moveY=DownX=DownY=moveTime=0;
					                    mFloatView.setBackgroundResource(R.drawable.back1);
					                 	if (fff==1){
					    				   	if(mFloatLayout2 != null)
					    					{
					    							mWindowManager2.removeView(mFloatLayout2);
					    							mFloatLayout2 = null;fff=0;
					    			     	}
					    				               }
					 
				break;
				}
				return true;

			}
			});
			
        
        mFloatView.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
			
				
				//Intent i = new Intent(FloatService.this,FunActivity.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			//	startActivity(i);
			}
		});
     
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
	
	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		// showNotification();
		if(mFloatLayout != null)
		{
			mWindowManager.removeView(mFloatLayout);mFloatLayout=null;
		}
		if(mFloatLayout2 != null)
		{
			fff=0;
			mWindowManager2.removeView(mFloatLayout2);mFloatLayout2=null;
		}
	}

	
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
    
    
    /**
	* 计算已使用内存的百分比，并返回。
	*
	* @param context
	* 可传入应用程序上下文。
	* @return 已使用内存的百分比，以字符串形式返回。
	*/
	public static int getUsedPercent(Context context) {
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
	return percent;
	} catch (IOException e) {
	e.printStackTrace();
	}
	return 1;
	}
	
}
