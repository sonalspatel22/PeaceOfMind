package com.sonal.apple.peaceofmind.prefrence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sonal.apple.peaceofmind.adaptor.Playlistadaptor;
import com.sonal.apple.peaceofmind.model.playlistmodel;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by apple on 05/03/18.
 */

public class MyPrefrence {


    public static SharedPreferences appSharedPrefs;
    public static SharedPreferences.Editor prefsEditor;
    ArrayList<playlistmodel> list = new ArrayList<>();


    public MyPrefrence(Context context) {
        appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = appSharedPrefs.edit();
    }

    public void setlist(ArrayList<playlistmodel> playlist) {
        Gson gson = new Gson();
        String json = gson.toJson(playlist);
        prefsEditor.putString("playlist", json);
        prefsEditor.commit();
    }

    public ArrayList<playlistmodel> getList() {
        Gson gson = new Gson();
        String response = appSharedPrefs.getString("playlist", "");
        Log.e("res", response);
        ArrayList<playlistmodel> lstArrayList = gson.fromJson(response, new TypeToken<ArrayList<playlistmodel>>() {
        }.getType());
        return lstArrayList;
    }
}
