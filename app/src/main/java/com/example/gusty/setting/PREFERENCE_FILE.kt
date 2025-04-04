package com.example.gusty.setting

import android.annotation.SuppressLint
import android.content.Context


object Preference {

    @SuppressLint("CommitPrefEdits")
    fun setLatitudeSharedPreference(lat: Double, context: Context) {
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putFloat("latitude", lat.toFloat())
            apply()
        }
    }

    fun getLatitudeSharedPreference(context: Context): Double {
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        return sharedPref.getFloat("latitude", 0.0f).toDouble()
    }

    fun setLongitudeSharedPreference(lon: Double, context: Context) {
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putFloat("longitude", lon.toFloat())
            apply()
        }
    }

    fun getLongitudeSharedPreference(context: Context): Double {
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        return sharedPref.getFloat("longitude", 0.0f).toDouble()
    }

    //location buttons state

    fun setLocationStateSharedPreference(locationString: String, context: Context) {
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("location_state", locationString)
            apply()
        }
    }

    fun getLocationStateSharedPreference(context: Context): String? {
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        return sharedPref.getString("location_state", " ")
    }
}

object UnitPreference {
    fun setUnitSharedPreference(unitName: String, context: Context) {
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("Unit", unitName)
            apply()
        }
    }

    fun getUnitSharedPreference(context: Context): String? {
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        return sharedPref.getString("Unit", " ")
    }
}

object WindPreference {
    fun setWindSharedPreference(unitName: String, context: Context) {
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("Wind_Unit", unitName)
            apply()
        }
    }
    fun getWindSharedPreference(context: Context) : String? {
        val sharedPref = context.getSharedPreferences("pref_shared", Context.MODE_PRIVATE)
        return sharedPref.getString("Wind_Unit", " ")
    }
}