# 📸 Photo Gallery App ✨

## Overview
The **Photo Gallery App** is a modern Android application built using **Jetpack Compose** and following the **MVVM (Model-View-ViewModel)** architectural pattern.  
It allows users to browse recent photos from Flickr, search for specific content, and view photo details all while demonstrating best practices in **UI state management**, **navigation**, and **error handling** in a Compose environment.


## 🚀 Key Features
- **Jetpack Compose (Material 3)**: Built entirely with modern declarative UI using Material 3 guidelines.
- **Search & Pagination**: Features a search bar and efficient, continuous loading of photos via infinite scrolling.
- **Aspect Ratio Preservation**: Detail view displays photos maintaining their original aspect ratio.
- **Global Error Handling**: Uses a centralized `SharedFlow` to report non-fatal errors (like connection issues) via SnackBar.
- **Reusability**: Features a reusable `ErrorView` component supporting both full-screen and dialog display modes.
- **Unit Tested Logic**: Key ViewModel logic for pagination and error reporting is set up for unit testing.

## 💻 Tech Stack
This project utilizes modern Android development tools and libraries:

| Layer | Technology |
|-------|-------------|
| **Language** | Kotlin |
| **UI** | Jetpack Compose (Material 3) |
| **Architecture** | MVVM with StateFlow / SharedFlow |
| **Async** | Kotlin Coroutines & Flow |
| **Networking** | Retrofit |
| **Image Loading** | Coil |
| **Testing** | kotlinx-coroutines-test for ViewModel unit tests |


## 📁 Architecture & Structure
The application follows a clean, layered architecture:

| Directory | Purpose | Key Components                                         |
|------------|----------|--------------------------------------------------------|
| `viewmodel` | UI logic and state management | `GalleryViewModel`, `BaseViewModel` (error bus)        |
| `data` | Data sources and business logic | `FlickrPhoto`, `FlickrRepository`                      |
| `ui/screens` | Full-screen Composables | `GalleryScreen`, `DetailsScreen`                       |
| `ui/components` | Reusable UI elements | `PhotoItem`, `ErrorView`, `Dialogs`, `LoadingView`     |
| `utils` | Helper classes | `PagingManager`, `ImageUrlFormatter`,  `NavigationUtils` |

**Note:** The `local.properties` file is correctly listed in the `.gitignore` to prevent your API key from being accidentally committed.

## 🎯 Future Improvements
Here are some features and improvements planned for the future:

-  Implement a "Favorites" feature to save photos locally.
-  Add theming support (Light/Dark mode toggle).
-  Write UI tests for key user flows.

## ⚙️ Setup and Running
### Prerequisites
- Android Studio **Hedgehog** (or newer)
- Active internet connection (required to fetch photos from Flickr)

### API Key Requirement
This application requires a **Flickr API key** to function.

1. Obtain a key from the [Flickr API development site](https://www.flickr.com/services/api/).
2. Add it to your project’s `local.properties` file:

   ```properties
   FLICKR_API_KEY="YOUR_API_KEY_HERE"
##

