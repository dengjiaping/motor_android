package com.moto.live;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.moto.constant.ImageMethod;
import com.moto.main.R;
import com.moto.mymap.MyMapApplication;
import com.moto.utils.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ResponseAdapter extends BaseAdapter{
    //	内部类实现BaseAdapter  ，自定义适配器
    private String imgPath = "http://motor.qiniudn.com/";
    private Context context;
    private LinkedList<HashMap<String, Object>> list;
    private HashMap<String, Object> map;
    private DisplayImageOptions options;
    public ResponseAdapter(Context context, LinkedList<HashMap<String, Object>> list)
    {
        this.context = context;
        this.list = list;
        options = ImageMethod.GetOptions();
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
        
        convertView = LayoutInflater.from(context).inflate(R.layout.live_kids_item_response_item, null);
        holder = new ViewHolder();
        holder.live_kids_item_response_item_img = (ImageView)convertView.findViewById(R.id.live_kids_item_response_item_img);
        holder.live_kids_item_response_item_username = (TextView)convertView.findViewById(R.id.live_kids_item_response_item_username);
        holder.live_kids_item_response_item_details = (TextView)convertView.findViewById(R.id.live_kids_item_response_item_details);
        holder.live_kids_item_time_response_item_text = (TextView)convertView.findViewById(R.id.live_kids_item_time_response_item_text);
        convertView.setTag(holder);
        holder = (ViewHolder) convertView.getTag();
        
        map = list.get(position);
        holder.live_kids_item_response_item_username.setText(map.get("author")+": ");
        holder.live_kids_item_response_item_details.setText((CharSequence) map.get("message"));
        holder.live_kids_item_time_response_item_text.setText(DateUtils.timestampToDeatil(map.get("dateline").toString()));
        if(!map.get("avatar").toString().equals("null"))
        {
            MyMapApplication.imageLoader.displayImage(imgPath+map.get("avatar")+"?imageView2/1/w/40/h/40",  holder.live_kids_item_response_item_img,options,null);
        }
        return convertView;
    }
    
    class ViewHolder{
        ImageView live_kids_item_response_item_img;
        TextView live_kids_item_response_item_username;
        TextView live_kids_item_response_item_details;
        TextView live_kids_item_time_response_item_text;
    }
    
}
//此类为上面getview里面view的引用，方便快速滑动

