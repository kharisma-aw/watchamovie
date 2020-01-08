package com.awkris.watchamovie.presentation.home

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.awkris.watchamovie.presentation.nowplaying.NowPlayingFragment
import com.awkris.watchamovie.presentation.watchlist.WatchlistFragment
import java.lang.IllegalStateException

class HomeViewPagerAdapter(fm: FragmentManager)
    : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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