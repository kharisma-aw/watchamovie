package com.awkris.watchamovie.presentation.nowplaying

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.awkris.watchamovie.R
import com.awkris.watchamovie.WatchAMovie.Companion.appComponent
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.di.factory.ViewModelFactory
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailActivity
import kotlinx.android.synthetic.main.activity_now_playing.*
import javax.inject.Inject

class NowPlayingActivity : AppCompatActivity() {
    @Inject
    lateinit var viewmodelFactory: ViewModelFactory<NowPlayingViewModel>
    lateinit var viewModel: NowPlayingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_now_playing)

        appComponent.inject(this)
        viewModel = viewmodelFactory.create(NowPlayingViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        val adapter = NowPlayingAdapter().apply {
            itemClickListener = object : MovieItemClickListener {
                override fun onItemClicked(id: Int) {
                    startActivity(MovieDetailActivity.newIntent(this@NowPlayingActivity, id))
                }
            }
        }

        rcv_now_playing.layoutManager = layoutManager
        viewModel.nowPlayingList.observe(this, Observer { adapter.submitList(it) })
        viewModel.networkState.observe(this, Observer {
            when (it) {
                NetworkState.Loading, NetworkState.Success -> {
                    showErrorState(false)
                    adapter.networkState = it
                }
                is NetworkState.Error -> {
                    showErrorState(true)
                    adapter.networkState = it
                }
            }
        })
        rcv_now_playing.adapter = adapter
    }

    private fun showErrorState(isVisible: Boolean) {
//        if (isVisible)  {
//            rcv_now_playing.visibility = View.INVISIBLE
//            error_state_view.visibility = View.VISIBLE
//        } else {
//            rcv_now_playing.visibility = View.VISIBLE
//            error_state_view.visibility = View.INVISIBLE
//        }
    }
}