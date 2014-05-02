package com.moto.user;

import java.util.ArrayList;
import java.util.HashMap;
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

public class User_numPost extends MyActivity implements OnClickListener{
	private ImageView numpost_return;
	private ListView myListView;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
	private HashMap<String, Object> map;
	private MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_numpost);
		init();
		
		for(int i = 0; i < 4; i++)
		{
			map = new HashMap<String, Object>();
			map.put("where", "咖啡馆");
			map.put("details", "I love U ");
			map.put("time", "23:11");
			list.add(map);
		}
		adapter = new MyAdapter(this, list);
		myListView.setAdapter(adapter);
	}
	private void init() {
		// TODO Auto-generated method stub
		numpost_return = (ImageView)findViewById(R.id.user_numpost_return);
		myListView = (ListView)findViewById(R.id.user_numpost_listview);
		numpost_return.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == numpost_return)
		{
			User_numPost.this.finish();
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
			
			convertView = LayoutInflater.from(context).inflate(R.layout.user_numpost_item, null);
			holder = new ViewHolder();
			holder.user_numpostitem_where = (TextView)convertView.findViewById(R.id.user_numpostitem_where);
			holder.user_numpostitem_time = (TextView)convertView.findViewById(R.id.user_numpostitem_time);
			holder.user_numpostitem_datails = (TextView)convertView.findViewById(R.id.user_numpostitem_datails);
			convertView.setTag(holder);
			holder = (ViewHolder) convertView.getTag();
			
			map = list.get(position);
			holder.user_numpostitem_where.setText((CharSequence)map.get("where"));
	        holder.user_numpostitem_time.setText((CharSequence)map.get("time"));
	        holder.user_numpostitem_datails.setText((CharSequence)map.get("details"));
	        return convertView;
		}
		//此类为上面getview里面view的引用，方便快速滑动
		class ViewHolder{
			TextView user_numpostitem_where;
			TextView user_numpostitem_time;
			TextView user_numpostitem_datails;
		}
	}
    
    
}
