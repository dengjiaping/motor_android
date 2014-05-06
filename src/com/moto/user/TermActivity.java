package com.moto.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;

public class TermActivity extends Moto_RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_term,"用户协议",barButtonIconType.barButtonIconType_Back,barButtonIconType.barButtonIconType_None);
        WebView wView = (WebView)findViewById(R.id.term_webview);
        WebSettings wSet = wView.getSettings();
        wSet.setJavaScriptEnabled(true);
        wView.loadUrl("file:///android_asset/term.html");
    }
}
