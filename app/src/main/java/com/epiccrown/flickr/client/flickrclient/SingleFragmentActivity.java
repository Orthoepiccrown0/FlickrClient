package com.epiccrown.flickr.client.flickrclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Epiccrown on 11.04.2018.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    public abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_client);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = createFragment();

        fm.beginTransaction()
                .replace(R.id.main_container,fragment)
                .disallowAddToBackStack()
                .commit();
    }
}
