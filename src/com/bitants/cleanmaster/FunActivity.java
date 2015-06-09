package com.bitants.cleanmaster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.bitants.cleanmaster.util.MemoryUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;



public class FunActivity extends Activity {

	static ActivityManager mActivityManager;
	private TextView neicun;
 	private android.os.Handler handler =null;
	// Handler handler = new Handler(); 
	 private static ToggleButton mTbtnLight7,mTbtnLight2,mTbtnLight3,mTbtnLight4,mTbtnLight5,mTbtnLight6;
	 
	 private static  PowerManager pmm;
	   private static  ContentResolver cr;
	    private static  WifiManager mWm;
	    private static BluetoothAdapter mBluetoothAdapter;
	    private static RotationObserver mRotationObserver;  
	    private long cha0,cha1;
	    SharedPreferences sp; 
	     SharedPreferences.Editor editor; 
	     private static  String pac="";
	     private int b,qf;
	     private static String f20,f21,f22;
	    
	    private ConnectivityManager mConnectivityManager;  
	    // 移动数据设置改变系统发送的广播  
	 //   private static final String NETWORK_CHANGE = "android.intent.action.ANY_DATA_STATE";  
	    
	    private IntentFilter mIntentFilter; 
	    
	    private Camera mCamera;
	    private Camera.Parameters parameters;
	    int ran=1;
		  //得到屏幕旋转的状态  
	    private int getRotationStatus(Context context)  
	    {  
	        int status = 0;  
	        try  
	        {  
	            status = android.provider.Settings.System.getInt(context.getContentResolver(),  
	                    android.provider.Settings.System.ACCELEROMETER_ROTATION);  
	        }  
	        catch (SettingNotFoundException e)  
	        {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        return status;  
	    }  
	  
	    private void setRotationStatus(ContentResolver resolver, int status)  
	    {  
	        //得到uri  
	        Uri uri = android.provider.Settings.System.getUriFor("accelerometer_rotation");  
	        //沟通设置status的值改变屏幕旋转设置  
	        android.provider.Settings.System.putInt(resolver, "accelerometer_rotation", status);  
	        //通知改变  
	        resolver.notifyChange(uri, null);  
	    }  
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > 18) 
		{    
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		    
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		//RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout); 7 relativeLayout.setPadding(0, getActionBarHeight()+getStatusBarHeight(), 0, 0); 8 
		}
		
		  pmm= (PowerManager)this.getSystemService(Context.POWER_SERVICE);
	         
	         
	         sp = getSharedPreferences("SP", MODE_PRIVATE);  
	         editor = sp.edit();
	       
	         editor.putInt("ff", 1);
	         editor.commit();
		        
		    mIntentFilter = new IntentFilter();  
	        // 添加广播接收器过滤的广播  
	        mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED"); 
		    mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
		    LocationManager alm =(LocationManager)this.getSystemService( Context.LOCATION_SERVICE );
		    
		    mRotationObserver = new RotationObserver(new Handler());  
		    
		    mCamera = Camera.open();  
	       	 parameters = mCamera.getParameters();
	        
		
		setContentView(R.layout.fun);
		Button button = (Button) findViewById(R.id.button1);
		Button close = (Button) findViewById(R.id.button2);
		Button set = (Button) findViewById(R.id.button4);
		
		Button liang = (Button) findViewById(R.id.liang);
		Button app = (Button) findViewById(R.id.appoption);
		neicun = (TextView) findViewById(R.id.neicun);
		
