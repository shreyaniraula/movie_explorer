package com.example.movieexplorer.data.local.entity

import androidx.room.PrimaryKey

data class RecentlyViewedEntity(
    @PrimaryKey
    val imdbId: String,
    val title: String,
    val posterUrl: String?,
    val viewedAtEpochMillis: Long,
)