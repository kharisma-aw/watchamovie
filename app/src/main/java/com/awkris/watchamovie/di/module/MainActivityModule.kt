package com.awkris.watchamovie.di.module

import com.awkris.watchamovie.presentation.nowplaying.NowPlayingFragment
import com.awkris.watchamovie.presentation.search.SearchFragment
import com.awkris.watchamovie.presentation.watchlist.WatchlistFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeNowPlayingInjector(): NowPlayingFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchInjector(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeWatchlistInjector(): WatchlistFragment
}