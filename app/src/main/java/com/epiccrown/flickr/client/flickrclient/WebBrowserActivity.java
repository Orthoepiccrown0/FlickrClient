package com.epiccrown.flickr.client.flickrclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.webkit.WebView;

public class WebBrowserActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return WebBrowser.getInstance(getIntent().getData());
    }

    public static Intent newIntent(Context context, Uri photoPageUri) {
        Intent i = new Intent(context, WebBrowserActivity.class);
        i.setData(photoPageUri);
        return  i;
    }

    @Override
    public void onBackPressed() {
        WebView webView = WebBrowser.getWebBrowser();
        if(webView!=null){
            if(webView.canGoBack()) webView.goBack();
            else super.onBackPressed();
        }
    }


}
