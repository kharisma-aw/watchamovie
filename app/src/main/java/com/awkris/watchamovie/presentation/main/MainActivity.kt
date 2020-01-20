package com.awkris.watchamovie.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.awkris.watchamovie.R
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailFragment
import com.awkris.watchamovie.presentation.search.SearchFragment


class MainActivity : AppCompatActivity(), FragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        if (bundle != null) {
            val movieId = bundle.getInt(MovieDetailFragment.MOVIE_ID)
            if (movieId != 0) {
                navigateToMovieDetail(movieId)
            }
        }
    }

    override fun navigateToMovieDetail(movieId: Int) {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.goto_movieDetailFragment,
            MovieDetailFragment.createBundle(movieId)
        )
    }

    override fun navigateToSearch() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(
                R.id.nav_host_fragment,
                SearchFragment.newInstance(),
                SEARCH_TAG
            )
            .hide(currentFragment!!)
            .addToBackStack(SEARCH_TAG)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    companion object {
        private const val SEARCH_TAG = "SEARCH_TAG"

        fun newIntent(context: Context, movieId: Int): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(MovieDetailFragment.MOVIE_ID, movieId)
            }
        }
    }
}