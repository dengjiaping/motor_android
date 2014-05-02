package com.moto.live;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.moto.main.R;

public class PlaceAdapter extends BaseAdapter{
    //	内部类实现BaseAdapter  ，自定义适配器
    private Context context;
    LinkedList<HashMap<String, String>> list;
    private HashMap<String, String> map;
    
    public PlaceAdapter(Context context, LinkedList<HashMap<String, String>> list)
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
        
        convertView = LayoutInflater.from(context).inflate(R.layout.places_item, null);
        holder = new ViewHolder();
        holder.name = (TextView)convertView.findViewById(R.id.address_name);
        holder.vicinity = (TextView)convertView.findViewById(R.id.address_vicinity);
        holder.num = (TextView)convertView.findViewById(R.id.address_num);
        convertView.setTag(holder);
        
        holder = (ViewHolder) convertView.getTag();
        
        map = list.get(position);
        holder.name.setText((CharSequence)map.get("name"));
        holder.vicinity.setText((CharSequence) map.get("vicinity"));
        holder.num.setText("0 人");
        
        return convertView;
    }
    
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolder{
		TextView name;
		TextView vicinity;
		TextView num;
	}
}
