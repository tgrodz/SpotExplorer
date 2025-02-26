package com.spotexplorer.data.database.converter

import androidx.room.TypeConverter
import com.spotexplorer.model.entity.marker.MarkerStatus

class MarkerStatusConverter {

    @TypeConverter
    fun fromMarkerStatus(status: MarkerStatus?): String? {
        return status?.value
    }

    @TypeConverter
    fun toMarkerStatus(status: String?): MarkerStatus? {
        return status?.let {
            MarkerStatus.values().find { markerStatus -> markerStatus.value == it }
        }
    }
}
