package com.awkris.watchamovie.presentation.nowplaying

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.repository.MovieDbRepository
import kotlinx.coroutines.*
import java.util.*


class NowPlayingDataSource(
    private val repository: MovieDbRepository
) : PageKeyedDataSource<Int, MovieResponse>() {
    var networkState = MutableLiveData<NetworkState>()
        private set

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieResponse>
    ) {
        networkState.postValue(NetworkState.Loading)
        scope.launch {
            try {
                val result = repository.getNowPlayingListCoroutine(Locale.getDefault().country)
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
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResponse>) {
        networkState.postValue(NetworkState.Loading)
        scope.launch {
            try {
                val result = repository.getNowPlayingListCoroutine(Locale.getDefault().country, params.key)
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