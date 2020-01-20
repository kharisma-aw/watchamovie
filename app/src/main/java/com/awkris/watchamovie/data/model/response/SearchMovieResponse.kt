package com.awkris.watchamovie.data.model.response

import com.google.gson.annotations.SerializedName

data class SearchMovieResponse (
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("results")
    val movieList: List<MovieResponse>
)