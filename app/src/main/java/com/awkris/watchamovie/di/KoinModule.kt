package com.awkris.watchamovie.di

import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA
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
import com.awkris.watchamovie.utils.storage.SessionSharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.*
import javax.security.auth.x500.X500Principal

val viewmodelModule = module {
    factory { SearchDataSourceFactory() }
    single { NowPlayingDataSourceFactory() }
    factory { NowPlayingDataSource(get()) }
    single { UpcomingDataSourceFactory() }
    factory { UpcomingDataSource(get()) }

    viewModel { MovieDetailViewModel() }
    viewModel { NowPlayingViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { UpcomingViewModel(get()) }
    viewModel { WatchlistViewModel(get()) }
}

val apiModule = module {
    fun provideApiFactory() = ApiFactory(provideMoshi())

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

val storageModule = module {
    single { SessionSharedPreferences(androidApplication(), provideMoshi()) }
    single { generateAsymmetricKey("generalKey", androidApplication()) }
}

private fun provideMoshi() = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private fun generateAsymmetricKey(keyAlias: String, context: Context): KeyStore.PrivateKeyEntry {
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)

    if (!keyStore.containsAlias(keyAlias)) {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.YEAR, 25)
        val keyPairGeneratorSpec = KeyPairGeneratorSpec.Builder(context.applicationContext)
            .setAlias(keyAlias)
            .setSubject(X500Principal("CN=$keyAlias"))
            .setSerialNumber(BigInteger.ZERO)
            .setStartDate(startDate.time)
            .setEndDate(endDate.time)
            .build()
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KEY_ALGORITHM_RSA,
            "AndroidKeyStore"
        )
        keyPairGenerator.initialize(keyPairGeneratorSpec)
        keyPairGenerator.generateKeyPair()
    }
    return keyStore.getEntry(keyAlias, null) as KeyStore.PrivateKeyEntry
}