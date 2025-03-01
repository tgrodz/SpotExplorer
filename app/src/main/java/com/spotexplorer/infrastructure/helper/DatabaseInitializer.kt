package com.spotexplorer.infrastructure.helper

import android.util.Log
import com.spotexplorer.data.database.AppDatabase
import com.spotexplorer.model.entity.AllocationEntity
import com.spotexplorer.model.entity.marker.MarkerEntity

object DatabaseInitializer {
    suspend fun initializeMarkersByAllocation(database: AppDatabase) {
        val allocationDao = database.allocationDao()
        val markerDao = database.markerDao()

        val defaultAllocation = AllocationEntity(
            sectionID = 0,
            placementUrl = "default_url"
        )
        val allocationId = allocationDao.insertAllocation(defaultAllocation)

        val markerEntities = MockData.markerLists.map { marker ->
            MarkerEntity(
                allocationId = allocationId.toInt(),
                name = marker.name,
                xPoint = marker.xPoint.toFloat(),
                yPoint = marker.yPoint.toFloat(),
                pixelSequentialNumberPoint = marker.pixelSequentialNumberPoint,
                status = marker.status,
                type = marker.type,
                hasPublicTransport = marker.hasPublicTransport,
                hasFurniture = marker.hasFurniture,
                hasBalcony = marker.hasBalcony
            )
        }
        markerDao.insertMarkers(markerEntities)
    }

    suspend fun initializeNotes(database: AppDatabase) {
        val noteDao = database.noteDao()
        val markerDao = database.markerDao()

        val existingMarkers = markerDao.getAllMarkers()
        Log.d("DB_CHECK", "Existing markers before inserting notes: ${existingMarkers.map { it.id }}")

        val validNotes = MockData.notesLists.filter { note ->
            existingMarkers.any { marker -> marker.id == note.markerId }
        }

        if (validNotes.isEmpty()) {
            Log.e("DB_ERROR", "No valid markers found for notes. Notes cannot be inserted.")
            return
        }

        Log.d("DB_INIT", "Inserting ${validNotes.size} notes with valid markers")
        noteDao.insertAll(validNotes)
    }
}
