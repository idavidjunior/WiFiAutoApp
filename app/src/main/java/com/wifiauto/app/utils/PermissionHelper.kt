package com.wifiauto.app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContextCompat

class PermissionHelper(private val context: Context) {

    fun todasPermissoesConcedidas(): Boolean {
        return hasLocationPermission() && hasBatteryOptimizationDisabled()
    }

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        val background = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        } else PackageManager.PERMISSION_GRANTED
        return fine == PackageManager.PERMISSION_GRANTED &&
                coarse == PackageManager.PERMISSION_GRANTED &&
                background == PackageManager.PERMISSION_GRANTED
    }

    fun hasBatteryOptimizationDisabled(): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    fun hasAdbPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            "android.permission.WRITE_SECURE_SETTINGS"
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        const val REQUEST_LOCATION = 1001
    }
}
