package com.awkris.watchamovie.presentation.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieResponse

class MovieListAdapter :
    PagedListAdapter<MovieResponse, RecyclerView.ViewHolder>(MovieResponse.DIFF_CALLBACK) {
    lateinit var itemMovieClickListener: ItemMovieClickListener
    lateinit var networkState: NetworkState

    override fun getItemViewType(position: Int): Int {
        return if (networkState == NetworkState.Loading && position == itemCount - 1) {
            ItemType.LoadingProgress.ordinal
        } else {
            ItemType.ItemMovie.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            ItemType.LoadingProgress.ordinal -> LoadingItemViewHolder(
                inflater.inflate(R.layout.item_loading, parent, false)
            )
            ItemType.ItemMovie.ordinal -> ItemMovieViewHolder(
                inflater.inflate(R.layout.item_movie, parent, false)
            ).also { it.itemMovieClickListener = itemMovieClickListener }
            else -> throw UnsupportedOperationException("There's no such item type!!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ItemMovieViewHolder -> holder.bind(requireNotNull(getItem(position)))
            is LoadingItemViewHolder -> {}
        }
    }

    inner class LoadingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    enum class ItemType {
        LoadingProgress,
        ItemMovie
    }
}