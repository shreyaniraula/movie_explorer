package com.example.movieexplorer.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieexplorer.core.datastore.UserPreferencesDataStore
import com.example.movieexplorer.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val preferencesDataStore: UserPreferencesDataStore,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)

    // _uiState is mutable and uiState is read-only
    // Only the viewmodel is allowed to change the screen's data(_uiState)
    // The screen can look at the data(uiState) but cannot change it directly.
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    val lastSearch: StateFlow<String> = preferencesDataStore.lastSearch
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    fun onSearchQueryChanged(query: String) {
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }

        // Starts a background task (like fetching data)
        // scope means that it is running only while the activity is running
        viewModelScope.launch {
            preferencesDataStore.setLastSearch(query)
            repository.saveSearchQuery(query)
            repository.searchMovies(query)

                // initially show loading
                .onStart { _uiState.value = SearchUiState.Loading }

                // catches errors that happen during the search
                .catch { throwable ->
                    _uiState.value =
                        SearchUiState.Error(throwable.message ?: "Something went wrong")
                }
                .collect { movies ->
                    _uiState.value = SearchUiState.Success(movies)
                }
        }
    }
}