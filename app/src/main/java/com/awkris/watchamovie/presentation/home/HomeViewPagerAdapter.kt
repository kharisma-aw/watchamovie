package com.awkris.watchamovie.presentation.home

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.awkris.watchamovie.presentation.nowplaying.NowPlayingFragment
import com.awkris.watchamovie.presentation.watchlist.WatchlistFragment

class HomeViewPagerAdapter(fm: FragmentManager)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList = listOf(
        NowPlayingFragment.newInstance(),
        WatchlistFragment.newInstance()
    )

    override fun getCount() = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence {
        return when (getItem(position)) {
            is NowPlayingFragment -> "Now Playing"
            is WatchlistFragment -> "Watchlist"
            else -> throw IllegalStateException("Doesn't exist")
        }
    }

    override fun getItem(position: Int) = fragmentList[position]
}