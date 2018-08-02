package com.epiccrown.flickr.client.flickrclient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebBrowser extends VisibleFragment {

    private Uri url;
    private ProgressBar progressBar;
    private static WebView mBrowser;
    public static WebBrowser getInstance(Uri uri){
        Bundle bundle = new Bundle();
        bundle.putParcelable("ARG_URI",uri);
        WebBrowser browser = new WebBrowser();
        browser.setArguments(bundle);
        return browser;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getParcelable("ARG_URI");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.web_browser,container,false);
        mBrowser = v.findViewById(R.id.webbrowser);
        progressBar = v.findViewById(R.id.progress_bar_webbrowser);
        progressBar.setMax(100);

        try {
            mBrowser.getSettings().setJavaScriptEnabled(true);
            mBrowser.setWebChromeClient(new WebChromeClient(){

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if(newProgress == 100){
                        progressBar.setVisibility(View.GONE);
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    }
                }

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    try {
                        AppCompatActivity activity = (AppCompatActivity) getActivity();
                        activity.getSupportActionBar().setSubtitle(title);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
            mBrowser.setWebViewClient(new WebViewClient(){

                @Override
                public void onLoadResource(WebView view, String url) {
                    String protocol = url.split(":")[0];
                    if(protocol.equals("http")||protocol.equals("https")){
                        //smt
                    }else{
                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                        startActivity(intent);
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return false;
                }
            });
            mBrowser.loadUrl(url.toString());
            Activity activity = getActivity();
            activity.onBackPressed();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return v;
    }

    public static WebView getWebBrowser(){
        return mBrowser;
    }


}
