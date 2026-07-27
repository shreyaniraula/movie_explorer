package com.example.movieexplorer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.movieexplorer.core.database.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY searchedAtTimestamp DESC LIMIT 10")
    fun getSearchHistory(): Flow<List<SearchHistoryEntity>>

    // since the primary key auto-generates, there's never a conflict
    // to resolve; every insert is guaranteed to succeed as a new row.
    @Insert
    suspend fun addSearchQuery(query: SearchHistoryEntity)
}