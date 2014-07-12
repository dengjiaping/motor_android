package com.moto.user;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.moto.main.R;
import com.moto.model.DataBaseModel;
import com.moto.photopicker.Bimp;
import com.moto.square.Publish_post;

import java.io.IOException;

/**
 * Created by chen on 14-7-7.
 */
public class User_Draftbox_writepost extends Publish_post{

    private DataBaseModel dataBaseModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDatabaseData();

        draftboxInit();
    }

    private void getDatabaseData(){
        dataBaseModel = (DataBaseModel)getIntent().getExtras().getSerializable("data");
    }

    private void draftboxInit(){

//        fid = dataBaseModel.fid;

        et_sendmessage.setText(dataBaseModel.message);
        IsHaveUserName = dataBaseModel.IsHaveUserName;
//        isHavePhoto = dataBaseModel.isHavePhoto;
//        filepath = dataBaseModel.imagepath;
        try {
            Bitmap b = Bimp.revitionImageSize(filepath);
            own_photos.setImageBitmap(b);
        } catch (IOException e) {

            e.printStackTrace();
        }


//        ImageManager2.from(User_Draftbox_writepost.this).displayImage(own_photos, filepath, R.drawable.default_add_img,100,100);

        write_theme.setText(dataBaseModel.subject);
    }


}
