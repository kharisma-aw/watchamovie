package com.awkris.watchamovie.data.datastore

import com.awkris.watchamovie.BuildConfig
import com.awkris.watchamovie.data.api.MovieDbApi
import com.awkris.watchamovie.data.model.PaginatedList
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse
import io.reactivex.Single
import javax.inject.Inject

class CloudMovieDataStore @Inject constructor(private val movieDbApi: MovieDbApi) {
    fun getMovieDetail(movieId: Int): Single<MovieDetailResponse> {
        return movieDbApi.getMovieDetail(movieId, KEY)
    }

    fun getNowPlayingList(region: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return movieDbApi.getNowPlayingList(KEY, region, page).map {
            PaginatedList(it.movieList, it.page, it.totalPages, it.totalResults)
        }
    }

    fun searchMovie(query: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return movieDbApi.searchMovie(KEY, query, page).map {
            PaginatedList(it.movieList, it.page, it.totalPages, it.totalResults)
        }
    }

    companion object {
        const val KEY = BuildConfig.moviedbkey
    }
}