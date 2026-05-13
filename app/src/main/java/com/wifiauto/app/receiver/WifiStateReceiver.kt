package com.wifiauto.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import com.wifiauto.app.WiFiAutoApplication
import com.wifiauto.app.location.LocationManager

class WifiStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
            val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
            if (info?.isConnected == true) {
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val ssid = wifiManager.connectionInfo.ssid.replace("\"", "")
                
                if (ssid != "<unknown ssid>") {
                    val app = context.applicationContext as WiFiAutoApplication
                    val locationManager = LocationManager(context)
                    locationManager.getUltimaLocalizacao { loc ->
                        app.connectivityStateMachine.sugerirNovoLugar(ssid, loc.latitude, loc.longitude)
                    }
                }
            }
        }
    }

    companion object {
        fun register(context: Context) {
            val filter = IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            context.registerReceiver(WifiStateReceiver(), filter)
        }
    }
}
