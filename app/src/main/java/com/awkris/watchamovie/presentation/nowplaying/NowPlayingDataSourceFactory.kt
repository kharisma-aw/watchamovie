package com.awkris.watchamovie.presentation.nowplaying

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.awkris.watchamovie.data.model.response.MovieResponse
import org.koin.core.KoinComponent
import org.koin.core.get

class NowPlayingDataSourceFactory : DataSource.Factory<Int, MovieResponse>(), KoinComponent {
    private lateinit var nowPlayingDataSource: NowPlayingDataSource
    private val _dataSource = MutableLiveData<NowPlayingDataSource>()
    val dataSource: LiveData<NowPlayingDataSource> = _dataSource

    override fun create(): DataSource<Int, MovieResponse> {
        nowPlayingDataSource = get()
        _dataSource.postValue(nowPlayingDataSource)
        return nowPlayingDataSource
    }

    fun recreate() {
        if (this::nowPlayingDataSource.isInitialized) nowPlayingDataSource.invalidate()
        create()
    }
}