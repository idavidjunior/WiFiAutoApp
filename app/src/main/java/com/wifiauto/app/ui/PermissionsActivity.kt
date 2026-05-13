package com.wifiauto.app.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.wifiauto.app.databinding.ActivityPermissionsBinding
import com.wifiauto.app.utils.PermissionHelper

class PermissionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionsBinding
    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionHelper = PermissionHelper(this)

        binding.btnPermissaoLocalizacao.setOnClickListener {
            requestPermissions(PermissionHelper.LOCATION_PERMISSIONS, PermissionHelper.REQUEST_LOCATION)
        }

        binding.btnOtimizacaoBateria.setOnClickListener {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }

        binding.btnPermissaoAdb.setOnClickListener {
            // Instruções para o usuário
        }

        atualizarStatus()
    }

    override fun onResume() {
        super.onResume()
        atualizarStatus()
    }

    private fun atualizarStatus() {
        val locOk = permissionHelper.hasLocationPermission()
        val batteryOk = permissionHelper.hasBatteryOptimizationDisabled()
        val adbOk = permissionHelper.hasAdbPermission()

        binding.ivStatusLocalizacao.setImageResource(if (locOk) android.R.drawable.presence_online else android.R.drawable.presence_busy)
        binding.ivStatusBateria.setImageResource(if (batteryOk) android.R.drawable.presence_online else android.R.drawable.presence_busy)
        binding.ivStatusAdb.setImageResource(if (adbOk) android.R.drawable.presence_online else android.R.drawable.presence_busy)
    }
}
