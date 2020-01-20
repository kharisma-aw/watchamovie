package com.awkris.watchamovie.data.room.dao

import androidx.room.*
import com.awkris.watchamovie.data.room.entity.Movie

@Dao
interface MovieDao {
    @Insert
    fun insert(movie: Movie)

    @Update
    fun update(vararg movies: Movie)

    @Delete
    fun delete(vararg movies: Movie)

    @Query("DELETE FROM movie WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM movie")
    fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Int): Movie?
}