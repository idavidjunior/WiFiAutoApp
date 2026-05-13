package com.wifiauto.app

import android.app.Application
import androidx.work.Configuration
import com.wifiauto.app.connectivity.ConnectivityStateMachine
import com.wifiauto.app.data.AppDatabase
import com.wifiauto.app.geofence.GeofenceManager
import com.wifiauto.app.receiver.AirplaneModeReceiver
import com.wifiauto.app.receiver.WifiStateReceiver
import com.wifiauto.app.service.AlertSonoroManager
import com.wifiauto.app.worker.EstadoWorkerFactory

class WiFiAutoApplication : Application(), Configuration.Provider {

    lateinit var connectivityStateMachine: ConnectivityStateMachine
    lateinit var geofenceManager: GeofenceManager
    lateinit var alertSonoroManager: AlertSonoroManager

    override fun onCreate() {
        super.onCreate()
        instance = this

        AppDatabase.getDatabase(this)
        connectivityStateMachine = ConnectivityStateMachine(this)
        geofenceManager = GeofenceManager(this)
        alertSonoroManager = AlertSonoroManager(this)

        WifiStateReceiver.register(this)
        AirplaneModeReceiver.register(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(EstadoWorkerFactory(connectivityStateMachine))
            .build()

    companion object {
        lateinit var instance: WiFiAutoApplication
            private set
    }
}
