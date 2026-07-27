package com.example.movieexplorer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieexplorer.core.database.dao.FavouriteMovieDao
import com.example.movieexplorer.core.database.dao.RecentlyViewedDao
import com.example.movieexplorer.core.database.dao.SearchHistoryDao
import com.example.movieexplorer.core.database.entity.RecentlyViewedEntity
import com.example.movieexplorer.core.database.entity.SearchHistoryEntity
import com.example.movieexplorer.core.database.entity.FavouriteMovieEntity

@Database(
    entities = [
        FavouriteMovieEntity::class,
        RecentlyViewedEntity::class, SearchHistoryEntity::class,
    ],
    version = 1,
    exportSchema = true
)

// Room's KSP processor generates the real implementation of this abstract class at compile time
abstract class MovieExplorerDatabase : RoomDatabase() {
    abstract fun favouriteMovieDao(): FavouriteMovieDao
    abstract fun recentlyViewedDao(): RecentlyViewedDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}