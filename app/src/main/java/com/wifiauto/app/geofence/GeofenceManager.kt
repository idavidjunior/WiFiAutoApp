package com.wifiauto.app.geofence

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.wifiauto.app.data.AppDatabase
import com.wifiauto.app.data.PlaceEntity
import com.wifiauto.app.receiver.GeofenceBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeofenceManager(private val context: Context) {

    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    fun criarGeofence(place: PlaceEntity) {
        val geofence = Geofence.Builder()
            .setRequestId(place.id.toString())
            .setCircularRegion(place.latitude, place.longitude, place.raio.toFloat())
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val pendingIntent = getPendingIntent()

        try {
            geofencingClient.addGeofences(request, pendingIntent)?.run {
                addOnSuccessListener { }
                addOnFailureListener { e -> e.printStackTrace() }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun removerGeofence(requestId: String) {
        geofencingClient.removeGeofences(listOf(requestId))?.run {
            addOnSuccessListener { }
            addOnFailureListener { e -> e.printStackTrace() }
        }
    }

    fun recriarTodasGeofences() {
        scope.launch {
            geofencingClient.removeGeofences(getPendingIntent())
            val lugares = AppDatabase.getDatabase(context).placeDao().getAll()
            lugares.forEach { criarGeofence(it) }
        }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
