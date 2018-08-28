package com.ourincheon.app_center.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferenceBase{

    public void putSharedPreference(String key, HashSet<String> value){
        SharedPreferences sp = NetworkController.getInstance().getSharedPreferences("storage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key,value);
        editor.commit();
    }
    public void putSharedPreference(String key, String value){
        SharedPreferences sp = NetworkController.getInstance().getSharedPreferences("storage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public Set<String> getSharedPreference(String key, @Nullable Set<String> defaultValue){
            SharedPreferences sp = NetworkController.getInstance().getSharedPreferences("storage", Context.MODE_PRIVATE);
            return sp.getStringSet(key,defaultValue);
    }

    public String getSharedPreference(String key, @Nullable String defaultValue){
        SharedPreferences sp = NetworkController.getInstance().getSharedPreferences("storage", Context.MODE_PRIVATE);
        return sp.getString(key,defaultValue);
    }
}
