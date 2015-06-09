package com.bitants.cleanmaster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent.ShortcutIconResource;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.bitants.cleanmaster.floa.FloatService;
import com.bitants.cleanmaster.util.MemoryUtil;
import com.bitants.cleanmaster.view.CircleProgressBar;

@TargetApi(19) public class MainActivity extends Activity implements OnTouchListener{

	
	static ActivityManager mActivityManager;
	private Button mButton1, mButton2, mButton3, mButton4,clean,sett,exit;
	private TextView xinxi;
	 /** 
     * 滚动显示和隐藏menu时，手指滑动需要达到的速度。 
     */  
    public static final int SNAP_VELOCITY = 200;  
    /** 
     * 用于计算手指滑动的速度。 
     */  
    private VelocityTracker mVelocityTracker;  
    /** 
     * 屏幕宽度值。 
     */  
    private int screenWidth;   long cha0,cha1;
  
    /** 
     * menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。 
     */  
    private int leftEdge;  
  
    /** 
     * menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。 
     */  
    private int rightEdge = 0;  
  
    /** 
     * menu完全显示时，留给content的宽度值。 
     */  
    private int menuPadding = 80;  
  
    /** 
     * 主内容的布局。 
     */  
    private View content;  
  
    /** 
     * menu的布局。 
     */  
    private View menu;  
  
    /** 
     * menu布局的参数，通过此参数来更改leftMargin的值。 
     */  
    private LinearLayout.LayoutParams menuParams;  
  
    /** 
     * 记录手指按下时的横坐标。 
     */  
    private float xDown;  
  
    /** 
     * 记录手指移动时的横坐标。 
     */  
    private float xMove;  
  
    /** 
     * 记录手机抬起时的横坐标。 
     */  
    private float xUp;
    private int rate,rate2,xs;
    private static ToggleButton mTbtnLight1;
    SharedPreferences sp; 
    SharedPreferences.Editor editor; 
  
    /** 
     * menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。 
     */  
    private boolean isMenuVisible; 
    CircleProgressBar progressBar;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		if (android.os.Build.VERSION.SDK_INT > 18) 
		{    
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		    
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		//RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout); 7 relativeLayout.setPadding(0, getActionBarHeight()+getStatusBarHeight(), 0, 0); 8 
		}
		setContentView(R.layout.activity_main);
		
		  cha0=getAvailableMemory(getBaseContext())/1000000;
		    String nc0="总CPU内存："+getTotalValue()/1024+" M"+"\n"+"剩余："+cha0+" M";
			
		   rate=getUsedPercent(getBaseContext());
		   
		   
		   sp = getSharedPreferences("SP", MODE_PRIVATE);  
	         editor = sp.edit();
	      xs=sp.getInt("xs", 0); 
	      int diyi=sp.getInt("diyi", 0); 
	      
	      if (diyi==0){createShortcut();
	      editor.putInt("diyi",1);editor.commit();
	      }
	      
	         
		mButton1 = (Button) findViewById(R.id.button1);
		mButton2 = (Button) findViewById(R.id.button2);
		mButton3 = (Button) findViewById(R.id.button3);
		mButton4 = (Button) findViewById(R.id.button4);
		xinxi = (TextView) findViewById(R.id.xinxi);
		sett = (Button) findViewById(R.id.sett);
		clean = (Button) findViewById(R.id.clean);
		exit = (Button) findViewById(R.id.exit);
		progressBar=(CircleProgressBar)findViewById(R.id.circleProgressbar);
		
		  //
        mTbtnLight1 = (ToggleButton)findViewById(R.id.mTbtnLight1);
        mTbtnLight1.setSelected(true);
        mTbtnLight1.setOnCheckedChangeListener(mChangeListener1); 
        
        if (xs==1){
        	 mTbtnLight1.setChecked(true);
        }else{ mTbtnLight1.setChecked(false);}
        
        xinxi.setText(nc0);
		
