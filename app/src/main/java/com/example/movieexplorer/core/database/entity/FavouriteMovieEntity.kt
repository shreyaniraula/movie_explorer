package com.example.movieexplorer.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
data class FavouriteMovieEntity(

    //natural key (imdbId) as primary key since it's already unique
    // and stable, rather than adding a synthetic autoIncrement id that serves no purpose here.
    @PrimaryKey
    val imdbId: String,
    val title: String,
    val year: String,
    val posterUrl: String,
    val type: String,
    val addedAtTimestamp: Long,
)