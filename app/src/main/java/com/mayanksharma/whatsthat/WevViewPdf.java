package com.mayanksharma.whatsthat;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class WevViewPdf extends AppCompatActivity {
    WebView mWebview;
    PDFView pdfView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  mWebview  = new WebView(WevViewPdf.this);
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
        String url = "http://docs.google.com/viewer?url="+getIntent().getStringExtra("url")+".pdf";
        final Activity activity = this;
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setTitle("Loading...");
                activity.setProgress(progress * 100);
                if(progress == 100)
                    activity.setTitle(R.string.app_name);
            }
        });
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });
        mWebview .loadUrl(url);*/
        setContentView(R.layout.activity_wev_view_pdf);
        pdfView=(PDFView) findViewById(R.id.pdfView);
        try {
            displayFromUri(getIntent().getStringExtra("url"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    private void displayFromUri(String uri) throws URISyntaxException {


        Uri file= Uri.parse(uri);
        pdfView.fromUri(file)
                .defaultPage(1)
                .enableSwipe(true)
                .load();
    }
}