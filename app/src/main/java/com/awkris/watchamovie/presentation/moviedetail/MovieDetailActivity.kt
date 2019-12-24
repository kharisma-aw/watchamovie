package com.awkris.watchamovie.presentation.moviedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.awkris.watchamovie.R
import com.awkris.watchamovie.utils.Constants
import com.awkris.watchamovie.utils.formatReleaseYear
import com.awkris.watchamovie.WatchAMovie.Companion.appComponent
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.di.factory.ViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.error_state.*
import javax.inject.Inject


class MovieDetailActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<MovieDetailViewModel>
    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        appComponent.inject(this)
        viewModel = viewModelFactory.create(MovieDetailViewModel::class.java)



        btn_retry.setOnClickListener { viewModel.onScreenCreated(MOVIE_ID) }
        viewModel.getMovieDetail().observe(
            this,
            Observer<MovieDetailResponse> { t ->
                showErrorState(false)
                showMovieDetail(t!!)
            }
        )
        viewModel.getNetworkState().observe(
            this,
            Observer {
                when (it) {
                    NetworkState.Loading -> showLoadingProgress(true)
                    NetworkState.Success -> {
                        showLoadingProgress(false)
                        showErrorState(false)
                    }
                    is NetworkState.Error -> {
                        showLoadingProgress(false)
                        showErrorState(true)
                    }
                }
            }
        )

        viewModel.onScreenCreated(MOVIE_ID)
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }

    private fun showMovieDetail(response: MovieDetailResponse) {
        movie_detail_container.visibility = View.VISIBLE
        with(response) {
            Picasso.get()
                .load(Constants.IMAGE_BASE_URL.format(backdropPath))
                .into(img_backdrop)
            Picasso.get()
                .load(Constants.IMAGE_BASE_URL_500.format(posterPath))
                .into(img_poster)
            val title = Html.fromHtml(resources.getString(
                R.string.title_movie_format,
                title,
                formatReleaseYear(releaseDate)
            )).toString()
            txt_title.text = title
            txt_overview_content.text = overview
        }
    }

    private fun showErrorState(isVisible: Boolean) {
        if (isVisible)  {
            movie_detail_container.visibility = View.INVISIBLE
            error_state_view.visibility = View.VISIBLE
        } else {
            movie_detail_container.visibility = View.VISIBLE
            error_state_view.visibility = View.INVISIBLE
        }
    }

    private fun showLoadingProgress(isVisible: Boolean) {
        progress_bar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        private const val MOVIE_ID = 181812
    }
}