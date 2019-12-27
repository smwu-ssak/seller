package com.example.ssak.DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

// Customized by SY

public class SharedPreferenceController {

    static final String myID = "MYID";

    static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setMyId(Context context, String myId){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(myID, myId);
        editor.commit();
    }

    public static String getMyId(Context context){
        return getSharedPreferences(context).getString(myID, "");
    }

    // 로그아웃
    public static void clearMyId(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

}
