package com.moto.inform;
import java.util.HashMap;
import java.util.LinkedList;

import com.moto.constant.Constant;
import com.moto.constant.ImageMethod;
import com.moto.main.R;
import com.moto.mymap.MyMapApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResponseAdapter extends BaseAdapter{
    
    private Context context;
    private LinkedList<HashMap<String, String>> list;
    private HashMap<String, String> map;
    private DisplayImageOptions options = ImageMethod.GetOptions();
    
    public ResponseAdapter(Context context, LinkedList<HashMap<String, String>> list)
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
            convertView = LayoutInflater.from(context).inflate(R.layout.inform_response_item, null);
            holder = new ViewHolder();
            holder.inform_response_username = (TextView)convertView.findViewById(R.id.inform_response_username);
            holder.inform_response_details = (TextView)convertView.findViewById(R.id.inform_response_details);
            holder.inform_response_theme = (TextView)convertView.findViewById(R.id.inform_response_theme);
            holder.inform_response_data = (TextView)convertView.findViewById(R.id.inform_response_data);
            holder.inform_response_time = (TextView)convertView.findViewById(R.id.inform_response_time);
            holder.inform_response_userimg = (ImageView)convertView.findViewById(R.id.inform_response_userimg);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        
        map = list.get(position);
        holder.inform_response_username.setText(map.get("author"));
        holder.inform_response_details.setText(map.get("message"));
        //            holder.inform_response_theme.setText( map.get("theme"));
        holder.inform_response_data.setText("2013");
        holder.inform_response_time.setText("03:24");
        MyMapApplication.imageLoader.displayImage(Constant.imgPath+map.get("avatar")+"?imageView2/1/w/40/h/40",  holder.inform_response_userimg,options,null);
        return convertView;
    }
    //此类为上面getview里面view的引用，方便快速滑动
    class ViewHolder{
        TextView inform_response_username;
        TextView inform_response_details;
        TextView inform_response_theme;
        TextView inform_response_data;
        TextView inform_response_time;
        ImageView inform_response_userimg;
    }
    
}

