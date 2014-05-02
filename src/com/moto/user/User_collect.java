package com.moto.user;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.moto.main.R;
import com.moto.myactivity.MyActivity;

public class User_collect extends MyActivity implements OnClickListener{
    
	private ListView mylistView;
	private ImageView collect_return;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
	private HashMap<String, Object> map;
	private MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_collect);
		init();
		for(int i = 0; i < 4; i++)
		{
			map = new HashMap<String, Object>();
			map.put("name", "kakashi");
			map.put("details", "I love U ");
			map.put("num", "1234");
			list.add(map);
		}
		adapter = new MyAdapter(this, list);
		mylistView.setAdapter(adapter);
	}
    
	private void init() {
		// TODO Auto-generated method stub
		mylistView = (ListView)findViewById(R.id.user_collect_listview);
		collect_return = (ImageView)findViewById(R.id.user_collect_return);
		collect_return.setOnClickListener(this);
	}
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == collect_return)
		{
			User_collect.this.finish();
		}
	}
	
	class MyAdapter extends BaseAdapter{
		private Context context;
		private ArrayList<HashMap<String, Object>> list;
		private HashMap<String, Object> map;
		
		public MyAdapter(Context context, ArrayList<HashMap<String, Object>> list)
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
			
			convertView = LayoutInflater.from(context).inflate(R.layout.user_collect_item, null);
			holder = new ViewHolder();
			holder.user_collect_details = (TextView)convertView.findViewById(R.id.user_collect_details);
			holder.user_collect_username = (TextView)convertView.findViewById(R.id.user_collect_username);
			holder.user_collect_numresponse = (TextView)convertView.findViewById(R.id.user_collect_numresponse);
			convertView.setTag(holder);
			holder = (ViewHolder) convertView.getTag();
			
			map = list.get(position);
			holder.user_collect_username.setText((CharSequence)map.get("name"));
	        holder.user_collect_details.setText((CharSequence)map.get("details"));
	        holder.user_collect_numresponse.setText((CharSequence)map.get("num"));
	        return convertView;
		}
		//此类为上面getview里面view的引用，方便快速滑动
		class ViewHolder{
			TextView user_collect_details;
			TextView user_collect_username;
			TextView user_collect_numresponse;
		}
	}
    
}
