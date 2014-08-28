package com.moto.asydata;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

public class LoadCacheResponseLoginouthandler extends AsyncHttpResponseHandler{
    private Context context;
    private LoadDatahandler mHandler;

    public LoadCacheResponseLoginouthandler(Context context,
                                            LoadDatahandler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
    }
    @Override
    public void onStart() {
        super.onStart();
        mHandler.onStart();
    }
    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        // TODO Auto-generated method stub
        super.onProgress(bytesWritten, totalSize);
        mHandler.onProgress(bytesWritten, totalSize);
    }
    @Override
    public void onFailure(Throwable error, String content) {
        super.onFailure(error, content);
        mHandler.onFailure("error", "网络连接超时");
    }
    @Override
    public void onFinish() {
        super.onFinish();
        mHandler.onFinish();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String content) {
        super.onSuccess(statusCode, headers, content);
        System.out.println("得到的返回码" + statusCode);
        try {
            mHandler.onSuccess(content);
            System.out.println("返回的内容" + content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 出错
     *
     * @param error
     * @param errorMessage
     */
    public void onFailure(String error, String errorMessage) {
        if (errorMessage != null) {
            mHandler.onFailure(error, errorMessage);
        }
    }
}
