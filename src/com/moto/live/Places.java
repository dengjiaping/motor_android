package com.moto.live;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
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
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.moto.constant.Constant;
import com.moto.listview.CustomScrollView;
import com.moto.listview.CustomScrollView.OnLoadListener;
import com.moto.listview.CustomScrollView.OnRefreshListener;
import com.moto.listview.NoScrollListview;
import com.moto.main.R;
import com.moto.myactivity.MyActivity;
import com.moto.toast.ToastClass;

public class Places extends MyActivity implements OnClickListener,
OnPoiSearchListener,AMapLocationListener,Runnable,OnMarkerClickListener,
OnInfoWindowClickListener, InfoWindowAdapter{
    
	private ImageView return_places;
	private LinkedList<HashMap<String, String>> list = new LinkedList<HashMap<String,String>>();
	private HashMap<String , String> map;
	private Handler handler;
	private NoScrollListview listView;
	private CustomScrollView refresh_scrollview;
	private PlaceAdapter adapter;
	private boolean isrefresh = true;
	
	private String KeyWord;    //关键字
	private String KeyCity;    //范围城市
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;// POI搜索
	private PoiResult poiResult; // poi返回的结果
	private int count = 0;
	private ProgressBar loadingBar;
	
	private CheckBox hotelBox;
	private CheckBox eatingBox;
	private CheckBox gasstationBox;
	private CheckBox sceneryBox;
	private int locationsign = 0;
	
	//地图显示图层
	protected AMap aMap;
	protected MapView mapView;
	private MarkerOptions markerOption;   //添加位置图层
	
	
	private LocationManagerProxy aMapLocManager = null;
	private AMapLocation aMapLocation;// 用于判断定位超时
	private Handler maphandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places);
		mapView = (MapView) findViewById(R.id.place_map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		
		init();
		adapter = new PlaceAdapter(Places.this, list);
		listView.setAdapter(adapter);
		
		aMapLocManager = LocationManagerProxy.getInstance(this);
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
		 * API定位采用GPS和网络混合定位方式
		 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
		aMapLocManager.requestLocationUpdates(
                                              LocationProviderProxy.AMapNetwork, 2000, 10, this);
		maphandler.postDelayed(this, 20000);// 设置超过12秒还没有定位到就停止定位
		
		handler = new Handler(){
            
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
                        //获取成功
                    case Constant.MSG_SUCCESS:
                        isrefresh = false;
                        adapter.notifyDataSetChanged();
                        refresh_scrollview.post(new Runnable() {
                            //让scrollview跳转到顶部，必须放在runnable()方法中
                            @Override
                            public void run() {
                                refresh_scrollview.scrollTo(0, 0);
                            }
                        });
                        aMap.clear();
                        addMarkersToMap();
                        loadingBar.setVisibility(View.GONE);
                        refresh_scrollview.onRefreshComplete();
                        break;
                        
                    case Constant.MSG_SUCCESSAGAIN:
                        isrefresh = false;
                        loadingBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        
                        refresh_scrollview.onLoadComplete();
                        aMap.clear();
                        addMarkersToMap();
                        break;
                    case Constant.MSG_FALTH:
                        isrefresh = false;
                        loadingBar.setVisibility(View.GONE);
				}
				super.handleMessage(msg);
			}
			
		};
		refresh_scrollview.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1500);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return null;
					}
                    
					@Override
					protected void onPostExecute(Void result) {
                        //						refresh_scrollview.onRefreshComplete();
					}
					@Override
					protected void onPreExecute() {
						isrefresh = true;
						count = 0;
						doSearchQuery();
					}
                    
				}.execute();
			}
		});
		refresh_scrollview.setOnLoadListener(new OnLoadListener() {
			public void onLoad() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(2000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}
					@Override
					protected void onPreExecute() {
						nextPoiPage();
					}
					@Override
					protected void onPostExecute(Void result) {
                        //						refresh_scrollview.onLoadComplete();
					}
                    
				}.execute();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
            
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
				// TODO Auto-generated method stub
				intent = new Intent();
				getPositionSign();
				
				intent.putExtra("data", list.get(position).get("name"));
				intent.putExtra("lat", list.get(position).get("lat"));
				intent.putExtra("lon", list.get(position).get("lon"));
				intent.putExtra("locationsign", locationsign+"");
				Places.this.setResult(3, intent);
				Places.this.finish();
			}
		});
	}
    
	
	private void init() {
		// TODO Auto-generated method stub
		hotelBox = (CheckBox)findViewById(R.id.hotel);
		eatingBox = (CheckBox)findViewById(R.id.eating);
		gasstationBox = (CheckBox)findViewById(R.id.gasstation);
		sceneryBox = (CheckBox)findViewById(R.id.scenery);
		loadingBar = (ProgressBar)findViewById(R.id.place_loading_progressBar);
		return_places = (ImageView)findViewById(R.id.return_places);
		refresh_scrollview = (CustomScrollView)findViewById(R.id.myscrollview_refresh);
		listView = (NoScrollListview)findViewById(R.id.places_list);
		return_places.setOnClickListener(this);
		
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
        //		addMarkersToMap();// 往地图上添加marker
	}
	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		int a = list.size();
		for(int i = 0; i < a; i++)
		{
			markerOption = new MarkerOptions();
			markerOption.position(new LatLng(Double.parseDouble(list.get(i).get("lat").toString()), Double.parseDouble(list.get(i).get("lon").toString())));
			markerOption.title(list.get(i).get("name").toString());
			aMap.addMarker(markerOption);
		}
		if(a > 0)
		{
			LatLngBounds bounds = new LatLngBounds.Builder()
			.include(new LatLng(Double.parseDouble(list.get(0).get("lat").toString()),
                                Double.parseDouble(list.get(0).get("lon").toString())))
			.build();
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
		}
		aMap.invalidate();
	}
	private void getPositionSign() {
		// TODO Auto-generated method stub
		locationsign = 0;
		if(hotelBox.isChecked())
		{
			locationsign = locationsign + 1;
		}
		if(eatingBox.isChecked())
		{
			locationsign = locationsign + 2;
		}
		if(gasstationBox.isChecked())
		{
			locationsign = locationsign + 4;
		}
		if(sceneryBox.isChecked())
		{
			locationsign = locationsign + 8;
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == return_places)
		{
			returnNull();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			if(event.getRepeatCount() == 0)
			{
				returnNull();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private void returnNull()
	{
		intent = new Intent();
		getPositionSign();
		intent.putExtra("data", "插入位置");
		intent.putExtra("locationsign", locationsign+"");
		Places.this.setResult(4, intent);
		Places.this.finish();
	}
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		stopLocation();// 停止定位
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
	 * 销毁定位
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}
	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if (location != null) {
			this.aMapLocation = location;// 判断超时机制
			Bundle locBundle = location.getExtras();
			KeyCity = location.getCity();
			String desc = null;
			if (locBundle != null) {
				desc = locBundle.getString("desc");
				String[] position = desc.split(" ");
				KeyWord = position[position.length - 2];
				stopLocation();
				doSearchQuery();
			}
		}
	}
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (aMapLocation == null) {
			handler.obtainMessage(Constant.MSG_FALTH).sendToTarget();
			ToastClass.SetToast(Places.this, "没有获取到位置");
			stopLocation();// 销毁掉定位
		}
	}
	
	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {
		query = new PoiSearch.Query(KeyWord, "", KeyCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(0);// 设置查第一页
        
		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}
	
	/**
	 * 点击下一页按钮
	 */
	public void nextPoiPage() {
		if (query != null && poiSearch != null && poiResult != null) {
			if (poiResult.getPageCount() - 1 > count) {
				count++;
				query.setPageNum(count);// 设置查后一页
				poiSearch.searchPOIAsyn();
			} else {
				refresh_scrollview.onLoadComplete();
				ToastClass.SetToast(Places.this, "没有更多的位置信息了");
			}
		}
	}
    
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					int a = poiItems.size();
					if(isrefresh)
					{
						list.clear();
					}
					if (poiItems != null && a > 0) {
						for(int i = 0; i < a; i++)
						{
							map = new HashMap<String, String>();
							map.put("name", poiItems.get(i).getTitle());
							map.put("vicinity", poiItems.get(i).getSnippet());
							map.put("lat", poiItems.get(i).getLatLonPoint().getLatitude()+"");
							map.put("lon", poiItems.get(i).getLatLonPoint().getLongitude()+"");
							list.add(map);
						}
						if(isrefresh)
						{
							handler.obtainMessage(Constant.MSG_SUCCESS).sendToTarget();
						}
						else {
							handler.obtainMessage(Constant.MSG_SUCCESSAGAIN).sendToTarget();
						}
						
					} else {
						ToastClass.SetToast(Places.this, "对不起，没有搜索到相关数据！");
						handler.obtainMessage(Constant.MSG_FALTH).sendToTarget();
					}
				}
			} else {
				ToastClass.SetToast(Places.this, "对不起，没有搜索到相关数据！");
				handler.obtainMessage(Constant.MSG_FALTH).sendToTarget();
			}
		} else if (rCode == 27) {
			ToastClass.SetToast(Places.this, "搜索失败,请检查网络连接！");
			handler.obtainMessage(Constant.MSG_FALTH).sendToTarget();
		} else {
			ToastClass.SetToast(Places.this, "未知错误，请稍后重试!");
			handler.obtainMessage(Constant.MSG_FALTH).sendToTarget();
		}
	}
    
}
