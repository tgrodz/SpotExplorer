package com.spotexplorer.model

import androidx.room.*
import com.spotexplorer.model.entity.marker.MarkerEntity
import com.spotexplorer.model.entity.NoteEntity

data class MarkerWithNotes(
    @Embedded val marker: MarkerEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "markerId"
    )
    val entities: List<NoteEntity>
)




