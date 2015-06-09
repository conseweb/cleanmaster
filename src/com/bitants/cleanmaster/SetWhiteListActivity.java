package com.bitants.cleanmaster;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bitants.cleanmaster.MainActivity;
import com.bitants.cleanmaster.R;
import com.bitants.cleanmaster.view.MAdapter;
import com.bitants.entity.AppItem;

public class SetWhiteListActivity extends Activity
{
	private ImageView back;
	private ListView allApp;
	private MAdapter adapter;
	private List<AppItem> data = null;
	private int DATA_OK=0x1,tui=0;
	private Handler handler =new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == DATA_OK)
			{
				setListData();
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (android.os.Build.VERSION.SDK_INT > 18) 
		{    
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		    
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		//RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout); 7 relativeLayout.setPadding(0, getActionBarHeight()+getStatusBarHeight(), 0, 0); 8 
		}
		
		setContentView(R.layout.white_list);
		back= (ImageView) findViewById(R.id.back);
		allApp = (ListView) findViewById(R.id.list);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("AAA", "back clecked");
				adapter.save();
				finish();
				
				Intent intent = new Intent(SetWhiteListActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		Toast.makeText(getBaseContext(), "稍等，请勿离开！！！", Toast.LENGTH_LONG).show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//Toast.makeText(SetWhiteListActivity.this, "正在读取列表，请稍侯", Toast.LENGTH_LONG).show();
				data = getAppItem();
				handler.sendEmptyMessage(DATA_OK);
			}
		}).start();
	}

	
	private void setListData()
	{
		if(data!=null&&data.size()>0)
		{
			adapter = new MAdapter(this, data);
		}
		allApp.setAdapter(adapter);
		
		allApp.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("AAA", "position:"+position);
				if(adapter.getItem(position).isSelected())
				{
					adapter.getItem(position).setSelected(false);
					adapter.remove(adapter.getItem(position).getPkgName());
				}
				else
				{
					adapter.getItem(position).setSelected(true);
					adapter.add(adapter.getItem(position).getPkgName());
					Toast.makeText(SetWhiteListActivity.this, adapter.getItem(position).getAppName()+"被添加到了白名单！", 1500).show();
				}
				adapter.notifyDataSetInvalidated();
			}
		});
	}
	/**
	 * 鑾峰彇绯荤粺鐨勫簲鐢ㄥ垪琛�
	 * @return
	 */
	private List<AppItem> getAppItem()
	{
		List<AppItem> appList = new ArrayList<AppItem>(); // 
		List<AppItem> appListSystem = new ArrayList<AppItem>();
		List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
		for(int i=0;i<packages.size();i++) 
		{
			PackageInfo packageInfo = packages.get(i);
			//闈炵郴缁熷簲鐢ㄥ姞涓婁笅闈㈡潯浠�
			//packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0
			if(!packageInfo.packageName.equals("android")&&!packageInfo.packageName.equals("com.bitants.cleanmaster"))
			{
				AppItem item =new AppItem();
				item.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
				item.setPkgName(packageInfo.packageName);
				item.setIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
				if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)
				{
					appList.add(item);
				}
				else
				{
					appListSystem.add(item);
				}
			}
			
			
		}
		appList.addAll(appListSystem);
		return appList;
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		adapter.save();
		super.onBackPressed();
	}

}
