package com.example.movieexplorer.data.repository

import com.example.movieexplorer.core.database.dao.FavouriteMovieDao
import com.example.movieexplorer.core.database.entity.FavouriteMovieEntity
import com.example.movieexplorer.core.network.OmdpApiService
import com.example.movieexplorer.data.remote.dto.toDomain
import com.example.movieexplorer.domain.model.Movie
import com.example.movieexplorer.domain.model.MovieDetails
import com.example.movieexplorer.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: OmdpApiService,
    private val favouriteMovieDao: FavouriteMovieDao,
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

    override fun getMovieDetails(imdbId: String): Flow<MovieDetails> = flow {
        val response = apiService.getMovieDetails(imdbId)

        if (response.response == "False") {
            throw Exception(response.error ?: "Unknown error occurred")
        }
        emit(response.toDomain())
    }

    override fun isFavourite(imdbId: String): Flow<Boolean> {
        return favouriteMovieDao.isFavourite(imdbId)
    }

    override suspend fun toggleFavourite(movieDetails: MovieDetails) {
        val isCurrentlyFavourite = favouriteMovieDao.isFavourite(movieDetails.imdbId).first()
        if (isCurrentlyFavourite) {
            favouriteMovieDao.removeFavourite(movieDetails.imdbId)
        }else{
            favouriteMovieDao.addFavourite(
                FavouriteMovieEntity(
                    imdbId = movieDetails.imdbId,
                    title = movieDetails.title,
                    year = movieDetails.year,
                    posterUrl = movieDetails.posterUrl,
                    type = "movie",
                    addedAtTimestamp = System.currentTimeMillis()
                )
            )
        }
    }
}