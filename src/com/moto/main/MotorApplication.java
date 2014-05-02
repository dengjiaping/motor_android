package com.moto.main;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.TypedValue;

import com.baidu.frontia.FrontiaApplication;
import com.chute.sdk.v2.utils.PreferenceUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/*
 * 如果您的工程中实现了Application的继承类，那么，您需要将父类改为com.baidu.frontia.FrontiaApplication。
 * 如果您没有实现Application的继承类，那么，请在AndroidManifest.xml的Application标签中增加属性： 
 * <application android:name="com.baidu.frontia.FrontiaApplication"
 * 。。。
 */
public class MotorApplication extends FrontiaApplication {
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	private HttpClient httpClient;
	public boolean isLogin = false;
	@Override
	public void onCreate() {
		super.onCreate();

		initImageLoader(getApplicationContext());
        httpClient = this.createHttpClient();
	}
	/**初始化图片加载类配置信息**/
    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
            .threadPriority(Thread.NORM_PRIORITY - 2)//加载图片的线程数
            .denyCacheImageMultipleSizesInMemory() //解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
            .discCacheFileNameGenerator(new Md5FileNameGenerator())//设置磁盘缓存文件名称
            .tasksProcessingOrder(QueueProcessingType.LIFO)//设置加载显示图片队列进程
            .writeDebugLogs() // Remove for release app
            .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
      
   // 创建HttpClient实例,,多图选择
   		private HttpClient createHttpClient() {
   			HttpParams params = new BasicHttpParams();
   			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
   			HttpProtocolParams.setContentCharset(params,
   					HTTP.DEFAULT_CONTENT_CHARSET);
   			HttpProtocolParams.setUseExpectContinue(params, true);
   			HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
   			HttpConnectionParams.setSoTimeout(params, 20 * 1000);
   			HttpConnectionParams.setSocketBufferSize(params, 8192);
   			SchemeRegistry schReg = new SchemeRegistry();
   			schReg.register(new Scheme("http", PlainSocketFactory
   					.getSocketFactory(), 80));
   			schReg.register(new Scheme("https",
   					SSLSocketFactory.getSocketFactory(), 443));

   			ClientConnectionManager connMgr = new ThreadSafeClientConnManager(
   					params, schReg);

   			return new DefaultHttpClient(connMgr, params);
   		}
   		
   	// 关闭连接管理器并释放资源
   	 	private void shutdownHttpClient() {
   	 		if (httpClient != null && httpClient.getConnectionManager() != null) {
   	 			httpClient.getConnectionManager().shutdown();
   	 		}
   	 	}

   	 	// 对外提供HttpClient实例
   	 	public HttpClient getHttpClient() {
   	 		return httpClient;
   	 	}
   	 	
   	 @Override
   	public void onLowMemory() {
   		super.onLowMemory();
   		this.shutdownHttpClient();
   	}

   	@Override
   	public void onTerminate() {
   		super.onTerminate();
   		this.shutdownHttpClient();
   	}
}
