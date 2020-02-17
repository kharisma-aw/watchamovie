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
    private lateinit var networkState: NetworkState

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
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
            else -> throw UnsupportedOperationException("Unknown viewtype!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ItemMovieViewHolder -> holder.bind(requireNotNull(getItem(position)))
            is LoadingItemViewHolder -> {}
        }
    }

    fun setNetworkState(state: NetworkState) {
        val previousState = if (this::networkState.isInitialized) networkState else null
        val hadExtraRow = hasExtraRow()
        networkState = state
        val hasExtraRow = hasExtraRow()
        if (hasExtraRow != hadExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != state) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow(): Boolean {
        return this::networkState.isInitialized && networkState == NetworkState.Loading
    }

    inner class LoadingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    enum class ItemType {
        LoadingProgress,
        ItemMovie
    }
}