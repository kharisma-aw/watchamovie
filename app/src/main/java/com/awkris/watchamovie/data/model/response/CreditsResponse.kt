package com.awkris.watchamovie.data.model.response

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("id")
    val movieId: Int,
    @SerializedName("cast")
    val casts: List<Cast>,
    @SerializedName("crew")
    val crews: List<Crew>
)

data class Cast(
    @SerializedName("cast_id")
    val castId: Int,
    @SerializedName("character")
    val character: String,
    @SerializedName("credit_id")
    val creditId: String,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("profile_path")
    val profilePath: String?
)

data class Crew(
    @SerializedName("credit_id")
    val creditId: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("job")
    val job: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String?
)