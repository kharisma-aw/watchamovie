package com.awkris.watchamovie.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.repository.MovieDbRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchDataSource(
    private val repository: MovieDbRepository,
    private val keyword: String
) : PageKeyedDataSource<Int, MovieResponse>() {
    var networkState = MutableLiveData<NetworkState>()
        private set

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieResponse>
    ) {
        if (keyword.isNotEmpty()) {
            networkState.postValue(NetworkState.Loading)
            scope.launch {
                try {
                    val result = repository.searchMovieCoroutine(keyword)
                    networkState.postValue(NetworkState.Success)
                    callback.onResult(
                        result.list,
                        null,
                        if (result.totalPages > 1) result.page + 1 else null
                    )
                } catch (e: Exception) {
                    networkState.postValue(NetworkState.Error(e.message))
                }
            }
        } else {
            callback.onResult(emptyList<MovieResponse>(), null, null)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResponse>) {
        networkState.postValue(NetworkState.Loading)
        scope.launch {
            try {
                val result = repository.searchMovieCoroutine(keyword, params.key)
                networkState.postValue(NetworkState.Success)
                callback.onResult(
                    result.list,
                    if (result.page < result.totalPages) result.page + 1 else null
                )
            } catch (e: Exception) {
                networkState.postValue(NetworkState.Error(e.message))
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResponse>) {
    }
}