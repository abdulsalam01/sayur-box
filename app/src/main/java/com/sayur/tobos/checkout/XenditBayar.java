package com.sayur.tobos.checkout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sayur.tobos.R;

public class XenditBayar extends AppCompatActivity {
    WebView webXendit;
    WebSettings webSettings;
    Button btnXendit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xendit_bayar);
        webXendit = findViewById(R.id.webXendit);
        webSettings = webXendit.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webXendit.setWebViewClient(new WebViewClient());
        webXendit.setWebChromeClient(new WebChromeClient());
        webXendit.loadUrl("https://checkout-staging.xendit.co/web/" + getIntent().getStringExtra("xendit_id"));
        btnXendit = findViewById(R.id.btnXenditConfirm);
        btnXendit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}