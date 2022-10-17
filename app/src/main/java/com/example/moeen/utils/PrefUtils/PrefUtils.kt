package com.example.moeen.utils.PrefUtils

import android.content.Context
import android.util.Log
import com.example.moeen.common.Constants.TAG
import java.lang.Exception
import java.security.Key

class PrefUtils {
    companion object{
        fun saveInPref(context: Context,key:String,value:Any){
            val prefs=androidx.preference.PreferenceManager.getDefaultSharedPreferences(context).edit()
            try {
                when(value){
                    is Int -> prefs.putInt(key,value)
                    is Long -> prefs.putLong(key,value)
                    is String -> prefs.putString(key,value)
                    is Boolean -> prefs.putBoolean(key,value)
                    is Float -> prefs.putFloat(key,value)
                }
            }catch (e:Exception){
                Log.d(TAG, "saveInPref: ${e.message}")
            }
        }

        fun getFromPref(context: Context,key: String,defaultValue:Any):Any?{
            val prefs=androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return try {
                when(defaultValue){
                    is Int -> prefs.getInt(key,defaultValue)
                    is Long -> prefs.getLong(key,defaultValue)
                    is String -> prefs.getString(key,defaultValue)
                    is Boolean -> prefs.getBoolean(key,defaultValue)
                    is Float -> prefs.getFloat(key,defaultValue)
                    else -> null
                }
            }catch (e:Exception){
                Log.d(TAG, "getFromPref: ${e.message}")
            }
        }
    }
}