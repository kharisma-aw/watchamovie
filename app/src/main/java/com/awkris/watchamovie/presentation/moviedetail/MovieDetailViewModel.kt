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
    private val isInWatchlist: MutableLiveData<Boolean> = MutableLiveData()
    private val isReminderEnabled: MutableLiveData<Boolean> = MutableLiveData()
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

    private val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    private val disposable = CompositeDisposable()

    fun onScreenCreated(movieId: Int) {
        findMovie(movieId)
        getMovieDetail(movieId)
    }

    override fun clear() {
        super.clear()
        if (!disposable.isDisposed) disposable.dispose()
    }

    fun isInWatchlist(): LiveData<Boolean> {
        return isInWatchlist
    }

    fun isReminderEnabled(): LiveData<Boolean> {
        return isReminderEnabled
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return networkState
    }

    fun deleteFromWatchlist(movieId: Int) {
        scope.launch {
            val result = repository.deleteMovieById(movieId)
            if (result == 1) {
                isInWatchlist.postValue(false)
                isReminderEnabled.postValue(null)
            }
        }
    }

    fun saveToWatchlist() {
        scope.launch {
            val rowId = repository.saveToWatchlistCoroutine(
                requireNotNull(movieDetail.value)
            )
            if (rowId != null && rowId > 0) {
                isInWatchlist.postValue(true)
                isReminderEnabled.postValue(false)
            }
        }
    }

    fun updateReminder(movieId: Int, setReminder: Boolean) {
        scope.launch {
            val result = repository.updateReminderCoroutine(movieId, setReminder)
            if (result == 1) {
                isReminderEnabled.postValue(setReminder)
            }
        }
    }

    private fun getMovieDetail(movieId: Int) {
        scope.launch {
            networkState.postValue(NetworkState.Loading)
            movieDetail.postValue(repository.getMovieDetailCoroutine(movieId))
            recommendations.postValue(repository.getRecommendations(movieId))
            casts.postValue(repository.getCredits(movieId).casts)
            networkState.postValue(NetworkState.Success)
        }
    }

    private fun findMovie(movieId: Int) {
        scope.launch {
            val movie = repository.findMovieCoroutine(movieId)
            isInWatchlist.postValue(movie != null)
        }
    }
}