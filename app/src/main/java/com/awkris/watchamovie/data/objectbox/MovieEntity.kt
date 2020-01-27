package com.awkris.watchamovie.data.objectbox

import androidx.recyclerview.widget.DiffUtil
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class MovieEntity(
    @Id(assignable = true)
    var id: Long,
    var adult: Boolean,
    var backdropPath: String?,
    var originalLanguage: String,
    var originalTitle: String,
    var overview: String?,
    var popularity: Double,
    var posterPath: String?,
    var releaseDate: String,
    var title: String,
    var video: Boolean,
    var voteAverage: Double,
    var voteCount: Int,
    var reminderState: Boolean = false
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}