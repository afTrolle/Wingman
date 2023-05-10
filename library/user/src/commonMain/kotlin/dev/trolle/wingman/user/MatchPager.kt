package dev.trolle.wingman.user

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.trolle.wingman.user.tinder.Tinder
import dev.trolle.wingman.user.tinder.model.Match
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay


class MatchesPagingSource(
    private val backend: Tinder,
) : PagingSource<String, Match>() {

    override fun getRefreshKey(state: PagingState<String, Match>): String? {
        // Not sure If we should support refresh could be better to just return null always here.
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey as String?
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Match> {
        return try {
            require(params.loadSize < 100) { "Require less than 100 items" }
            val nextPageToken = params.key
            delay(2000)
            val response = backend.matches(params.loadSize, nextPageToken)

            val updatedPageToken = response.data?.nextPageToken
            val matches: List<Match> = response.data?.matches ?: emptyList()

            // Remove this when sealed class implementation is moved into common code
            LoadResult.Page(
                data = matches,
                prevKey = nextPageToken, // Only paging forward.
                nextKey = updatedPageToken, // Null on end of data
            )
        } catch (e: Exception) {
            when (e) {
                is CancellationException -> throw e
                else -> LoadResult.Error(e)
            }
        }
    }

}