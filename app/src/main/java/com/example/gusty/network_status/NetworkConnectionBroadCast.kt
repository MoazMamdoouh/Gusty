package com.example.gusty.network_status

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.Toast

class NetworkConnectionBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            Toast.makeText(context, "Wifi status changed ", Toast.LENGTH_SHORT).show()
            val cm: ConnectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val actionNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean =
                actionNetwork != null && actionNetwork.isConnectedOrConnecting
            if(isConnected){
                NetworkSharedPreference.setNetworkStatusPref(context , isConnected)
                Log.i("network", "onReceive !! $isConnected ")
                Log.i("network", "onReceive network pref ${NetworkSharedPreference.getNetworkStatus(context)} ")
            }else {
                NetworkSharedPreference.setNetworkStatusPref(context , isConnected)
                Log.i("network", "onReceive !! $isConnected ")
                Log.i("network", "onReceive network pref ${NetworkSharedPreference.getNetworkStatus(context)} ")

            }

        }
    }
}