		app.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent intent=new Intent(FunActivity.this, LockActivity.class);
				startActivity(intent);finish();
			}
		});
		
		liang.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			int mode = getScreenMode();
			if (ran>=5) ran=0;
			if (mode==1) //自动
			{
				setScreenMode(0);//调为手动
			}
			
			switch (ran) {
			case 0:setScreenMode(1); Toast.makeText(FunActivity.this, "亮度：自动", 200).show();
			break;
			
			case 1:saveScreenBrightness(10);setScreenBrightness(10); Toast.makeText(FunActivity.this, "亮度：弱", 200).show();
			break;
			case 2:saveScreenBrightness(100);setScreenBrightness(90); Toast.makeText(FunActivity.this, "亮度：一般", 200).show();
			break;
			case 3:saveScreenBrightness(200);setScreenBrightness(150); Toast.makeText(FunActivity.this, "亮度：中", 200).show();
			break;
			case 4:saveScreenBrightness(200);setScreenBrightness(240); Toast.makeText(FunActivity.this, "亮度：强", 200).show();
			break;
			
			default:break;
			}
				 ran=ran+1;
				
			}
		});
		
		button.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				  
				MemoryUtil.clearMemory(getApplicationContext());
				cha1=getAvailableMemory(getBaseContext())/1000000;
				String nc="当前使用CPU内存："+getUsedPercentValue(getBaseContext())+"\n"+"总CPU内存："+getTotalValue()/1024+" M"+"\n"+"剩余："+cha1+" M";
				neicun.setText(nc);
				long cha=cha1-cha0;
				if (cha>0){
				Toast.makeText(FunActivity.this, "成功清理:"+cha+" M", Toast.LENGTH_SHORT).show();
				}else{Toast.makeText(FunActivity.this, "良好运行中，无需清理", Toast.LENGTH_SHORT).show();}
				cha0=cha1;
			
			}
		});
		set.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
     	Intent intent=new Intent(FunActivity.this, MainActivity.class);
			startActivity(intent);
				finish();
			
			}
		});
		close.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
			finish();
			//	Intent intent=new Intent(MainActivity.this, CleanActivity.class);
			//	startActivity(intent);
			}
		});
	    cha0=getAvailableMemory(getBaseContext())/1000000;
	    String nc0="当前使用CPU内存："+getUsedPercentValue(getBaseContext())+"\n"+"总CPU内存："+getTotalValue()/1024+" M"+"\n"+"剩余："+cha0+" M";
		
		neicun.setText(nc0);
      
         
         //wifi
         mTbtnLight2 = (ToggleButton)findViewById(R.id.wifi);
         mTbtnLight2.setSelected(true);
         mTbtnLight2.setOnCheckedChangeListener(mChangeListener2);
         
         mTbtnLight2.setOnLongClickListener(new OnLongClickListener() 
            {	
    			public boolean onLongClick(View v) 
    			{
    				// TODO Auto-generated method stub
    				 if(android.os.Build.VERSION.SDK_INT > 10) {
    		            	// 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面 
    		            	startActivity(new Intent( android.provider.Settings.ACTION_WIFI_SETTINGS)); 
    		            	} else { 
    		            	startActivity(new Intent( android.provider.Settings.ACTION_WIRELESS_SETTINGS)); 
    		            	}
					return false;
    			}
    		});
        
         
         
         //2g 3g
         mTbtnLight3 = (ToggleButton)findViewById(R.id.edge);
         mTbtnLight3.setSelected(true);
         mTbtnLight3.setOnCheckedChangeListener(mChangeListener3);
         mTbtnLight3.setOnLongClickListener(new OnLongClickListener() 
            {	
    			public boolean onLongClick(View v) 
    			{
    				// TODO Auto-generated method stub
    	
    		            	startActivity(new Intent( android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS)); 
    		            	
					return false;
    			}
    		});
         
         //gps
         mTbtnLight4 = (ToggleButton)findViewById(R.id.gps);
         mTbtnLight4.setSelected(true);
         mTbtnLight4.setOnCheckedChangeListener(mChangeListener4);
         mTbtnLight4.setOnLongClickListener(new OnLongClickListener() 
            {	
    			public boolean onLongClick(View v) 
    			{
    				// TODO Auto-generated method stub
    	
    		            	startActivity(new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)); 
    		            	
					return false;
    			}
    		});
         
       //蓝牙
         mTbtnLight5 = (ToggleButton)findViewById(R.id.blue);
         mTbtnLight5.setSelected(true);
         mTbtnLight5.setOnCheckedChangeListener(mChangeListener5);
         mTbtnLight5.setOnLongClickListener(new OnLongClickListener() 
            {	
    			public boolean onLongClick(View v) 
    			{
    				// TODO Auto-generated method stub
    	
    		            	startActivity(new Intent( android.provider.Settings.ACTION_BLUETOOTH_SETTINGS)); 
    		            	
					return false;
    			}
    		});
         
         
         //屏幕旋转
         mTbtnLight6 = (ToggleButton)findViewById(R.id.xuan);
         mTbtnLight6.setSelected(true);
         mTbtnLight6.setOnCheckedChangeListener(mChangeListener6); 
         
         
         //屏幕旋转
         mTbtnLight7 = (ToggleButton)findViewById(R.id.light);
         mTbtnLight7.setSelected(true);
         mTbtnLight7.setOnCheckedChangeListener(mChangeListener7); 
         
        mTbtnLight7.setChecked(false);
        		 
         
      
         
      
        		
         
         if (isWiFi()){
        	 qf=1;
        	 mTbtnLight2.setChecked(true);
            } else{mTbtnLight2.setChecked(false);
            qf=0;}; 
         
         if (getMobileDataStatus())  
            {  
        	 mTbtnLight3.setChecked(true);
            } else{mTbtnLight3.setChecked(false);}; 
         
        if (hasGPSDevice(FunActivity.this)){
         if( alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER ) )
           {mTbtnLight4.setChecked(true);
           }else{mTbtnLight4.setChecked(false);};
                                    }else{mTbtnLight4.setChecked(false);}
        
        
        //蓝牙开关有问题
       mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
       		  if (mBluetoothAdapter == null) {
       			  mTbtnLight5.setChecked(false);
       		  }else if (!mBluetoothAdapter.isEnabled()) {
       			  mTbtnLight5.setChecked(false);// 本地蓝牙没开            			 
       		  }else{ mTbtnLight5.setChecked(true);};
       		  
        
        //屏幕旋转
        if (getRotationStatus(this) == 0)  
        {  
        	mTbtnLight6.setChecked(false);
        }  
        else  
        {  
        	mTbtnLight6.setChecked(true);
        }  ;
        
       //  cr = getContentResolver();
        
   
         
       if (false){
         // 取得当前亮度
         int normal = Settings.System.getInt(getContentResolver(),
         Settings.System.SCREEN_BRIGHTNESS, 255);
         // 进度条绑定当前亮度
      
         int tmpInt =100;
         // 根据当前进度改变亮度
         Settings.System.putInt(getContentResolver(),
         Settings.System.SCREEN_BRIGHTNESS, tmpInt);
         tmpInt = Settings.System.getInt(getContentResolver(),
         Settings.System.SCREEN_BRIGHTNESS, -1);
         WindowManager.LayoutParams wl = getWindow().getAttributes();
         float tmpFloat = (float) tmpInt / 255;
         if (tmpFloat > 0 && tmpFloat <= 1) {
         wl.screenBrightness = tmpFloat;
         }
         getWindow().setAttributes(wl);
         
         
         }
         
       
        
}



