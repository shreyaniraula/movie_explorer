package com.example.movieexplorer.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recently_viewed")
data class RecentlyViewedEntity(
    @PrimaryKey
    val imdbId: String,
    val title: String,
    val year: String,
    val posterUrl: String,
    val type: String,
    val plot: String,
    val runtime: String,
    val genre: String,
    val director: String,
    val actors: String,
    val awards: String,
    val imdbRating: String,
    val boxOffice: String,
    val viewedAtTimestamp: Long
)