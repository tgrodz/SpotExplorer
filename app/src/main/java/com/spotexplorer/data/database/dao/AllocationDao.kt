package com.spotexplorer.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.spotexplorer.model.entity.AllocationEntity
import com.spotexplorer.model.AllocationWithMarkers
import kotlinx.coroutines.flow.Flow

@Dao
interface AllocationDao {

    @Query("SELECT * FROM allocations")
    suspend fun getAllAllocations(): List<AllocationEntity>

    @Query("SELECT * FROM allocations WHERE id = :id")
    suspend fun getAllocationById(id: Int): AllocationEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllocation(guardAllocation: AllocationEntity): Long

    @Transaction
    @Query("SELECT * FROM allocations WHERE id = :id")
    suspend fun getAllocationWithDetails(id: Int): AllocationWithMarkers?

    @Transaction
    @Query("SELECT * FROM allocations")
    suspend fun getAllGuardAllocationsWithDetails(): List<AllocationWithMarkers>

    @Query("SELECT * FROM allocations WHERE isSynced = 0")
    suspend fun getUnsyncedAllocations(): List<AllocationEntity>

    @Query("UPDATE allocations SET isSynced = 1 WHERE id = :id")
    suspend fun markAllocationAsSynced(id: Int)

    @Query("DELETE FROM allocations")
    suspend fun deleteAllAllocations()

    @Transaction
    @Query("SELECT * FROM allocations")
    fun getAllAllocationsWithDetailsFlow(): Flow<List<AllocationWithMarkers>>
}
