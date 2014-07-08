package com.moto.user;

import android.os.Bundle;
import android.util.Log;

import com.moto.live.WriteLiveActivity;
import com.moto.model.DataBaseModel;

import org.json.JSONArray;

/**
 * Created by chen on 14-7-7.
 */
public class User_Draftbox_writeLive extends WriteLiveActivity{

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
        subject = dataBaseModel.subject;
        live_name.setText(subject);



        location = dataBaseModel.location;
        if(location.equals(""))
            real_position.setText("插入位置");
        else
            real_position.setText(location);

        lon = dataBaseModel.longitude;
        lat = dataBaseModel.latitude;
        locationsign = dataBaseModel.locationsign;
        mentionUsername = dataBaseModel.atuser;
        IsHaveUserName = dataBaseModel.IsHaveUserName;
        isHavePhoto = dataBaseModel.isHavePhotoarray;

        try {
            JSONArray array = new JSONArray(dataBaseModel.arrayimagepath);
            int num = array.length();
            for(int i = 0; i < num; i++)
            {
                dataList.add(array.get(i).toString());
            }
        }catch (Exception e)
        {}

        if (dataList != null && dataList.size()>0) {
            if (dataList.size() < 5) {
                dataList.add("default_add_img");
            }
        }
        gridImageAdapter.notifyDataSetChanged();

        et_sendmessage.setText(dataBaseModel.message);

    }

}
