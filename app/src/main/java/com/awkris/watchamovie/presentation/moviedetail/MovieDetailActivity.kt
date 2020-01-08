package com.awkris.watchamovie.presentation.moviedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.awkris.watchamovie.R
import com.awkris.watchamovie.WatchAMovie.Companion.appComponent
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.di.factory.ViewModelFactory
import com.awkris.watchamovie.utils.Constants
import com.awkris.watchamovie.utils.NotificationUtils
import com.awkris.watchamovie.utils.formatReleaseYear
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.error_state.*
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import timber.log.Timber
import javax.inject.Inject


class MovieDetailActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<MovieDetailViewModel>
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var menu: Menu

    private var movieId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_movie_detail)
        appComponent.inject(this)

        val bundle = intent.extras
        movieId = requireNotNull(bundle).getInt(MOVIE_ID)

        viewModel = viewModelFactory.create(MovieDetailViewModel::class.java)

        setObserver()
        btn_retry.setOnClickListener { viewModel.onScreenCreated(movieId) }
        viewModel.onScreenCreated(movieId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = requireNotNull(menu)
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        viewModel.isInWatchlist().observe(
            this,
            Observer<Boolean> { inWatchlist ->
                toggleAddWatchlistVisibility(!inWatchlist)
                if (inWatchlist) {
                    toggleAddReminderVisibility(!(viewModel.isReminderEnabled().value ?: false))
                    viewModel.isReminderEnabled().observe(
                        this,
                        Observer<Boolean> { enabled ->
                            toggleAddReminderVisibility(if (enabled != null) !enabled else null)
                        }
                    )
                }
            }
        )
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_reminder -> {
                Timber.d("Adding reminder")
                NotificationUtils.scheduleAlarmsForReminder(
                    this,
                    requireNotNull(viewModel.getMovieDetail().value)
                )
                viewModel.updateReminder(movieId, true)
                true
            }
            R.id.menu_delete_reminder -> {
                NotificationUtils.deleteAlarmsForReminder(
                    this,
                    requireNotNull(viewModel.getMovieDetail().value)
                )
                viewModel.updateReminder(movieId, false)
                true
            }
            R.id.menu_add_watchlist -> {
                viewModel.saveToWatchlist()
                true
            }
            R.id.menu_remove_watchlist -> {
                viewModel.deleteFromWatchlist(movieId)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }

    private fun setObserver() {
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
    }

    private fun showMovieDetail(response: MovieDetailResponse) {
        movie_detail_container.visibility = View.VISIBLE
        with(response) {
            Picasso.get()
                .load(Constants.IMAGE_BASE_URL.format(backdropPath))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(img_backdrop)
            Picasso.get()
                .load(Constants.IMAGE_BASE_URL_500.format(posterPath))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(img_poster)
            val title = Html.fromHtml(resources.getString(
                R.string.title_movie_format,
                title,
                formatReleaseYear(releaseDate)
            )).toString()
            txt_title.text = title
            txt_overview_content.text = if (overview.isNullOrEmpty()) "No overview" else overview
        }
    }

    private fun showErrorState(turnVisible: Boolean) {
        if (turnVisible)  {
            movie_detail_container.visibility = View.INVISIBLE
            error_state_view.visibility = View.VISIBLE
        } else {
            movie_detail_container.visibility = View.VISIBLE
            error_state_view.visibility = View.INVISIBLE
        }
    }

    private fun showLoadingProgress(turnVisible: Boolean) {
        progress_bar.visibility = if (turnVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun toggleAddWatchlistVisibility(turnVisible: Boolean) {
        menu.findItem(R.id.menu_add_watchlist).isVisible = turnVisible
        menu.findItem(R.id.menu_remove_watchlist).isVisible = !turnVisible
    }

    private fun toggleAddReminderVisibility(turnVisible: Boolean? = null) {
        if (turnVisible != null) {
            menu.findItem(R.id.menu_add_reminder).isVisible = turnVisible
            menu.findItem(R.id.menu_delete_reminder).isVisible = !turnVisible
        } else {
            menu.findItem(R.id.menu_add_reminder).isVisible = false
            menu.findItem(R.id.menu_delete_reminder).isVisible = false
        }
    }

    companion object {
        const val MOVIE_ID = "MOVIE_ID"
        const val MOVIE_TITLE = "MOVIE_TITLE"

        fun newIntent(context: Context, movieId: Int): Intent {
            return Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(MOVIE_ID, movieId)
            }
        }
    }
}