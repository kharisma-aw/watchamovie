package com.awkris.watchamovie.presentation.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.repository.MovieDbRepository
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val repository: MovieDbRepository
) : ViewModel() {
    private val movieDetail: MutableLiveData<MovieDetailResponse> = MutableLiveData()
    private val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    private lateinit var disposable: Disposable

    private fun getMovieDetail(movieId: Int) {
        networkState.postValue(NetworkState.Loading)
        repository.getMovieDetail(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : SingleObserver<MovieDetailResponse> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onSuccess(item: MovieDetailResponse) {
                        networkState.postValue(NetworkState.Success)
                        movieDetail.postValue(item)
                    }

                    override fun onError(error: Throwable) {
                        networkState.postValue(NetworkState.Error(error.message))
                    }
                }
            )
    }

    fun clear() {
        if (!disposable.isDisposed) disposable.dispose()
    }

    fun onScreenCreated(movieId: Int) {
        getMovieDetail(movieId)
    }

    fun getMovieDetail(): LiveData<MovieDetailResponse> {
        return movieDetail
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return networkState
    }
}