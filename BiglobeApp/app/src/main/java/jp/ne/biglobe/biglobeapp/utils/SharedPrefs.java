package jp.ne.biglobe.biglobeapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by taipa on 7/2/15.
 */
public class SharedPrefs {
    private static SharedPreferences sharedPreferences = null;

    public static void init(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     *
     * @param key
     * @return
     */
    public static boolean getBool(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * Get bool with default value is true
     *
     * @param key
     * @return
     */
    public static boolean getBool2(String key){
        return sharedPreferences.getBoolean(key, true);
    }

    /**
     *
     * @param key
     * @param value
     */
    public static void saveBool(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     *
     * @param key
     * @return
     */
    public static String getString(String key){
        return sharedPreferences.getString(key, null);
    }

    /**
     *
     * @param key
     * @param value
     */
    public static void saveString(String key, String value){
        sharedPreferences.edit().putString(key, value).apply();
    }

    /**
     *
     * @param key
     * @return
     */
    public static long getLong(String key){
        return sharedPreferences.getLong(key, 0);
    }

    /**
     *
     * @param key
     * @param value
     */
    public static void saveLong(String key, long value){
        sharedPreferences.edit().putLong(key, value).apply();
    }


}
