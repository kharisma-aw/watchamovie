package com.awkris.watchamovie.data.datastore

import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.room.MovieDatabase
import com.awkris.watchamovie.data.room.mapper.transform
import javax.inject.Inject

class DiskMovieDataStore @Inject constructor(private val db: MovieDatabase) {
    fun saveToWatchlist(movie: MovieDetailResponse) {
        db.movieDao().insert(transform(movie))
    }

    fun saveToWatchlist(movie: MovieResponse) {
        db.movieDao().insert(transform(movie))
    }
}