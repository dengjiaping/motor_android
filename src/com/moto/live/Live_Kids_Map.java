package com.moto.live;
import java.util.ArrayList;
import java.util.HashMap;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.moto.main.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

public class Live_Kids_Map extends Activity implements OnMarkerClickListener,
OnInfoWindowClickListener, InfoWindowAdapter{
    
	private ArrayList<HashMap<String, Object>> LocationList;
	private Intent intent;
	private ImageView backView;
	//定位所需变量
	private AMap aMap;
	private MapView mapView;
	private MarkerOptions markerOption;   //添加位置图层
	//划线
	private Polyline polyline;
	private PolylineOptions polylineOptions;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_kids_map);
		mapView = (MapView) findViewById(R.id.live_kids_bigmap);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		intent = getIntent();
		//必须申明为arraylist吧不能够为linkedlist
		LocationList = (ArrayList<HashMap<String, Object>>) intent.getSerializableExtra("list");
		backView = (ImageView)findViewById(R.id.live_kids_map_back);
		backView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Live_Kids_Map.this.finish();
			}
		});
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}
	
	private void setUpMap() {
        //		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		addMarkersToMap();// 往地图上添加marker
	}
    
	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		int a = LocationList.size();
		polylineOptions = new PolylineOptions();
		for(int i = 0; i < a; i++)
		{
			markerOption = new MarkerOptions();
			markerOption.position(new LatLng(Double.parseDouble(LocationList.get(i).get("latitude").toString()), Double.parseDouble(LocationList.get(i).get("longitude").toString())));
			markerOption.title(LocationList.get(i).get("location").toString());
			polylineOptions.add(new LatLng(Double.parseDouble(LocationList.get(i).get("latitude").toString()), Double.parseDouble(LocationList.get(i).get("longitude").toString())));
			aMap.addMarker(markerOption);
		}
		if(a > 0)
		{
			LatLngBounds bounds = new LatLngBounds.Builder()
			.include(new LatLng(Double.parseDouble(LocationList.get(0).get("latitude").toString()),
                                Double.parseDouble(LocationList.get(0).get("longitude").toString())))
			.build();
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
            // 绘制线条
            polyline = aMap.addPolyline(polylineOptions
                                        .width(3).color(Color.rgb(4, 234, 188)));
		}
		aMap.invalidate();//刷新地图
	}
	/**
	 * marker点击时跳动一下
	 */
	public void jumpPoint(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final LatLng latLng = marker.getPosition();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(latLng);
		startPoint.offset(0, -100);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1500;
        
		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
                                                        / duration);
				double lng = t * latLng.longitude + (1 - t)
                * startLatLng.longitude;
				double lat = t * latLng.latitude + (1 - t)
                * startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				aMap.invalidate();// 刷新地图
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
        
	}
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
        //		SetGaoDeMap();
		mapView.onResume();
	}
    
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}
    
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
    
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
        //		imageLoader.destroy();
		mapView.onDestroy();
	}
    
	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
    
	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
    
	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if (aMap != null) {
			jumpPoint(marker);
		}
		return false;
	}
}
