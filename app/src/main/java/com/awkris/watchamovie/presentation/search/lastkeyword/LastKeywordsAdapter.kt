package com.awkris.watchamovie.presentation.search.lastkeyword

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.awkris.watchamovie.R
import kotlinx.android.synthetic.main.item_suggestion.view.*

class LastKeywordsAdapter : RecyclerView.Adapter<LastKeywordsAdapter.KeywordViewHolder>() {
    lateinit var itemClickListener: KeywordClickListener
    private var lastKeywords = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        return KeywordViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_suggestion, parent, false)
        )
    }

    override fun getItemCount(): Int = lastKeywords.size

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(lastKeywords[position], itemClickListener)
    }

    fun submitList(list: List<String>) {
        if (list.isNotEmpty()) {
            lastKeywords.apply {
                clear()
                addAll(list)
            }
        }
        notifyDataSetChanged()
    }

    inner class KeywordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: String, clickListener: KeywordClickListener) {
            (itemView as TextView).apply {
                text = data
                setOnClickListener {
                    clickListener.onKeywordClicked(data)
                }
            }
        }
    }
}