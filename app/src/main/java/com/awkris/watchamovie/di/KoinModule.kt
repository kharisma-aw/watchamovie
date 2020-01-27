package com.awkris.watchamovie.di

import androidx.room.Room
import com.awkris.watchamovie.data.api.MovieDbApi
import com.awkris.watchamovie.data.api.utils.ApiFactory
import com.awkris.watchamovie.data.datastore.CloudMovieDataStore
import com.awkris.watchamovie.data.datastore.DiskMovieDataStore
import com.awkris.watchamovie.data.objectbox.MyObjectBox
import com.awkris.watchamovie.data.repository.MovieDbRepository
import com.awkris.watchamovie.data.room.MovieDatabase
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailViewModel
import com.awkris.watchamovie.presentation.nowplaying.NowPlayingDataSource
import com.awkris.watchamovie.presentation.nowplaying.NowPlayingDataSourceFactory
import com.awkris.watchamovie.presentation.nowplaying.NowPlayingViewModel
import com.awkris.watchamovie.presentation.search.SearchDataSourceFactory
import com.awkris.watchamovie.presentation.search.SearchViewModel
import com.awkris.watchamovie.presentation.watchlist.WatchlistViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appContext = module {
    single(named("appContext")) { androidContext() }
}

val viewmodelModule = module {
    single { SearchDataSourceFactory() }
    single { NowPlayingDataSourceFactory() }
    single { NowPlayingDataSource(get()) }

    viewModel { MovieDetailViewModel(get()) }
    viewModel { NowPlayingViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { WatchlistViewModel(get()) }
}

val apiModule = module {
    fun provideApiFactory() = ApiFactory(Gson())

    single { provideApiFactory().createApi(MovieDbApi::class.java) }
}

val dataStoreModule = module {
    single {
        MyObjectBox.builder()
            .androidContext(androidContext())
            .build()
    }
    single {
        Room.databaseBuilder(
            get(),
            MovieDatabase::class.java,
            MovieDatabase.DB_NAME
        ).build()
    }
    single { CloudMovieDataStore(get()) }
    single { DiskMovieDataStore(get(), get()) }
}

val repositoryModule = module {
    single { MovieDbRepository(get(), get()) }
}