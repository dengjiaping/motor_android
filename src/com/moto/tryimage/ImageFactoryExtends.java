package com.moto.tryimage;

import android.os.Bundle;
import android.view.View;

import com.moto.photopicker.Bimp;

/**
 * Created by chen on 14-7-12.
 */
public class ImageFactoryExtends extends ImageFactoryActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photo_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(202);
                ImageFactoryExtends.this.finish();
            }
        });
    }
}
