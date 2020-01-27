package com.awkris.watchamovie.presentation.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.awkris.watchamovie.data.objectbox.MovieEntity
import com.awkris.watchamovie.data.repository.MovieDbRepository
import io.objectbox.android.ObjectBoxDataSource

class WatchlistViewModel(repository: MovieDbRepository) : ViewModel() {
    private val watchList: LiveData<PagedList<MovieEntity>>
    private val query = repository.getWatchList()

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build()

        watchList = LivePagedListBuilder(
            ObjectBoxDataSource.Factory(query),
            pagedListConfig
        ).build()
    }

    fun getWatchList() = watchList
}