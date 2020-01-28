package com.awkris.watchamovie.data.repository

import com.awkris.watchamovie.data.datastore.CloudMovieDataStore
import com.awkris.watchamovie.data.datastore.DiskMovieDataStore
import com.awkris.watchamovie.data.model.PaginatedList
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.objectbox.MovieEntity
import com.awkris.watchamovie.data.room.entity.Movie
import io.objectbox.query.Query
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

    fun saveToWatchlistCoroutine(movie: MovieDetailResponse): Long {
        return diskMovieDataStore.saveToWatchlistCoroutine(movie)
    }

    fun deleteMovie(id: Int): Completable {
        return diskMovieDataStore.deleteMovie(id)
    }

    fun deleteMovieById(id: Int): Int {
        return diskMovieDataStore.deleteMovieById(id)
    }

    fun findMovie(id: Int): Maybe<Movie> {
        return diskMovieDataStore.findMovie(id)
    }

    fun findMovieCoroutine(id: Int): MovieEntity? {
        return diskMovieDataStore.findMovieCoroutine(id)
    }

    fun getAllReminders(): Single<List<Movie>> {
        return diskMovieDataStore.getAllReminders()
    }

    fun getAllRemindersCoroutine(): List<MovieEntity> {
        return diskMovieDataStore.getAllRemindersCoroutine()
    }

    fun getWatchList(): Query<MovieEntity> {
        return diskMovieDataStore.getWatchListBox()
    }

    fun updateReminder(id: Int, setReminder: Boolean): Completable {
        return diskMovieDataStore.updateReminder(id, setReminder)
    }

    fun updateReminderCoroutine(id: Int, setReminder: Boolean): Int {
        return diskMovieDataStore.updateReminderCoroutine(id, setReminder)
    }
}