package com.example.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movieexplorer.core.network.NetworkModule
import com.example.movieexplorer.data.repository.MovieRepositoryImpl
import com.example.movieexplorer.domain.repository.MovieRepository
import com.example.movieexplorer.presentation.search.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository: MovieRepository =
                MovieRepositoryImpl(NetworkModule.provideOmdbApiService())
            SearchScreen(repository = repository, onMovieClick = { /* Day 4 */ })
        }
    }
}