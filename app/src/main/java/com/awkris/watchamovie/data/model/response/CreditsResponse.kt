package com.awkris.watchamovie.data.model.response

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("id")
    val movieId: String,
    @SerializedName("cast")
    val casts: List<Cast>,
    @SerializedName("crew")
    val crews: List<Crew>
)

data class Cast(
    @SerializedName("cast_id")
    val cast_id: Int,
    @SerializedName("character")
    val character: String,
    @SerializedName("credit_id")
    val credit_id: String,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("profile_path")
    val profile_path: String?
)

data class Crew(
    @SerializedName("cast_id")
    val credit_id: Int,
    @SerializedName("character")
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
    val profile_path: String?
)