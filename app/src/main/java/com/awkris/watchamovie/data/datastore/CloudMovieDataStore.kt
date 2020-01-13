package com.awkris.watchamovie.data.datastore

import com.awkris.watchamovie.BuildConfig
import com.awkris.watchamovie.data.api.MovieDbApi
import com.awkris.watchamovie.data.api.utils.log
import com.awkris.watchamovie.data.model.PaginatedList
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse
import io.reactivex.Single
import org.koin.core.KoinComponent

class CloudMovieDataStore(private val movieDbApi: MovieDbApi) : KoinComponent {
    fun getMovieDetail(movieId: Int): Single<MovieDetailResponse> {
        return movieDbApi.getMovieDetail(movieId, KEY)
            .doOnError(::log)
    }

    fun getNowPlayingList(region: String, page: Int?): Single<PaginatedList<MovieResponse>> {
        return movieDbApi.getNowPlayingList(KEY, region, page)
            .map {
                PaginatedList(it.movieList.filterNotNull(), it.page, it.totalPages, it.totalResults)
            }
            .doOnError(::log)
    }

    fun searchMovie(query: String, page: Int?): Single<PaginatedList<MovieResponse>> {
        return movieDbApi.searchMovie(KEY, query, page)
            .map {
                PaginatedList(it.movieList.filterNotNull(), it.page, it.totalPages, it.totalResults)
            }
            .doOnError(::log)
    }

    companion object {
        const val KEY = BuildConfig.moviedbkey
    }
}