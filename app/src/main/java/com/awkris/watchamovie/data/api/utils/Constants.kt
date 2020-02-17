package com.awkris.watchamovie.data.api.utils

object UrlConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val CREDITS = "movie/{movie_id}/credits"
    const val CREW_CREDITS = "person/{person_id}/movie_credits"
    const val MOVIE_DETAIL = "movie/{movie_id}"
    const val NOW_PLAYING = "movie/now_playing"
    const val PERSON_DETAIL = "person/{person_id}"
    const val RECOMMENDATIONS = "movie/{movie_id}/recommendations"
    const val SEARCH_MOVIE = "search/movie"
    const val UPCOMING = "movie/upcoming"
}