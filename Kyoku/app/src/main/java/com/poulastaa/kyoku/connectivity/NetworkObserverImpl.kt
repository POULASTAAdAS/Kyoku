package com.poulastaa.kyoku.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkObserverImpl(
    context: Context
): NetworkObserver {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<NetworkObserver.STATUS> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch {
                    send(NetworkObserver.STATUS.AVAILABLE)
                }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                launch { send(NetworkObserver.STATUS.LOSING) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(NetworkObserver.STATUS.LOST) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                launch {
                    send(NetworkObserver.STATUS.UNAVAILABLE)
                }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}