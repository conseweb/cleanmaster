package com.bitants.cleanmaster;


import android.annotation.SuppressLint;
import android.app.Activity; 
import android.app.admin.DevicePolicyManager; 
import android.content.ComponentName; 
import android.content.Context; 
import android.content.Intent; 
import android.os.Bundle; 
import android.util.Log;
   
@SuppressLint("NewApi")
public class LockActivity extends Activity { 
   
	private DevicePolicyManager mDevicePolicyManager;
	private ComponentName mComponentName;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		//传说中的Log.i
		Log.i("--->lock!!", "start lock");
		mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mComponentName = new ComponentName(this, LockReceiver.class);
		// 判断是否有权�?
		if (mDevicePolicyManager.isAdminActive(mComponentName)) {
			mDevicePolicyManager.lockNow();
			// 下面两行都不好使，在android4.2
			// android.os.Process.killProcess(android.os.Process.myPid());
			// System.exit(0);
			LockActivity.this.finish();
		} else {
			activeManager();
		}
	}

	private void activeManager() {
		// 
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "快速锁屏");
		startActivity(intent);
		finish();
	}


}
