package com.example.movieexplorer.core.network

import com.example.movieexplorer.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

// Hilt is built on top of Dagger; Dagger is the underlying compile-time DI framework(very verbose).
// Hilt provides standard, predefined android components.

// Module and InstallIn(SingletonComponent::class) tells Hilt "these @Provides functions are
// recipes for building types, and live in the app-wide singleton scope"
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://www.omdbapi.com/"
    private const val API_KEY = BuildConfig.OMDB_API_KEY

    // @Provides tells Hilt how to create an object when Hilt can't create it automatically.
    // @Singleton creates one instance for the entire app lifetime.
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(API_KEY))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {

            // Without this, kotlin serialization throws on any unrecognized JSON key by default
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideOmdbApiService(retrofit: Retrofit): OmdpApiService {
        return retrofit.create(OmdpApiService::class.java)
    }
}