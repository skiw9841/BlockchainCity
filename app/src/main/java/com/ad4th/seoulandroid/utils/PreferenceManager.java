package com.ad4th.seoulandroid.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PreferenceManager {

    // intro check
    public static void setIntroCheck(Context context, boolean isFirst)
    {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences("INTRO", 0);
        editor = pref.edit();
        editor.putBoolean("check", isFirst);
        editor.commit();
    }

    public static boolean isIntroCheck(Context context)
    {
        return context.getSharedPreferences("INTRO", 0).getBoolean("check", true);
    }

    // citizen check
    public static void setCitizenCheck(Context context, boolean isFirst)
    {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences("CITIZEN", 0);
        editor = pref.edit();
        editor.putBoolean("check", isFirst);
        editor.commit();
    }

    public static boolean isCitizenCheck(Context context)
    {
        boolean check = context.getSharedPreferences("CITIZEN", Context.MODE_PRIVATE).getBoolean("check", false);
        return check;
    }


    // address
    public static void setCitizenAddress(Context context, String address)
    {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences("CITIZEN", 0);
        editor = pref.edit();
        editor.putString("address", address);
        editor.commit();
    }

    public static String getCitizenAddress(Context context)
    {
        return context.getSharedPreferences("CITIZEN", 0).getString("address", "");
    }

    // private key
    public static void setCitizenPrivateKey(Context context, String address)
    {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences("CITIZEN", 0);
        editor = pref.edit();
        editor.putString("private_key", address);
        editor.commit();
    }

    public static String getCitizenPrivateKey(Context context)
    {
        return context.getSharedPreferences("CITIZEN", 0).getString("private_key", "");
    }

    // AUTH History
    public static void addAuthLog(Context context, String date, String type, String agency)
    {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences("AUTH_LOG", 0);
        editor = pref.edit();

        ArrayList<String> _dateArray = getAuthLogAll(context, "date");
        _dateArray.add(date);
        ArrayList<String> _typeArray = getAuthLogAll(context, "type");
        _typeArray.add(type);
        ArrayList<String> _agencyArray = getAuthLogAll(context, "agency");
        _agencyArray.add(agency);


        editor.putString("date", _dateArray.toString());
        editor.putString("type", _typeArray.toString());
        editor.putString("agency", _agencyArray.toString() );
        editor.commit();

    }

    public static ArrayList<String> getAuthLogAll(Context context, String key)
    {
        String json = context.getSharedPreferences("AUTH_LOG", 0).getString(key, "");

        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public static void clearAll(Context context )
    {
        context.getSharedPreferences("INTRO",  Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences("CITIZEN",  Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences("AUTH_LOG",  Context.MODE_PRIVATE).edit().clear().apply();
    }

}
