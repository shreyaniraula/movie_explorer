package com.example.movieexplorer.presentation.search

import com.example.movieexplorer.domain.model.Movie

// sealed interface over a plain data class to emit exactly one of these states at a time
// enforced by the type system; makes illegal states unrepresentable at compile time only

// data object gives a proper .toString() used for logs
sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Success(val movies: List<Movie>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}