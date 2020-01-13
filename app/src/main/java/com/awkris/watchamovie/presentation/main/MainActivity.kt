package com.awkris.watchamovie.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.awkris.watchamovie.R
import com.awkris.watchamovie.presentation.home.HomeFragment
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailActivity
import com.awkris.watchamovie.presentation.search.SearchFragment


class MainActivity : AppCompatActivity(), FragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun navigateToMovieDetail(movieId: Int) {
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
        private const val SEARCH_TAG = "SEARCH_TAG"

        fun newIntent(context: Context, movieId: Int): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(MovieDetailActivity.MOVIE_ID, movieId)
            }
        }
    }
}