package com.epiccrown.flickr.client.flickrclient;

/**
 * Created by Epiccrown on 11.04.2018.
 */

public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mURL;

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

    @Override
    public String toString() {
        return mCaption;
    }
}
