package com.example.movieexplorer.core.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://www.omdbapi.com/"
    private const val API_KEY = ""

    private val json = Json {

        // Without this, kotlin serialization throws on any unrecognized JSON key by default
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun provideOmdbApiService(): OmdpApiService {

        // Okhttp lets customize interceptors, timeouts and connection pooling
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(API_KEY))
            .addInterceptor(loggingInterceptor)
            .build()

        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        // retrofit reads the interface's annotations via reflection
        // and hands back a working implementation
        return retrofit.create(OmdpApiService::class.java)
    }
}