package com.awkris.watchamovie.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.awkris.watchamovie.data.model.response.MovieResponse
import org.koin.core.KoinComponent
import org.koin.core.get

class SearchDataSourceFactory : DataSource.Factory<Int, MovieResponse>(), KoinComponent {
    private var keyword = ""
    private lateinit var searchDataSource: SearchDataSource
    private val dataSource = MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<Int, MovieResponse> {
        searchDataSource = SearchDataSource(get(), keyword)
        dataSource.postValue(searchDataSource)
        return searchDataSource
    }

    fun recreate(keyword: String?) {
        if (!keyword.isNullOrEmpty()) this.keyword = keyword
        invalidate()
        create()
    }

    fun getDataSource(): LiveData<SearchDataSource> {
        return dataSource
    }

    fun invalidate() {
        if (this::searchDataSource.isInitialized) searchDataSource.invalidate()
    }
}