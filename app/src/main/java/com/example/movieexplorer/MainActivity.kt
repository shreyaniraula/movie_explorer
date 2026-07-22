package com.example.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movieexplorer.presentation.search.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

// Any Activity/Fragment that hosts Hilt-injected Compose screens needs @AndroidEntryPoint
// it's the entry point where Hilt's generated code hooks into the Android lifecycle.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SearchScreen(onMovieClick = { /* Day 4 */ })
        }
    }
}

// How hiltViewModel() works under the hood:
// 1. Finds the current screen's lifecycle owner (Activity or NavBackStackEntry).
// 2. Asks Hilt to build SearchViewModel.
// 3. Resolves dependencies automatically:
//    - MovieRepository -> mapped to MovieRepositoryImpl (@Binds)
//    - OmdbApiService  -> built by NetworkModule (@Provides)
// 4. Returns a ready-to-use SearchViewModel.