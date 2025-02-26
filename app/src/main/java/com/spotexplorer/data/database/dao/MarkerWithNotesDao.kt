package com.spotexplorer.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.spotexplorer.model.MarkerWithNotes
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkerWithNotesDao {
    @Transaction
    @Query("SELECT * FROM markers WHERE id = :markerId")
    fun getMarkerWithNotes(markerId: Int): Flow<MarkerWithNotes>
}
