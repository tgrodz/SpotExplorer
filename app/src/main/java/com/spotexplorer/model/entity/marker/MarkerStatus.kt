package com.spotexplorer.model.entity.marker

import androidx.compose.runtime.Immutable

@Immutable
enum class MarkerStatus(val value: String) {
    RED("red"),
    YELLOW("yellow"),
    GREEN("green");

    fun displayStatusText(): String {
        return when (this) {
            RED -> "Busy"
            YELLOW -> "Free-Review"
            GREEN -> "Free-Full"
        }
    }
}
