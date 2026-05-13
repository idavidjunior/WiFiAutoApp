package com.wifiauto.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learning_entries")
data class LearningEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ssid: String,
    val latitude: Double,
    val longitude: Double,
    val contagem: Int = 1,
    val sugerido: Boolean = false
)
