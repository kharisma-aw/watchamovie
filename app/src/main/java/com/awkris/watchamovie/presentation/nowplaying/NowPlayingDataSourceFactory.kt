package com.awkris.watchamovie.presentation.nowplaying

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.awkris.watchamovie.WatchAMovie.Companion.appComponent
import com.awkris.watchamovie.data.model.response.MovieResponse
import javax.inject.Inject

class NowPlayingDataSourceFactory @Inject constructor() : DataSource.Factory<Int, MovieResponse>() {
    private lateinit var nowPlayingDataSource: NowPlayingDataSource
    private val dataSource = MutableLiveData<NowPlayingDataSource>()

    override fun create(): DataSource<Int, MovieResponse> {
        nowPlayingDataSource = appComponent.nowPlayingDataSource()
        dataSource.postValue(nowPlayingDataSource)
        return nowPlayingDataSource
    }

    fun recreate() {
        nowPlayingDataSource.invalidate()
        create()
    }

    fun getDataSource() = dataSource
}