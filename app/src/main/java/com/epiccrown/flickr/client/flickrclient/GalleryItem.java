package com.epiccrown.flickr.client.flickrclient;

import android.app.UiAutomation;
import android.net.Uri;

/**
 * Created by Epiccrown on 11.04.2018.
 */

public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mURL;
    private String mOwner;

    public GalleryItem() {
    }

    public GalleryItem(String mCaption, String mId, String mURL) {
        this.mCaption = mCaption;
        this.mId = mId;
        this.mURL = mURL;
    }

    public String getmCaption() {
        return mCaption;
    }

    public void setmCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmURL() {
        return mURL;
    }

    public void setmURL(String mURL) {
        this.mURL = mURL;
    }

    public String getmOwner() {
        return mOwner;
    }

    public void setmOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public Uri generateLink(){
        Uri uri = Uri.parse("http://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(getmOwner())
                .appendPath(getmId())
                .build();

        return uri;
    }

    @Override
    public String toString() {
        return mCaption;
    }
}
