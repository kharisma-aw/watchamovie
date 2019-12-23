package com.awkris.watchamovie.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie"
)
data class Movie(
    @ColumnInfo(name = "id", index = true)
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val backdropPath: String?,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String?,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)