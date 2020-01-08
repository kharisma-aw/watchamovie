package com.awkris.watchamovie.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.awkris.watchamovie.data.datastore.CloudMovieDataStore
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.PaginatedList
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.repository.MovieDbRepository
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.*
import javax.inject.Inject

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
            repository.searchMovie(keyword, page).subscribe(
                object : SingleObserver<PaginatedList<MovieResponse>> {
                    override fun onSuccess(t: PaginatedList<MovieResponse>) {
                        networkState.postValue(NetworkState.Success)
                        callback.onResult(
                            t.list,
                            if (t.page > 1) t.page - 1 else null,
                            if (t.totalPages > 1) t.page + 1 else null
                        )
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        networkState.postValue(NetworkState.Error(e.message))
                    }

                }
            )
        } else {
            networkState.postValue(NetworkState.Success)
            callback.onResult(emptyList<MovieResponse>(), null, null)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResponse>) {
        networkState.postValue(NetworkState.Loading)
        repository.searchMovie(keyword, params.key).subscribe(
            object : SingleObserver<PaginatedList<MovieResponse>> {
                override fun onSuccess(t: PaginatedList<MovieResponse>) {
                    networkState.postValue(NetworkState.Success)
                    callback.onResult(t.list, if (t.page < t.totalPages) t.page + 1 else null)
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    networkState.postValue(NetworkState.Error(e.message))
                }

            }
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResponse>) {
        networkState.postValue(NetworkState.Loading)
        repository.searchMovie(keyword, params.key).subscribe(
            object : SingleObserver<PaginatedList<MovieResponse>> {
                override fun onSuccess(t: PaginatedList<MovieResponse>) {
                    networkState.postValue(NetworkState.Success)
                    callback.onResult(t.list, if (t.page > 1) t.page - 1 else null)
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    networkState.postValue(NetworkState.Error(e.message))
                }

            }
        )
    }
}