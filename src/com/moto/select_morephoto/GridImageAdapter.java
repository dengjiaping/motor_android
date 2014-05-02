package com.moto.select_morephoto;

import java.util.ArrayList;

import com.moto.main.R;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridImageAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> dataList;
	private DisplayMetrics dm;

	public GridImageAdapter(Context c, ArrayList<String> dataList) {

		mContext = c;
		this.dataList = dataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.live_photo_adapter, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.gcImageViewThumb);
			convertView.setTag(holder);
//			imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, dipToPx(65)));
		} else
			holder = (ViewHolder) convertView.getTag();
			
		String path;
		if (dataList != null && position<dataList.size() )
			path = dataList.get(position);
		else
			path = "default_add_img";
		if (path.contains("default"))
			holder.imageView.setImageResource(R.drawable.default_add_img);
		else{
            ImageManager2.from(mContext).displayImage(holder.imageView, path,R.drawable.default_add_img,100,100);
		}
		return convertView;
	}
	
	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}
	
	//此类为上面getview里面view的引用，方便快速滑动
		class ViewHolder{
			ImageView imageView;
		}

}
