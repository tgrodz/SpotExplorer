package com.spotexplorer.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Stable
data class SquareInfo(
    val markerId: Int,
    val name: String,
    val xPosition: Float,
    val yPosition: Float,
    val color: Color,
    val offset: Offset,
    val size: Float
)
