package com.example.photo_gallery.ui.navigation


import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.photo_gallery.ui.screens.DetailsScreen
import com.example.photo_gallery.ui.screens.GalleryScreen
import com.example.photo_gallery.utils.NavigationUtils
import com.example.photo_gallery.viewmodel.GalleryViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    data object Gallery : Screen("gallery")
    data object Details : Screen("details/{photoJson}") {
        fun createRoute(encodedPhoto: String) = "details/$encodedPhoto"
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    galleryViewModelFactory: ViewModelProvider.Factory,
    startDestination: String = Screen.Gallery.route
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Gallery.route) {

            val galleryViewModel: GalleryViewModel = viewModel(
                factory = galleryViewModelFactory
            )

            val globalErrorFlow = galleryViewModel.globalErrorFlow

            GlobalErrorCollector(globalErrorFlow = globalErrorFlow)

            GalleryScreen(
                viewModel = galleryViewModel,
                onPhotoClick = { photo ->
                    val encoded = NavigationUtils.encodePhoto(photo)
                    navController.navigate(Screen.Details.createRoute(encoded))
                }
            )
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("photoJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedPhoto = backStackEntry.arguments?.getString("photoJson") ?: return@composable
            val photo = NavigationUtils.decodePhoto(encodedPhoto)
            DetailsScreen(photo = photo, onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun GlobalErrorCollector(globalErrorFlow: SharedFlow<String>) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(globalErrorFlow) {
        globalErrorFlow.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = "DISMISS",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}
