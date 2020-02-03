package com.awkris.watchamovie.data.repository

import androidx.paging.DataSource
import com.awkris.watchamovie.data.datastore.CloudMovieDataStore
import com.awkris.watchamovie.data.datastore.DiskMovieDataStore
import com.awkris.watchamovie.data.model.PaginatedList
import com.awkris.watchamovie.data.model.response.CreditsResponse
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.room.entity.Movie
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.koin.core.KoinComponent

class MovieDbRepository(
    private val cloudMovieDataStore: CloudMovieDataStore,
    private val diskMovieDataStore: DiskMovieDataStore
) : KoinComponent{
    fun getMovieDetail(movieId: Int): Single<MovieDetailResponse> {
        return cloudMovieDataStore.getMovieDetail(movieId)
    }

    suspend fun getMovieDetailCoroutine(movieId: Int): MovieDetailResponse {
        return cloudMovieDataStore.getMovieDetailCoroutine(movieId)
    }

    fun getNowPlayingList(region: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return cloudMovieDataStore.getNowPlayingList(region, page)
    }

    suspend fun getNowPlayingListCoroutine(
        region: String,
        page: Int? = null
    ): PaginatedList<MovieResponse> {
        return cloudMovieDataStore.getNowPlayingListCoroutine(region, page)
    }

    fun searchMovie(query: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return cloudMovieDataStore.searchMovie(query, page)
    }

    suspend fun searchMovieCoroutine(query: String, page: Int? = null): PaginatedList<MovieResponse> {
        return cloudMovieDataStore.searchMovieCoroutine(query, page)
    }

    fun saveToWatchlist(movie: MovieDetailResponse): Completable {
        return diskMovieDataStore.saveToWatchlist(movie)
    }

    suspend fun saveToWatchlistCoroutine(movie: MovieDetailResponse): Long {
        return diskMovieDataStore.saveToWatchlistCoroutine(movie)
    }

    fun deleteMovie(id: Int): Completable {
        return diskMovieDataStore.deleteMovie(id)
    }

    suspend fun deleteMovieById(id: Int): Int {
        return diskMovieDataStore.deleteMovieById(id)
    }

    fun findMovie(id: Int): Maybe<Movie> {
        return diskMovieDataStore.findMovie(id)
    }

    suspend fun findMovieCoroutine(id: Int): Movie? {
        return diskMovieDataStore.findMovieCoroutine(id)
    }

    fun getAllReminders(): Single<List<Movie>> {
        return diskMovieDataStore.getAllReminders()
    }

    suspend fun getAllRemindersCoroutine(): List<Movie> {
        return diskMovieDataStore.getAllRemindersCoroutine()
    }

    fun getWatchList(): DataSource.Factory<Int, Movie> {
        return diskMovieDataStore.getWatchList()
    }

    fun updateReminder(id: Int, setReminder: Boolean): Completable {
        return diskMovieDataStore.updateReminder(id, setReminder)
    }

    suspend fun updateReminderCoroutine(id: Int, setReminder: Boolean): Int {
        return diskMovieDataStore.updateReminderCoroutine(id, setReminder)
    }

    suspend fun getRecommendations(movieId: Int): List<MovieResponse> {
        return cloudMovieDataStore.getRecommendations(movieId)
    }

    suspend fun getCredits(movieId: Int): CreditsResponse {
        return cloudMovieDataStore.getCredits(movieId)
    }
}