package com.example.movieexplorer.domain.repository

import androidx.paging.PagingData
import com.example.movieexplorer.domain.model.Movie
import com.example.movieexplorer.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun searchMovies(query: String): Flow<List<Movie>>
    fun getMovieDetails(imdbId: String): Flow<MovieDetails?>
    suspend fun refreshMovieDetails(imdbId: String)
    fun isFavourite(imdbId: String): Flow<Boolean>
    suspend fun toggleFavourite(movieDetails: MovieDetails)
    fun getAllFavourites(): Flow<List<Movie>>
    fun getRecentlyViewed(): Flow<List<Movie>>
    fun getSearchHistory(): Flow<List<String>>
    suspend fun saveSearchQuery(query: String)
    fun searchMoviesPaged(query: String): Flow<PagingData<Movie>>
    suspend fun cleanupOldRecentlyViewed(olderThanDays: Int)
}