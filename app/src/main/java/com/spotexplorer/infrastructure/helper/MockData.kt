package com.spotexplorer.infrastructure.helper

import android.util.Log
import com.spotexplorer.model.entity.marker.MarkerEntity
import com.spotexplorer.model.entity.NoteEntity
import com.spotexplorer.model.entity.marker.MarkerStatus
import com.spotexplorer.model.entity.marker.MarkerType
import java.util.Date

object MockData {

    val markerLists = listOf(
        MarkerEntity(
            id = 0,
            allocationId = 1,
            name = "Marker01",
            xPoint = 150f,
            yPoint = 170f,
            pixelSequentialNumberPoint = 1001,
            status = MarkerStatus.YELLOW,
            type = MarkerType.UNKNOWN,
            hasPublicTransport = true,
            hasFurniture = false,
            hasBalcony = false
        ),
        MarkerEntity(
            id = 0,
            allocationId = 1,
            name = "Marker02",
            xPoint = 350f,
            yPoint = 450f,
            pixelSequentialNumberPoint = 1002,
            status = MarkerStatus.GREEN,
            type = MarkerType.UNKNOWN,
            hasPublicTransport = true,
            hasFurniture = true,
            hasBalcony = true
        ),
        MarkerEntity(
            id = 0,
            allocationId = 1,
            name = "Marker03",
            xPoint = 700f,
            yPoint = 1150f,
            pixelSequentialNumberPoint = 1003,
            status = MarkerStatus.RED,
            type = MarkerType.UNKNOWN,
            hasPublicTransport = true,
            hasFurniture = false,
            hasBalcony = false
        ),
        MarkerEntity(
            0,
            1,
            "Marker05",
            1150f,
            1150f,
            1004,
            MarkerStatus.GREEN,
            MarkerType.UNKNOWN,
            true,
            true,
            true
        ),
        MarkerEntity(
            0,
            1,
            name = "Marker06",
            xPoint = 1400f,
            yPoint = 715f,
            1005,
            MarkerStatus.YELLOW,
            MarkerType.UNKNOWN,
            true,
            true,
            true
        ),
        MarkerEntity(
            0,
            1,
            name = "Marker07",
            xPoint = 1934f,
            yPoint = 680f,
            1006,
            MarkerStatus.GREEN,
            MarkerType.UNKNOWN,
            true,
            true,
            true
        ),
        MarkerEntity(
            0,
            1,
            name = "Marker08",
            xPoint = 2450f,
            yPoint = 2417f,
            1008,
            MarkerStatus.YELLOW,
            MarkerType.UNKNOWN,
            true,
            false,
            true
        ),
        MarkerEntity(
            0,
            1,
            name = "Marker09",
            xPoint = 2050f,
            yPoint = 2017f,
            1009,
            MarkerStatus.RED,
            MarkerType.UNKNOWN,
            true,
            true,
            true
        ),
        MarkerEntity(
            0,
            1,
            name = "Marker Copy Marker09",
            xPoint = 1750f,
            yPoint = 245f,
            1009,
            MarkerStatus.RED,
            MarkerType.UNKNOWN,
            true,
            true,
            true
        ),
    )

    val notesLists = listOf(
        NoteEntity(
            id = 0,
            markerId = 1,
            title = "Note01",
            rating = 0f,
            comment = "Simple text 01",
            timestamp = Date(),
        ),
        NoteEntity(
            id = 0,
            markerId = 1,
            title = "Note02",
            rating = 0f,
            comment = "Simple text 02",
            timestamp = Date(),
        ),
        NoteEntity(
            id = 0,
            markerId = 1,
            title = "Note03",
            rating = 0f,
            comment = "Simple text 03",
            timestamp = Date(),
        ),
        NoteEntity(
            id = 0,
            markerId = 2,
            title = "Note04",
            rating = 0f,
            comment = "Simple text 04",
            timestamp = Date(),
        ),
        NoteEntity(
            id = 0,
            markerId = 2,
            title = "Note05",
            rating = 0f,
            comment = "Simple text 05",
            timestamp = Date(),
        ),
        NoteEntity(
            id = 0,
            markerId = 3,
            title = "Note06",
            rating = 0f,
            comment = "Simple text 06",
            timestamp = Date(),
        ),
    )

    fun generateDefaultMarkers(): List<MarkerEntity> {
        val markers = (1..10).map { index ->
            Thread.sleep(25) // Small delay to vary data
            MarkerEntity(
                id = 0,
                allocationId = 1,
                name = "Marker$index",
                xPoint = (100..2000).random().toFloat(),
                yPoint = (100..2000).random().toFloat(),
                pixelSequentialNumberPoint = 1000 + index,
                status = listOf(MarkerStatus.YELLOW, MarkerStatus.GREEN, MarkerStatus.RED).random(),
                type = MarkerType.UNKNOWN,
                hasPublicTransport = (0..1).random() == 1,
                hasFurniture = (0..1).random() == 1,
                hasBalcony = (0..1).random() == 1
            )
        }

        Log.d("DB_INIT", "Generated ${markers.size} default markers.")
        if (markers.isEmpty()) {
            Log.e("DB_ERROR", "generateDefaultMarkers() returned an empty list!")
        }

        return markers
    }


    fun generateDefaultNotes(): List<NoteEntity> {
        val notes = (1..3).flatMap { markerId ->
            (1..5).map { i ->
                Thread.sleep(25) // Small delay to vary timestamps
                NoteEntity(
                    id = 0,
                    markerId = markerId,
                    title = "Note${markerId}-$i",
                    rating = 0f,
                    comment = "Simple text $i for markerId=$markerId",
                    timestamp = Date()
                )
            }
        }

        Log.d("DB_INIT", "Generated ${notes.size} default notes.")
        if (notes.isEmpty()) {
            Log.e("DB_ERROR", "generateDefaultNotes() returned an empty list!")
        }

        return notes
    }

}