package com.saurabh.hackathon18;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class LinkActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.activity_link);
        webView = findViewById(R.id.Web);
        webView.getSettings().setJavaScriptEnabled(true);
        String Link = bundle.getString("Link");
        if (!Link.startsWith("https://")&& Link.startsWith("www.")){
            Link = "http://"+Link;
        }
        if (!Link.startsWith("https://www.")){
            Link = "https://www."+Link;
        }
        if (!Link.endsWith(".com")){
            Link = Link+".com";
        }
        webView.loadUrl(Link);
    }
}
