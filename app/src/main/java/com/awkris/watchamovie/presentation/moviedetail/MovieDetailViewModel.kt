package com.awkris.watchamovie.presentation.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.repository.MovieDbRepository
import com.awkris.watchamovie.data.room.entity.Movie
import io.reactivex.CompletableObserver
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MovieDetailViewModel(private val repository: MovieDbRepository) : ViewModel() {
    private val isInWatchlist: MutableLiveData<Boolean> = MutableLiveData()
    private val isReminderEnabled: MutableLiveData<Boolean> = MutableLiveData()
    private val movieDetail: MutableLiveData<MovieDetailResponse> = MutableLiveData()
    private val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    private val disposable = CompositeDisposable()

    fun onScreenCreated(movieId: Int) {
        findMovie(movieId)
        getMovieDetail(movieId)
    }

    fun clear() {
        if (!disposable.isDisposed) disposable.dispose()
    }

    fun isInWatchlist(): LiveData<Boolean> {
        return isInWatchlist
    }

    fun isReminderEnabled(): LiveData<Boolean> {
        return isReminderEnabled
    }

    fun getMovieDetail(): LiveData<MovieDetailResponse> {
        return movieDetail
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return networkState
    }

    fun deleteFromWatchlist(movieId: Int) {
        repository.deleteMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : CompletableObserver {
                    override fun onComplete() {
                        isInWatchlist.postValue(false)
                        isReminderEnabled.postValue(null)
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        networkState.postValue(NetworkState.Error(e.message))
                    }

                }
            )
    }

    fun saveToWatchlist() {
        repository.saveToWatchlist(requireNotNull(getMovieDetail().value))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : CompletableObserver {
                    override fun onComplete() {
                        isInWatchlist.postValue(true)
                        isReminderEnabled.postValue(false)
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        networkState.postValue(NetworkState.Error(e.message))
                    }

                }
            )
    }

    fun updateReminder(movieId: Int, setReminder: Boolean) {
        repository.updateReminder(movieId, setReminder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : CompletableObserver {
                    override fun onComplete() {
                        isReminderEnabled.postValue(setReminder)
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        networkState.postValue(NetworkState.Error(e.message))
                    }

                }
            )
    }

    private fun getMovieDetail(movieId: Int) {
        networkState.postValue(NetworkState.Loading)
        repository.getMovieDetail(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : SingleObserver<MovieDetailResponse> {
                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
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

    private fun findMovie(movieId: Int) {
        repository.findMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : MaybeObserver<Movie> {
                    override fun onSuccess(t: Movie) {
                        isInWatchlist.postValue(true)
                        isReminderEnabled.postValue(t.reminderState)
                    }

                    override fun onComplete() {
                        isInWatchlist.postValue(false)
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        networkState.postValue(NetworkState.Error(e.message))
                    }
                }
            )
    }
}