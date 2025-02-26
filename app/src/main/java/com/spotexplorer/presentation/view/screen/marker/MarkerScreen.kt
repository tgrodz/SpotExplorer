package com.spotexplorer.presentation.view.screen.marker

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.spotexplorer.R
import com.spotexplorer.infrastructure.constants.MarkerScreenText
import com.spotexplorer.infrastructure.constants.MarkerScreenText.REVIEW_COLOR
import com.spotexplorer.model.entity.marker.MarkerStatus
import com.spotexplorer.presentation.view.screen.marker.note.NoteBottomSheetContent
import com.spotexplorer.presentation.view.screen.marker.note.NoteViewModel
import com.spotexplorer.presentation.view.ui.theme.AppBackground
import com.spotexplorer.presentation.view.ui.theme.AppTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerPositionDetailScreen(
    markerId: Int,
    positionTitle: String,
    positionData: String = "None",
    status: String = "None",
    navController: NavHostController,
) {
    val context = LocalContext.current

    val viewModel: MarkerViewModel = viewModel()
    val noteViewModel: NoteViewModel = viewModel()

    val marker by viewModel.markerDetails.collectAsState()
    val reviewsList by noteViewModel.notesFlow.collectAsState(initial = emptyList())

    var isOccupied by remember { mutableStateOf(false) }

    // Checkbox states for features
    var checkBoxStatePublicTransport by remember { mutableStateOf(false) }
    var checkBoxStateFurniture by remember { mutableStateOf(false) }
    var checkBoxStateBalcony by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var newReviewText by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showReviewSheet by remember { mutableStateOf(false) }

    LaunchedEffect(markerId) {
        viewModel.loadMarkerById(markerId)
    }

    LaunchedEffect(marker) {
        marker?.let { marker ->
            val currentStatus = marker.getCurrentStatus()
            isOccupied = currentStatus != MarkerStatus.RED
            checkBoxStatePublicTransport = marker.hasPublicTransport
            checkBoxStateFurniture = marker.hasFurniture
            checkBoxStateBalcony = marker.hasBalcony
            noteViewModel.setMarkerId(marker.id)
        }
    }

    val computedStatus = when {
        !isOccupied -> MarkerStatus.RED
        checkBoxStatePublicTransport && checkBoxStateFurniture && checkBoxStateBalcony -> MarkerStatus.GREEN
        else -> MarkerStatus.YELLOW
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(MarkerScreenText.HEADER, color = AppTextColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = MarkerScreenText.BACK_DESCRIPTION,
                            tint = AppTextColor
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = AppBackground)
            )
        },
        containerColor = AppBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.pin_marker),
                        contentDescription = "Location Marker",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = positionTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTextColor
                    )

                    Text(
                        text = MarkerScreenText.SEQUENTIAL_NUMBER + (marker?.pixelSequentialNumberPoint
                            ?: "N/A"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTextColor
                    )
                    Text(
                        text = MarkerScreenText.POSITION_Y + (marker?.xPoint?.toInt() ?: "N/A"),
                        fontSize = 14.sp,
                        color = AppTextColor
                    )
                    Text(
                        text = MarkerScreenText.POSITION_X + (marker?.yPoint?.toInt() ?: "N/A"),
                        fontSize = 14.sp,
                        color = AppTextColor
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = MarkerScreenText.STATUS + computedStatus.displayStatusText(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTextColor
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status selection buttons
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatusButton(
                            painter = painterResource(id = R.drawable.ic_check_green),
                            label = MarkerScreenText.FREE,
                            isSelected = isOccupied,
                            computedStatus = if (isOccupied) computedStatus else null,
                            onClick = { isOccupied = true }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        StatusButton(
                            painter = painterResource(id = R.drawable.ic_cross_red),
                            label = MarkerScreenText.BUSY,
                            isSelected = !isOccupied,
                            onClick = { isOccupied = false }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = MarkerScreenText.AVAILABLE_FEATURES,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue
                    )
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        CheckboxRow(
                            MarkerScreenText.CHECKBOX_PUBLIC_TRANSPORT,
                            checkBoxStatePublicTransport,
                            isOccupied
                        ) { checkBoxStatePublicTransport = it }
                        CheckboxRow(
                            MarkerScreenText.CHECKBOX_FURNITURE,
                            checkBoxStateFurniture,
                            isOccupied
                        ) { checkBoxStateFurniture = it }
                        CheckboxRow(
                            MarkerScreenText.CHECKBOX_BALCONY,
                            checkBoxStateBalcony,
                            isOccupied
                        ) { checkBoxStateBalcony = it }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = { showReviewSheet = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                            modifier = Modifier.fillMaxWidth(0.7f)
                        ) {
                            Text(MarkerScreenText.VIEW_REVIEWS, fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                marker?.let { marker ->
                                    val newStatus = if (isOccupied) {
                                        if (checkBoxStatePublicTransport && checkBoxStateFurniture && checkBoxStateBalcony)
                                            MarkerStatus.GREEN
                                        else
                                            MarkerStatus.YELLOW
                                    } else {
                                        MarkerStatus.RED
                                    }
                                    viewModel.updateMarker(
                                        marker.copy(
                                            status = newStatus,
                                            hasPublicTransport = checkBoxStatePublicTransport,
                                            hasFurniture = checkBoxStateFurniture,
                                            hasBalcony = checkBoxStateBalcony
                                        )
                                    )
                                    Toast.makeText(
                                        context,
                                        "Marker id ${marker.id} updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                navController.popBackStack()
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AppTextColor),
                            modifier = Modifier.fillMaxWidth(0.7f)
                        ) {
                            Text(MarkerScreenText.CONFIRM, fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (marker != null) {
                            Button(
                                onClick = { showDeleteDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.fillMaxWidth(0.7f)
                            ) {
                                Text(MarkerScreenText.DELETE_MARKER, fontSize = 16.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(MarkerScreenText.CONFIRM_DELETION_TITLE) },
            text = { Text(MarkerScreenText.CONFIRM_DELETION_TEXT) },
            confirmButton = {
                Button(
                    onClick = {
                        marker?.let { marker ->
                            viewModel.deleteMarkerById(marker.id) {
                                showDeleteDialog = false
                                navController.popBackStack()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = AppTextColor
                    )
                ) {
                    Text(MarkerScreenText.DELETE)
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text(MarkerScreenText.CANCEL)
                }
            }
        )
    }

    if (showReviewSheet) {
        ModalBottomSheet(
            onDismissRequest = { showReviewSheet = false },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            NoteBottomSheetContent(
                notes = reviewsList,
                newText = newReviewText,
                onTextChange = { newReviewText = it },
                onAddNote = {
                    if (newReviewText.isNotBlank()) {
                        marker?.id?.let { markerId ->
                            noteViewModel.addNote(markerId, newReviewText)
                            newReviewText = ""
                        }
                    }
                },
                onDeleteNote = { review -> noteViewModel.deleteNote(review) },
                onClose = { showReviewSheet = false }
            )
        }
    }
}

@Composable
fun CheckboxRow(
    label: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = CheckboxDefaults.colors(checkmarkColor = AppBackground)
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (enabled) AppTextColor else Color.Gray
        )
    }
}

@Composable
fun StatusButton(
    painter: Painter,
    label: String,
    isSelected: Boolean,
    computedStatus: MarkerStatus? = null,
    onClick: () -> Unit
) {
    val iconColor = if (!isSelected) {
        Color.Gray
    } else {
        when (label) {
            MarkerScreenText.BUSY -> Color.Red
            MarkerScreenText.FREE -> when (computedStatus) {
                MarkerStatus.GREEN -> Color.Green
                MarkerStatus.YELLOW -> Color.Yellow
                else -> REVIEW_COLOR
            }

            else -> Color.White
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = if (isSelected) Color.Blue else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = label,
            modifier = Modifier.size(60.dp),
            contentScale = ContentScale.Fit,
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(iconColor)
        )
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = iconColor,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PositionDetailPreview() {
    MarkerPositionDetailScreen(
        markerId = 0,
        positionTitle = "Test Title",
        navController = NavHostController(LocalContext.current),
    )
}
