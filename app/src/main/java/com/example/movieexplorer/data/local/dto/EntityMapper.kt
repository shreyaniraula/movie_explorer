package com.example.movieexplorer.data.local.dto

import com.example.movieexplorer.core.database.entity.RecentlyViewedEntity
import com.example.movieexplorer.domain.model.MovieDetails


fun RecentlyViewedEntity.toDomain(): MovieDetails {
    return MovieDetails(
        imdbId = imdbId,
        title = title,
        year = year,
        posterUrl = posterUrl,
        plot = plot,
        runtime = runtime,
        genre = genre,
        director = director,
        actors = actors,
        awards = awards,
        imdbRating = imdbRating,
        boxOffice = boxOffice
    )
}