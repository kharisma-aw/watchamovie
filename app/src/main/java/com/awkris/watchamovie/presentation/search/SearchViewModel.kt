package com.awkris.watchamovie.presentation.search

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieResponse
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val dataSourceFactory: SearchDataSourceFactory
) : ViewModel() {
    val networkState: LiveData<NetworkState>
    val searchList: LiveData<PagedList<MovieResponse>>

    private val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(20)
        .setPageSize(20)
        .build()


    init {
        networkState = Transformations.switchMap(dataSourceFactory.getDataSource()) { dataSource ->
            dataSource.networkState
        }
        searchList = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    }

    fun search(keyword: String? = null, page: Int = 1) {
        dataSourceFactory.recreate(keyword, page)
    }
}