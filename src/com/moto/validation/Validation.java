package com.moto.validation;


public class Validation {
	
	String check=null;
	String checkName;
	String checkPassword;
	String checkTelephone;
	//判断邮箱是否正确
public static boolean checkEmail(String email) {
	// 验证邮箱的正则表达式
	String format = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
	if(email.matches(format)){
		//邮箱名合法,返回true
		return false;
		}else{
		//邮箱名不合法,返回false
		return true;
		}
}

//public static boolean nameContentCheck(String names){
//	char tempName[]=names.toCharArray();
//	for(int i=0;i<tempName.length;i++){
//		if((tempName[i]>47 && tempName[i]<58)  ||  (tempName[i]>64 && tempName[i]<91) || (tempName[i]>96 && tempName[i]<123) || (tempName[i]==95)){
//			continue;  
//		}else
//			return true;
//		}
//	return false;
//}
public static boolean nameContentCheck(String string) {
	   // TODO Auto-generated method stub
	   if(string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length()==0){
	    //如果不包含特殊字符
	    return false;
	   }
	   return true;
	}
public static boolean nameLengthCheck(String names){
	   char tempName[]=names.toCharArray();
		 
 	   if(tempName.length<3 || tempName.length>10){
 		  return true;
        }   
 	   return false;
}			    
public static boolean passwordContentCheck(String passwords){
	char tempPass[]=passwords.toCharArray();
	 for(int i=0;i<tempPass.length;i++){
	       if((tempPass[i]>47 && tempPass[i]<58)  ||  (tempPass[i]>64 && tempPass[i]<91) || (tempPass[i]>96 && tempPass[i]<123) || (tempPass[i]==95)){
		           continue;
	       }
	       else
	    	   return true;
     }
	 return false;
}	 				
public static boolean passwordLenghtCheck(String passwords){	
    char tempPass[]=passwords.toCharArray();
		    if(tempPass.length<6 || tempPass.length>15){  
		    	return true;
		    }	    	
		    return false;
	}
}
