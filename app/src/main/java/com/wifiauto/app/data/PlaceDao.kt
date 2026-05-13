package com.wifiauto.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places")
    suspend fun getAll(): List<PlaceEntity>

    @Query("SELECT * FROM places WHERE id = :id")
    suspend fun getById(id: Long): PlaceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: PlaceEntity): Long

    @Update
    suspend fun update(place: PlaceEntity)

    @Delete
    suspend fun delete(place: PlaceEntity)
}
