package com.wifiauto.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wifiauto.app.connectivity.ConnectivityStateMachine
import com.wifiauto.app.data.AppDatabase
import com.wifiauto.app.data.PlaceEntity
import com.wifiauto.app.databinding.ActivityMainBinding
import com.wifiauto.app.geofence.GeofenceManager
import com.wifiauto.app.service.WifiService
import com.wifiauto.app.ui.adapters.PlaceAdapter
import com.wifiauto.app.utils.PermissionHelper
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var database: AppDatabase
    private lateinit var connectivityStateMachine: ConnectivityStateMachine
    private lateinit var geofenceManager: GeofenceManager
    private lateinit var permissionHelper: PermissionHelper

    private val addPlaceLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            carregarLugares()
            geofenceManager.recriarTodasGeofences()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        geofenceManager = GeofenceManager(this)
        connectivityStateMachine = ConnectivityStateMachine(this)
        permissionHelper = PermissionHelper(this)

        configurarRecyclerView()
        configurarBotoes()
        verificarPermissoesIniciais()
        carregarLugares()
    }

    private fun configurarRecyclerView() {
        placeAdapter = PlaceAdapter(
            onEditClick = { place -> abrirEditor(place) },
            onDeleteClick = { place -> excluirLugar(place) }
        )
        binding.recyclerViewLugares.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLugares.adapter = placeAdapter
    }

    private fun configurarBotoes() {
        binding.btnAdicionarLugar.setOnClickListener {
            abrirEditor(null)
        }

        binding.btnConfiguracoes.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnPermissoes.setOnClickListener {
            startActivity(Intent(this, PermissionsActivity::class.java))
        }

        binding.radioGroupModo.setOnCheckedChangeListener { _, checkedId ->
            val modoRua = checkedId == com.wifiauto.app.R.id.radioModoUltrabasico
            connectivityStateMachine.setModoOperacao(if (modoRua) "ULTRABASICO" else "GEOFENCE")
            binding.layoutLugares.visibility = if (modoRua) View.GONE else View.VISIBLE
        }
    }

    private fun verificarPermissoesIniciais() {
        if (!permissionHelper.todasPermissoesConcedidas()) {
            Toast.makeText(this, "Configure as permissões necessárias", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, PermissionsActivity::class.java))
        }
    }

    private fun carregarLugares() {
        lifecycleScope.launch {
            val lugares = database.placeDao().getAll()
            placeAdapter.submitList(lugares)
        }
    }

    private fun abrirEditor(place: PlaceEntity?) {
        val intent = Intent(this, AddEditPlaceActivity::class.java)
        intent.putExtra("place_id", place?.id)
        addPlaceLauncher.launch(intent)
    }

    private fun excluirLugar(place: PlaceEntity) {
        lifecycleScope.launch {
            database.placeDao().delete(place)
            carregarLugares()
            geofenceManager.removerGeofence(place.id.toString())
        }
    }
}
