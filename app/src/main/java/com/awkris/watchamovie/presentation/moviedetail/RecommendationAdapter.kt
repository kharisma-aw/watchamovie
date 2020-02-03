package com.awkris.watchamovie.presentation.moviedetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.presentation.common.ItemMovieClickListener
import com.awkris.watchamovie.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recommendation.view.*

class RecommendationAdapter(
    private val list: List<MovieResponse>
) : RecyclerView.Adapter<RecommendationAdapter.RecommendationItemViewHolder>() {
    lateinit var itemMovieClickListener: ItemMovieClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationItemViewHolder {
        return RecommendationItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation, parent, false)
        ).also {
            it.itemMovieClickListener = itemMovieClickListener
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecommendationItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class RecommendationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var itemMovieClickListener: ItemMovieClickListener

        fun bind(data: MovieResponse) {
            Picasso.get()
                .load(Constants.IMAGE_BASE_URL_500.format(data.posterPath))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(itemView.img_movie_thumbnail)
            itemView.txt_movie_title.text = data.title
            itemView.setOnClickListener { itemMovieClickListener.onItemClicked(data.id) }
        }
    }
}