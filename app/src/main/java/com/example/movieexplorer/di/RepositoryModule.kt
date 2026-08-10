package com.example.movieexplorer.di

import com.example.movieexplorer.data.repository.MovieRepositoryImpl
import com.example.movieexplorer.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // Binds is strictly more efficient at compile time when mapping
    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository
}

// use @Binds for interface→implementation mapping
// use @Provides when you need actual construction logic
// @Binds goes on an abstract fun inside an abstract class marked @Module.
// Used specifically for "interface X should map to implementation Y,"
// nothing else — no real code needed, just a type mapping