package com.spotexplorer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spotexplorer.data.database.dao.AllocationDao
import com.spotexplorer.model.entity.marker.MarkerEntity
import com.spotexplorer.model.entity.AllocationEntity
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.spotexplorer.data.database.converter.DateConverter
import com.spotexplorer.data.database.converter.MarkerStatusConverter
import com.spotexplorer.data.database.converter.MarkerTypeConverter
import com.spotexplorer.data.database.dao.MarkerWithNotesDao
import com.spotexplorer.data.database.dao.MarkerDao
import com.spotexplorer.data.database.dao.NoteDao
import com.spotexplorer.model.entity.NoteEntity

@Database(
    entities = [
        AllocationEntity::class,
        MarkerEntity::class,
        NoteEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MarkerStatusConverter::class, MarkerTypeConverter::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun allocationDao(): AllocationDao
    abstract fun markerDao(): MarkerDao
    abstract fun noteDao(): NoteDao
    abstract fun markerWithNotesDao(): MarkerWithNotesDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add isSynced column to guard_allocations table
                database.execSQL("ALTER TABLE guard_allocations ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0")

                // Create an index for guardAllocationId in allocation_details table
                database.execSQL("CREATE INDEX IF NOT EXISTS index_allocation_details_guardAllocationId ON allocation_details(guardAllocationId)")
            }
        }
    }
}
