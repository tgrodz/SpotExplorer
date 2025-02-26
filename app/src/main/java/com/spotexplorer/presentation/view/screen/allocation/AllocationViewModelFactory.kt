package com.spotexplorer.presentation.view.screen.allocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spotexplorer.data.database.DatabaseProvider
import com.spotexplorer.presentation.view.ui.StockImage


class AllocationViewModelFactory(
    private val stockImage: StockImage
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllocationViewModel::class.java)) {
            val db = DatabaseProvider.getDatabase()
            val markerDao = db.markerDao()
            val allocationDao = db.allocationDao()
            @Suppress("UNCHECKED_CAST")
            return AllocationViewModel(stockImage, markerDao, allocationDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
