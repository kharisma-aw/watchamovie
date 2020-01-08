package com.awkris.watchamovie.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.awkris.watchamovie.R
import com.awkris.watchamovie.WatchAMovie.Companion.appComponent
import com.awkris.watchamovie.presentation.home.HomeFragment
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailActivity
import com.awkris.watchamovie.presentation.search.SearchFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasAndroidInjector,
    FragmentListener {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_main)

        setDefaultFragment()
        val bundle = intent.extras
        if (bundle != null) {
            val movieId = bundle.getInt(MovieDetailActivity.MOVIE_ID)
            if (movieId != 0) {
                navigateToMovieDetail(movieId)
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun navigateToMovieDetail(movieId: Int) {
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction
//            .add(
//                R.id.main_container,
//                MovieDetailFragment.newInstance(movieId),
//                MOVIE_DETAIL_TAG
//            )
//            .addToBackStack(MOVIE_DETAIL_TAG)
//            .apply { currentFragment?.let { hide(it) } }
//            .commit()
        startActivity(MovieDetailActivity.newIntent(this, movieId))
    }

    override fun navigateToSearch() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(
                R.id.main_container,
                SearchFragment.newInstance(),
                SEARCH_TAG
            )
            .hide(currentFragment!!)
            .addToBackStack(SEARCH_TAG)
            .commit()
    }

    private fun setDefaultFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.main_container, HomeFragment.newInstance())
            .commit()
    }

    companion object {
        private const val MOVIE_DETAIL_TAG = "MOVIE_DETAIL_TAG"
        private const val SEARCH_TAG = "SEARCH_TAG"

        fun newIntent(context: Context, movieId: Int): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(MovieDetailActivity.MOVIE_ID, movieId)
            }
        }
    }
}