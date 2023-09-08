package com.example.myattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;

public class ViewPdfActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        webView=findViewById(R.id.pdfwebview);
        webView.getSettings().setJavaScriptEnabled(true);
        String filename=getIntent().getStringExtra("filename");
        String fileurl=getIntent().getStringExtra("fileurl");

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(filename);
        progressDialog.setMessage("Opening.....");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });

        String url="";
        try {
            url = URLEncoder.encode(fileurl, "UTF-8");
        }
        catch (Exception e)
        {

        }
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);

    }
}