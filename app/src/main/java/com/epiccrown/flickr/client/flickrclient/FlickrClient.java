package com.epiccrown.flickr.client.flickrclient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FlickrClient extends SingleFragmentActivity {


    public static Intent newIntent(Context context){
        return new Intent(context,FlickrClient.class);
    }

    @Override
    public Fragment createFragment() {
        return new GalleryFragment();
    }
}
