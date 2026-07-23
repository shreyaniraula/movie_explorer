package com.example.movieexplorer.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieexplorer.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
}