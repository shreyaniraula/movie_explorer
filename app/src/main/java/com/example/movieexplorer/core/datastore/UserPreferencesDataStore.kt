package com.example.movieexplorer.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Special delegate ensures only ONE DataStore file exists per Context.
// Enforced by the delegate itself, not by Hilt.
// We still use Hilt @Singleton on the wrapper class for a safety approach.
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val LAST_SEARCH = stringPreferencesKey("last_search")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[Keys.DARK_MODE] ?: false }

    val lastSearch: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[Keys.LAST_SEARCH] ?: "" }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.DARK_MODE] = enabled
        }
    }

    suspend fun setLastSearch(query: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.LAST_SEARCH] = query
        }
    }
}