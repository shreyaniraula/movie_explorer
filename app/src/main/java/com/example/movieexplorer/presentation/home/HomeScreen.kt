package com.example.movieexplorer.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieexplorer.domain.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val favourites by viewModel.favourites.collectAsStateWithLifecycle()
    val recentlyViewed by viewModel.recentlyViewed.collectAsStateWithLifecycle()
    val searchHistory by viewModel.searchHistory.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Explorer") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onSearchClick,
                text = { Text("Search") },
                icon = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (searchHistory.isNotEmpty()) {
                SectionTitle("Recent Searches")
                LazyRow {
                    items(searchHistory) { query ->
                        AssistChip(onClick = onSearchClick, label = { Text(query) })
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            if (recentlyViewed.isNotEmpty()) {
                SectionTitle("Recently Viewed")
                MovieRow(recentlyViewed, onMovieClick)
                Spacer(Modifier.height(16.dp))
            }

            if (favourites.isNotEmpty()) {
                SectionTitle("favourites")
                MovieRow(favourites, onMovieClick)
            }

            if (searchHistory.isEmpty() && recentlyViewed.isEmpty() && favourites.isEmpty()) {
                Text("Nothing here yet — try searching for a movie.")
            }
        }

    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun MovieRow(movies: List<Movie>, onMovieClick: (String) -> Unit) {
    LazyRow {
        items(movies) { movie ->
            Card(
                onClick = { onMovieClick(movie.imdbId) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(120.dp)
            ) {
                Column(Modifier.padding(8.dp)) {
                    Text(movie.title, maxLines = 2, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}