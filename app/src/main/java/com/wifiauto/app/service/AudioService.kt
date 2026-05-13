package com.wifiauto.app.service

import android.Manifest
import android.content.Context
import android.media.AudioManager
import android.os.Build

class AudioService : DeviceService {
    override fun enable(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL)
        }
    }

    override fun disable(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT)
        }
    }

    override fun isAvailable(): Boolean = true
    override fun getPermissionRequired(): String? = Manifest.permission.ACCESS_NOTIFICATION_POLICY
}
