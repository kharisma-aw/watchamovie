package com.awkris.watchamovie.presentation.moviedetail

import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.model.MovieDetail
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.Cast
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.presentation.common.ItemMovieClickListener
import com.awkris.watchamovie.utils.Constants
import com.awkris.watchamovie.utils.NotificationUtils
import com.awkris.watchamovie.utils.formatReleaseYear
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import kotlinx.android.synthetic.main.error_state.*
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class MovieDetailFragment : Fragment() {
    private val viewModel: MovieDetailViewModel by viewModel()
    private lateinit var menu: Menu

    private var movieId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieId = requireNotNull(arguments).getInt(MOVIE_ID)

        setObserver()
        btn_retry.setOnClickListener { viewModel.onScreenCreated(movieId) }
        viewModel.onScreenCreated(movieId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        viewModel.isInWatchlist.observe(
            viewLifecycleOwner,
            Observer<Boolean> { inWatchlist ->
                toggleAddWatchlistVisibility(!inWatchlist)
                if (inWatchlist) {
                    toggleAddReminderVisibility(!(viewModel.isReminderEnabled.value ?: false))
                    viewModel.isReminderEnabled.observe(
                        viewLifecycleOwner,
                        Observer<Boolean> { enabled ->
                            toggleAddReminderVisibility(if (enabled != null) !enabled else null)
                        }
                    )
                }
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_reminder -> {
                NotificationUtils.scheduleAlarmsForReminder(
                    requireContext(),
                    requireNotNull(viewModel.movieDetailWithRecommendations.value).movieDetail!!
                )
                viewModel.updateReminder(movieId, true)
                true
            }
            R.id.menu_delete_reminder -> {
                NotificationUtils.deleteAlarmsForReminder(
                    requireContext(),
                    requireNotNull(viewModel.movieDetailWithRecommendations.value).movieDetail!!
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
        viewModel.movieDetailWithRecommendations.observe(
            viewLifecycleOwner,
            Observer<MovieDetail> { t ->
                showErrorState(false)
                t.movieDetail?.let { showMovieDetail(it) }
                setRecommendations(t.recommendations)
                setCasts(t.casts)
            }
        )

        viewModel.networkState.observe(
            viewLifecycleOwner,
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

    private fun setCasts(list: List<Cast>) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CastAdapter(list)
        rcv_casts.layoutManager = layoutManager
        rcv_casts.adapter = adapter
    }

    private fun setRecommendations(list: List<MovieResponse>) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecommendationAdapter(list).apply {
            itemMovieClickListener = object : ItemMovieClickListener {
                override fun onItemClicked(id: Int) {
                    findNavController().navigate(
                        R.id.action_movieDetailFragment_self,
                        createBundle(id)
                    )
                }
            }
        }
        rcv_recommendations.layoutManager = layoutManager
        rcv_recommendations.adapter = adapter
    }

    private fun showMovieDetail(response: MovieDetailResponse) {
        movie_detail_container.visibility = View.VISIBLE
        with(response) {
            Glide.with(requireContext())
                .load(Constants.IMAGE_BASE_URL.format(backdropPath))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .downsample(DownsampleStrategy.AT_MOST)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(img_backdrop)
            val title = Html.fromHtml(resources.getString(
                R.string.title_movie_format,
                title,
                formatReleaseYear(releaseDate)
            )).toString()
            txt_title.text = title
            val genrelist = genres.map { it.name.toLowerCase() }
            txt_genres.text = genrelist.joinToString(", ")
            if (tagline.isNullOrEmpty()) {
                txt_tagline.visibility = View.GONE
            } else {
                txt_tagline.text = String.format("\"%s\"", tagline)
            }
            txt_overview_content.text = if (overview.isNullOrEmpty()) {
                "No overview provided"
            } else {
                overview
            }
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

        fun createBundle(movieId: Int): Bundle {
            return bundleOf(Pair(MOVIE_ID, movieId))
        }
    }
}