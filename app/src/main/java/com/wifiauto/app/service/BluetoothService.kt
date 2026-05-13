package com.wifiauto.app.service

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class BluetoothService : DeviceService {
    override fun enable(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            val adapter = BluetoothAdapter.getDefaultAdapter()
            adapter?.enable()
        }
    }

    override fun disable(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            val adapter = BluetoothAdapter.getDefaultAdapter()
            adapter?.disable()
        }
    }

    override fun isAvailable(): Boolean = BluetoothAdapter.getDefaultAdapter() != null
    override fun getPermissionRequired(): String? = Manifest.permission.BLUETOOTH_CONNECT
}
