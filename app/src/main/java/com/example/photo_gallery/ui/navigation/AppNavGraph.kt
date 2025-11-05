package com.example.photo_gallery.ui.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.photo_gallery.ui.screens.GalleryScreen
import com.example.photo_gallery.viewmodel.GalleryViewModel
import kotlinx.coroutines.flow.SharedFlow

sealed class Screen(val route: String) {
    data object Gallery : Screen("gallery")
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
                onPhotoClick = { /* ... */ }
            )
        }
        // ... Details Screen ...
    }
}

@Composable
fun GlobalErrorCollector(globalErrorFlow: SharedFlow<String>) {

}
