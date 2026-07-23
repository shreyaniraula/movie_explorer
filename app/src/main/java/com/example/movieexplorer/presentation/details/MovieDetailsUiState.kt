package com.example.movieexplorer.presentation.details

import com.example.movieexplorer.domain.model.MovieDetails

sealed interface MovieDetailsUiState {
    data object Loading : MovieDetailsUiState
    data class Success(val movieDetails: MovieDetails) : MovieDetailsUiState
    data class Error(val message: String) : MovieDetailsUiState
}