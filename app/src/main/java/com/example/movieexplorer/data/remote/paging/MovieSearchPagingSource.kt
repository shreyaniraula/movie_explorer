package com.example.movieexplorer.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieexplorer.core.network.OmdpApiService
import com.example.movieexplorer.data.remote.dto.toDomain
import com.example.movieexplorer.domain.model.Movie

class MovieSearchPagingSource(
    private val apiService: OmdpApiService,
    private val query: String
) : PagingSource<Int, Movie>() {

    // called by Paging whenever it needs a page
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = apiService.searchMovies(query = query, page = page)

            if (response.response == "False") {
                // OMDb returns this specific message for zero matches — that's not a real error,
                // just an empty result. Treat it as a successful empty page instead.
                if (response.error == "Movie not found!") {
                    return LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
                }
                return LoadResult.Error(Exception(response.error ?: "Unknown error"))
            }

            val movies = response.search?.map { it.toDomain() } ?: emptyList()
            val totalResults = response.totalResults?.toIntOrNull() ?: 0
            val maxPage = (totalResults + 9) / 10 //OMDB returns 10 per page

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= maxPage) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // used when Paging needs to reload from scratch(e.g. pull to refresh)
    // figures out which page to restart so the user doesn't jump back to page 1
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}