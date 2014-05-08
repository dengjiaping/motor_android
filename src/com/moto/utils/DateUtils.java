package com.moto.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	  
	public static Date timestampToLocalDate(String timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date = new Date();
		try {
			date = sdf.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

    public static Date timestampToLocalDateWithMillisecond(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = new Date();
        try {
            date = sdf.parse(timestamp);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public static String getUTCCurrentTimestamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        String timestamp = sdf.format(date);
        return timestamp;
    }

    public static boolean compareUTCTimestamp(String beforeTimestamp, String afterTimestamp)
    {
        Date beforeDate = timestampToLocalDateWithMillisecond(beforeTimestamp);
        Date afterDate = timestampToLocalDateWithMillisecond(afterTimestamp);
        long a = beforeDate.getTime();
        long b = afterDate.getTime();
        return beforeDate.getTime() > afterDate.getTime();
    }

    /**
     * 根据传入的时间计算距离该时间是几分钟前，几小时前，还是显示具体时间
	 * 
     *
     *
     * @param timestamp UTC时间
     * @return String 	根据传入的的时间距离当前时间的时长，返回不同的字符串
     */
	public static String timestampToDeatil(String timestamp){
		Date date = DateUtils.timestampToLocalDate(timestamp);
		String detailString = new String();
		long  distance = System.currentTimeMillis() - date.getTime();
		if(distance > 0 && distance < 60000)
		{
			detailString = formatSecondDate(distance);
		}
		else if (distance > 60000 && distance < 3600000) {
	    	detailString = formatMinuteDate(distance);
	    }else if(distance >= 3600000 && distance < 86400000){
	        detailString = formatDayDate(distance);
	    }
	    else{
	        detailString = formatDeatilDate(date);
	    }
		return detailString;
	}

    /**
     * 根据传入的时间计算距离该时间是几分钟前，几小时前，还是显示具体时间
     *
     *
     *
     * @param timestamp UTC时间
     * @return String 	根据传入的的时间距离当前时间的时长，返回不同的字符串
     * 针对的是后面有毫秒的
     */
    public static String StimestampToDeatil(String timestamp){
        Date date = DateUtils.timestampToLocalDateWithMillisecond(timestamp);
        String detailString = new String();
        long  distance = System.currentTimeMillis() - date.getTime();
        if(distance > 0 && distance < 60000)
        {
            detailString = formatSecondDate(distance);
        }
        else if (distance > 60000 && distance < 3600000) {
            detailString = formatMinuteDate(distance);
        }else if(distance >= 3600000 && distance < 86400000){
            detailString = formatDayDate(distance);
        }
        else{
            detailString = formatDeatilDate(date);
        }
        return detailString;
    }
	
	
	public static String formatSecondDate(long distance) {
		return new StringBuilder().append(distance / 1000)
								  .append("秒前")
								  .toString();
	}
	public static String formatMinuteDate(long distance) {
		return new StringBuilder().append(distance / 60000)
								  .append("分钟前")
								  .toString();
	}
	
	public static String formatDayDate(long distance) {
		return new StringBuilder().append(distance / 3600000)
								  .append("小时前")
								  .toString();
	}
	
	public static String formatDeatilDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		return sdf.format(date);
	}
}
