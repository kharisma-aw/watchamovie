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

    suspend fun getMovieDetailCoroutine(movieId: Int): MovieDetailResponse {
        return movieDbApi.getMovieDetailCoroutine(movieId, KEY)
    }

    fun getNowPlayingList(region: String, page: Int?): Single<PaginatedList<MovieResponse>> {
        return movieDbApi.getNowPlayingList(KEY, region, page)
            .map {
                PaginatedList(it.movieList.filterNotNull(), it.page, it.totalPages, it.totalResults)
            }
            .doOnError(::log)
    }

    suspend fun getNowPlayingListCoroutine(
        region: String,
        page: Int?
    ): PaginatedList<MovieResponse> {
        val response = movieDbApi.getNowPlayingListCoroutine(KEY, region, page)
        return with(response) {
            PaginatedList(movieList.filterNotNull(), this.page, totalPages, totalResults)
        }
    }

    fun searchMovie(query: String, page: Int?): Single<PaginatedList<MovieResponse>> {
        return movieDbApi.searchMovie(KEY, query, page)
            .map {
                PaginatedList(it.movieList.filterNotNull(), it.page, it.totalPages, it.totalResults)
            }
            .doOnError(::log)
    }

    suspend fun searchMovieCoroutine(query: String, page: Int?): PaginatedList<MovieResponse> {
        val response = movieDbApi.searchMovieCoroutine(KEY, query, page)
        return with(response) {
            PaginatedList(movieList.filterNotNull(), this.page, totalPages, totalResults)
        }
    }

    companion object {
        const val KEY = BuildConfig.moviedbkey
    }
}