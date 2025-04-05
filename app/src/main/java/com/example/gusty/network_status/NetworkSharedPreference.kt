package com.example.gusty.network_status

import android.content.Context
import android.util.Log

object NetworkSharedPreference {

    fun setNetworkStatusPref(context: Context , status : Boolean){
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("network", status)
            apply()
        }
    }
    fun getNetworkStatus(context: Context) : Boolean{
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("network", true)
    }
}