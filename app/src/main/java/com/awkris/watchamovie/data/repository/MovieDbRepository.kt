package com.awkris.watchamovie.data.repository

import com.awkris.watchamovie.data.datastore.CloudMovieDataStore
import com.awkris.watchamovie.data.datastore.DiskMovieDataStore
import com.awkris.watchamovie.data.model.PaginatedList
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse
import io.reactivex.Single
import javax.inject.Inject

class MovieDbRepository @Inject constructor(
    private val cloudMovieDataStore: CloudMovieDataStore,
    private val diskMovieDataStore: DiskMovieDataStore
) {
    fun getMovieDetail(movieId: Int): Single<MovieDetailResponse> {
        return cloudMovieDataStore.getMovieDetail(movieId)
    }

    fun getNowPlayingList(region: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return cloudMovieDataStore.getNowPlayingList(region, page)
    }

    fun searchMovie(query: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return cloudMovieDataStore.searchMovie(query, page)
    }

    fun saveToWatchlist(movie: MovieDetailResponse) {
        diskMovieDataStore.saveToWatchlist(movie)
    }

    fun saveToWatchlist(movie: MovieResponse) {
        diskMovieDataStore.saveToWatchlist(movie)
    }
}