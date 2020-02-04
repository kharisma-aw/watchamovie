package com.awkris.watchamovie.presentation.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awkris.watchamovie.data.model.MovieDetailWithAdditionalInfo
import com.awkris.watchamovie.data.model.NetworkState
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
    private val _isInWatchlist: MutableLiveData<Boolean> = MutableLiveData()
    val isInWatchlist: LiveData<Boolean> = _isInWatchlist

    private val _isReminderEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val isReminderEnabled: LiveData<Boolean> = _isReminderEnabled

    private val _movieDetail: MutableLiveData<MovieDetailWithAdditionalInfo> = MutableLiveData()
    val movieDetail: LiveData<MovieDetailWithAdditionalInfo> = _movieDetail

    private val _networkState: MutableLiveData<NetworkState> = MutableLiveData()
    val networkState: LiveData<NetworkState> = _networkState

    private val disposable = CompositeDisposable()

    fun onScreenCreated(movieId: Int) {
        findMovie(movieId)
        getMovieDetail(movieId)
    }

    fun clear() {
        if (!disposable.isDisposed) disposable.dispose()
    }

    fun deleteFromWatchlist(movieId: Int) {
        repository.deleteMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : CompletableObserver {
                    override fun onComplete() {
                        _isInWatchlist.postValue(false)
                        _isReminderEnabled.postValue(null)
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        _networkState.postValue(NetworkState.Error(e.message))
                    }

                }
            )
    }

    fun saveToWatchlist() {
        repository.saveToWatchlist(movieDetail.value!!.movieDetail)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : CompletableObserver {
                    override fun onComplete() {
                        _isInWatchlist.postValue(true)
                        _isReminderEnabled.postValue(false)
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        _networkState.postValue(NetworkState.Error(e.message))
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
                        _isReminderEnabled.postValue(setReminder)
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        _networkState.postValue(NetworkState.Error(e.message))
                    }

                }
            )
    }

    private fun getMovieDetail(movieId: Int) {
        _networkState.postValue(NetworkState.Loading)
        repository.getMovieDetailWithAdditionalInfo(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : SingleObserver<MovieDetailWithAdditionalInfo> {
                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onSuccess(item: MovieDetailWithAdditionalInfo) {
                        _networkState.postValue(NetworkState.Success)
                        _movieDetail.postValue(item)
                    }

                    override fun onError(error: Throwable) {
                        _networkState.postValue(NetworkState.Error(error.message))
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
                        _isInWatchlist.postValue(true)
                        _isReminderEnabled.postValue(t.reminderState)
                    }

                    override fun onComplete() {
                        _isInWatchlist.postValue(false)
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        _networkState.postValue(NetworkState.Error(e.message))
                    }
                }
            )
    }
}