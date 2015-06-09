package com.bitants.cleanmaster.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitants.cleanmaster.R;
import com.bitants.cleanmaster.util.MSaveList;
import com.bitants.entity.AppItem;


public class MAdapter extends BaseAdapter
{
	private MSaveList sL =null; 
	private List<AppItem> appList=null;
	private LayoutInflater inflater =null;
	private ViewHolder holder;
	private Context context = null;
	public MAdapter(Context context,List<AppItem> list)
	{
		this.context =context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sL = new MSaveList(context.getSharedPreferences("demo360", Activity.MODE_PRIVATE));
		this.appList = list;
		List<String> selected= sL.load();
		if(selected!=null)
		{
			for(int i=0;i<selected.size();i++)
			{
				String temp = selected.get(i);
				for(int j =0;j<appList.size();j++)
				{
					if(temp.equals(appList.get(j).getPkgName()))
					{	
						appList.get(j).setSelected(true);
						AppItem appItem = appList.get(i);
						appList.set(i, appList.get(j));
						appList.set(j, appItem);
						break;
					}
				}
			}
		}
		
	}

	public void save()
	{
		sL.save();
	}
	
	public void add(String pkgName)
	{
		Log.i("AAA", "add:"+pkgName);
		sL.add(pkgName);
	}
	public void remove(String pkgName)
	{
		Log.i("AAA", "remove:"+pkgName);
		sL.remove(pkgName);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appList.size();
	}

	@Override
	public AppItem getItem(int position) {
		// TODO Auto-generated method stub
		return appList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.list_item, null);
			holder= new ViewHolder();
			holder.appName = (TextView) convertView.findViewById(R.id.app_name);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.select = (ImageView) convertView.findViewById(R.id.checkBox);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getBackground().getLevel()==0)
				{
					v.getBackground().setLevel(1);
					getItem(position).setSelected(true);
					add(getItem(position).getPkgName());
					Toast.makeText(context, getItem(position).getAppName()+"已加入白名单！", 1500).show();
				}
				else
				{
					v.getBackground().setLevel(0);
					getItem(position).setSelected(false);
					remove(getItem(position).getPkgName());
				}
			}
		});
		holder.appName.setText(getItem(position).getAppName());
		holder.icon.setImageDrawable(getItem(position).getIcon());
		if(getItem(position).isSelected())
			holder.select.getBackground().setLevel(1);
		else
			holder.select.getBackground().setLevel(0);
		holder.pkgName = getItem(position).getPkgName();
		return convertView;
	}
	
	static class ViewHolder
	{
		public ImageView icon;
		public TextView appName;
		public ImageView select;
		public String pkgName=null;
	}
}
