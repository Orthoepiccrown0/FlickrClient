package com.epiccrown.flickr.client.flickrclient;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FlickrClient extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return new GalleryFragment();
    }
}
