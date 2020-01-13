package com.awkris.watchamovie.data.repository

import androidx.paging.DataSource
import com.awkris.watchamovie.data.datastore.CloudMovieDataStore
import com.awkris.watchamovie.data.datastore.DiskMovieDataStore
import com.awkris.watchamovie.data.model.PaginatedList
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

    fun getNowPlayingList(region: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return cloudMovieDataStore.getNowPlayingList(region, page)
    }

    fun searchMovie(query: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return cloudMovieDataStore.searchMovie(query, page)
    }

    fun saveToWatchlist(movie: MovieDetailResponse): Completable {
        return diskMovieDataStore.saveToWatchlist(movie)
    }

    fun deleteMovie(id: Int): Completable {
        return diskMovieDataStore.deleteMovie(id)
    }

    fun findMovie(id: Int): Maybe<Movie> {
        return diskMovieDataStore.findMovie(id)
    }

    fun getAllReminders(): Single<List<Movie>> {
        return diskMovieDataStore.getAllReminders()
    }

    fun getWatchList(): DataSource.Factory<Int, Movie> {
        return diskMovieDataStore.getWatchList()
    }

    fun updateReminder(id: Int, setReminder: Boolean): Completable {
        return diskMovieDataStore.updateReminder(id, setReminder)
    }
}