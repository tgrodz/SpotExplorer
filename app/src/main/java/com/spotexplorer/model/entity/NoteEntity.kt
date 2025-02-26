package com.spotexplorer.model.entity

import androidx.compose.runtime.Stable
import androidx.room.*
import com.spotexplorer.model.entity.marker.MarkerEntity
import java.util.Date

@Stable
@Entity(
    tableName = "notes",
    foreignKeys = [ForeignKey(
        entity = MarkerEntity::class,
        parentColumns = ["id"],
        childColumns = ["markerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["markerId"])]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val markerId: Int,
    val title: String,
    val rating: Float,
    val comment: String,
    val timestamp: Date
)





