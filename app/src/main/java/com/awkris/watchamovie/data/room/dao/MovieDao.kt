package com.awkris.watchamovie.data.room.dao

import androidx.paging.DataSource
import androidx.room.*
import com.awkris.watchamovie.data.room.entity.Movie
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoroutine(movie: Movie): Long

    @Query("UPDATE movie SET reminderState = :setReminder WHERE id = :id")
    fun updateReminder(id: Int, setReminder: Boolean): Completable

    @Query("UPDATE movie SET reminderState = :setReminder WHERE id = :id")
    suspend fun updateReminderCoroutine(id: Int, setReminder: Boolean): Int

    @Query("DELETE FROM movie WHERE id = :id")
    fun deleteById(id: Int): Completable

    @Query("DELETE FROM movie WHERE id = :id")
    suspend fun deleteByIdCoroutine(id: Int): Int

    @Query("SELECT * FROM movie WHERE reminderState = 'true'")
    fun getAllReminders(): Single<List<Movie>>

    @Query("SELECT * FROM movie WHERE reminderState = 'true'")
    suspend fun getAllRemindersCoroutine(): List<Movie>

    @Query("SELECT * FROM movie")
    fun getWatchList(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Int): Maybe<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun getMovieCoroutine(id: Int): Movie?
}