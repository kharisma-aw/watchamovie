package com.awkris.watchamovie.di

import android.content.Context
import androidx.room.Room
import com.awkris.watchamovie.data.api.MovieDbApi
import com.awkris.watchamovie.data.api.utils.ApiFactory
import com.awkris.watchamovie.data.datastore.CloudMovieDataStore
import com.awkris.watchamovie.data.datastore.DiskMovieDataStore
import com.awkris.watchamovie.data.repository.MovieDbRepository
import com.awkris.watchamovie.data.room.MovieDatabase
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailViewModel
import com.awkris.watchamovie.presentation.nowplaying.NowPlayingDataSource
import com.awkris.watchamovie.presentation.nowplaying.NowPlayingDataSourceFactory
import com.awkris.watchamovie.presentation.nowplaying.NowPlayingViewModel
import com.awkris.watchamovie.presentation.search.SearchDataSourceFactory
import com.awkris.watchamovie.presentation.search.SearchViewModel
import com.awkris.watchamovie.presentation.upcoming.UpcomingDataSource
import com.awkris.watchamovie.presentation.upcoming.UpcomingDataSourceFactory
import com.awkris.watchamovie.presentation.upcoming.UpcomingViewModel
import com.awkris.watchamovie.presentation.watchlist.WatchlistViewModel
import com.google.gson.Gson
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    factory { SearchDataSourceFactory() }
    single { NowPlayingDataSourceFactory() }
    factory { NowPlayingDataSource(get()) }
    single { UpcomingDataSourceFactory() }
    factory { UpcomingDataSource(get()) }

    viewModel { MovieDetailViewModel(get()) }
    viewModel { NowPlayingViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { UpcomingViewModel(get()) }
    viewModel { WatchlistViewModel(get()) }
}

val apiModule = module {
    fun provideApiFactory() = ApiFactory(Gson())

    single { provideApiFactory().createApi(MovieDbApi::class.java) }
}

val dataStoreModule = module {
    single {
        Room.databaseBuilder(
            get(),
            MovieDatabase::class.java,
            MovieDatabase.DB_NAME
        ).build()
    }
    single { CloudMovieDataStore(get()) }
    single { DiskMovieDataStore(get()) }
}

val repositoryModule = module {
    single { MovieDbRepository(get(), get()) }
}