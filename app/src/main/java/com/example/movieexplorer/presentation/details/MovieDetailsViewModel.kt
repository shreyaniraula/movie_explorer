package com.example.movieexplorer.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieexplorer.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


// Hilt automatically injects SavedStateHandle into the ViewModel.
// It acts like a Map holding navigation arguments passed to this screen
// (e.g., savedStateHandle["imdbId"] gets the "imdbId" passed in the route).
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository
) : ViewModel() {
    private val imdbId: String = checkNotNull(savedStateHandle["imdbId"])

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow();

    // Converts a cold data stream (Flow) into a hot state stream (StateFlow)
    // This automatically updates the UI when the database changes, without manual .value assignments
    val isFavourite: StateFlow<Boolean> = repository.isFavourite(imdbId)
        .stateIn(
            // Ties the stream lifecycle to this ViewModel; cancels automatically when ViewModel dies
            scope = viewModelScope,

            // Keeps query active while UI is visible. If UI disappears (like a screen rotation),
            // it waits 5 seconds before stopping the database query to save resources without lag.
            started = SharingStarted.WhileSubscribed(5000),

            // Default placeholder value to show on screen while the database loads asynchronously
            initialValue = false,
        )

    init {
        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        viewModelScope.launch {
            repository.getMovieDetails(imdbId)
                .catch { throwable ->
                    _uiState.value =
                        MovieDetailsUiState.Error(throwable.message ?: "Something went wrong")
                }
                .collect { details ->
                    _uiState.value = MovieDetailsUiState.Success(details)
                }
        }
    }

    fun onFavouriteClick() {
        val currentState = uiState.value
        if (currentState is MovieDetailsUiState.Success) {
            viewModelScope.launch {
                repository.toggleFavourite(currentState.movieDetails)
            }
        }
    }
}