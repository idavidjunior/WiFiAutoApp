package com.wifiauto.app.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wifiauto.app.databinding.ActivitySettingsBinding
import com.wifiauto.app.utils.Constants

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

        binding.switchAlertaChegada.isChecked = prefs.getBoolean(Constants.KEY_ALERTA_CHEGADA, true)
        binding.switchAlertaSaida.isChecked = prefs.getBoolean(Constants.KEY_ALERTA_SAIDA, true)
        binding.switchAlertaRua.isChecked = prefs.getBoolean(Constants.KEY_ALERTA_RUA, false)
        binding.sliderVolume.progress = prefs.getInt(Constants.KEY_VOLUME_ALERTA, 80)

        binding.btnSalvarConfig.setOnClickListener {
            prefs.edit()
                .putBoolean(Constants.KEY_ALERTA_CHEGADA, binding.switchAlertaChegada.isChecked)
                .putBoolean(Constants.KEY_ALERTA_SAIDA, binding.switchAlertaSaida.isChecked)
                .putBoolean(Constants.KEY_ALERTA_RUA, binding.switchAlertaRua.isChecked)
                .putInt(Constants.KEY_VOLUME_ALERTA, binding.sliderVolume.progress)
                .apply()
            finish()
        }
    }
}
