package com.wifiauto.app.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wifiauto.app.data.AppDatabase
import com.wifiauto.app.data.PlaceEntity
import com.wifiauto.app.databinding.ActivityAddEditPlaceBinding
import com.wifiauto.app.location.LocationManager
import kotlinx.coroutines.launch

class AddEditPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditPlaceBinding
    private lateinit var database: AppDatabase
    private var placeId: Long? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        val id = intent.getLongExtra("place_id", -1)
        placeId = if (id != -1L) id else null

        configurarSliderRaio()
        carregarLocalizacaoAtual()

        binding.btnSalvar.setOnClickListener { salvarLugar() }

        if (placeId != null) {
            carregarDadosExistentes()
        }
    }

    private fun configurarSliderRaio() {
        val raios = arrayOf("50m", "100m", "150m", "200m", "300m", "400m", "500m")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, raios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRaio.adapter = adapter
        binding.spinnerRaio.setSelection(1) // Padrão 100m
    }

    private fun carregarLocalizacaoAtual() {
        val locationManager = LocationManager(this)
        locationManager.getUltimaLocalizacao { loc ->
            latitude = loc.latitude
            longitude = loc.longitude
            binding.tvCoordenadas.text = "Lat: %.4f, Lon: %.4f".format(latitude, longitude)
        }
    }

    private fun carregarDadosExistentes() {
        lifecycleScope.launch {
            val place = database.placeDao().getById(placeId!!)
            place?.let {
                binding.etNome.setText(it.nome)
                binding.etSsid.setText(it.ssid)
                latitude = it.latitude
                longitude = it.longitude
                binding.tvCoordenadas.text = "Lat: %.4f, Lon: %.4f".format(latitude, longitude)
                // Ajustar spinner do raio se necessário
            }
        }
    }

    private fun salvarLugar() {
        val nome = binding.etNome.text.toString()
        val ssid = binding.etSsid.text.toString()
        val raioStr = binding.spinnerRaio.selectedItem.toString().replace("m", "")
        val raio = raioStr.toInt()

        if (nome.isEmpty()) return

        lifecycleScope.launch {
            val place = PlaceEntity(
                id = placeId ?: 0,
                nome = nome,
                latitude = latitude,
                longitude = longitude,
                raio = raio,
                ssid = if (ssid.isEmpty()) null else ssid
            )
            if (placeId == null) {
                database.placeDao().insert(place)
            } else {
                database.placeDao().update(place)
            }
            setResult(RESULT_OK)
            finish()
        }
    }
}
