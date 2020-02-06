package com.awkris.watchamovie.presentation.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieResponse

class UpcomingViewModel(
    private val dataSourceFactory: UpcomingDataSourceFactory
) : ViewModel() {
    val networkState: LiveData<NetworkState>
    val nowPlayingList: LiveData<PagedList<MovieResponse>>

    init {
        networkState = Transformations.switchMap(dataSourceFactory.dataSource) { dataSource ->
            dataSource.networkState
        }

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build()

        nowPlayingList = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    }

    fun refresh() {
        dataSourceFactory.recreate()
    }}