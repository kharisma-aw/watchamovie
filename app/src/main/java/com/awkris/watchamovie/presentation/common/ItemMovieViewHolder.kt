package com.awkris.watchamovie.presentation.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.room.entity.Movie
import com.awkris.watchamovie.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class ItemMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var itemMovieClickListener: ItemMovieClickListener

    fun bind(data: MovieResponse) {
        Picasso.get()
            .load(Constants.IMAGE_BASE_URL_500.format(data.backdropPath))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(itemView.img_movie_thumbnail)
        itemView.txt_movie_title.text = data.title
        itemView.setOnClickListener { itemMovieClickListener.onItemClicked(data.id) }
    }

    fun bind(data: Movie) {
        Picasso.get()
            .load(Constants.IMAGE_BASE_URL_500.format(data.backdropPath))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(itemView.img_movie_thumbnail)
        itemView.txt_movie_title.text = data.title
        itemView.setOnClickListener { itemMovieClickListener.onItemClicked(data.id) }
    }
}