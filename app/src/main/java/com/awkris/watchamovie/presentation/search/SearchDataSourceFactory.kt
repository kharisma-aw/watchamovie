package com.awkris.watchamovie.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.awkris.watchamovie.WatchAMovie.Companion.appComponent
import com.awkris.watchamovie.data.model.response.MovieResponse
import javax.inject.Inject

class SearchDataSourceFactory @Inject constructor() : DataSource.Factory<Int, MovieResponse>() {
    private var keyword = ""
    private var page = 1
    private lateinit var searchDataSource: SearchDataSource
    private val dataSource = MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<Int, MovieResponse> {
        searchDataSource = SearchDataSource(appComponent.repository(), keyword, page)
        dataSource.postValue(searchDataSource)
        return searchDataSource
    }

    fun recreate(keyword: String?, page: Int) {
        if (!keyword.isNullOrEmpty()) this.keyword = keyword
        if (page != this.page) this.page = page
        if (this::searchDataSource.isInitialized) searchDataSource.invalidate()
        create()
    }

    fun getDataSource(): LiveData<SearchDataSource> {
        return dataSource
    }
}