package com.epiccrown.flickr.client.flickrclient;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.prefs.PreferencesFactory;

/**
 * Created by Epiccrown on 15.04.2018.
 */

public class MyPreferences {
    public static final String TAG = "searchHistory";

    public static String getStoredQuery(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(TAG,null);
    }

    public static void setStoredQuery(Context context, String query){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(TAG,query)
                .apply();
    }
}