//Wifi开关
private OnCheckedChangeListener mChangeListener2 = new OnCheckedChangeListener()
{
    
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
              if (isChecked)
              {
            	  if (qf==1){}else{
            	  setWifi(false);
            	  setWifi(true);}
              }
              else 
              {
            	  setWifi(false);qf=0;
              }
      }
};  

public void setWifi(boolean isEnable) {  
  
  //  
  if (mWm == null) {  
      mWm = (WifiManager)getBaseContext().getSystemService(Context.WIFI_SERVICE);  
      return;  
  }  
  if (isEnable) {// 开启wifi  
	 if (!mWm.isWifiEnabled()) {  
          mWm.setWifiEnabled(true);  
      }  
  } else {  
      // 关闭 wifi  
      if (mWm.isWifiEnabled()) {  
          mWm.setWifiEnabled(false);  
      }  
  }  

}  

//网络开关
 private OnCheckedChangeListener mChangeListener3 = new OnCheckedChangeListener()
 {
       
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
                 if (isChecked)
                 {
                	 if (getMobileDataStatus())  
                     {  
                     }  
                     else  
                     {  
                         setMobileDataStatus(true); 
                     }  
                 }
                 else 
                 {
                	 if (getMobileDataStatus())  
                     {  
                         setMobileDataStatus(false);   
                     }  ;
                 }
         }
 };  
 
 //gps开关
	  private OnCheckedChangeListener mChangeListener4 = new OnCheckedChangeListener()
    {
           
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                    if (isChecked)
                    {
                    	startActivity(new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)); 
		            	
                    	if(false){
                    	//打开GPS
                    //	Settings.Secure.setLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER, true);
                    	if (hasGPSDevice(FunActivity.this)){
                    		
                    		
                    		Intent gpsIntent = new Intent();  
                    	    gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");  
                    	    gpsIntent.addCategory("android.intent.category.ALTERNATIVE");  gpsIntent.setData(Uri.parse("custom:3"));  
                    	    try {  
                    	        PendingIntent.getBroadcast(FunActivity.this, 0, gpsIntent, 0).send();  
                    	    } catch (CanceledException e) {  
                    	        e.printStackTrace();  
                    	    }        }else {
                    		Toast.makeText(FunActivity.this, "本机没有找到GPS硬件或驱动！", Toast.LENGTH_SHORT).show();
                    		SystemClock.sleep(500);mTbtnLight4.setChecked(false);
                    	                                    };
                    	}
                    	mTbtnLight4.setChecked(false);
                    	
                    }else{
                    	Intent gpsIntent = new Intent();  
                        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");  
                        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");  gpsIntent.setData(Uri.parse("custom:3"));  
                        try {  
                            PendingIntent.getBroadcast(FunActivity.this, 0, gpsIntent, 0).send();  
                        } catch (CanceledException e) {  
                            e.printStackTrace();  
                        }    }
            }
    };  
    
    //蓝牙 开关
 	  private OnCheckedChangeListener mChangeListener5 = new OnCheckedChangeListener()
       {
               
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
               {
                       if (isChecked)
                       {
                    	   if (mBluetoothAdapter == null) {
    	            		   Toast.makeText(FunActivity.this, "本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_SHORT).show();}
                    	   mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
   	    				//直接打开系统的蓝牙设置面板
   	    				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
   	    				startActivityForResult(intent, 0x1);
   	    				//直接打开蓝牙
   	    				mBluetoothAdapter.enable();
   	    				//关闭蓝牙
   	    				//adapter.disable();
   	    				//打开本机的蓝牙发现功能（默认打开120秒，可以将时间最多延长至300秒）
   	    				Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
   	    				discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);//设置持续时间（最多300秒）
   	    				startActivityForResult(discoveryIntent, 0x1);
                       }
                       else 
                       {
      	    				mBluetoothAdapter.disable();
                       }
               }
       };  
       
//屏幕旋转 开关
private OnCheckedChangeListener mChangeListener6 = new OnCheckedChangeListener()
        {
                
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                        if (isChecked)
                        {
                        	if (getRotationStatus(FunActivity.this) == 0)  
                            {  
                               
                                setRotationStatus(getContentResolver(), 1);  
                            }
                        }
                        else 
                        {
                        	if (getRotationStatus(FunActivity.this) == 1)  
                            {  
                               
                                setRotationStatus(getContentResolver(), 0);  
                            }  
                            
                        }
                }
        };       
        
      //
        private OnCheckedChangeListener mChangeListener7 = new OnCheckedChangeListener()
                {
                        
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                        {
                                if (isChecked)
                                {
                                	openLight();
                                }
                                else 
                                {
                                	closeLight();
                                    
                                }
                        }
                };  

                
                /**
                 * 鎵撳紑鎵嬬數
                 * @author 
                 */
                 private void openLight()
                 {
                         parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
                         mCamera.setParameters(parameters);
                         mCamera.startPreview();
                 }
                 
                 /**
                 * 鍏抽棴鎵嬬數
                 * @author 
                 */
                 private void closeLight()
                 {
                         parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                         mCamera.setParameters(parameters);
                 }

