package com.moto.asydata;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

/**
 * Created by chen on 14-8-28.
 */
public class RequnstClientOwn {
    /**
     * 定义一个异步网络客户端 默认超时未20秒 当超过，默认重连次数为5次 默认最大连接数为10个
     */
    private static AsyncHttpClient mClient = new AsyncHttpClient();
    static {
        mClient.setTimeout(4000);
    }
    public static void post(String url, AsyncHttpResponseHandler handler) {
        post(url, null, handler);
    }
    /**
     * post 请求
     *
     * @param url
     *            API 地址
     * @param params
     *            请求的参数
     * @param handler
     *            数据加载句柄对象
     */
    public static void post(String url, RequestParams params,
                            AsyncHttpResponseHandler handler) {
        System.out.println("进入post");
        mClient.post(url, params, handler);
    }
    public static void get(String url, AsyncHttpResponseHandler handler) {
        // using a socket factory that allows self-signed SSL certificates.
        mClient.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        mClient.get(url, handler);
    }
    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        System.out.println("进入get");
        mClient.get(url, params, handler);
    }
}
