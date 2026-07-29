package com.example.movieexplorer.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieexplorer.domain.model.Movie

@Composable
fun SearchScreen(
    onMovieClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val query by viewModel.query.collectAsStateWithLifecycle()

    // collectAsLazyPagingItems() turns Flow<PagingData<Movie>> into something
    // LazyColumn can loop over — like a normal List, but loads more pages as you scroll.
    val lazyPagingItems = viewModel.pagedMovies.collectAsLazyPagingItems()

    Scaffold() { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    viewModel.onSearchQueryChanged(it)
                },
                label = { Text("Search movies") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            if (query.isBlank()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Search for a movie to get started")
                }
                return@Column
            }


            when (val refresh = lazyPagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${refresh.error.message}")
                    }
                }

                is LoadState.NotLoading -> {
                    if (lazyPagingItems.itemCount == 0) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No movies found for \"$query\"")
                        }
                    } else {
                        PullToRefreshBox(
                            isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading,
                            onRefresh = { lazyPagingItems.refresh() }
                        ) {
                            LazyColumn(
                                Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                items(lazyPagingItems.itemCount) { index ->
                                    lazyPagingItems[index]?.let { movie ->
                                        MovieListItem(
                                            movie = movie,
                                            onClick = { onMovieClick(movie.imdbId) })
                                    }
                                }

                                item {
                                    when (val append = lazyPagingItems.loadState.append) {
                                        is LoadState.Loading -> {
                                            Box(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(
                                                        24.dp
                                                    )
                                                )
                                            }
                                        }

                                        is LoadState.Error -> {
                                            Row(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Text("Failed to load more")
                                                TextButton(onClick = { lazyPagingItems.retry() }) {
                                                    Text(
                                                        "Retry"
                                                    )
                                                }
                                            }
                                        }

                                        else -> {}
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
private fun MovieListItem(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(movie.title, style = MaterialTheme.typography.titleMedium)
                Text(movie.year, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}