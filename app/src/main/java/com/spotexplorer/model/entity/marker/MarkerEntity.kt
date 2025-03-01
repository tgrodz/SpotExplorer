package com.spotexplorer.model.entity.marker

import androidx.compose.runtime.Stable
import androidx.room.*
import com.spotexplorer.model.entity.AllocationEntity


/**
 * The MarkerEntity model represents a marker in the application.
 *
 * This is a rich model, meaning an object that not only stores data but also contains business logic.
 * This approach aligns with the concept of a rich domain model in DDD,
 * where data and logic are encapsulated together in a single object.
 */
@Stable
@Entity(
    tableName = "markers",
    foreignKeys = [ForeignKey(
        entity = AllocationEntity::class,
        parentColumns = ["id"],
        childColumns = ["allocation_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["allocation_id"])]
)
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "allocation_id") val allocationId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "x_point") val xPoint: Float,
    @ColumnInfo(name = "y_point") val yPoint: Float,
    @ColumnInfo(name = "sequential_number_point") val pixelSequentialNumberPoint: Int,

    @ColumnInfo(name = "status") val status: MarkerStatus,
    @ColumnInfo(name = "type") val type: MarkerType,

    @ColumnInfo(name = "has_public_transport") val hasPublicTransport: Boolean,
    @ColumnInfo(name = "has_furniture") val hasFurniture: Boolean,
    @ColumnInfo(name = "has_balcony") val hasBalcony: Boolean,

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

