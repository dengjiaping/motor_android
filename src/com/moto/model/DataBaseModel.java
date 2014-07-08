package com.moto.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by chen on 14-7-4.
 */
@Table(name = "MotoData")
public class DataBaseModel extends Model implements Serializable{

//    @Column(name = "type")
//    public int type;

    @Column(name = "token")
    public String token;

//    @Column(name = "fid")
//    public String fid;

    @Column(name = "subject")
    public String subject;

    @Column(name = "message")
    public String message;

    @Column(name = "location")
    public String location;

    @Column(name = "longitude")
    public String longitude;

    @Column(name = "latitude")
    public String latitude;

    @Column(name = "locationsign")
    public String locationsign;

    @Column(name = "atuser")
    public String atuser;

    @Column(name = "IsHaveUserName")
    public boolean IsHaveUserName;

    @Column(name = "arrayimagepath")
    public String arrayimagepath;

    @Column(name = "isHavePhotoarray")
    public boolean isHavePhotoarray;

//    @Column(name = "imagepath")
//    public String imagepath;

//    @Column(name = "isHavePhoto")
//    public boolean isHavePhoto;

    @Column(name = "time")
    public String time;



    // 确保每个model类中都有一个默认的构造方法
    public DataBaseModel(){
        super();
    }

    public DataBaseModel(int type, String token,
                         String fid, String subject,
                         String message, String atuser,
                         boolean isHaveUserName, String imagepath,
                         boolean isHavePhoto){
        super();
//        this.type = type;
        this.token = token;
//        this.fid = fid;
        this.subject = subject;
        this.message = message;
        this.atuser = atuser;
//        this.imagepath = imagepath;
//        this.isHavePhoto = isHavePhoto;
        this.IsHaveUserName = isHaveUserName;
    }

    public DataBaseModel(String token,String fid,
                         String subject, String message,
                         String location, String longitude,
                         String latitude,  String locationsign,
                         String atuser, boolean isHaveUserName,
                         String arrayimagepath, boolean isHavePhotoarray,
                         String time){
        super();
//        this.type = type;
        this.token = token;
        this.subject = subject;
        this.message = message;
        this.atuser = atuser;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationsign = locationsign;
        this.arrayimagepath = arrayimagepath;
        this.isHavePhotoarray = isHavePhotoarray;
        this.IsHaveUserName = isHaveUserName;
//        this.imagepath = imagepath;
//        this.isHavePhoto = isHavePhoto;
        this.time = time;

    }

}
