package com.example.photo_gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.photo_gallery.data.remote.service.DependencyProvider
import com.example.photo_gallery.ui.navigation.AppNavGraph
import com.example.photo_gallery.ui.theme.Photo_galleryTheme
import com.example.photo_gallery.utils.createGalleryViewModelFactory

class MainActivity : ComponentActivity() {
    private val galleryViewModelFactory by lazy {
        createGalleryViewModelFactory(
            repository = DependencyProvider.flickrRepository,
            pagingManager = DependencyProvider.pagingManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Photo_galleryTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    galleryViewModelFactory = galleryViewModelFactory
                )
            }
        }
    }
}