package com.example.chatwithfirestore.utills

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager {
    private lateinit var sharedPreference: SharedPreferences

    public constructor(context: Context){
        sharedPreference = context.getSharedPreferences(Constants.KEY_PREFERENCES_NAME,Context.MODE_PRIVATE)
    }

    fun putBoolean(key: String,value:Boolean){
        var editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }
    fun getBoolean(key: String):Boolean{
        return sharedPreference.getBoolean(key,false)
    }

    fun putString(key: String,value: String) {
        var editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.putString(key,value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return sharedPreference.getString(key,null)
    }
    fun clear(){
       var editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.clear()
        editor.apply()
    }
}