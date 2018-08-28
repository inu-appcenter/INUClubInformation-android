package com.ourincheon.app_center.mainActivity.Setting.Application;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.ourincheon.app_center.R;
import com.ourincheon.app_center.mainActivity.Setting.ModifyClubInformation.ModifyText;

public class New_Application extends AppCompatActivity {

    WebView webView;
    String method_URL = "https://www.google.com/intl/ko_kr/forms/about/";
    Button bt_complete;
    String clubnum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_application);


        webView = (WebView) findViewById(R.id.Webview_new);
        bt_complete = (Button) findViewById(R.id.Application_Complete);
        EditText ap_link = (EditText) findViewById(R.id.AttachURLTextView);

        Intent intent = getIntent();
        clubnum = intent.getStringExtra("clubIdNumber");


        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);

        client();
        webView.loadUrl(method_URL);

        bt_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ApplicationLink = ap_link.getText().toString();
                Intent intent = new Intent(New_Application.this, ModifyText.class);
                intent.putExtra("Link", ApplicationLink);
                intent.putExtra("FromNew", 111);
                intent.putExtra("clubIdNumber", clubnum);
                startActivity(intent);
                MakeApplication.makeApplication.finish();
                finish();
            }
        });
    }

    public void client(){
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
