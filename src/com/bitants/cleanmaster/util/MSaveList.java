package com.bitants.cleanmaster.util;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.util.Log;

public class MSaveList extends ArrayList<String>
{
	private SharedPreferences share =null;
	public MSaveList (SharedPreferences share)
	{
		this.share = share;
	}
	public void save()
	{
		String temp = "";
		for(int i = 0;i<size(); i++)
		{
			temp =temp+get(i)+"/";
		}
		Log.i("AAA", "save:"+temp);
		SharedPreferences.Editor editor = share.edit();
		editor.putString("white_list", temp);
		editor.commit();
	}
	
	public List<String> load()
	{
		String temp = share.getString("white_list", "");
		if(temp.equals(""))
			return null;
		String s[] = temp.split("/");
		Log.i("AAA", "load:"+temp);
		for(int i = 0;i<s.length;i++)
		{
			add(s[i]);
		}
		return this;
	}
}
