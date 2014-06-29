package com.moto.inform;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moto.constant.ImageMethod;
import com.moto.main.MotorApplication;
import com.moto.main.R;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<HashMap<String, Object>> list;
	private HashMap<String, Object> map;
    private DisplayImageOptions options = ImageMethod.GetOptions();
	
	public FriendsAdapter(Activity activity, Context context, ArrayList<HashMap<String, Object>> list)
	{
		this.context = context;
		this.list = list;
	}
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
    
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
    
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		// TODO Auto-generated method stub
		
		convertView = LayoutInflater.from(context).inflate(R.layout.inform_private_friends_item, null);
		holder = new ViewHolder();
		holder.inform_private_friends_username = (TextView)convertView.findViewById(R.id.inform_private_friends_username);
		holder.inform_private_friends_write = (TextView)convertView.findViewById(R.id.inform_private_friends_write);
		holder.inform_private_friends_userimg = (ImageView)convertView.findViewById(R.id.inform_private_friends_userimg);
		convertView.setTag(holder);
		holder = (ViewHolder) convertView.getTag();
		
		map = list.get(position);
		holder.inform_private_friends_username.setText((CharSequence)map.get("name"));
        holder.inform_private_friends_write.setText((CharSequence)map.get("details"));
        MotorApplication.imageLoader.displayImage(UrlUtils.imageUrl(map.get("avatar").toString()),  holder.inform_private_friends_userimg,options,null);
        
        return convertView;
	}
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolder{
		TextView inform_private_friends_username;
		TextView inform_private_friends_write;
		ImageView inform_private_friends_userimg;
	}
}
