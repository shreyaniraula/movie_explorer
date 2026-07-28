package com.example.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieexplorer.navigation.MovieExplorerNavHost
import com.example.movieexplorer.presentation.settings.SettingsViewModel
import com.example.movieexplorer.ui.theme.MovieExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

// Any Activity/Fragment that hosts Hilt-injected Compose screens needs @AndroidEntryPoint
// it's the entry point where Hilt's generated code hooks into the Android lifecycle.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsStateWithLifecycle()
            MovieExplorerTheme(darkTheme = isDarkMode) {
                MovieExplorerNavHost()
            }
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