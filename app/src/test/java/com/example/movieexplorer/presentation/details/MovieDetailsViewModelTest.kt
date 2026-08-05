package com.example.movieexplorer.presentation.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.movieexplorer.MainDispatcherRule
import com.example.movieexplorer.domain.model.MovieDetails
import com.example.movieexplorer.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MovieDetailsViewModelTest {
    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    // creates a fake object implementing the MovieRepository interface,
    // no real network/Room involved.
    private val repository: MovieRepository = mockk()

    private val sampleMovie = MovieDetails(
        imdbId = "tt0096895",
        title = "Batman",
        year = "1989",
        posterUrl = "https://example.com/poster.jpg",
        plot = "A dark knight rises.",
        runtime = "126 min",
        genre = "Action",
        director = "Tim Burton",
        actors = "Michael Keaton",
        awards = "N/A",
        imdbRating = "7.5",
        boxOffice = "$400,000,000"
    )

    private fun createViewModel(): MovieDetailsViewModel {
        every { repository.isFavourite(any()) } returns flowOf(false)
        val savedStateHandle = SavedStateHandle(mapOf("imdbId" to "tt0096895"))
        return MovieDetailsViewModel(savedStateHandle, repository)
    }

    @Test
    fun `when cached data exists uiState emits Success`() = runTest {
        // every for regular functions
        every { repository.getMovieDetails("tt0096895") } returns flowOf(sampleMovie)
        // coEvery for suspend functions
        coEvery { repository.refreshMovieDetails("tt0096895") } returns Unit

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem() is MovieDetailsUiState.Loading)
            val state = awaitItem()
            assertTrue(state is MovieDetailsUiState.Success)

            assertEquals("Batman", (state as MovieDetailsUiState.Success).movieDetails.title)
        }
    }

    @Test
    fun `when no cache and refresh fails, ui emits Error`() = runTest {
        every { repository.getMovieDetails("tt0096895") } returns flowOf(null)
        coEvery { repository.refreshMovieDetails("tt0096895") } throws Exception("Movie not found")

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem() is MovieDetailsUiState.Loading)
            val state = awaitItem()
            assertTrue(state is MovieDetailsUiState.Error)
        }
    }

    @Test
    fun `onFavouriteClick calls toggleFavourite with current movie`() = runTest {
        every { repository.getMovieDetails("tt0096895") } returns flowOf(sampleMovie)
        coEvery { repository.refreshMovieDetails("tt0096895") } returns Unit
        coEvery { repository.toggleFavourite(sampleMovie) } returns Unit

        val viewModel = createViewModel()

        // .test { } starts collecting the Flow/StateFlow,
        // awaitItem() suspends until the next emission arrives, then returns it
        viewModel.uiState.test {
            assertTrue(awaitItem() is MovieDetailsUiState.Loading)

            val state = awaitItem()
            assertTrue(state is MovieDetailsUiState.Success)
            viewModel.onFavouriteClick()
            advanceUntilIdle()
            coVerify(exactly = 1) { repository.toggleFavourite(sampleMovie) }
        }
    }
}