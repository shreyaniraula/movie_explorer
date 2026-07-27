package com.example.movieexplorer.di

import android.content.Context
import androidx.room.Room
import com.example.movieexplorer.core.database.dao.FavouriteMovieDao
import com.example.movieexplorer.core.database.MovieExplorerDatabase
import com.example.movieexplorer.core.database.dao.RecentlyViewedDao
import com.example.movieexplorer.core.database.dao.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    // anything scoped @Singleton and living for the app's full lifetime should never hold a reference
    // to something shorter-lived like an Activity, to prevent context leaks so @ApplicationContext instead of @Application
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieExplorerDatabase {
        return Room.databaseBuilder(
            context, MovieExplorerDatabase::class.java,
            "movie_explorer.db"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideFavouriteMovieDao(database: MovieExplorerDatabase): FavouriteMovieDao {
        return database.favouriteMovieDao()
    }

    @Provides
    @Singleton
    fun provideRecentlyViewedDao(database: MovieExplorerDatabase): RecentlyViewedDao {
        return database.recentlyViewedDao()
    }

    @Provides
    @Singleton
    fun provideSearchHistoryDao(database: MovieExplorerDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}