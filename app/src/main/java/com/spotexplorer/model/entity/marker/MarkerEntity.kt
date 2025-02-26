package com.spotexplorer.model.entity.marker

import androidx.compose.runtime.Stable
import androidx.room.*
import com.spotexplorer.model.entity.AllocationEntity

@Stable
@Entity(
    tableName = "markers",
    foreignKeys = [ForeignKey(
        entity = AllocationEntity::class,
        parentColumns = ["id"],
        childColumns = ["allocationId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["allocationId"])]
)
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "allocationId") val allocationId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "pixel_x_point") val xPoint: Float,
    @ColumnInfo(name = "pixel_y_point") val yPoint: Float,
    @ColumnInfo(name = "pixel_sequential_number_point") val pixelSequentialNumberPoint: Int,

    @ColumnInfo(name = "status") val status: MarkerStatus,
    @ColumnInfo(name = "type") val type: MarkerType,

    @ColumnInfo(name = "hasPublicTransport") val hasPublicTransport: Boolean,
    @ColumnInfo(name = "hasFurniture") val hasFurniture: Boolean,
    @ColumnInfo(name = "hasBalcony") val hasBalcony: Boolean,

    val isSynced: Boolean = false
) {
    fun getCurrentStatus(): MarkerStatus {
        return if (status == MarkerStatus.RED) {
            MarkerStatus.RED
        } else {
            if (hasAdditionalOptions()) MarkerStatus.GREEN else MarkerStatus.YELLOW
        }
    }

    private fun hasAdditionalOptions(): Boolean = hasPublicTransport && hasFurniture && hasBalcony
}

