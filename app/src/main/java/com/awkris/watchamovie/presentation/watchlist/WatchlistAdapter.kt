package com.awkris.watchamovie.presentation.watchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.room.entity.Movie
import com.awkris.watchamovie.presentation.common.ItemMovieClickListener
import com.awkris.watchamovie.presentation.common.ItemMovieViewHolder

class WatchlistAdapter :
    PagedListAdapter<Movie, ItemMovieViewHolder>(Movie.DIFF_CALLBACK) {
    lateinit var itemMovieClickListener: ItemMovieClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMovieViewHolder {
        return ItemMovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemMovieViewHolder, position: Int) {
        holder.bind(requireNotNull(getItem(position)), itemMovieClickListener)
    }
}