package com.spotexplorer.presentation.view.screen.marker

import com.spotexplorer.data.database.DatabaseProvider
import com.spotexplorer.model.entity.marker.MarkerEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.spotexplorer.presentation.shared.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MarkerViewModel() : BaseViewModel() {

    private val _markerDetails = MutableStateFlow<MarkerEntity?>(null)
    val markerDetails: StateFlow<MarkerEntity?> = _markerDetails

    private val appDatabase = DatabaseProvider.getDatabase()
    private val markersDao = appDatabase.markerDao()

    fun loadMarkerById(id: Int) {
        launchInBackground {
            _markerDetails.value = markersDao.getMarkerById(id)
        }
    }

    fun updateMarker(updatedMarker: MarkerEntity) {
        launchInBackground {
            markersDao.updateMarker(updatedMarker)
            _markerDetails.value = updatedMarker
        }
    }

    fun deleteMarkerById(id: Int, onSuccess: () -> Unit) {
        launchInBackground {
            markersDao.deleteMarkerById(id)
            _markerDetails.value = null
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }


}
