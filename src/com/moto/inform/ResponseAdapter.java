package com.moto.inform;
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
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.HashMap;
import java.util.LinkedList;

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

            holder.inform_response_time = (TextView)convertView.findViewById(R.id.inform_response_time);
            holder.inform_response_userimg = (ImageView)convertView.findViewById(R.id.inform_response_userimg);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        
        map = list.get(position);
        holder.inform_response_username.setText(map.get("author"));
        holder.inform_response_details.setText(map.get("reply_message"));
        holder.inform_response_theme.setText( map.get("subject"));
        holder.inform_response_time.setText(DateUtils.timestampToDeatil(map.get("dateline")));
        MyMapApplication.imageLoader.displayImage(UrlUtils.imageUrl(map.get("avatar")),  holder.inform_response_userimg,options,null);
        return convertView;
    }
    //此类为上面getview里面view的引用，方便快速滑动
    class ViewHolder{
        TextView inform_response_username;
        TextView inform_response_details;
        TextView inform_response_theme;
        TextView inform_response_time;
        ImageView inform_response_userimg;
    }
    
}

