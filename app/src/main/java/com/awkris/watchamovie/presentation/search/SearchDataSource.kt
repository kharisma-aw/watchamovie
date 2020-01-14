package com.awkris.watchamovie.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.repository.MovieDbRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchDataSource(
    private val repository: MovieDbRepository,
    private val keyword: String,
    private val page: Int
) : PageKeyedDataSource<Int, MovieResponse>() {
    var networkState = MutableLiveData<NetworkState>()
        private set

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieResponse>
    ) {
        if (keyword.isNotEmpty()) {
            networkState.postValue(NetworkState.Loading)
            GlobalScope.launch {
                try {
                    val result = repository.searchMovieCoroutine(keyword, page)
                    networkState.postValue(NetworkState.Success)
                    callback.onResult(
                        result.list,
                        if (result.page > 1) result.page - 1 else null,
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
        GlobalScope.launch {
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
        networkState.postValue(NetworkState.Loading)
        GlobalScope.launch {
            try {
                val result = repository.searchMovieCoroutine(keyword, params.key)
                networkState.postValue(NetworkState.Success)
                callback.onResult(
                    result.list,
                    if (result.page > 1) result.page - 1 else null
                )
            } catch (e: Exception) {
                networkState.postValue(NetworkState.Error(e.message))
            }
        }
    }
}