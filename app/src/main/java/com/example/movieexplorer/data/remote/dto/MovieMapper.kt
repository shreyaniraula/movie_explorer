package com.example.movieexplorer.data.remote.dto

import com.example.movieexplorer.domain.model.Movie
import com.example.movieexplorer.domain.model.MovieDetails

// Extension function used as a mapper
// Keeps the mapping colocated with DTO definitions
// without polluting the domain model with data-layer knowledge
fun MovieDto.toDomain(): Movie {
    return Movie(
        imdbId = imdbId,
        title = title,
        year = year,
        posterUrl = if (poster == "N/A") "" else poster,
        type = type
    )
}

fun MovieDetailsDto.toDomain(): MovieDetails {
    return MovieDetails(
        imdbId = imdbId.orEmpty(),
        title = title.orEmpty(),
        year = year.orEmpty(),
        posterUrl = poster.takeUnless { it.isNullOrBlank() || it == "N/A" }.orEmpty(),
        plot = plot.takeUnless { it.isNullOrBlank() || it == "N/A" }.orEmpty(),
        runtime = runtime.takeUnless { it.isNullOrBlank() || it == "N/A" }.orEmpty(),
        genre = genre.orEmpty(),
        director = director.orEmpty(),
        actors = actors.orEmpty(),
        awards = awards.orEmpty(),
        imdbRating = imdbRating.takeUnless { it.isNullOrBlank() || it == "N/A" }.orEmpty(),
        boxOffice = boxOffice.takeUnless { it.isNullOrBlank() || it == "N/A" }.orEmpty()
    )
}