package com.moto.model;

import org.json.JSONException;
import org.json.JSONObject;



public interface NetWorkModelListener {

    /**
     * 当返回值中is为1时被调用
     *
     * @param JSONObject 服务器返回的数据
     */
	public void handleNetworkDataWithSuccess(JSONObject JSONObject) throws JSONException;
    /**
     * 当返回值中is为0时被调用
     *
     * @param JSONObject 服务器返回的数据
     */
	public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException;
    /**
     * 当进度被
     *
     * @param progress 网络服务请求时发送的数据进度
     */
	public void handleNetworkDataWithUpdate(float progress) throws JSONException;
	
	/**
     * 获取失败
     *
     * @param progress 网络服务请求失败
     */
	public void handleNetworkDataGetFail(String message) throws JSONException;
	
	/**
     * 获取失败
     *
     * @param progress 网络服务请求失败
     */
	public void handleNetworkDataStart() throws JSONException;
}
