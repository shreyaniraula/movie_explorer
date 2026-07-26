package com.example.movieexplorer.data.local.entity

import androidx.room.PrimaryKey

data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val query: String,
    val searchedAtEpochMillis: Long,
)