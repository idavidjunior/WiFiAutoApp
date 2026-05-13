package com.wifiauto.app.service

import android.content.Context

interface DeviceService {
    fun enable(context: Context)
    fun disable(context: Context)
    fun isAvailable(): Boolean
    fun getPermissionRequired(): String?
}
