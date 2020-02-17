package com.awkris.watchamovie.presentation.moviedetail

import androidx.lifecycle.*
import com.awkris.watchamovie.data.model.Event
import com.awkris.watchamovie.data.model.MovieDetailWithAdditionalInfo
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.repository.MovieDbRepository
import com.awkris.watchamovie.data.room.entity.Movie
import com.awkris.watchamovie.utils.isUpcoming
import io.reactivex.CompletableObserver
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class MovieDetailViewModel : ViewModel(), KoinComponent {
    private val repository: MovieDbRepository by inject()
    private val _isInWatchlist: MutableLiveData<Boolean> = MutableLiveData()
    val isInWatchlist: LiveData<Boolean>
        get() = _isInWatchlist

    private val _isReminderEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val isReminderEnabled: LiveData<Boolean>
        get() = _isReminderEnabled

    private val _isUpcoming: MutableLiveData<Boolean> = MutableLiveData()
    val isUpcoming: LiveData<Boolean>
        get() = _isUpcoming

    private val _movieDetail: MutableLiveData<MovieDetailWithAdditionalInfo> = MutableLiveData()
    val movieDetail: LiveData<MovieDetailWithAdditionalInfo>
        get() = _movieDetail

    private val _networkState: MutableLiveData<Event<NetworkState>> = MutableLiveData()
    val networkState: LiveData<Event<NetworkState>>
        get() = _networkState

    private val disposable = CompositeDisposable()

    lateinit var lifecycle: Lifecycle

    fun onScreenCreated(movieId: Int) {
        findMovie(movieId)
        getMovieDetail(movieId)
        lifecycle.addObserver(
            object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun destroyListener() {
                    Timber.d("ondestroy listener called")
                    disposable.clear()
                }
            }
        )
    }

//    fun clear() {
//        if (!disposable.isDisposed) disposable.dispose()
//    }

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
                        _networkState.postValue(Event(NetworkState.Error(e.message)))
                    }

                }
            )
    }

    fun saveToWatchlist() {
        repository.saveToWatchlist(movieDetail.value!!.movieDetail)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : MaybeObserver<Long> {
                    override fun onSuccess(t: Long) {
                        findMovie(t.toInt())
                    }

                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        _networkState.postValue(Event(NetworkState.Error(e.message)))
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
                        _networkState.postValue(Event(NetworkState.Error(e.message)))
                    }

                }
            )
    }

    private fun getMovieDetail(movieId: Int) {
        _networkState.postValue(Event(NetworkState.Loading))
        repository.getMovieDetailWithAdditionalInfo(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : SingleObserver<MovieDetailWithAdditionalInfo> {
                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onSuccess(item: MovieDetailWithAdditionalInfo) {
                        _networkState.postValue(Event(NetworkState.Success))
                        _movieDetail.postValue(item)
                    }

                    override fun onError(error: Throwable) {
                        _networkState.postValue(Event(NetworkState.Error(error.message)))
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
                        _isUpcoming.postValue(isUpcoming(t.releaseDate))
                    }

                    override fun onComplete() {
                        _isInWatchlist.postValue(false)
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        _networkState.postValue(Event(NetworkState.Error(e.message)))
                    }
                }
            )
    }
}