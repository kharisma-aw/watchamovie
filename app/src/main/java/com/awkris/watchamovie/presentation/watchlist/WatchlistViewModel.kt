package com.awkris.watchamovie.presentation.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.awkris.watchamovie.data.repository.MovieDbRepository
import com.awkris.watchamovie.data.room.entity.Movie

class WatchlistViewModel(repository: MovieDbRepository) : ViewModel() {
    private val watchList: LiveData<PagedList<Movie>>
    private val dataSourceFactory = repository.getWatchList()

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build()

        watchList = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    }

    fun getWatchList() = watchList
}