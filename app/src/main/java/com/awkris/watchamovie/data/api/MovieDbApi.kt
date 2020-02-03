package com.awkris.watchamovie.data.api

import com.awkris.watchamovie.data.api.utils.UrlConstants
import com.awkris.watchamovie.data.model.response.CreditsResponse
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieListGeneralResponse
import com.awkris.watchamovie.data.model.response.NowPlayingResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApi {
    @GET(UrlConstants.NOW_PLAYING)
    fun getNowPlayingList(@Query("api_key") apiKey: String,
                          @Query("region") region: String,
                          @Query("page") page: Int?
    ): Single<NowPlayingResponse>

    @GET(UrlConstants.NOW_PLAYING)
    suspend fun getNowPlayingListCoroutine(@Query("api_key") apiKey: String,
                                   @Query("region") region: String,
                                   @Query("page") page: Int?
    ): NowPlayingResponse

    @GET(UrlConstants.SEARCH_MOVIE)
    fun searchMovie(@Query("api_key") apiKey: String,
                    @Query("query") query: String,
                    @Query("page") page: Int?
    ): Single<MovieListGeneralResponse>

    @GET(UrlConstants.SEARCH_MOVIE)
    suspend fun searchMovieCoroutine(@Query("api_key") apiKey: String,
                                     @Query("query") query: String,
                                     @Query("page") page: Int?
    ): MovieListGeneralResponse

    @GET(UrlConstants.MOVIE_DETAIL)
    fun getMovieDetail(@Path("movie_id") movieId: Int,
                       @Query("api_key") apiKey: String
    ): Single<MovieDetailResponse>

    @GET(UrlConstants.MOVIE_DETAIL)
    suspend fun getMovieDetailCoroutine(@Path("movie_id") movieId: Int,
                                        @Query("api_key") apiKey: String
    ): MovieDetailResponse

    @GET(UrlConstants.RECOMMENDATIONS)
    suspend fun getRecommendations(@Path("movie_id") movieId: Int,
                                   @Query("api_key") apiKey: String
    ): MovieListGeneralResponse

    @GET(UrlConstants.CREDITS)
    suspend fun getCredits(@Path("movie_id") movieId: Int,
                           @Query("api_key") apiKey: String
    ): CreditsResponse
}