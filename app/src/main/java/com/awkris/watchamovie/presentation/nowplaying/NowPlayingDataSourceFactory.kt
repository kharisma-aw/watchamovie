package com.awkris.watchamovie.presentation.nowplaying

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.awkris.watchamovie.data.model.response.MovieResponse
import org.koin.core.KoinComponent
import org.koin.core.get

class NowPlayingDataSourceFactory : DataSource.Factory<Int, MovieResponse>(), KoinComponent {
    private lateinit var nowPlayingDataSource: NowPlayingDataSource
    private val dataSource = MutableLiveData<NowPlayingDataSource>()

    override fun create(): DataSource<Int, MovieResponse> {
        nowPlayingDataSource = get()
        dataSource.postValue(nowPlayingDataSource)
        return nowPlayingDataSource
    }

    fun recreate() {
        nowPlayingDataSource.invalidate()
        create()
    }

    fun getDataSource(): LiveData<NowPlayingDataSource> {
        return dataSource
    }
}