package com.moto.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StringUtils {

	public static LinkedList<String> hashToArray(String hash){
		LinkedList<String>list = new LinkedList<String>();
		JSONArray array = null;
		try {
			array = new JSONArray(hash);
			int num = array.length();
			for(int i = 0; i < num; i++)
			{
				try {
					list.add(array.getString(i));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return list;
	}
	
	public static LinkedList<HashMap<String,Integer>> hashToWidthHeightArray(String hash){
		LinkedList<HashMap<String,Integer>>list = new LinkedList<HashMap<String,Integer>>();
		JSONArray array = null;
		JSONObject jsonObject = null;
		HashMap<String, Integer> map = null;
		try {
			array = new JSONArray(hash);
			int num = array.length();
			for(int i = 0; i < num; i++)
			{
				map = new HashMap<String, Integer>();
				try {
					jsonObject = array.getJSONObject(i);
					map.put("width",jsonObject.getInt("width"));
					map.put("height",jsonObject.getInt("height"));
					list.add(map);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return list;
	}
	
	/**
     * 将对象保存到SharedPreferences
     */
    public static String LinkedlistToBase(List<HashMap<String, Object>> list){
        
         ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
         ObjectOutputStream oos=null;
         try {
          oos = new ObjectOutputStream(baos);
          oos.writeObject(list);
         } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
         }
         // 将Product对象放到OutputStream中
         // 将Product对象转换成byte数组，并将其进行base64编码
         String newWord = new String(Base64.encodeBase64(baos.toByteArray()));
         return newWord;
    }
    
    /**
     * 将对象保存到SharedPreferences
     */
    public static String PhotoLinkedlistToBase(List<LinkedList<String>> list){
        
         ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
         ObjectOutputStream oos=null;
         try {
          oos = new ObjectOutputStream(baos);
          oos.writeObject(list);
         } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
         }
         // 将Product对象放到OutputStream中
         // 将Product对象转换成byte数组，并将其进行base64编码
         String newWord = new String(Base64.encodeBase64(baos.toByteArray()));
         return newWord;
    }
    
    /**
     * 将对象保存到SharedPreferences
     */
    public static String LikeLinkedlistToBase(List<String> list){
        
         ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
         ObjectOutputStream oos=null;
         try {
          oos = new ObjectOutputStream(baos);
          oos.writeObject(list);
         } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
         }
         // 将Product对象放到OutputStream中
         // 将Product对象转换成byte数组，并将其进行base64编码
         String newWord = new String(Base64.encodeBase64(baos.toByteArray()));
         return newWord;
    }

    /**
     * 将对象保存到SharedPreferences
     */
    public static String CommentLinkedlistToBase(List<String> list){

        ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
        ObjectOutputStream oos=null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(list);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 将Product对象放到OutputStream中
        // 将Product对象转换成byte数组，并将其进行base64编码
        String newWord = new String(Base64.encodeBase64(baos.toByteArray()));
        return newWord;
    }
    /**
     * 将对象保存到SharedPreferences
     */
    public static String LocationArraylistToBase(List<HashMap<String, Object>> LocationList){
        
         ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
         ObjectOutputStream oos=null;
         try {
          oos = new ObjectOutputStream(baos);
          oos.writeObject(LocationList);
         } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
         }
         // 将Product对象放到OutputStream中
         // 将Product对象转换成byte数组，并将其进行base64编码
         String newWord = new String(Base64.encodeBase64(baos.toByteArray()));
         return newWord;
    }
    /**
     * 将对象保存到SharedPreferences
     */
    public static String WidthHeightLinkedlistToBase(List<LinkedList<HashMap<String,Integer>>> WidthHeightList){
        
         ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
         ObjectOutputStream oos=null;
         try {
          oos = new ObjectOutputStream(baos);
          oos.writeObject(WidthHeightList);
         } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
         }
         // 将Product对象放到OutputStream中
         // 将Product对象转换成byte数组，并将其进行base64编码
         String newWord = new String(Base64.encodeBase64(baos.toByteArray()));
         return newWord;
    }
    /**
     * 从SharedPreferences中读取对象
     */
   public static LinkedList<HashMap<String, Object>> readLinkedlistFromBase(String base){
    // 对Base64格式的字符串进行解码
    byte[] base64Bytes = Base64.decodeBase64(base .getBytes());
    ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
    ObjectInputStream ois;
    try {
	  ois = new ObjectInputStream(bais);
	  // 从ObjectInputStream中读取Product对象
	  //   AddNewWord addWord= (AddNewWord ) ois.readObject();
	  List<HashMap<String, Object>> list = (List<HashMap<String, Object>>)ois.readObject();
	  return new LinkedList<HashMap<String,Object>>(list) ;
    } catch (StreamCorruptedException e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
    } catch (IOException e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
    } catch (ClassNotFoundException e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
    }
    return null;
   }
   
   /**
    * 从SharedPreferences中读取对象
    */
  public static LinkedList<LinkedList<String>> readPhotoLinkedlistFromBase(String base){
   // 对Base64格式的字符串进行解码
       byte[] base64Bytes = Base64.decodeBase64(base .getBytes());
         ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
      ObjectInputStream ois;
 try {
  ois = new ObjectInputStream(bais);
   // 从ObjectInputStream中读取Product对象
//  AddNewWord addWord= (AddNewWord ) ois.readObject();
  List<LinkedList<String>> list = (List<LinkedList<String>>)ois.readObject();
  return new LinkedList<LinkedList<String>>(list) ;
 } catch (StreamCorruptedException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
 } catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
 } catch (ClassNotFoundException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
 }
 return null;
  }
  
  /**
   * 从SharedPreferences中读取对象
   */
 public static ArrayList<HashMap<String, Object>> readLocationLinkedlistFromBase(String base){
  // 对Base64格式的字符串进行解码
  byte[] base64Bytes = Base64.decodeBase64(base .getBytes());
  ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
  ObjectInputStream ois;
  try {
	  ois = new ObjectInputStream(bais);
	  // 从ObjectInputStream中读取Product对象
	  //   AddNewWord addWord= (AddNewWord ) ois.readObject();
	  List<HashMap<String, Object>> list = (List<HashMap<String, Object>>)ois.readObject();
	  return new ArrayList<HashMap<String,Object>>(list) ;
  } catch (StreamCorruptedException e) {
  	// TODO Auto-generated catch block
  	e.printStackTrace();
  } catch (IOException e) {
  	// TODO Auto-generated catch block
  	e.printStackTrace();
  } catch (ClassNotFoundException e) {
  	// TODO Auto-generated catch block
  	e.printStackTrace();
  }
  return null;
 }
 
 /**
  * 从SharedPreferences中读取对象
  */
public static LinkedList<LinkedList<HashMap<String,Integer>>> readWidthHeightLinkedlistFromBase(String base){
 // 对Base64格式的字符串进行解码
 byte[] base64Bytes = Base64.decodeBase64(base .getBytes());
 ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
 ObjectInputStream ois;
 try {
	  ois = new ObjectInputStream(bais);
	  // 从ObjectInputStream中读取Product对象
	  //   AddNewWord addWord= (AddNewWord ) ois.readObject();
	  List<LinkedList<HashMap<String,Integer>>> list = (List<LinkedList<HashMap<String,Integer>>>)ois.readObject();
	  return new LinkedList<LinkedList<HashMap<String,Integer>>>(list) ;
 } catch (StreamCorruptedException e) {
 	// TODO Auto-generated catch block
 	e.printStackTrace();
 } catch (IOException e) {
 	// TODO Auto-generated catch block
 	e.printStackTrace();
 } catch (ClassNotFoundException e) {
 	// TODO Auto-generated catch block
 	e.printStackTrace();
 }
 return null;
}

/**
 * 从SharedPreferences中读取对象
 */
public static LinkedList<String> readLikeLinkedlistFromBase(String base){
// 对Base64格式的字符串进行解码
byte[] base64Bytes = Base64.decodeBase64(base .getBytes());
ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
ObjectInputStream ois;
try {
	  ois = new ObjectInputStream(bais);
	  // 从ObjectInputStream中读取Product对象
	  //   AddNewWord addWord= (AddNewWord ) ois.readObject();
	  List<String> list = (List<String>)ois.readObject();
	  return new LinkedList<String>(list) ;
} catch (StreamCorruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (ClassNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
return null;
}

/**
 * 裁剪图片数组
 */
public static ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

	ArrayList<String> tDataList = new ArrayList<String>();

	for (String s : dataList) {
		if (!s.contains("default")) {
			tDataList.add(s);
		}
	}

	return tDataList;

}
}

