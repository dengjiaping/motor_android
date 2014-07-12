package com.moto.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.moto.live.WriteLiveActivity;
import com.moto.model.DataBaseModel;
import com.moto.photopicker.Bimp;
import com.moto.toast.ToastClass;
import com.moto.utils.DateUtils;
import com.moto.utils.StringUtils;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by chen on 14-7-7.
 */
public class User_Draftbox_writeLive extends WriteLiveActivity{

    private DataBaseModel dataBaseModel;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDatabaseData();
        draftboxInit();

        //重写write_send监听
        write_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DataBaseModel dataBaseModel1 = new Select().from(DataBaseModel.class).where("id = ?",id).executeSingle();
                dataBaseModel1.subject = subject;
                dataBaseModel1.IsHaveUserName = IsHaveUserName;
                dataBaseModel1.isHavePhotoarray = isHavePhoto;
                ArrayList<String> tdataList = StringUtils.getIntentArrayList(dataList);
                dataBaseModel1.arrayimagepath = new JSONArray(tdataList).toString();
                dataBaseModel1.atuser = mentionUsername;
                dataBaseModel1.latitude = lat;
                dataBaseModel1.location = location;
                dataBaseModel1.longitude = lon;
                dataBaseModel1.message = et_sendmessage.getText().toString();
                dataBaseModel1.time = DateUtils.getUTCCurrentTimestamp();
                dataBaseModel1.token = ToastClass.GetTokenString(User_Draftbox_writeLive.this);
                dataBaseModel1.save();

                GetAsyData();
            }
        });

    }


    private void getDatabaseData(){
        dataBaseModel = (DataBaseModel)getIntent().getExtras().getSerializable("data");
        id = getIntent().getExtras().getString("id");
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
                Bimp.drr.add(array.get(i).toString());
            }
        }catch (Exception e)
        {}


        gridImageAdapter.update();

        et_sendmessage.setText(dataBaseModel.message);

    }

}
