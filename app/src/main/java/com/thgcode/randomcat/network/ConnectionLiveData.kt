package com.thgcode.randomcat.network

import androidx.lifecycle.LiveData
import com.thgcode.randomcat.model.ConnectionModel
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

class ConnectionLiveData(private val context: Context) : LiveData<ConnectionModel>() {

    override fun onActive() {
        super.onActive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.extras != null) {
                val activeNetwork = intent.extras?.get(ConnectivityManager.EXTRA_NETWORK_INFO) as NetworkInfo
                val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

                if (isConnected) {
                    when (activeNetwork.type) {
                        ConnectivityManager.TYPE_WIFI -> postValue(ConnectionModel(ConnectivityManager.TYPE_WIFI, true))
                        ConnectivityManager.TYPE_MOBILE -> postValue(
                            ConnectionModel(
                                ConnectivityManager.TYPE_MOBILE,
                                true
                            )
                        )
                    }
                } else {
                    postValue(ConnectionModel(0, false))
                }
            }
        }
    }
}