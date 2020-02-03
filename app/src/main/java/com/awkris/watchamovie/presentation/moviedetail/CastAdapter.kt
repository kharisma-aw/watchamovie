package com.awkris.watchamovie.presentation.moviedetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.model.response.Cast
import com.awkris.watchamovie.utils.CircularTransformation
import com.awkris.watchamovie.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_cast.view.*

class CastAdapter(
    private val list: List<Cast>
) : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Cast) {
            Picasso.get()
                .load(Constants.IMAGE_BASE_URL_500.format(data.profile_path))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(CircularTransformation())
                .into(itemView.img_cast)
            itemView.txt_cast.text = data.name
        }
    }
}