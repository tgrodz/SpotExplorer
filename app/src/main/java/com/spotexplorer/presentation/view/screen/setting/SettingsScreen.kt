package com.spotexplorer.presentation.view.screen.setting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.spotexplorer.infrastructure.constants.SettingsText
import com.spotexplorer.presentation.shared.SettingsSharedViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    settingsSharedViewModel: SettingsSharedViewModel = viewModel()
) {
    val markerSize by settingsSharedViewModel.markerSize.collectAsState()
    val useHapticEffect by settingsSharedViewModel.useHapticEffect.collectAsState()
    val animationOption by settingsSharedViewModel.animationOption.collectAsState()
    val showText by settingsSharedViewModel.isShowText.collectAsState()
    val showRoute by settingsSharedViewModel.useRouteLine.collectAsState()
    val isImageDarker by settingsSharedViewModel.enableImageDarker.collectAsState()
    val enableAnimation by settingsSharedViewModel.enableAnimation.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) { Toast.makeText(context, "Markierungen erhalten", Toast.LENGTH_SHORT).show() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(SettingsText.SETTINGS_TITLE) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1F1F1F), // Dark grey for the top bar
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = SettingsText.BACK_BUTTON_DESCRIPTION
                        )
                    }
                },
            )
        },
        modifier = Modifier.background(Color.Black)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Marker Size Section
            SettingsCard {
                Text(SettingsText.MARKER_SIZE_TITLE, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { settingsSharedViewModel.updateMarkerSize(50f) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) { Text(SettingsText.SMALL_BUTTON) }

                    Button(
                        onClick = { settingsSharedViewModel.updateMarkerSize(100f) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) { Text(SettingsText.MEDIUM_BUTTON) }

                    Button(
                        onClick = { settingsSharedViewModel.updateMarkerSize(150f) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) { Text(SettingsText.LARGE_BUTTON) }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    SettingsText.SELECTED_MARKER_SIZE.format(markerSize.toInt()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }


            // Vibration Effect Toggle
            SettingsCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(SettingsText.HAPTIC_EFFECT_TITLE, style = MaterialTheme.typography.bodyLarge, color = Color.White, modifier = Modifier.weight(1f))
                    Switch(
                        checked = useHapticEffect,
                        onCheckedChange = { settingsSharedViewModel.updateUseHapticEffect(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF6200EE),
                            uncheckedThumbColor = Color.Gray
                        )
                    )
                }
            }

            // Draw Route
            SettingsCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(SettingsText.DRAW_ROUTE_TITLE, style = MaterialTheme.typography.bodyLarge, color = Color.White, modifier = Modifier.weight(1f))
                    Switch(
                        checked = showRoute ,
                        onCheckedChange = { settingsSharedViewModel.updateUseRouteLine(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF6200EE),
                            uncheckedThumbColor = Color.Gray
                        )
                    )
                }
            }

            //The Image Darker
            SettingsCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(SettingsText.IMAGE_DARKER_TITLE , style = MaterialTheme.typography.bodyLarge, color = Color.White, modifier = Modifier.weight(1f))
                    Switch(
                        checked = isImageDarker ,
                        onCheckedChange = { settingsSharedViewModel.updateEnableImageDarker(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF6200EE),
                            uncheckedThumbColor = Color.Gray
                        )
                    )
                }
            }

            // Animation Settings
            SettingsCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(SettingsText.ENABLE_ANIMATION_TITLE, style = MaterialTheme.typography.bodyLarge, color = Color.White, modifier = Modifier.weight(1f))
                    Switch(
                        checked = enableAnimation,
                        onCheckedChange = { settingsSharedViewModel.updateEnableAnimation(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF6200EE),
                            uncheckedThumbColor = Color.Gray
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    SettingsText.ANIMATION_OPTION.format(animationOption),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { settingsSharedViewModel.updateAnimationOption(1) },
                        enabled = enableAnimation,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (enableAnimation) Color.DarkGray else Color.Gray
                        )
                    ) {
                        Text("Option 1")
                    }
                    Button(
                        onClick = { settingsSharedViewModel.updateAnimationOption(2) },
                        enabled = enableAnimation,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (enableAnimation) Color.DarkGray else Color.Gray
                        )
                    ) {
                        Text("Option 2")
                    }
                    Button(
                        onClick = { settingsSharedViewModel.updateAnimationOption(3) },
                        enabled = enableAnimation,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (enableAnimation) Color.DarkGray else Color.Gray
                        )
                    ) {
                        Text("Option 3")
                    }
                }
            }

            // Show Text Toggle
            SettingsCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(SettingsText.SHOW_TEXT_TITLE, style = MaterialTheme.typography.bodyLarge, color = Color.White, modifier = Modifier.weight(1f))
                    Switch(
                        checked = showText,
                        onCheckedChange = { settingsSharedViewModel.updateShowText(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF6200EE),
                            uncheckedThumbColor = Color.Gray
                        )
                    )
                }
            }

        }
    }
}

@Composable
fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2C)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}