package com.awkris.watchamovie.data.model.response

import com.google.gson.annotations.SerializedName

data class NowPlayingResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("dates")
    val dates: DateResponse,
    @SerializedName("results")
    val movieList: List<MovieResponse>
)

data class DateResponse(
    @SerializedName("maximum")
    val maximum: String,
    @SerializedName("minimum")
    val minimum: String
)