package com.awkris.watchamovie.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NowPlayingResponse(
    @Json(name = "page")
    val page: Int,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int,
    @Json(name = "dates")
    val dates: DateResponse,
    @Json(name = "results")
    val movieList: List<MovieResponse>
)

@JsonClass(generateAdapter = true)
data class DateResponse(
    @Json(name = "maximum")
    val maximum: String,
    @Json(name = "minimum")
    val minimum: String
)