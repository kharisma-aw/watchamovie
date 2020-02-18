package com.awkris.watchamovie.presentation.nowplaying

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.PaginatedList
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.repository.MovieDbRepository
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*


class NowPlayingDataSource(
    private val repository: MovieDbRepository
) : PageKeyedDataSource<Int, MovieResponse>() {
    var networkState = MutableLiveData<NetworkState>()
        private set
    private val compositeDisposable = CompositeDisposable()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieResponse>
    ) {
        networkState.postValue(NetworkState.Loading)
        repository.getNowPlayingList(Locale.getDefault().country).subscribe(
            object : SingleObserver<PaginatedList<MovieResponse>> {
                override fun onSuccess(t: PaginatedList<MovieResponse>) {
                    networkState.postValue(NetworkState.Success)
                    callback.onResult(
                        t.list,
                        null,
                        if (t.totalPages > 1) t.page + 1 else null
                    )
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    networkState.postValue(NetworkState.Error(e.message))
                }

            }
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResponse>) {
        networkState.postValue(NetworkState.Loading)
        repository.getNowPlayingList(Locale.getDefault().country, params.key).subscribe(
            object : SingleObserver<PaginatedList<MovieResponse>> {
                override fun onSuccess(t: PaginatedList<MovieResponse>) {
                    networkState.postValue(NetworkState.Success)
                    callback.onResult(t.list, if (t.page < t.totalPages) t.page + 1 else null)
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    networkState.postValue(NetworkState.Error(e.message))
                }

            }
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResponse>) {
    }

    override fun invalidate() {
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
        super.invalidate()
    }
}