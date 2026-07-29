package com.example.movieexplorer.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieexplorer.core.datastore.UserPreferencesDataStore
import com.example.movieexplorer.domain.model.Movie
import com.example.movieexplorer.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

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

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val pagedMovies: Flow<PagingData<Movie>> = _query
        // wait 500ms after the user stops typing before searching
        .debounce(500.milliseconds)
        .filter { it.isNotBlank() }
        // skip if the query is the same as last time
        .distinctUntilChanged()
        .flatMapLatest { q -> repository.searchMoviesPaged(q) }
        .cachedIn(viewModelScope)

    // Save "last search" and "search history" only after the user pauses typing
    // same debounce as the actual search, so we don't save a row per keystroke

    init {
        observeQueryForHistory()
    }

    @OptIn(FlowPreview::class)
    private fun observeQueryForHistory() {
        viewModelScope.launch {
            _query
                .debounce(500.milliseconds)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collect { q ->
                    preferencesDataStore.setLastSearch(q)
                    repository.saveSearchQuery(q)
                }
        }
    }

    // Only updates the raw text now — no side effects here anymore.
    fun onSearchQueryChanged(query: String) {
        _query.value = query;
    }
}