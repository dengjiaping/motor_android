package com.moto.inform;
import java.util.HashMap;
import java.util.LinkedList;

import com.moto.constant.Constant;
import com.moto.constant.ImageMethod;
import com.moto.date.DateUtils;
import com.moto.main.MotorApplication;
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

//内部类实现BaseAdapter  ，自定义适配器
public class PrivateAdapter extends BaseAdapter{
    
    private Context context;
    private LinkedList<HashMap<String, String>> list;
    private HashMap<String, String> map;
    private DisplayImageOptions options = ImageMethod.GetOptions();
    public PrivateAdapter(Context context, LinkedList<HashMap<String, String>> list)
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
            convertView = LayoutInflater.from(context).inflate(R.layout.inform_private_item, null);
            holder = new ViewHolder();
            holder.inform_private_username = (TextView)convertView.findViewById(R.id.inform_private_username);
            holder.inform_private_details = (TextView)convertView.findViewById(R.id.inform_private_details);
            holder.inform_private_userimg = (ImageView)convertView.findViewById(R.id.inform_private_userimg);
            holder.inform_private_time = (TextView)convertView.findViewById(R.id.inform_private_time);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        map = list.get(position);
        holder.inform_private_username.setText(map.get("username"));
        holder.inform_private_details.setText(map.get("lastmessage"));
        holder.inform_private_time.setText(com.moto.utils.DateUtils.StimestampToDeatil(map.get("updated_at")));
        MotorApplication.imageLoader.displayImage(Constant.imgPath+map.get("avatar"),  holder.inform_private_userimg,options,null);
        return convertView;
    }	
    //此类为上面getview里面view的引用，方便快速滑动
    class ViewHolder{
        TextView inform_private_username;
        TextView inform_private_details;
        ImageView inform_private_userimg;
        TextView inform_private_time;
    }
}
