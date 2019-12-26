package com.awkris.watchamovie.presentation.nowplaying

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.awkris.watchamovie.WatchAMovie.Companion.appComponent
import com.awkris.watchamovie.data.model.response.MovieResponse
import javax.inject.Inject

class NowPlayingDataSourceFactory @Inject constructor(
    private val nowPlayingDataSource: NowPlayingDataSource
) : DataSource.Factory<Int, MovieResponse>() {
    private val dataSource = MutableLiveData<NowPlayingDataSource>()

    override fun create(): DataSource<Int, MovieResponse> {
        dataSource.postValue(nowPlayingDataSource)
        return nowPlayingDataSource
    }

    fun getDataSource() = dataSource
}