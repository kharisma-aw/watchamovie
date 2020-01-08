package com.awkris.watchamovie.data.room.mapper

import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.room.entity.Movie

fun transform(movieDetailResponse: MovieDetailResponse): Movie {
    return with(movieDetailResponse) {
        Movie(
            id = id,
            adult = adult,
            backdropPath = backdropPath,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            releaseDate = releaseDate,
            title = title,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }
}