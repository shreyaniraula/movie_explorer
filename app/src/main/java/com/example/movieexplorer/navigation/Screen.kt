package com.example.movieexplorer.navigation


// Navigation arguments should be primitive and serializable since they become part of the back stack's saved state
// passing complex objects breaks process-death restoration and couples screens together unnecessarily.
// Fetch-by-ID keeps each screen self-sufficient and consistent with an offline-first repository pattern.
sealed class Screen(val route: String) {
    data object Search : Screen("search")
    data object Details : Screen("details/{imdbId}") {
        fun createRoute(imdbId: String) = "details/$imdbId"
    }

    data object Home : Screen("home")
    data object Settings : Screen("settings")
}