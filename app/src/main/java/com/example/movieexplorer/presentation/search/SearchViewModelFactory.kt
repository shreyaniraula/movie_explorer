package com.example.movieexplorer.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieexplorer.domain.repository.MovieRepository

// ViewModelProvider.Factory tells Android how to build the ViewModel; this is the custom blueprint
class SearchViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // The factory asks: "Are you asking me to build a SearchViewModel?" If yes, it builds it.
        // If you accidentally ask it to build a ProfileViewModel, it crashes on purpose
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}