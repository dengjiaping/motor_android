package com.moto.square;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.moto.inform.Inform_Friends;

/**
 * Created by chen on 14-6-29.
 */
public class Theme_Post_Touchme extends Inform_Friends{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("name",list.get(position).get("name").toString());
                intent.putExtras(bundle);
                Theme_Post_Touchme.this.setResult(3,intent);
                Theme_Post_Touchme.this.finish();
            }
        });
    }
}
