package com.awkris.watchamovie.data.datastore

import androidx.paging.DataSource
import com.awkris.watchamovie.data.api.utils.log
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.room.MovieDatabase
import com.awkris.watchamovie.data.room.entity.Movie
import com.awkris.watchamovie.data.room.mapper.transform
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.koin.core.KoinComponent

class DiskMovieDataStore(private val db: MovieDatabase) : KoinComponent {
    fun saveToWatchlist(movie: MovieDetailResponse): Maybe<Long> {
        return db.movieDao().insert(transform(movie))
            .doOnError(::log)
    }

    fun deleteMovie(id: Int): Completable {
        return db.movieDao().deleteById(id)
            .doOnError(::log)
    }

    fun findMovie(id: Int): Maybe<Movie> {
        return db.movieDao().getMovie(id)
            .doOnError(::log)
    }

    fun getAllReminders(): Single<List<Movie>> {
        return db.movieDao().getAllReminders()
            .doOnError(::log)
    }

    fun getWatchList(): DataSource.Factory<Int, Movie> {
        return db.movieDao().getWatchList()
    }

    fun updateReminder(id: Int, setReminder: Boolean): Completable {
        return db.movieDao().updateReminder(id, setReminder)
    }
}