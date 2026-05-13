package com.wifiauto.app.service

import android.content.Context
import android.content.Intent
import android.provider.Settings

class WifiService : DeviceService {
    override fun enable(context: Context) {
        val intent = Intent(Settings.Panel.ACTION_WIFI)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun disable(context: Context) {
        val intent = Intent(Settings.Panel.ACTION_WIFI)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun isAvailable(): Boolean = true
    override fun getPermissionRequired(): String? = null
}
