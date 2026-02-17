package ai.ljp.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import androidx.tracing.trace
import com.ljp.common.network.AIForumDispatchers
import com.ljp.common.network.Dispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ConnectivityManagerNetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(AIForumDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : NetworkMonitor{
    override val isOnline: Flow<Boolean>
        get() = callbackFlow {
            trace("NetworkMonitor.callbackFlow") {
                val connectivityManager  = context.getSystemService<ConnectivityManager>()
                if (connectivityManager == null) {
                    channel.trySend(false)
                    channel.close()
                    return@callbackFlow
                }
                val callback = object : ConnectivityManager.NetworkCallback() {
                    private val networks = mutableSetOf<Network>()
                    override fun onAvailable(network: Network) {
                        networks += network
                        channel.trySend(true)
                    }

                    override fun onLost(network: Network) {
                        networks -= network
                        channel.trySend(networks.isNotEmpty())
                    }
                }
                trace("NetworkMonitor.registerNetworkCallback") {
                    val request = NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build()
                    connectivityManager.registerNetworkCallback(request,callback)
                }
                channel.trySend(connectivityManager.isCurrentConnected())
                awaitClose {
                    connectivityManager.unregisterNetworkCallback(callback)
                }
            }

        }
    private fun ConnectivityManager.isCurrentConnected() : Boolean {
        val networkCapabilities = getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}