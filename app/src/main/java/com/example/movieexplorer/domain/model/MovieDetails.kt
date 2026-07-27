package com.example.movieexplorer.domain.model

data class MovieDetails(
    val imdbId: String,
    val title: String,
    val year: String,
    val posterUrl: String,
    val plot: String,
    val runtime: String,
    val genre: String,
    val director: String,
    val actors: String,
    val awards: String,
    val imdbRating: String,
    val boxOffice: String,
)