//获取移动数据开关状态  
private boolean getMobileDataStatus()  
{  
  String methodName = "getMobileDataEnabled";  
  Class cmClass = mConnectivityManager.getClass();  
  Boolean isOpen = null;  
    
  try   
  {  
      Method method = cmClass.getMethod(methodName, null);  

      isOpen = (Boolean) method.invoke(mConnectivityManager, null);  
  }   
  catch (Exception e)   
  {  
      e.printStackTrace();  
  }  
  return isOpen;  
}  
    
// 通过反射实现开启或关闭移动数据  
private void setMobileDataStatus(boolean enabled)   
{  
  try   
  {  
      Class<?> conMgrClass = Class.forName(mConnectivityManager.getClass().getName());  
      //得到ConnectivityManager类的成员变量mService（ConnectivityService类型）  
      Field iConMgrField = conMgrClass.getDeclaredField("mService");  
      iConMgrField.setAccessible(true);  
      //mService成员初始化  
      Object iConMgr = iConMgrField.get(mConnectivityManager);  
      //得到mService对应的Class对象  
      Class<?> iConMgrClass = Class.forName(iConMgr.getClass().getName());  
      /*得到mService的setMobileDataEnabled(该方法在android源码的ConnectivityService类中实现)， 
       * 该方法的参数为布尔型，所以第二个参数为Boolean.TYPE 
       */  
      Method setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(  
              "setMobileDataEnabled", Boolean.TYPE);  
      setMobileDataEnabledMethod.setAccessible(true);  
      /*调用ConnectivityManager的setMobileDataEnabled方法（方法是隐藏的）， 
       * 实际上该方法的实现是在ConnectivityService(系统服务实现类)中的 
       */  
      setMobileDataEnabledMethod.invoke(iConMgr, enabled);  
  } catch (ClassNotFoundException e)   
  {  
      e.printStackTrace();  
  } catch (NoSuchFieldException e)   
  {  
      e.printStackTrace();  
  } catch (SecurityException e)   
  {  
      e.printStackTrace();  
  } catch (NoSuchMethodException e)   
  {  
      e.printStackTrace();  
  } catch (IllegalArgumentException e)   
  {  
      e.printStackTrace();  
  } catch (IllegalAccessException e)   
  {  
      e.printStackTrace();  
  } catch (InvocationTargetException e)   
  {  
      e.printStackTrace();  
  }  
}  

