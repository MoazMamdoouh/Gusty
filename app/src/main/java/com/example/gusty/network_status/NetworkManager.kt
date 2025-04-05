package com.example.gusty.network_status

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log

object NetworkManager {
    private var isConnected = false
    private var networkStateCallback: ((Boolean) -> Unit)? = null

    fun registerNetworkCallback(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.i("network", "onAvailable: ")
                    isConnected = true
                    networkStateCallback?.invoke(isConnected)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Log.i("network", "onLost:")
                    isConnected = false
                    networkStateCallback?.invoke(isConnected)
                }
            })
    }

    // Get current network state
    fun getNetworkState(): Boolean {
        return isConnected
    }

    // Observe network state changes
    fun setNetworkStateListener(callback: (Boolean) -> Unit) {
        networkStateCallback = callback
    }
}