package com.spotexplorer.presentation.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.spotexplorer.presentation.view.ui.StockImage
import com.spotexplorer.presentation.view.screen.marker.MarkerPositionDetailScreen
import com.spotexplorer.presentation.view.screen.setting.SettingsScreen
import com.spotexplorer.presentation.view.screen.allocation.AllocationScreen
import com.spotexplorer.presentation.shared.SettingsSharedViewModel

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = MaterialTheme.typography
    ) {
        val settingsSharedViewModel: SettingsSharedViewModel = viewModel()
        val navController: NavHostController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Route.Allocation.route,
            modifier = modifier
        ) {
            composable(
                route = Route.Allocation.route,
                arguments = listOf(
                    navArgument("imageName") {
                        type = NavType.StringType
                        defaultValue = StockImage.Stock0.name
                    }
                )
            ) { backStackEntry ->
                val imageName =
                    backStackEntry.arguments?.getString("imageName") ?: StockImage.Stock0.name
                val selectedStock = StockImage.valueOf(imageName)
                AllocationScreen(navController, selectedStock, settingsSharedViewModel)
            }

            composable(Route.Settings.route) {
                SettingsScreen(
                    navController,
                    settingsSharedViewModel
                )
            }

            composable(
                route = Route.MarkerPositionDetail.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("status") { type = NavType.StringType },
                    navArgument("content") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val markerId = backStackEntry.arguments?.getInt("id") ?: -9
                val title = backStackEntry.arguments?.getString("title") ?: ""
                val status = backStackEntry.arguments?.getString("status") ?: ""
                val content = backStackEntry.arguments?.getString("content") ?: ""
                MarkerPositionDetailScreen(
                    markerId = markerId,
                    positionTitle = title,
                    positionData = content,
                    status = status,
                    navController = navController,
                )
            }
        }
    }
}
