package com.spotexplorer.infrastructure.constants

import com.spotexplorer.model.entity.AllocationEntity
import com.spotexplorer.presentation.view.ui.StockImage

object AppConstant {
    internal const val DB_NAME = "spot_explorer_database"
    internal const val PREF_NAME = "SpotExplorerPreferences"
    const val DEFAULT_MARKER_SIZE = 100f

    val DEFAULT_ALLOCATION = AllocationEntity(
        sectionID = 1,
        placementUrl = StockImage.StockDefault.name
    )
}
