package com.example.xiaoma.myapplication.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by SGC on 2015/12/26.
 */
public class SharedHelper {
    private Context mContext;

    public SharedHelper() {

    }

    public SharedHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void save(String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("key", key);
        editor.putString("value", value);
        editor.commit();
    }

    public String get(String key) {
        String value = "";
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        value = sp.getString("key", "");
        return value;
    }
}