// .........
public boolean isWiFi() {    
  
      if (mConnectivityManager != null) {    
          NetworkInfo[] infos = mConnectivityManager.getAllNetworkInfo();    
          if (infos != null) {    
          	for(NetworkInfo ni : infos){
          		//备份下面的代码   && ni.isConnected()
          		if(ni.getTypeName().equals("WIFI") && ni.isConnectedOrConnecting() ){
          			return true;
          		}
          	}
          }    
      }    
      return false;    
  } 

public boolean hasGPSDevice(Context context)
{
	final LocationManager mgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	if ( mgr == null ) 
		return false;
	final List<String> providers = mgr.getAllProviders();
	if ( providers == null ) 
		return false;
	return providers.contains(LocationManager.GPS_PROVIDER);
}


	

    
  //观察屏幕旋转设置变化，类似于注册动态广播监听变化机制  
    private class RotationObserver extends ContentObserver  
    {  
    ContentResolver mResolver;  

    public RotationObserver(Handler handler)   
    {  
    super(handler);  
    mResolver = getContentResolver();  
    // TODO Auto-generated constructor stub  
    }  

    //屏幕旋转设置改变时调用  
    @Override  
    public void onChange(boolean selfChange)   
    {  
    // TODO Auto-generated method stub  
    super.onChange(selfChange);  
    }  

    public void startObserver()  
    {  
    mResolver.registerContentObserver(Settings.System  
            .getUriFor(Settings.System.ACCELEROMETER_ROTATION), false,  
            this);  
    }  

    public void stopObserver()  
    {  
    mResolver.unregisterContentObserver(this);  
    }  
    } 
    
    public void turnGPSOn()
    {
         Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
         intent.putExtra("enabled", true);
         this.sendBroadcast(intent);
     
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3")); 
            this.sendBroadcast(poke);
        }
    }
    
    /**
     * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private int getScreenMode() {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {

        }
        return screenMode;
    }

    /**
     * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private void setScreenMode(int paramInt) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    private int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {

        }
        return screenBrightness;
    }

    /**
     * 设置当前屏幕亮度值 0--255
     */
    private void saveScreenBrightness(int paramInt) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 保存当前的屏幕亮度值，并使之生效
     */
    private void setScreenBrightness(int paramInt) {
        Window localWindow = getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }
    
    @Override  
    protected void onDestroy() {  
        // TODO Auto-generated method stub  
        super.onDestroy();  
        //解除观察变化  
        editor.putInt("ff", 0);
        editor.commit();
        mCamera.release();
        mRotationObserver.stopObserver();  
    }  
  
  
    @Override  
    protected void onResume() {  
        // TODO Auto-generated method stub  
        super.onResume();  
        //注册观察变化  
        mRotationObserver.startObserver();  
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
    
}
