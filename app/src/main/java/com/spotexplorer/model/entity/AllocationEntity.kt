package com.spotexplorer.model.entity

import androidx.compose.runtime.Immutable
import androidx.room.*

@Immutable
@Entity(tableName = "allocations")
data class AllocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sectionID: Int,
    val placementUrl: String
)

