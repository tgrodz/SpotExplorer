package com.spotexplorer.presentation.navigation

import android.net.Uri

sealed class Route(val route: String) {
    object Settings : Route("settings_screen")
    object Allocation : Route("allocation_screen")
    object MarkerPositionDetail : Route("position_detail/{id}/{title}/{status}/{content}") {
        fun createRoute(id: Int, title: String, status: String, content: String): String {
            val encodedTitle = Uri.encode(title)
            val encodedStatus = Uri.encode(status)
            val encodedContent = Uri.encode(content)
            return "position_detail/$id/$encodedTitle/$encodedStatus/$encodedContent"
        }
    }
}
