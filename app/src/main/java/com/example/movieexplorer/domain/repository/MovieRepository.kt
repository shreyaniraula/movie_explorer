package com.example.movieexplorer.domain.repository

import com.example.movieexplorer.domain.model.Movie
import com.example.movieexplorer.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun searchMovies(query: String): Flow<List<Movie>>
    fun getMovieDetails(imdbId: String): Flow<MovieDetails?>
    suspend fun refreshMovieDetails(imdbId: String)
    fun isFavourite(imdbId: String): Flow<Boolean>
    suspend fun toggleFavourite(movieDetails: MovieDetails)
}