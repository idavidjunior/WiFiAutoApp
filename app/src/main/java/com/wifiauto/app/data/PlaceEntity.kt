package com.wifiauto.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val latitude: Double,
    val longitude: Double,
    val raio: Int = 100,
    val ssid: String? = null,
    val aprendiz: Boolean = false
)
