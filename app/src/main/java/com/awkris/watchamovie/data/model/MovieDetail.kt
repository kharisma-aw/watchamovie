package com.awkris.watchamovie.data.model

import com.awkris.watchamovie.data.model.response.Cast
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse

data class MovieDetail(
    val movieDetail: MovieDetailResponse?,
    val recommendations: List<MovieResponse>,
    val casts: List<Cast>
)