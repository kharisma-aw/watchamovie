package com.awkris.watchamovie.presentation.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.data.room.entity.Movie
import com.awkris.watchamovie.utils.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import kotlinx.android.synthetic.main.item_movie.view.*

class ItemMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var itemMovieClickListener: ItemMovieClickListener

    fun bind(data: MovieResponse) {
        data.backdropPath?.let {
            Glide.with(itemView.context)
                .load(Constants.IMAGE_BASE_URL_500.format(it))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .downsample(DownsampleStrategy.AT_MOST)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(itemView.img_movie_thumbnail)
        }
        itemView.txt_movie_title.text = data.title
        itemView.setOnClickListener { itemMovieClickListener.onItemClicked(data.id) }
    }

    fun bind(data: Movie) {
        data.backdropPath?.let {
            Glide.with(itemView.context)
                .load(Constants.IMAGE_BASE_URL_500.format(it))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .downsample(DownsampleStrategy.AT_MOST)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(itemView.img_movie_thumbnail)
        }
        itemView.txt_movie_title.text = data.title
        itemView.setOnClickListener { itemMovieClickListener.onItemClicked(data.id) }
    }
}