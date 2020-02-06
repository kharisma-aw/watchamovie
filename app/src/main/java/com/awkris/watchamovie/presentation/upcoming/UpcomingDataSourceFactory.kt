package com.awkris.watchamovie.presentation.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.awkris.watchamovie.data.model.response.MovieResponse
import org.koin.core.KoinComponent
import org.koin.core.get

class UpcomingDataSourceFactory: DataSource.Factory<Int, MovieResponse>(), KoinComponent {
    private lateinit var upcomingDataSource: UpcomingDataSource
    private val _dataSource = MutableLiveData<UpcomingDataSource>()
    val dataSource: LiveData<UpcomingDataSource> = _dataSource

    override fun create(): DataSource<Int, MovieResponse> {
        upcomingDataSource = get()
        _dataSource.postValue(upcomingDataSource)
        return upcomingDataSource
    }

    fun recreate() {
        if (this::upcomingDataSource.isInitialized) upcomingDataSource.invalidate()
        create()
    }
}