		mButton1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,FloatService.class);
				//intent.putExtra("action", 1);
				stopService(intent);
				
				Toast.makeText(getApplicationContext(), "关闭浮窗！", Toast.LENGTH_SHORT).show();
				
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			

				scrollToContent();
			
			}
		});
		sett.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				scrollToMenu();
				
			
			}
		});

		mButton2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,FloatService.class);
				//intent.putExtra("action", 2);
				stopService(intent);startService(intent);
				Toast.makeText(getApplicationContext(), "开启浮窗！", Toast.LENGTH_SHORT).show();
				
				
				
			}
		});
		
		mButton4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SetWhiteListActivity.class);
				startActivity(intent);
				
				finish();
			}
		});

		mButton3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				MemoryUtil.clearMemory(getApplicationContext());
				cha1=getAvailableMemory(getBaseContext())/1000000;rate2=getUsedPercent(getBaseContext());
				String nc="总CPU内存："+getTotalValue()/1024+" M"+"\n"+"剩余："+cha1+" M";
				
				long cha=cha1-cha0;
				if (cha>0){
				Toast.makeText(getApplicationContext(), "成功清理:"+cha+" M", Toast.LENGTH_SHORT).show();
				}else{Toast.makeText(getApplicationContext(), "良好运行中，无需清理", Toast.LENGTH_SHORT).show();}
				cha0=cha1;

				if (rate>rate2){
				 new Thread()
			        {
			        public void run()
			        {
			        int i=rate;
			        while(i>=rate2)
			        {
			        	 if (i>=20&i<40){ progressBar.setProgressNotInUiThread(i,1);}else
						        if (i>=40&i<60){ progressBar.setProgressNotInUiThread(i,2);}else
						        if (i>=60&i<80){ progressBar.setProgressNotInUiThread(i,3);}else
						        if (i>=80&i<101){ progressBar.setProgressNotInUiThread(i,4);}else
						        {progressBar.setProgressNotInUiThread(i,1);}
			        i--;
			        try {
			        sleep(100);
			        } catch (InterruptedException e) {
			        // TODO 自动生成的 catch 块
			        e.printStackTrace();
			        }
			        }
			         
			        }
			        }.start();}
				
			  xinxi.setText(nc);
        
			}
		});
		
		clean.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				MemoryUtil.clearMemory(getApplicationContext());
				cha1=getAvailableMemory(getBaseContext())/1000000;rate2=getUsedPercent(getBaseContext());
				String nc="总CPU内存："+getTotalValue()/1024+" M"+"\n"+"剩余："+cha1+" M";
				
				long cha=cha1-cha0;
				if (cha>0){
				Toast.makeText(getApplicationContext(), "成功清理:"+cha+" M", Toast.LENGTH_SHORT).show();
				}else{Toast.makeText(getApplicationContext(), "良好运行中，无需清理", Toast.LENGTH_SHORT).show();}
				cha0=cha1;

				if (rate>rate2){
				 new Thread()
			        {
			        public void run()
			        {
			        int i=rate;
			        while(i>=rate2)
			        {
			        if (i>=20&i<40){ progressBar.setProgressNotInUiThread(i,1);}else
			        if (i>=40&i<60){ progressBar.setProgressNotInUiThread(i,2);}else
			        if (i>=60&i<80){ progressBar.setProgressNotInUiThread(i,3);}else
			        if (i>=80&i<101){ progressBar.setProgressNotInUiThread(i,4);}else
			        {progressBar.setProgressNotInUiThread(i,1);}
			        
			        i--;
			        try {
			        sleep(100);
			        } catch (InterruptedException e) {
			        // TODO 自动生成的 catch 块
			        e.printStackTrace();
			        }
			        }
			         
			        }
			        }.start();}
				rate=rate2+3;
				 xinxi.setText(nc);
			
        
			}
		});
		
		Intent intent = new Intent(MainActivity.this,FloatService.class);
		//intent.putExtra("action", 1);
		stopService(intent);startService(intent);
		
		 initValues();  
	        content.setOnTouchListener(this);  
	        
	        
	        new Thread()
	        {
	        public void run()
	        {
	        int i=0;
	        while(i<=rate)
	        {
	        	 if (i>=20&i<40){ progressBar.setProgressNotInUiThread(i,1);}else
				        if (i>=40&i<60){ progressBar.setProgressNotInUiThread(i,2);}else
				        if (i>=60&i<80){ progressBar.setProgressNotInUiThread(i,3);}else
				        if (i>=80&i<101){ progressBar.setProgressNotInUiThread(i,4);}else
				        {progressBar.setProgressNotInUiThread(i,1);}
	        i++;
	        try {
	        sleep(50);
	        } catch (InterruptedException e) {
	        // TODO 自动生成的 catch 块
	        e.printStackTrace();
	        }
	        }
	         
	        }
	        }.start();
		
	}
	
	
	  //
	  private OnCheckedChangeListener mChangeListener1 = new OnCheckedChangeListener()
     {
             
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
             {
                     if (isChecked)
                     {
                  	xs=1;editor.putInt("xs", 1);editor.commit();
                     }
                     else 
                     {
                    		xs=0;editor.putInt("xs", 0);editor.commit();	
                     }
             }
     };  
	
	  /** 
     * 初始化一些关键性数据。包括获取屏幕的宽度，给content布局重新设置宽度，给menu布局重新设置宽度和偏移距离等。 
     */  
    private void initValues() {  
        WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
        screenWidth = window.getDefaultDisplay().getWidth();  
        content = findViewById(R.id.content);  
        menu = findViewById(R.id.menu);  
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();  
        // 将menu的宽度设置为屏幕宽度减去menuPadding  
        menuParams.width = screenWidth - menuPadding;  
        // 左边缘的值赋值为menu宽度的负数  
        leftEdge = -menuParams.width;  
        // menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见  
        menuParams.leftMargin = leftEdge;  
        // 将content的宽度设置为屏幕宽度  
        content.getLayoutParams().width = screenWidth;  
    }  
  
    @Override  
    public boolean onTouch(View v, MotionEvent event) {  
        createVelocityTracker(event);  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            // 手指按下时，记录按下时的横坐标  
            xDown = event.getRawX();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu  
            xMove = event.getRawX();  
            int distanceX = (int) (xMove - xDown);  
            if (isMenuVisible) {  
                menuParams.leftMargin = distanceX;  
            } else {  
                menuParams.leftMargin = leftEdge + distanceX;  
            }  
            if (menuParams.leftMargin < leftEdge) {  
                menuParams.leftMargin = leftEdge;  
            } else if (menuParams.leftMargin > rightEdge) {  
                menuParams.leftMargin = rightEdge;  
            }  
            menu.setLayoutParams(menuParams);  
            break;  
        case MotionEvent.ACTION_UP:  
            // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面  
            xUp = event.getRawX();  
            if (wantToShowMenu()) {  
                if (shouldScrollToMenu()) {  
                    scrollToMenu();  
                } else {  
                    scrollToContent();  
                }  
            } else if (wantToShowContent()) {  
                if (shouldScrollToContent()) {  
                    scrollToContent();  
                } else {  
                    scrollToMenu();  
                }  
            }  
            recycleVelocityTracker();  
            break;  
        }  
        return true;  
    }  
  
    /** 
     * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。 
     *  
     * @return 当前手势想显示content返回true，否则返回false。 
     */  
    private boolean wantToShowContent() {  
        return xUp - xDown < 0 && isMenuVisible;  
    }  
  
    /** 
     * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是正数，且当前menu是不可见的，则认为当前手势是想要显示menu。 
     *  
     * @return 当前手势想显示menu返回true，否则返回false。 
     */  
    private boolean wantToShowMenu() {  
        return xUp - xDown > 0 && !isMenuVisible;  
    }  
  
    /** 
     * 判断是否应该滚动将menu展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY， 
     * 就认为应该滚动将menu展示出来。 
     *  
     * @return 如果应该滚动将menu展示出来返回true，否则返回false。 
     */  
    private boolean shouldScrollToMenu() {  
        return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;  
    }  
  
    /** 
     * 判断是否应该滚动将content展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2， 
     * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将content展示出来。 
     *  
     * @return 如果应该滚动将content展示出来返回true，否则返回false。 
     */  
    private boolean shouldScrollToContent() {  
        return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;  
    }  
  
    /** 
     * 将屏幕滚动到menu界面，滚动速度设定为30. 
     */  
    private void scrollToMenu() {  
        new ScrollTask().execute(30);  
    }  
  
    /** 
     * 将屏幕滚动到content界面，滚动速度设定为-30. 
     */  
    private void scrollToContent() {  
        new ScrollTask().execute(-30);  
    }  
  
    /** 
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。 
     *  
     * @param event 
     *            content界面的滑动事件 
     */  
    private void createVelocityTracker(MotionEvent event) {  
        if (mVelocityTracker == null) {  
            mVelocityTracker = VelocityTracker.obtain();  
        }  
        mVelocityTracker.addMovement(event);  
    }  
  
    /** 
     * 获取手指在content界面滑动的速度。 
     *  
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。 
     */  
    private int getScrollVelocity() {  
        mVelocityTracker.computeCurrentVelocity(1000);  
        int velocity = (int) mVelocityTracker.getXVelocity();  
        return Math.abs(velocity);  
    }  
  
    /** 
     * 回收VelocityTracker对象。 
     */  
    private void recycleVelocityTracker() {  
        mVelocityTracker.recycle();  
        mVelocityTracker = null;  
    }  
  
    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {  
  
        protected Integer doInBackground(Integer... speed) {  
            int leftMargin = menuParams.leftMargin;  
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。  
            while (true) {  
                leftMargin = leftMargin + speed[0];  
                if (leftMargin > rightEdge) {  
                    leftMargin = rightEdge;  
                    break;  
                }  
                if (leftMargin < leftEdge) {  
                    leftMargin = leftEdge;  
                    break;  
                }  
                publishProgress(leftMargin);  
                // 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。  
                sleep(20);  
            }  
            if (speed[0] > 0) {  
                isMenuVisible = true;  
            } else {  
                isMenuVisible = false;  
            }  
            return leftMargin;  
        }  
        
        @Override 
        protected void onProgressUpdate(Integer... leftMargin) {  
            menuParams.leftMargin = leftMargin[0];  
            menu.setLayoutParams(menuParams);  
        }  
        
        @Override 
        protected void onPostExecute(Integer leftMargin) {  
            menuParams.leftMargin = leftMargin;  
            menu.setLayoutParams(menuParams);  
        }  
    }  
  
    /** 
     * 使当前线程睡眠指定的毫秒数。 
     *  
     * @param millis 
     *            指定当前线程睡眠多久，以毫秒为单位 
     */  
    private void sleep(long millis) {  
        try {  
            Thread.sleep(millis);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }  
	
	@Override
	protected void onResume() {
		super.onResume();
		
		boolean isServiceRunning = isServiceRunning(this, "com.bitants.cleanmaster.service.CoreService");
		
	//	mButton1.setVisibility(isServiceRunning ? View.GONE : View.VISIBLE);
	//	mButton2.setVisibility(isServiceRunning ? View.VISIBLE : View.GONE);
	}
	
	private boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
        mContext.getSystemService(Context.ACTIVITY_SERVICE); 
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
        
        if (!(serviceList.size()>0)) {
            return false;
        }

        for (int i=0; i<serviceList.size(); i++) {
        	
        	String serviceName = serviceList.get(i).service.getClassName();
        	
        	Log.i("@Cundong", "serviceName:" + serviceName);
            if (serviceName.equals(className)) {
            	
            	
                isRunning = true;
                break;
            }
        }
        return isRunning;
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
		
		 /**
			* 计算已使用内存的百分比，并返回。
			*
			* @param context
			* 可传入应用程序上下文。
			* @return 已使用内存的百分比，以字符串形式返回。
			*/
			public static long getTotalValue() {
			String dir = "/proc/meminfo";
			try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr, 2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
			br.close();
			long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
			
			return totalMemorySize;
			} catch (IOException e) {
			e.printStackTrace();
			}
			return 0;
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
		
		private void createShortcut()
		{
			 Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			 String name = getResources().getString(R.string.app_name);
		     shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,name);
		     //不允许重复创建
			 shortcut.putExtra("duplicate", false);  
			 Intent shortcutIntent = new Intent();
			 ComponentName componentName = new ComponentName(getPackageName(), "com.bitants.cleanmaster.MainActivity");
			 shortcutIntent.setComponent(componentName);
			 shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
			 ShortcutIconResource iconRes=null;
			 iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.oh);
			 shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes); 
			 sendBroadcast(shortcut); 
			 Log.i("AAA", "sendBroadcast : INSTALL_SHORTCUT");
		}
	
	
}


