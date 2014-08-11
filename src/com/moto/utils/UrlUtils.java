package com.moto.utils;

public class UrlUtils {
	private final static String imagePath = "http://motor.qiniudn.com/";
	
	public static String avatarUrl(String avatarHash) {
		StringBuilder stringBuilder = new StringBuilder();
		String url =  stringBuilder.append(imagePath).append(avatarHash).append("?imageView2/1/w/60/h/60").toString();
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
        String url =  stringBuilder.append(imagePath).append(imageHash).append("?imageView2/2/w/")
                .append(width).toString();

        return url;
    }

    public static String imageUrl_avatar(String imageHash, int width)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String url =  stringBuilder.append(imagePath).append(imageHash).append("?imageView2/2/w/")
                .append(width).toString();

       return url;
    }
}
