package com.awkris.watchamovie.data.model.response

import com.google.gson.annotations.SerializedName

data class Collection(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("poster_path")
    val posterPath: String?
)

data class Company(
    @SerializedName("id")
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String?
)

data class Country(
    @SerializedName("iso_3166_1")
    val code: String,
    @SerializedName("name")
    val name: String
)

data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class Language(
    @SerializedName("iso_639_1")
    val code: String,
    @SerializedName("name")
    val name: String
)

data class MovieDetailResponse(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: Collection?,
    @SerializedName("budget")
    val budget: Int,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("homepage")
    val homepage: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<Company>,
    @SerializedName("production_countries")
    val productionCountries: List<Country>,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("revenue")
    val revenue: Int,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<Language>,
    @SerializedName("status")
    val status: String,
    @SerializedName("tagline")
    val tagline: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)