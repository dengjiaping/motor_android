package com.moto.inform;
import java.util.HashMap;
import java.util.LinkedList;

import com.moto.constant.Constant;
import com.moto.constant.ImageMethod;
import com.moto.main.R;
import com.moto.mymap.MyMapApplication;
import com.moto.utils.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//内部类实现BaseAdapter  ，自定义适配器
public class TouchmeAdapter extends BaseAdapter{
    
	private Context context;
	private LinkedList<HashMap<String, String>> list;
	private HashMap<String, String> map;
	private DisplayImageOptions options = ImageMethod.GetOptions();
	
	public TouchmeAdapter(Context context, LinkedList<HashMap<String, String>> list)
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
		
		if(convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.inform_touchme_item, null);
			holder = new ViewHolder();
			holder.inform_touchme_username = (TextView)convertView.findViewById(R.id.inform_touchme_username);
			holder.inform_touchme_details = (TextView)convertView.findViewById(R.id.inform_touchme_details);
			holder.inform_touchme_theme = (TextView)convertView.findViewById(R.id.inform_touchme_theme);
			holder.inform_touchme_time = (TextView)convertView.findViewById(R.id.inform_touchme_time);
			holder.inform_touchme_userimg = (ImageView)convertView.findViewById(R.id.inform_touchme_userimg);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		map = list.get(position);
		holder.inform_touchme_username.setText(map.get("author"));
        holder.inform_touchme_details.setText(map.get("at_message"));
        holder.inform_touchme_theme.setText(map.get("subject"));
        holder.inform_touchme_time.setText(DateUtils.timestampToDeatil(map.get("dateline")));
        MyMapApplication.imageLoader.displayImage(Constant.imgPath+map.get("avatar")+"?imageView2/1/w/40/h/40",  holder.inform_touchme_userimg,options,null);
        return convertView;
	}
    
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolder{
		TextView inform_touchme_username;
		TextView inform_touchme_details;
        TextView inform_touchme_theme;
		TextView inform_touchme_time;
		ImageView inform_touchme_userimg;
	}
}
