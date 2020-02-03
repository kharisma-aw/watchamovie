package com.awkris.watchamovie.presentation.watchlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.awkris.watchamovie.R
import com.awkris.watchamovie.presentation.common.ItemMovieClickListener
import com.awkris.watchamovie.presentation.main.FragmentListener
import kotlinx.android.synthetic.main.fragment_refreshable_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class WatchlistFragment : Fragment() {
    private val viewModel: WatchlistViewModel by viewModel()
    private lateinit var fragmentListener: FragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        assert(context is FragmentListener)
        fragmentListener = context as FragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_refreshable_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh.isEnabled = false
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        val adapter = WatchlistAdapter().apply {
            itemMovieClickListener = object :
                ItemMovieClickListener {
                override fun onItemClicked(id: Int) {
                    fragmentListener.navigateToMovieDetail(id)
                }
            }
        }

        viewModel.getWatchList().observe(viewLifecycleOwner, Observer { adapter.submitList(it) })

        rcv_list.layoutManager = layoutManager
        rcv_list.adapter = adapter
    }

    companion object {
        fun newInstance() = WatchlistFragment()
    }
}