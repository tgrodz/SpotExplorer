package com.spotexplorer.presentation.view.screen.allocation

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.spotexplorer.R
import com.spotexplorer.infrastructure.constants.AllocationScreenText
import com.spotexplorer.model.entity.marker.MarkerEntity
import com.spotexplorer.model.entity.marker.MarkerStatus
import com.spotexplorer.model.SquareInfo
import com.spotexplorer.presentation.navigation.Route
import com.spotexplorer.presentation.view.ui.StockImage
import com.spotexplorer.presentation.shared.SettingsSharedViewModel
import com.spotexplorer.presentation.view.ui.theme.AppBackground



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllocationScreen(
    navController: NavHostController,
    selectedStock: StockImage,
    settingsSharedViewModel: SettingsSharedViewModel
) {
    val viewModel: AllocationViewModel = viewModel(
        factory = AllocationViewModelFactory(selectedStock)
    )

    val context = LocalContext.current

    var showExitDialog by remember { mutableStateOf(false) }
    BackHandler { showExitDialog = true }

    var showDialog by remember { mutableStateOf(false) }

    // State for the selected marker (for animation)
    var selectedSquare by remember { mutableStateOf<SquareInfo?>(null) }
    var imageDetails by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    // States for new marker creation
    var newMarkerPosition by remember { mutableStateOf<Offset?>(null) }
    var showAddMarkerDialog by remember { mutableStateOf(false) }
    var newMarkerName by remember { mutableStateOf("") }

    val markerSize by settingsSharedViewModel.markerSize.collectAsState()
    val imageBitmap by viewModel.imageBitmap

    val allocationData by viewModel.allocation.collectAsState()
    val squares by viewModel.mapAllocationsToSquares().collectAsState(initial = emptyList())

    // Observe settings from settingsSharedViewModel
    val darkenImage by settingsSharedViewModel.enableImageDarker.collectAsState()
    val useVibration by settingsSharedViewModel.useHapticEffect.collectAsState()
    val enableAnimation by settingsSharedViewModel.enableAnimation.collectAsState()
    val animationOption by settingsSharedViewModel.animationOption.collectAsState()
    val showText by settingsSharedViewModel.isShowText.collectAsState()
    val isDrawDashRoute by settingsSharedViewModel.useRouteLine.collectAsState()

    val allCoordinates = allocationData?.allocationDetails ?: emptyList()
    val totalMarkersCount = allocationData?.allocationDetails?.size ?: 0

    var showContextMenu by remember { mutableStateOf(false) }


    // Simulate loading delay
    val isLoading = produceState(initialValue = true) {
        kotlinx.coroutines.delay(600)
        value = false
    }


    LaunchedEffect(newMarkerPosition) {
        if (newMarkerPosition != null) {
            newMarkerName = "New Marker ${allCoordinates.size + 1}"
        }
    }

    LaunchedEffect(imageBitmap) {
        if (imageBitmap != null) {
            imageDetails = viewModel.getImageDetails()
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        Log.d("AllocationScreen", "LaunchedEffect: Refreshing markers")
        viewModel.refreshMarkers()
    }

    SideEffect {
        Log.d("AllocationScreen", "LaunchedEffect: totalMarkersCount is $totalMarkersCount")
    }

    DisposableEffect(imageBitmap) {
        if (imageBitmap != null) {
            viewModel.refreshMarkers()
        }
        onDispose { }
    }

    fun onMarkerSelected(marker: MarkerEntity) {
        val matchingSquare = squares.firstOrNull { square ->
            square.markerId == marker.id
        }
        selectedSquare = matchingSquare
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(AllocationScreenText.CONFIRMATION_REQUIRED) },
            text = { Text(AllocationScreenText.EXIT_DIALOG_TEXT) },
            confirmButton = {
                Button(onClick = {
                    showExitDialog = false
                    navController.popBackStack()
                }) { Text(AllocationScreenText.YES) }
            },
            dismissButton = {
                Button(onClick = { showExitDialog = false }) { Text(AllocationScreenText.NO) }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        if (isLoading.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                Text(AllocationScreenText.LOADING_TEXT, color = Color.White, fontSize = 20.sp)
            }
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = AllocationScreenText.ALLOCATION_TITLE,
                                    color = Color.White,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = AppBackground),
                        actions = {
                            IconButton(onClick = { showContextMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = AllocationScreenText.MENU,
                                    tint = Color.White
                                )
                            }
                            DropdownMenu(
                                expanded = showContextMenu,
                                onDismissRequest = { showContextMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(AllocationScreenText.OPEN_SETTINGS) },
                                    onClick = {
                                        navController.navigate(Route.Settings.route)
                                        showContextMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(AllocationScreenText.CLEAN_SELECTION) },
                                    onClick = {
                                        selectedSquare = null
                                        showContextMenu = false
                                    }
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(AllocationScreenText.ARRANGEMENT_SCREEN, color = Color.White)
                    Spacer(modifier = Modifier.height(80.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .background(Color.Red)
                            .border(4.dp, Color.Gray)
                            .clipToBounds()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        imageBitmap?.let { imgBitmap ->
                            SpotAllocator(
                                imageBitmap = imgBitmap,
                                squares = squares,
                                selectedSquare = selectedSquare,
                                markerSize = markerSize,
                                useOverlay = darkenImage,
                                useVibration = useVibration,
                                useAnimation = enableAnimation,
                                animationOption = animationOption,
                                showText = showText,
                                showDashRoute = isDrawDashRoute,
                                onSquareClick = { square, tapOffset, gesture ->
                                    if (square != null) {
                                        selectedSquare = square
                                        showDialog = true
                                    } else if (tapOffset != null) {
                                        when (gesture) {
                                            "long" -> {
                                                newMarkerPosition = tapOffset
                                                showAddMarkerDialog = true
                                            }
                                            "tap" -> {
                                                Toast.makeText(
                                                    context,
                                                    AllocationScreenText.YOU_CLICKED_EMPTY,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            )
                        } ?: CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    Text(AllocationScreenText.ALL_MARKERS_LABEL + totalMarkersCount)
                    Text(AllocationScreenText.MARKER_SIZE_LABEL + "$markerSize px.", fontSize = 12.sp)

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppBackground),
                        contentPadding = paddingValues
                    ) {
                        item {
                            ImageDetailsSection(imageDetails = imageDetails)
                        }
                        items(allCoordinates) { marker ->
                            ListOfMarkersCard(
                                marker = marker,
                                onMarkerSelected = { onMarkerSelected(it) },
                                navController = navController,
                                dialogTitle = "",
                                dialogContent = ""
                            )
                        }
                    }

                    if (showAddMarkerDialog) {
                        AlertDialog(
                            onDismissRequest = { showAddMarkerDialog = false },
                            title = { Text(AllocationScreenText.ADD_MARKER_TITLE) },
                            text = {
                                Column {
                                    Text(
                                        AllocationScreenText.POSITION_LABEL +
                                                "${newMarkerPosition?.x?.toInt()}, " +
                                                "${newMarkerPosition?.y?.toInt()}"
                                    )
                                    OutlinedTextField(
                                        value = newMarkerName,
                                        onValueChange = { newMarkerName = it },
                                        label = { Text(AllocationScreenText.MARKER_NAME_LABEL) }
                                    )
                                }
                            },
                            confirmButton = {
                                Button(onClick = {
                                    newMarkerPosition?.let { pos ->
                                        viewModel.addNewMarker(pos, newMarkerName)
                                    }
                                    showAddMarkerDialog = false
                                }) {
                                    Text(AllocationScreenText.ADD_BUTTON)
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showAddMarkerDialog = false }) {
                                    Text(AllocationScreenText.CANCEL_RU)
                                }
                            }
                        )
                    }

                    MarkerDetailsDialog(
                        showDialog = showDialog,
                        selectedSquare = selectedSquare,
                        markers = allCoordinates,
                        navController = navController,
                        onDismiss = { showDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun ImageDetailsSection(imageDetails: Map<String, String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground)
            .padding(8.dp)
    ) {
        Text(
            AllocationScreenText.IMAGE_INFORMATION,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (imageDetails.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppBackground),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    imageDetails.forEach { (key, value) ->
                        Text(
                            text = "$key: $value",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        } else {
            Text(
                AllocationScreenText.IMAGE_NOT_LOADED,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ListOfMarkersCard(
    marker: MarkerEntity,
    onMarkerSelected: (MarkerEntity) -> Unit,
    navController: NavHostController,
    dialogTitle: String,
    dialogContent: String
) {

    val computedStatus = marker.getCurrentStatus()
    val cardColor = when (computedStatus) {
        MarkerStatus.RED -> Color(0xFFb52220)
        MarkerStatus.GREEN -> Color(0xFF9cc418)
        MarkerStatus.YELLOW -> Color(0xFFFFC107)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onMarkerSelected(marker) },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pin_location_green),
                contentDescription = "Marker Icon",
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 8.dp)
            )
            Column {
                Text(
                    AllocationScreenText.NAME_LABEL + marker.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                Text(
                    String.format(
                        AllocationScreenText.COORDINATES_LABEL,
                        marker.xPoint.toInt(),
                        marker.yPoint.toInt()
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                Text(
                    AllocationScreenText.STATUS_LABEL + computedStatus.displayStatusText(),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                Text(
                    AllocationScreenText.PIXEL_INDEX_LABEL + marker.pixelSequentialNumberPoint,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun MarkerDetailsDialog(
    showDialog: Boolean,
    selectedSquare: SquareInfo?,
    markers: List<MarkerEntity>,
    navController: NavHostController,
    onDismiss: () -> Unit
) {
    if (!showDialog) return

    val matchingMarker = markers.firstOrNull { it.id == selectedSquare?.markerId }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = selectedSquare?.let { AllocationScreenText.MARKER_DETAILS_TITLE }
                    ?: AllocationScreenText.NO_MARKER_FOUND
            )
        },
        text = {
            if (matchingMarker != null) {
                Text(
                    AllocationScreenText.NAME_LABEL + matchingMarker.name + "\n" +
                            "X: " + matchingMarker.xPoint.toInt() + "," +
                            " Y: " + matchingMarker.yPoint.toInt() + "\n" +
                            AllocationScreenText.STATUS_LABEL
                            + matchingMarker.status.displayStatusText() + "\n" +
                            AllocationScreenText.PIXEL_INDEX_LABEL
                            + matchingMarker.pixelSequentialNumberPoint
                )
            } else {
                Text(AllocationScreenText.YOU_CLICKED_EMPTY)
            }
        },
        confirmButton = {
            Row {
                TextButton(onClick = { onDismiss() }) {
                    Text(AllocationScreenText.CANCEL_EN)
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (matchingMarker != null) {
                    TextButton(
                        onClick = {
                            onDismiss()
                            val markerId = matchingMarker.id
                            val title = Uri.encode(matchingMarker.name)
                            val status = Uri.encode(matchingMarker.status.value)
                            val content = Uri.encode(
                                """
                                ${AllocationScreenText.NAME_LABEL} ${matchingMarker.name}
                                X: ${matchingMarker.xPoint.toInt()},
                                 Y: ${matchingMarker.yPoint.toInt()}
                                ${AllocationScreenText.STATUS_LABEL} ${matchingMarker.status}
                                ${AllocationScreenText.PIXEL_INDEX_LABEL}
                                 ${matchingMarker.pixelSequentialNumberPoint}
                                """.trimIndent()
                            )
                            val route = "position_detail/$markerId/$title/$status/$content"
                            navController.navigate(route)
                        }
                    ) {
                        Text(AllocationScreenText.VIEW_DETAILS)
                    }
                }
            }
        }
    )
}
