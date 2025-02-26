package com.spotexplorer.data.database.converter


import androidx.room.TypeConverter
import com.spotexplorer.model.entity.marker.MarkerType

class MarkerTypeConverter {

    @TypeConverter
    fun fromMarkerType(type: MarkerType): String {
        return type.name
    }

    @TypeConverter
    fun toMarkerType(value: String): MarkerType {
        return try {
            MarkerType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            MarkerType.UNKNOWN
        }
    }
}
