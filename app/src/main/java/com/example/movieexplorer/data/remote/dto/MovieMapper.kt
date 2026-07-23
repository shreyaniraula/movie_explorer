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
        imdbId = imdbId,
        title = title,
        year = year,
        posterUrl = if (poster == "N/A") "" else poster,
        plot = if (plot == "N/A") "" else plot,
        runtime = if (runtime == "N/A") "" else runtime,
        genre = genre,
        director = director,
        actors = actors,
        awards = awards,
        imdbRating = if (imdbRating == "N/A") "" else imdbRating,
        boxOffice = if (boxOffice == "N/A") "" else boxOffice
    )
}