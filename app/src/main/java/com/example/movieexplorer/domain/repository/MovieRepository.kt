package com.example.movieexplorer.domain.repository

import com.example.movieexplorer.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository{
    fun searchMovies(query: String): Flow<List<Movie>>
}