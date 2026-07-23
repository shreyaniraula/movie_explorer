package com.example.movieexplorer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    @SerialName("imdbID")
    val imdbId: String? = null,

    @SerialName("Title")
    val title: String? = null,

    @SerialName("Year")
    val year: String? = null,

    @SerialName("Poster")
    val poster: String? = null,

    @SerialName("Plot")
    val plot: String? = null,

    @SerialName("Runtime")
    val runtime: String? = null,

    @SerialName("Genre")
    val genre: String? = null,

    @SerialName("Director")
    val director: String? = null,

    @SerialName("Actors")
    val actors: String? = null,

    @SerialName("Awards")
    val awards: String? = null,

    @SerialName("imdbRating")
    val imdbRating: String? = null,

    @SerialName("BoxOffice")
    val boxOffice: String? = null,

    @SerialName("Response")
    val response: String? = null,

    @SerialName("Error")
    val error: String? = null,
) {
}