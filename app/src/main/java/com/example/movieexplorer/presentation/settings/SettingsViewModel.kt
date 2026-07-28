package com.example.movieexplorer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieexplorer.core.datastore.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesDataStore: UserPreferencesDataStore
) : ViewModel() {
    val isDarkMode: StateFlow<Boolean> = preferencesDataStore.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun onDarkModeToggle(enabled: Boolean) {
        viewModelScope.launch {
            preferencesDataStore.setDarkMode(enabled)
        }
    }
}