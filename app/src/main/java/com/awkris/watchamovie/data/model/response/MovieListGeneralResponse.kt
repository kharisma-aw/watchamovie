package com.awkris.watchamovie.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieListGeneralResponse (
    @Json(name = "page")
    val page: Int,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int,
    @Json(name = "results")
    val movieList: List<MovieResponse>
)