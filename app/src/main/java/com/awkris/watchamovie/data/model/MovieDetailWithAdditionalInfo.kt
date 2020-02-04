package com.awkris.watchamovie.data.model

import com.awkris.watchamovie.data.model.response.Cast
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse

data class MovieDetailWithAdditionalInfo(
    val movieDetail: MovieDetailResponse,
    val casts: List<Cast>,
    val recommendations: List<MovieResponse>
)