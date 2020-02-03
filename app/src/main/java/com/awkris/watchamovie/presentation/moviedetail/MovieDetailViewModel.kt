package com.awkris.watchamovie.presentation.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.awkris.watchamovie.data.model.MovieDetail
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.Cast
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.repository.MovieDbRepository
import com.awkris.watchamovie.presentation.base.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val repository: MovieDbRepository) : BaseViewModel() {
    private val _isInWatchlist: MutableLiveData<Boolean> = MutableLiveData()
    val isInWatchlist: LiveData<Boolean> = _isInWatchlist

    private val _isReminderEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val isReminderEnabled: LiveData<Boolean> = _isReminderEnabled

    private val movieDetail = MutableLiveData<MovieDetailResponse>()
    private val recommendations = MutableLiveData<List<MovieResponse>>()
    private val casts = MutableLiveData<List<Cast>>()

    private val _movieDetailWithRecommendations = MediatorLiveData<MovieDetail>().apply {
        addSource(movieDetail) {
            val recommendations = value?.recommendations ?: emptyList()
            val cast = value?.casts ?: emptyList()
            postValue(MovieDetail(it, recommendations, cast))
        }
        addSource(recommendations) {
            val movieDetail = value?.movieDetail
            val cast = value?.casts ?: emptyList()
            postValue(MovieDetail(movieDetail, it, cast))
        }
        addSource(casts) {
            val movieDetail = value?.movieDetail
            val recommendations = value?.recommendations ?: emptyList()
            postValue(MovieDetail(movieDetail, recommendations, it))
        }
    }
    val movieDetailWithRecommendations: LiveData<MovieDetail> = _movieDetailWithRecommendations

    private val _networkState: MutableLiveData<NetworkState> = MutableLiveData()
    val networkState: LiveData<NetworkState> = _networkState

    private val disposable = CompositeDisposable()

    fun onScreenCreated(movieId: Int) {
        findMovie(movieId)
        getMovieDetail(movieId)
    }

    override fun clear() {
        super.clear()
        if (!disposable.isDisposed) disposable.dispose()
    }

    fun deleteFromWatchlist(movieId: Int) {
        scope.launch {
            val result = repository.deleteMovieById(movieId)
            if (result == 1) {
                _isInWatchlist.postValue(false)
                _isReminderEnabled.postValue(null)
            }
        }
    }

    fun saveToWatchlist() {
        scope.launch {
            val rowId = repository.saveToWatchlistCoroutine(
                requireNotNull(movieDetail.value)
            )
            if (rowId > 0) {
                _isInWatchlist.postValue(true)
                _isReminderEnabled.postValue(false)
            }
        }
    }

    fun updateReminder(movieId: Int, setReminder: Boolean) {
        scope.launch {
            val result = repository.updateReminderCoroutine(movieId, setReminder)
            if (result == 1) {
                _isReminderEnabled.postValue(setReminder)
            }
        }
    }

    private fun getMovieDetail(movieId: Int) {
        scope.launch {
            _networkState.postValue(NetworkState.Loading)
            movieDetail.postValue(repository.getMovieDetailCoroutine(movieId))
            recommendations.postValue(repository.getRecommendations(movieId))
            casts.postValue(repository.getCredits(movieId).casts)
            _networkState.postValue(NetworkState.Success)
        }
    }

    private fun findMovie(movieId: Int) {
        scope.launch {
            val movie = repository.findMovieCoroutine(movieId)
            _isInWatchlist.postValue(movie != null)
        }
    }
}