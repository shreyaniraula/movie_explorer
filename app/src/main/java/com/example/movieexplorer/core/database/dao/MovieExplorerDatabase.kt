package com.example.movieexplorer.core.database.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieexplorer.data.local.entity.FavouriteMovieEntity

@Database(
    entities = [FavouriteMovieEntity::class],
    version = 1,
    exportSchema = true
)

// Room's KSP processor generates the real implementation of this abstract class at compile time
abstract class MovieExplorerDatabase : RoomDatabase() {
    abstract fun favouriteMovieDao(): FavouriteMovieDao
}