package com.gabriel.astronomypod.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class NetworkStateReceiver(
    private val context: Context,
    private val listener: NetworkStateListener
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val connManager = this.context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (connManager.activeNetworkInfo?.isConnected == true) {
            listener.onNetworkConnected()
        } else {
            listener.onNetworkLost()
        }

    }


    fun register() {
        context.registerReceiver(this, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun unregister() {
        try {
            context.unregisterReceiver(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface NetworkStateListener {

        fun onNetworkLost()

        fun onNetworkConnected()

    }

}