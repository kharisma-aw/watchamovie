package com.awkris.watchamovie.presentation.nowplaying

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.awkris.watchamovie.R
import com.awkris.watchamovie.utils.Constants
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*


class NowPlayingAdapter : PagedListAdapter<MovieResponse, RecyclerView.ViewHolder>(MovieResponse.DIFF_CALLBACK) {
    lateinit var itemClickListener: MovieItemClickListener
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
            ItemType.ItemMovie.ordinal -> MovieItemViewHolder(
                inflater.inflate(R.layout.item_movie, parent, false)
            )
            else -> throw UnsupportedOperationException("There's no such item type!!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is MovieItemViewHolder -> holder.bind(requireNotNull(getItem(position)))
            is LoadingItemViewHolder -> {}
        }
    }

    inner class MovieItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: MovieResponse) {
            Picasso.get()
                .load(Constants.IMAGE_BASE_URL_500.format(data.posterPath))
                .into(itemView.img_movie_thumbnail)
            itemView.txt_movie_title.text = data.title
            itemView.txt_movie_overview.text = data.overview
            itemView.setOnClickListener { itemClickListener.onItemClicked(data.id) }
        }
    }

    inner class LoadingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    enum class ItemType {
        LoadingProgress,
        ItemMovie
    }
}