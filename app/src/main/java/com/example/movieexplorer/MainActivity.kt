package com.example.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieexplorer.core.util.ConnectivityObserver
import com.example.movieexplorer.navigation.MovieExplorerNavHost
import com.example.movieexplorer.presentation.settings.SettingsViewModel
import com.example.movieexplorer.ui.theme.MovieExplorerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// Any Activity/Fragment that hosts Hilt-injected Compose screens needs @AndroidEntryPoint
// it's the entry point where Hilt's generated code hooks into the Android lifecycle.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Field injection - needed here because ConnectivityObserver is a  plain
    // @Singleton class, not a ViewModel, so hiltViewModel() can't provide it.

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsStateWithLifecycle()
            MovieExplorerTheme(darkTheme = isDarkMode) {
                MainContent(connectivityObserver)
            }
        }
    }
}

@Composable
private fun MainContent(connectivityObserver: ConnectivityObserver) {
    val isOnline by connectivityObserver.isOnline.collectAsStateWithLifecycle(initialValue = true)

    Column {
        if (!isOnline) {
            Surface(color = MaterialTheme.colorScheme.errorContainer) {
                Text(
                    "You're offline",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        MovieExplorerNavHost()
    }
}

// How hiltViewModel() works under the hood:
// 1. Finds the current screen's lifecycle owner (Activity or NavBackStackEntry).
// 2. Asks Hilt to build SearchViewModel.
// 3. Resolves dependencies automatically:
//    - MovieRepository -> mapped to MovieRepositoryImpl (@Binds)
//    - OmdbApiService  -> built by NetworkModule (@Provides)
// 4. Returns a ready-to-use SearchViewModel.





// Coroutines are for async code-like network or database calls in a sync style
// suspend fun can't be called from anywhere-it needs a coroutine to run inside.
// eg inside viewModelScope.launch{}
// launch starts a new coroutine. Everything inside that block can call suspend fun freely.

// Flow is like subscribing to updates-it emits new values whenever something changes
// StateFlow is a Flow that always holds a current value and immediately gives new observers that value.
// It's the standard way to expose UI state from a ViewModel, since the screen always needs to know the current state, not just wait for future changes.