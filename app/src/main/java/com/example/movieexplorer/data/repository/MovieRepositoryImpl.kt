package com.example.movieexplorer.data.repository

import com.example.movieexplorer.core.network.OmdpApiService
import com.example.movieexplorer.data.remote.dto.toDomain
import com.example.movieexplorer.domain.model.Movie
import com.example.movieexplorer.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: OmdpApiService
) : MovieRepository {

    // flow instead of just suspend fun for later
    // when it needs to combine offline-first and api approach
    override fun searchMovies(query: String): Flow<List<Movie>> = flow {
        val response = apiService.searchMovies(query = query)

        if (response.response == "False") {
            throw Exception(response.error ?: "Unknown error occurred")
        }

        val movies = response.search?.map { it.toDomain() } ?: emptyList()
        emit(movies)
    }
}