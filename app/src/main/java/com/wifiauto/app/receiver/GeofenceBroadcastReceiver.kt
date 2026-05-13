package com.wifiauto.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.wifiauto.app.WiFiAutoApplication

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) return

        val transition = geofencingEvent.geofenceTransition
        val app = context.applicationContext as WiFiAutoApplication

        if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            val requestId = triggeringGeofences?.get(0)?.requestId
            app.connectivityStateMachine.entrouEmAreaSalva(requestId)
        } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            app.connectivityStateMachine.saiuDeAreaSalva()
        }
    }
}
