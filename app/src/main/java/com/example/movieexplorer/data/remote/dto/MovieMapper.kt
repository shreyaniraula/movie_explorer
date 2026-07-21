package com.example.movieexplorer.data.remote.dto

import com.example.movieexplorer.domain.model.Movie

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