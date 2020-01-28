package com.awkris.watchamovie.data.datastore

import androidx.paging.DataSource
import com.awkris.watchamovie.data.api.utils.log
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.objectbox.MovieEntity
import com.awkris.watchamovie.data.objectbox.MovieEntity_
import com.awkris.watchamovie.data.room.MovieDatabase
import com.awkris.watchamovie.data.room.entity.Movie
import com.awkris.watchamovie.data.room.mapper.transform
import io.objectbox.Box
import io.objectbox.query.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.koin.core.KoinComponent
import timber.log.Timber


class DiskMovieDataStore(
    private val db: MovieDatabase,
    private val movieBox: Box<MovieEntity>
) : KoinComponent {

    fun saveToWatchlist(movie: MovieDetailResponse): Completable {
        return db.movieDao().insert(transform(movie))
            .doOnError(::log)
    }

    fun saveToWatchlistCoroutine(movie: MovieDetailResponse): Long {
        if (movieBox.get(movie.id.toLong()) == null) {
            movieBox.put(movie.transform())
        }
        with(movieBox.get(movie.id.toLong())) {
            Timber.d("saved movie item with:\n" +
                    "id: $id\n" +
                    "title: $title")
        }
        return if (movieBox.get(movie.id.toLong()) != null) 1 else 0
    }

    fun deleteMovie(id: Int): Completable {
        return db.movieDao().deleteById(id)
            .doOnError(::log)
    }

    fun deleteMovieById(id: Int): Int {
        movieBox.remove(id.toLong())
        return if (movieBox.get(id.toLong()) == null) 1 else 0
    }

    fun findMovie(id: Int): Maybe<Movie> {
        return db.movieDao().getMovie(id)
            .doOnError(::log)
    }

    fun findMovieCoroutine(id: Int): MovieEntity? {
        return movieBox.get(id.toLong())
    }

    fun getAllReminders(): Single<List<Movie>> {
        return db.movieDao().getAllReminders()
            .doOnError(::log)
    }

    fun getAllRemindersCoroutine(): List<MovieEntity> {
        return movieBox.query().equal(MovieEntity_.reminderState, true)
            .build()
            .find()
    }

    fun getWatchList(): DataSource.Factory<Int, Movie> {
        return db.movieDao().getWatchList()
    }

    fun getWatchListBox(): Query<MovieEntity> {
        return movieBox.query().build()
    }

    fun updateReminder(id: Int, setReminder: Boolean): Completable {
        return db.movieDao().updateReminder(id, setReminder)
    }

    fun updateReminderCoroutine(id: Int, setReminder: Boolean): Int {
        val updatedItem = movieBox.get(id.toLong()).apply {
            reminderState = setReminder
        }
        movieBox.put(updatedItem)
        return if (movieBox.get(id.toLong()).reminderState == setReminder) 1 else 0
    }
}