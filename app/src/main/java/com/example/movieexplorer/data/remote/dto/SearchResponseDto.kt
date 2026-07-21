package com.example.movieexplorer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Serializable maps a wire field to a clean Kotlin property name
@Serializable
data class SearchResponseDto(
    @SerialName("Search")
    val search: List<MovieDto>? = null,

    @SerialName("totalResults")
    val totalResults: String? = null,

    @SerialName("Response")
    val response: String,

    @SerialName("Error")
    val error: String? = null,
)

@Serializable
data class MovieDto(
    @SerialName("imdbID")
    val imdbId: String,

    @SerialName("Title")
    val title: String,

    @SerialName("Year")
    val year: String,

    @SerialName("Poster")
    val poster: String,

    @SerialName("Type")
    val type: String,
)