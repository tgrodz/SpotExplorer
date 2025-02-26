package com.spotexplorer.presentation.view.screen.allocation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.viewModelScope
import com.spotexplorer.data.database.DatabaseProvider
import com.spotexplorer.data.database.dao.AllocationDao
import com.spotexplorer.data.database.dao.MarkerDao
import com.spotexplorer.model.entity.marker.MarkerEntity
import com.spotexplorer.model.AllocationWithMarkers
import com.spotexplorer.infrastructure.App
import com.spotexplorer.infrastructure.constants.AppConstant.DEFAULT_ALLOCATION
import com.spotexplorer.infrastructure.constants.AppConstant.DEFAULT_MARKER_SIZE
import com.spotexplorer.infrastructure.helper.DatabaseInitializer
import com.spotexplorer.model.entity.marker.MarkerStatus
import com.spotexplorer.model.entity.marker.MarkerType
import com.spotexplorer.model.SquareInfo
import com.spotexplorer.presentation.shared.BaseViewModel
import com.spotexplorer.presentation.view.ui.StockImage
import com.spotexplorer.presentation.view.ui.theme.FreeReviewColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

class AllocationViewModel(
    private val stockImage: StockImage = StockImage.StockDefault,
    private val markerDao: MarkerDao,
    private val allocationDao: AllocationDao
) : BaseViewModel() {

    private val _allocation = MutableStateFlow<AllocationWithMarkers?>(null)
    val allocation: StateFlow<AllocationWithMarkers?> get() = _allocation

    private val _markers = MutableStateFlow<List<MarkerEntity>>(emptyList())
    val markers: StateFlow<List<MarkerEntity>> get() = _markers

    val imageBitmap = mutableStateOf<ImageBitmap?>(null)
    private var bitmap: Bitmap? = null
    private var originalBitmap: Bitmap? = null

    private val _databaseResetState = MutableStateFlow(false)
    val databaseResetState = _databaseResetState.asStateFlow()

    private val _markerSize = MutableStateFlow(DEFAULT_MARKER_SIZE)
    val markerSize: StateFlow<Float> get() = _markerSize

    val shouldDrawMarkers = mutableStateOf(true)

    init {
        fetchAllocation()
        loadImage()
    }

    private fun loadImage() {
        launchInBackground {
            val loadedBitmap = BitmapFactory.decodeResource(
                App.instance.resources,
                stockImage.drawableResId
            )?.copy(Bitmap.Config.ARGB_8888, true)

            loadedBitmap?.let {
                bitmap = it
                originalBitmap = it.copy(Bitmap.Config.ARGB_8888, true)
                withContext(Dispatchers.Main) {
                    imageBitmap.value = it.asImageBitmap()
                }
            }
        }
    }


    fun getImageDetails(): Map<String, String> {
        val bmp = bitmap ?: return emptyMap()
        val width = bmp.width
        val height = bmp.height
        val totalPixels = width.toLong() * height.toLong()

        return mapOf(
            "Name" to "My City",
            "Width" to "${formatNumberWithSpaces(width.toLong())} px",
            "Height" to "${formatNumberWithSpaces(height.toLong())} px",
            "Total Pixels" to "${formatNumberWithSpaces(totalPixels)} px",
            "Format" to "Bitmap"
        )
    }

    private fun formatNumberWithSpaces(number: Long): String {
        return NumberFormat.getNumberInstance(Locale.US).format(number).replace(",", " ")
    }

    private fun fetchAllocation() {
        launchInBackground {
            val allocations = allocationDao.getAllAllocations()
            if (allocations.isNotEmpty()) {
                _allocation.value = allocationDao.getAllocationWithDetails(allocations.first().id)
            } else {
                Log.e("AllocationViewModel", "No allocations found in database.")
            }
        }
    }

    fun mapAllocationsToSquares(): Flow<List<SquareInfo>> {
        if (!shouldDrawMarkers.value) return flowOf(emptyList())

        return _markers.map { markerList -> markerList.map { mapMarkerToSquare(it) } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    }

    private fun mapMarkerToSquare(marker: MarkerEntity): SquareInfo {
        return SquareInfo(
            markerId = marker.id,
            name = marker.name,
            xPosition = marker.xPoint,
            yPosition = marker.yPoint,
            color = getColorForMarker(marker.status),
            offset = calculateCenteredOffset(marker.xPoint, marker.yPoint),
            size = DEFAULT_MARKER_SIZE
        )
    }

    private fun getColorForMarker(status: MarkerStatus): Color {
        return when (status) {
            MarkerStatus.RED -> Color.Red
            MarkerStatus.YELLOW -> FreeReviewColor
            MarkerStatus.GREEN -> Color.Green
        }
    }

    private fun calculateCenteredOffset(x: Float, y: Float): Offset {
        return Offset(x - 50, y - 50)
    }


    fun refreshMarkers() {
        launchInBackground {
            val allocationId = _allocation.value?.allocationResult?.id ?: DEFAULT_ALLOCATION.id
            _markers.value = markerDao.getMarkersByAllocationId(allocationId)
            _allocation.value = allocationDao.getAllocationWithDetails(allocationId)
        }
    }

    fun addNewMarker(position: Offset, markerName: String) {
        launchInBackground {
            val allocationId = _allocation.value?.allocationResult?.id ?: DEFAULT_ALLOCATION.id
            val newMarker = MarkerEntity(
                name = markerName,
                allocationId = allocationId,
                xPoint = position.x,
                yPoint = position.y,
                status = MarkerStatus.YELLOW,
                type = MarkerType.UNKNOWN,
                pixelSequentialNumberPoint = Random.nextInt(1, 10001),
                hasPublicTransport = false,
                hasFurniture = false,
                hasBalcony = false,
            )
            markerDao.insertMarker(newMarker)
            withContext(Dispatchers.Main) {
                refreshMarkers()
            }
        }
    }

    fun clearTablesAndReinitializeMarkers() {
        launchInBackground {
            markerDao.deleteAllMarkers()
            _markers.value = emptyList()
            _databaseResetState.value = false

            val db = DatabaseProvider.getDatabase()
            DatabaseInitializer.initializeMarkersByAllocation(db)
            DatabaseInitializer.initializeNotes(db)

            _databaseResetState.value = true
            refreshMarkers()
        }
    }
}
