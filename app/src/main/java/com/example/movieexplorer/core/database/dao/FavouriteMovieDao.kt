package com.example.movieexplorer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieexplorer.core.database.entity.FavouriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteMovieDao {

    // reads are naturally continuous/observable, writes are discrete one-time actions;
    // Room automatically re-queries and re-emits whenever the underlying table changes.

    @Query("SELECT * FROM favourite_movies ORDER BY addedAtTimestamp DESC")
    fun getAllFavourites(): Flow<List<FavouriteMovieEntity>>

    // rather than fetching the whole row just to check presence, EXISTS short-circuits at the database level
    // and returns a single boolean-like 0/1.
    @Query("SELECT EXISTS (SELECT 1 FROM favourite_movies where imdbId = :imdbId)")
    fun isFavourite(imdbId: String): Flow<Boolean>

    // Since imdbId is the primary key, inserting a movie that's already favourite
    // would normally throw a primary key constraint violation.
    // REPLACE means "if it already exists, just overwrite it" — makes addFavorite idempotent (safe to call multiple times without crashing),
    // which matters because a user could theoretically double-tap a favorite button before the UI updates.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavourite(movie: FavouriteMovieEntity)

    @Query("DELETE FROM favourite_movies WHERE imdbId = :imdbId")
    suspend fun removeFavourite(imdbId: String)
}