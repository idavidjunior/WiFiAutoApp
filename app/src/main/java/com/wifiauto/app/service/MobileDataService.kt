package com.wifiauto.app.service

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import android.telephony.TelephonyManager

class MobileDataService : DeviceService {

    override fun enable(context: Context) {
        if (hasAdbPermission(context)) {
            setMobileDataEnabled(context, true)
        } else {
            fallbackToSettingsPanel(context)
        }
    }

    override fun disable(context: Context) {
        if (hasAdbPermission(context)) {
            setMobileDataEnabled(context, false)
        } else {
            fallbackToSettingsPanel(context)
        }
    }

    override fun isAvailable(): Boolean {
        return true
    }

    override fun getPermissionRequired(): String? {
        return "WRITE_SECURE_SETTINGS"
    }

    private fun hasAdbPermission(context: Context): Boolean {
        return context.checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS") ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun fallbackToSettingsPanel(context: Context) {
        try {
            val intent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    private fun setMobileDataEnabled(context: Context, enabled: Boolean) {
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val method = telephonyManager.javaClass.getDeclaredMethod("setDataEnabled", Boolean::class.javaPrimitiveType)
            method.invoke(telephonyManager, enabled)
        } catch (e: Exception) {
            fallbackToSettingsPanel(context)
        }
    }
}
