package com.wifiauto.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface LearningDao {
    @Query("SELECT * FROM learning_entries WHERE ssid = :ssid")
    suspend fun getBySsid(ssid: String): List<LearningEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: LearningEntry)

    @Update
    suspend fun update(entry: LearningEntry)
}
