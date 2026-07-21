package com.example.movieexplorer.core.network

import com.example.movieexplorer.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit uses annotation processing + reflection at app startup
// to build a real implementation of this interface; no need to write HTTP call logic

interface OmdpApiService {
    @GET(".")

    // Retrofit has first-class coroutine support;
    // mark the method suspend, and it automatically runs the network call on a background dispatcher
    // and resumes the coroutine with result

    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("page") page: Int = 1,
    ): SearchResponseDto
}