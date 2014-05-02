package com.moto.emotion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 *
 ******************************************
 * @author Âªñ‰πÉÊ≥? * @Êñá‰ª∂ÂêçÁß∞	:  FileUtils.java
 * @ÂàõÂª∫Êó∂Èó¥	: 2013-1-27 ‰∏ãÂçà02:35:09
 * @Êñá‰ª∂ÊèèËø∞	: Êñá‰ª∂Â∑•ÂÖ∑Á±? ******************************************
 */
public class FileUtils {
	/**
	 * ËØªÂèñË°®ÊÉÖÈÖçÁΩÆÊñá‰ª∂
	 *
	 * @param context
	 * @return
	 */
	public static List<String> getEmojiFile(Context context) {
		try {
			List<String> list = new ArrayList<String>();
			InputStream in = context.getResources().getAssets().open("emoji");// ÔøΩƒºÔøΩÔøΩÔøΩÔøΩÔøΩŒ™rose.txt
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
                                                                         "UTF-8"));
			String str = null;
			while ((str = br.readLine()) != null) {
				list.add(str);
			}
            
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
