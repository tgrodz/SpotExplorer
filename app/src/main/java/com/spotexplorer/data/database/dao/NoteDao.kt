package com.spotexplorer.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spotexplorer.model.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(entity: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<NoteEntity>)

    @Query("SELECT * FROM notes WHERE markerId = :markerId ORDER BY timestamp DESC")
    fun getNotesForMarker(markerId: Int): Flow<List<NoteEntity>>

    @Delete
    suspend fun deleteNote(entity: NoteEntity)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}
