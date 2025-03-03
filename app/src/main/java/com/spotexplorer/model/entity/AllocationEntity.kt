package com.spotexplorer.model.entity

import androidx.compose.runtime.Immutable
import androidx.room.*

@Immutable
@Entity(tableName = "allocations")
data class AllocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "section_id") val sectionID: Int,
    @ColumnInfo(name = "placement_url") val placementUrl: String
)

