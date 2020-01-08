package com.awkris.watchamovie.presentation.nowplaying

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.di.factory.ViewModelFactory
import com.awkris.watchamovie.presentation.common.MovieListAdapter
import com.awkris.watchamovie.presentation.main.FragmentListener
import com.awkris.watchamovie.presentation.common.ItemMovieClickListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_refreshable_list.*
import javax.inject.Inject

class NowPlayingFragment : Fragment() {
    @Inject
    lateinit var viewmodelFactory: ViewModelFactory<NowPlayingViewModel>

    private lateinit var viewModel: NowPlayingViewModel
    private lateinit var fragmentListener: FragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        assert(context is FragmentListener)
        fragmentListener = context as FragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_refreshable_list, container, false)
        viewModel = viewmodelFactory.create(NowPlayingViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh.setOnRefreshListener { viewModel.refresh() }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        val adapter = MovieListAdapter()
            .apply {
            itemMovieClickListener = object :
                ItemMovieClickListener {
                override fun onItemClicked(id: Int) {
                    fragmentListener.navigateToMovieDetail(id)
                }
            }
        }

        viewModel.nowPlayingList.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            adapter.networkState = it
            when (it) {
                is NetworkState.Error, NetworkState.Success -> swipe_refresh.isRefreshing = false
            }
        })

        rcv_list.layoutManager = layoutManager
        rcv_list.adapter = adapter
    }

    companion object {
        fun newInstance() = NowPlayingFragment()
    }
}