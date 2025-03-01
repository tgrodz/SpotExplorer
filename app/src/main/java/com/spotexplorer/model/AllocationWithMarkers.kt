package com.spotexplorer.model

import androidx.room.Embedded
import androidx.room.Relation
import com.spotexplorer.model.entity.AllocationEntity
import com.spotexplorer.model.entity.marker.MarkerEntity

data class AllocationWithMarkers(
    @Embedded val allocationResult: AllocationEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "allocation_id"
    )
    val allocationDetails: List<MarkerEntity> = emptyList()
)

