package com.moto.inform;

import java.util.ArrayList;
import java.util.HashMap;

import com.moto.constant.Constant;
import com.moto.constant.ImageMethod;
import com.moto.main.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<HashMap<String, Object>> list;
	private HashMap<String, Object> map;
	private Activity activity;
	
	public FriendsAdapter(Activity activity, Context context, ArrayList<HashMap<String, Object>> list)
	{
		this.context = context;
		this.list = list;
		this.activity = activity;
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
        ImageMethod.setImageSourse(activity).loadImage(Constant.imgPath, holder.inform_private_friends_userimg);
        
        return convertView;
	}
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolder{
		TextView inform_private_friends_username;
		TextView inform_private_friends_write;
		ImageView inform_private_friends_userimg;
	}
}
