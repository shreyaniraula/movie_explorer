package com.example.movieexplorer.data.repository

import com.example.movieexplorer.core.database.dao.FavouriteMovieDao
import com.example.movieexplorer.core.database.dao.RecentlyViewedDao
import com.example.movieexplorer.core.database.dao.SearchHistoryDao
import com.example.movieexplorer.core.network.OmdpApiService
import com.example.movieexplorer.data.remote.dto.MovieDetailsDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MovieRepositoryImplTest {
    private val apiService: OmdpApiService = mockk()
    private val favouriteMovieDao: FavouriteMovieDao = mockk()
    private val recentlyViewedDao: RecentlyViewedDao = mockk()
    private val searchHistoryDao: SearchHistoryDao = mockk()

    private lateinit var repository: MovieRepositoryImpl

    // JUnit annotation, runs before every @Test. Used here to construct a fresh repository each time,
    // so tests don't leak mock state between each other.
    @Before
    fun setup() {
        repository =
            MovieRepositoryImpl(apiService, favouriteMovieDao, recentlyViewedDao, searchHistoryDao)
    }

    @Test
    fun `getMovieDetails throws when OMDp returns Response False`() = runTest {
        val errorDto = MovieDetailsDto(
            imdbId = null, title = null, year = null, poster = null, plot = null,
            runtime = null, genre = null, director = null, actors = null, awards = null,
            imdbRating = null, boxOffice = null,
            response = "False",
            error = "Incorrect IMDb ID."
        )

        coEvery { apiService.getMovieDetails("bad_id") } returns errorDto

        try {
            repository.refreshMovieDetails("bad_id")
            error("Expected an exception to be thrown")
        } catch (e: Exception) {
            assertEquals("Incorrect IMDb ID.", e.message)
        }
    }

    @Test
    fun `refreshMovieDetails saves mapped entity to RecentlyViewedDao on success`() = runTest {
        val successDto = MovieDetailsDto(
            imdbId = "tt0096895", title = "Batman", year = "1989",
            poster = "https://example.com/poster.jpg", plot = "A dark knight rises.",
            runtime = "126 min", genre = "Action", director = "Tim Burton",
            actors = "Michael Keaton", awards = "N/A", imdbRating = "7.5",
            boxOffice = "$400,000,000", response = "True", error = null
        )
        coEvery { apiService.getMovieDetails("tt0096895") } returns successDto
        coEvery { recentlyViewedDao.addRecentlyViewed(any()) } returns Unit

        repository.refreshMovieDetails("tt0096895")

        coVerify(exactly = 1) {
            recentlyViewedDao.addRecentlyViewed(match { it.imdbId == "tt0096895" && it.title == "Batman" })
        }
    }
}