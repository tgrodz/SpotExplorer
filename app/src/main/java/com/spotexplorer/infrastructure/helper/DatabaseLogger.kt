package com.spotexplorer.infrastructure.helper

import android.util.Log
import com.spotexplorer.data.database.AppDatabase
import com.spotexplorer.data.database.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DatabaseLogger(private val appDatabase: AppDatabase) {

    fun logAllData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("DatabaseLogger", "--------------------- DATABASE LOG ---------------------")

                logAllocations()
                logMarkers()

                Log.d("DatabaseLogger", "------------------------------------------------------")
            } catch (e: Exception) {
                Log.e("DatabaseLogger", "Error logging database data", e)
            }
        }
    }

    private suspend fun logAllocations() {
        val allocations = appDatabase.allocationDao().getAllAllocations()
        if (allocations.isNotEmpty()) {
            Log.d("DatabaseLogger", "Guard Allocations:\n${allocations.joinToString("\n")}")
        } else {
            Log.d("DatabaseLogger", "No guard allocations found.")
        }
    }

    private suspend fun logMarkers() {
        val markers = appDatabase.markerDao().getAllMarkers()
        if (markers.isNotEmpty()) {
            Log.d("DatabaseLogger", "Markers:\n${markers.joinToString("\n")}")
        } else {
            Log.d("DatabaseLogger", "No markers found.")
        }
    }


    suspend fun logAllMarkerNotes() {
        //val database = DatabaseProvider.getDatabase(context, dropExisting = false)
        val database = DatabaseProvider.getDatabase()
        val markerDao = database.markerDao()
        val reviewDao = database.noteDao()

        try {
            val allMarkers = markerDao.getAllMarkers()

            if (allMarkers.isEmpty()) {
                Log.d("DatabaseLogger", "No markers found in the database.")
                return
            }

            allMarkers.forEach { marker ->
                val reviews = reviewDao.getNotesForMarker(marker.id).firstOrNull().orEmpty()
                Log.d(
                    "DatabaseLogger",
                    "Marker ID=${marker.id}, Name=${marker.name}, Reviews=${reviews.ifEmpty { "No reviews" }}"
                )
            }
        } catch (e: Exception) {
            Log.e("DatabaseLogger", "Error logging marker reviews", e)
        }
    }



}
