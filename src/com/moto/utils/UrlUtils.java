package com.moto.utils;

public class UrlUtils {
	private final static String imagePath = "http://motor.qiniudn.com/";
	
	public static String avatarUrl(String avatarHash) {
		StringBuilder stringBuilder = new StringBuilder();
		String url =  stringBuilder.append(imagePath).append(avatarHash).append("?imageView2/1/w/40/h/40").toString();
		return url;
	}
	
	public static String imageUrl(String imageHash){
		StringBuilder stringBuilder = new StringBuilder();
		String url =  stringBuilder.append(imagePath).append(imageHash).toString();
		return url;
	}

    public static String imageUrl_avatar(String imageHash,int width,int height)
    {
        StringBuilder stringBuilder = new StringBuilder();
//        String url =  stringBuilder.append(imagePath).append(imageHash).append("?imageMogr2/thumbnail/")
//                .append(width).append("x").append(height).append("!").toString();
        String url =  stringBuilder.append(imagePath).append(imageHash).append("?imageView2/1/w/")
                .append(width).append("/h/").append(height).toString();
        return url;
    }
}
