package com.example.movieexplorer.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieexplorer.core.datastore.UserPreferencesDataStore
import com.example.movieexplorer.domain.model.Movie
import com.example.movieexplorer.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val preferencesDataStore: UserPreferencesDataStore,
) : ViewModel() {

    // _query replaces _uiState.
    // We don't need an "Idle" state anymore — if query is blank, UI just shows the empty message.
    // Paging 3 handles Loading/Error/Success on its own via LoadState.
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // Only search when query is non-blank.
    // flatMapLatest = if a new query comes in, cancel the old search, start a new one.
    // cachedIn(viewModelScope) = keep loaded pages in memory across screen rotation.

    val pagedMovies: Flow<PagingData<Movie>> = _query
        .filter { it.isNotBlank() }
        .flatMapLatest { q -> repository.searchMoviesPaged(q) }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String) {
        _query.value = query;

        if (query.isNotBlank()) {
            viewModelScope.launch {
                preferencesDataStore.setLastSearch(query)
                repository.saveSearchQuery(query)
            }
        }
    }
}