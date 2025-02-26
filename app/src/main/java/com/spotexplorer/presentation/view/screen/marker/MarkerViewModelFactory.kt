package com.spotexplorer.presentation.view.screen.marker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MarkerViewModelFactory(
    private val markerId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarkerViewModel::class.java)) {
            return MarkerViewModel().apply {
                loadMarkerById(markerId)
            } as T as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
