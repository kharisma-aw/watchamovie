package com.awkris.watchamovie.data.api

import com.awkris.watchamovie.data.api.utils.UrlConstants
import com.awkris.watchamovie.data.model.response.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApi {
    @GET(UrlConstants.CREDITS)
    fun getCredits(@Path("movie_id") movieId: Int,
                   @Query("api_key") apiKey: String
    ): Single<CreditsResponse>

    @GET(UrlConstants.CREW_CREDITS)
    fun getCrewCedits(@Path("person_id") personId: Int,
                      @Query("api_key") apiKey: String
    ): Single<CrewCreditsResponse>

    @GET(UrlConstants.MOVIE_DETAIL)
    fun getMovieDetail(@Path("movie_id") movieId: Int,
                       @Query("api_key") apiKey: String
    ): Single<MovieDetailResponse>

    @GET(UrlConstants.NOW_PLAYING)
    fun getNowPlayingList(@Query("api_key") apiKey: String,
                          @Query("region") region: String,
                          @Query("page") page: Int?
    ): Single<NowPlayingResponse>

    @GET(UrlConstants.PERSON_DETAIL)
    fun getPersonDetail(@Path("person_id") personId: Int,
                       @Query("api_key") apiKey: String
    ): Single<PersonDetailResponse>

    @GET(UrlConstants.RECOMMENDATIONS)
    fun getRecommendations(@Path("movie_id") movieId: Int,
                           @Query("api_key") apiKey: String
    ): Single<MovieListGeneralResponse>

    @GET(UrlConstants.SEARCH_MOVIE)
    fun searchMovie(@Query("api_key") apiKey: String,
                    @Query("query") query: String,
                    @Query("page") page: Int?
    ): Single<MovieListGeneralResponse>

    @GET(UrlConstants.UPCOMING)
    fun getUpcomingList(@Query("api_key") apiKey: String,
                        @Query("region") region: String,
                        @Query("page") page: Int?
    ): Single<NowPlayingResponse>
}