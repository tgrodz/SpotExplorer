package com.spotexplorer.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.spotexplorer.model.entity.marker.MarkerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkerDao {

    @Query("SELECT * FROM markers WHERE allocationId = :allocationId")
    suspend fun getMarkersByAllocationId(allocationId: Int): List<MarkerEntity>

    @Query("SELECT * FROM markers WHERE allocationId = :allocationId")
    fun getMarkersByAllocationIdFlow(allocationId: Int): Flow<List<MarkerEntity>>

    @Query("SELECT * FROM markers")
    suspend fun getAllMarkers(): List<MarkerEntity>

    @Query("SELECT * FROM markers WHERE id = :id")
    suspend fun getMarkerById(id: Int): MarkerEntity?

    @Query("SELECT * FROM markers WHERE name = :name")
    suspend fun getMarkerByName(name: String): MarkerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkers(allocationDetails: List<MarkerEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarker(allocationDetail: MarkerEntity): Long

    @Query("DELETE FROM markers WHERE id = :id")
    suspend fun deleteMarkerById(id: Int)

    @Query("DELETE FROM markers WHERE name = :name")
    suspend fun deleteMarkerByName(name: String)

    @Query("DELETE FROM markers")
    suspend fun deleteAllMarkers()

    @Update
    suspend fun updateMarker(allocationDetail: MarkerEntity): Int
}
